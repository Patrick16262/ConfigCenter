package site.patrickshao.admin.biz.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.patrickshao.admin.biz.mapper.AuthorizationInfoMapper;
import site.patrickshao.admin.biz.secure.AuthorizationContext;
import site.patrickshao.admin.common.annotation.NotForController;
import site.patrickshao.admin.common.entity.HaveApplicationParent;
import site.patrickshao.admin.common.entity.PojoWithIdentifier;
import site.patrickshao.admin.common.entity.bo.SpecifiedPermissionBO;
import site.patrickshao.admin.common.entity.dto.SpecifiedUserDTO;
import site.patrickshao.admin.common.entity.po.*;
import site.patrickshao.admin.common.utils.PojoUtils;
import site.patrickshao.admin.common.utils.Throwables;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

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
    private ActionService actionService;
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private AuthorizationInfoMapper authorizationInfoMapper;

    private static final Logger log = LoggerFactory.getLogger(AuthorizationService.class);

    @Transactional
    public boolean checkPermission() {
        List<PermissionPO> requiredPermissions = actionService
                .getPermissionByActionName(AuthorizationContext.getActionName());
        Throwables.validateRequest(requiredPermissions == null, "No such action Name");
        SpecifiedUserDTO specifiedUserDTO = AuthorizationContext.getSpecifiedUserDTO();
        log.debug("get required permissions: " + requiredPermissions);
        log.debug("get user details: " + specifiedUserDTO);
        return checkIfPermissionSatisfied(
                specifiedUserDTO,
                requiredPermissions,
                AuthorizationContext.getTargetApplicationId(),
                AuthorizationContext.getTargetNamespaceId()
        );
    }


    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    @Transactional
    @NotForController
    public boolean checkIfHavePermissionToAssignRole(Long userId, Long roleId,@Nullable Long applicationId,@Nullable Long namespaceId) {
        List<PermissionPO> requiredList = roleService.getRoleAssignRequiredPermissions(roleId);
        SpecifiedUserDTO user = userService.getUserDTO(userId);
        return checkIfPermissionSatisfied(user, requiredList, applicationId, namespaceId);
    }

    @SuppressWarnings("DataFlowIssue")
    @Transactional
    @NotForController
    public boolean checkIfHavePermissionToSeeApplication(SpecifiedUserDTO userDTO, Long applicationId) {
        List<PermissionPO> requiredList = actionService.getPermissionByActionName("Application#Visible");
        Throwables.throwOnNull(requiredList);
        return checkIfPermissionSatisfied(userDTO, requiredList, applicationId, null);
    }

    @Transactional
    @NotForController
    public void makeTargetAppNamespaceIdInfo(PojoWithIdentifier targetPojo) {
        if (targetPojo instanceof HaveApplicationParent pojo) {
            AuthorizationContext.setTargetApplicationId(pojo.getApplicationId());
        }
        if (targetPojo instanceof NamespacePO pojo) {
            AuthorizationContext.setTargetNamespaceId(pojo.getId());
        }
    }


    private boolean checkIfPermissionSatisfied(
            SpecifiedUserDTO specifiedUserDTO,
            List<PermissionPO> requiredPermissions,
            @Nullable Long applicationId,
            @Nullable Long namespaceId) {
        List<SpecifiedPermissionBO> authorizedPermissions = PojoUtils.getAllPermissionsDistinct(specifiedUserDTO);

        //按Id排序权限
        authorizedPermissions = new ArrayList<>(authorizedPermissions);
        requiredPermissions = new ArrayList<>(requiredPermissions);
        authorizedPermissions.sort(Comparator.comparing(SpecifiedPermissionBO::getId));
        requiredPermissions.sort(Comparator.comparing(PermissionPO::getId));

        //验证权限
        int i = 0;
        for (PermissionPO required : requiredPermissions) {
            for (; i < authorizedPermissions.size() + 1; i++) {
                if (i == authorizedPermissions.size()) {
                    //认证失败
                    return false;
                }
                var authorized = authorizedPermissions.get(i);
                //没有权限满足该要求，认证失败，直接返回false
                if (required.getId() < authorized.getId()) {
                    //认证失败
                    return false;
                }
                //不是一类权限
                if (!required.getId().equals(authorized.getId())) {
                    //权限不符合
                    continue;
                }
                break;
//                log.error("""
//                                Authorization exception
//                                Detail:
//                                class authorized = {}
//                                class required = {}
//                                Long applicationId = {}
//                                Long namespaceId = {}
//
//                                StackTrace:
//                                {}""",
//                        authorized,
//                        required,
//                        applicationId,
//                        namespaceId,
//                        new RuntimeException().getStackTrace()
//                );
            }
        }
        return true;
    }


}
