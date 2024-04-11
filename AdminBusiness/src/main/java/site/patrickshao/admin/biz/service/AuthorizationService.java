package site.patrickshao.admin.biz.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.patrickshao.admin.biz.repository.DefaultRepository;
import site.patrickshao.admin.biz.utils.AuthorizationBusiness;
import site.patrickshao.admin.biz.utils.AuthorizationUtils;
import site.patrickshao.admin.common.constants.DataBaseFields;
import site.patrickshao.admin.common.entity.bo.AuthorizationContextBO;
import site.patrickshao.admin.common.entity.bo.SpecifiedPermissionBO;
import site.patrickshao.admin.common.entity.po.*;
import site.patrickshao.admin.common.exception.http.Http400BadRequest;
import site.patrickshao.admin.common.utils.ArrayUtils;
import site.patrickshao.admin.common.utils.StringUtils;
import site.patrickshao.admin.common.utils.Throwables;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

import static site.patrickshao.admin.biz.utils.AuthorizationUtils.convertToRolePermissionPOPartition;
import static site.patrickshao.admin.common.constants.DataBaseFields.RolePO.ROLE_NAME;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/10
 */
@SuppressWarnings({"SpringJavaInjectionPointsAutowiringInspection", "SpringJavaAutowiredFieldsWarningInspection"})
@Service
@ParametersAreNonnullByDefault
public class AuthorizationService {
    @Autowired
    private DefaultRepository<UserPO> userRepository;
    @Autowired
    private DefaultRepository<UserRolePO> userRoleRepository;
    @Autowired
    private DefaultRepository<RolePO> roleRepository;
    @Autowired
    private DefaultRepository<RolePermissionPO> rolePermissionRepository;
    @Autowired
    private DefaultRepository<PermissionPO> permissionRepository;
    @Autowired
    private DefaultRepository<ActionRequirePO> actionRequireRepository;
    @Autowired
    private DefaultRepository<ActionPO> actionRepository;
    private static final Logger log = LoggerFactory.getLogger(AuthorizationService.class);

    @Transactional
    public boolean checkPermission(AuthorizationContextBO authorizationContext) {
        ActionPO po = getActionPO(authorizationContext.getActionName());
        Throwables.throwOnCondition(po == null, new Http400BadRequest("No such action"));
        List<PermissionPO> requiredPermissions = getActionRequiredPermissionPO(po.getId());
        List<SpecifiedPermissionBO> authorizedPermission = getPermissionByUser(authorizationContext.getUserId());
        log.debug("get requiredPermissions: " + requiredPermissions);
        log.debug("get authorizedPermission: " + authorizedPermission);
        return AuthorizationBusiness
                .checkIfPermissionSatisfied(
                        authorizedPermission,
                        requiredPermissions,
                        authorizationContext.getTargetApplicationId(),
                        authorizationContext.getTargetNamespaceId()
                );
    }


    private List<PermissionPO> getActionRequiredPermissionPO(Long actionId) {
        ActionRequirePO po = new ActionRequirePO();
        po.setActionID(actionId);
        List<Long> list = actionRequireRepository
                .selectByPartition(po)
                .stream()
                .map(ActionRequirePO::getPermissionID)
                .toList();
        return permissionRepository.selectByIds(list);
    }

    @Nullable
    private ActionPO getActionPO(String actionName) {
        List<ActionPO> actionPOS = actionRepository.selectByWrapper(
                new QueryWrapper<ActionPO>()
                        .eq(DataBaseFields.ActionPO.ACTION_NAME, actionName)
        );
        if (actionPOS.size() != 1) return null;
        else return actionPOS.get(0);
    }

    private List<SpecifiedPermissionBO> getPermissionByUser(Long userId) {
        List<UserRolePO> list = userRoleRepository.selectByPartition(
                AuthorizationUtils.convertToUserRolePOPartition(userId));
        List<SpecifiedPermissionBO> permissionBOS = new ArrayList<>();
        for (var userRole : list) {
            List<PermissionPO> permissionLs = getPermissionByRole(userRole.getRoleId());
            for (var permission : permissionLs) {
                SpecifiedPermissionBO bo = getSpecifiedPermissionBO(userRole, permission);
                permissionBOS.add(bo);
            }
        }
        return permissionBOS;
    }

    private SpecifiedPermissionBO getSpecifiedPermissionBO(UserRolePO userRole, PermissionPO permission) {
        SpecifiedPermissionBO bo = new SpecifiedPermissionBO();
        bo.setId(permission.getId());
        if (permission.getSpecifyApplication() && userRole.getApplicationSpecification() != null) {
            bo.setTargetApplication(userRole.getApplicationSpecification());
        }
        if (permission.getSpecifyNamespace() && userRole.getNamespaceSpecification() != null) {
            bo.setTargetNamespace(userRole.getNamespaceSpecification());
        }
        return bo;
    }

    private List<PermissionPO> getPermissionByRole(Long roleId) {
        List<Long> nextRoles = new ArrayList<>();
        Set<Long> queriedRoles = new HashSet<>();
        List<PermissionPO> permissionList = new ArrayList<>();
        nextRoles.add(roleId);
        do {
            Long id = nextRoles.get((int) 0);
            nextRoles.remove((int) 0);
            if (queriedRoles.contains(id)) {
                continue;
            }
            var permissionIds = rolePermissionRepository
                    .selectByPartition(convertToRolePermissionPOPartition(id))
                    .stream()
                    .map(RolePermissionPO::getPermissionId)
                    .toList();
            permissionList.addAll(permissionRepository.selectByIds(permissionIds));
            var nextIds = getNextRoleIds(id);
            nextRoles.addAll(nextIds);
            queriedRoles.add(id);
        } while (!nextRoles.isEmpty());
        return ArrayUtils.distinctByPojoIdentifier(permissionList);
    }

    private List<PermissionPO> getPermissionByActionName(String actionName) {
        return permissionRepository.selectByIds(
                actionRepository.selectByWrapper(
                                new QueryWrapper<ActionPO>()
                                        .eq(DataBaseFields.ActionPO.ACTION_NAME, actionName)
                        ).stream()
                        .map(AuthorizationUtils::convertToActionPOPartition)
                        .map(actionRequireRepository::selectByPartition)
                        .flatMap(List::stream)
                        .map(ActionRequirePO::getPermissionID)
                        .toList()
        );

    }

    @NotNull
    private List<Long> getNextRoleIds(Long roleId) {
        String names = roleRepository.selectById(roleId).getParentRoleNames();
        if (names == null || names.isEmpty()) {
            return new ArrayList<>();
        }
        List<String> nameList = StringUtils.splitByComma(names);
        return nameList.stream()
                .map(str -> roleRepository
                        .selectByWrapper(new QueryWrapper<RolePO>().eq(ROLE_NAME, str))
                )
                .flatMap(List::stream)
                .map(RolePO::getId)
                .toList();
    }

}
