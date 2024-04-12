package site.patrickshao.admin.biz.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.patrickshao.admin.biz.repository.DefaultRepository;
import site.patrickshao.admin.biz.secure.AuthorizationContext;
import site.patrickshao.admin.common.annotation.OnlyForService;
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

    private static final Logger log = LoggerFactory.getLogger(AuthorizationService.class);

    @Transactional
    public boolean checkPermission() {
        List<PermissionPO> requiredPermissions = actionService
                .getPermissionByActionName(AuthorizationContext.getActionName());
        Throwables.validateRequest(requiredPermissions == null, "No such action Name");
        SpecifiedUserDTO specifiedUserDTO = userService.getUserDTO(AuthorizationContext.getUserId());
        log.debug("get required permissions: " + requiredPermissions);
        log.debug("get user details: " + specifiedUserDTO);
        return checkIfPermissionSatisfied(
                specifiedUserDTO,
                requiredPermissions,
                AuthorizationContext.getTargetApplicationId(),
                AuthorizationContext.getTargetNamespaceId()
        );
    }


    @Transactional
    @OnlyForService
    public boolean checkIfHavePermissionToAssignRole(Long userId, Long roleId,@Nullable Long applicationId,@Nullable Long namespaceId) {
        List<PermissionPO> requiredList = roleService.getRoleAssignRequiredPermissions(roleId);
        SpecifiedUserDTO user = userService.getUserDTO(userId);
        return checkIfPermissionSatisfied(user, requiredList, applicationId, namespaceId);
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
                //已认证权限的application作用范围为全部，则该权限符合
                if (authorized.getApplicationId() == null) {
                    //权限符合
                    break;
                }
                //操作不在具体的application对象内，则需求的权限application作用范围为全部
                //但已认证权限的application作用范围为局部，则该权限符合不符合
                if (applicationId == null) {
                    //权限不符合
                    continue;
                }
                //已认证权限作用的application范围为部分，但是需求权限作用范围是全都，则该权限符合不符合
                if (!required.getSpecifyApplication()) {
                    //权限不符合
                    continue;
                }
                //已认证权限作用的application范围和需求权限作用范围不一致，则该权限符合不符合
                if (!authorized.getApplicationId().equals(applicationId)) {
                    //权限不符合
                    continue;
                }
                //已认证权限的namespace作用范围为全部，则该权限符合
                if (authorized.getNamespaceId() == null) {
                    //权限符合
                    break;
                }
                //操作不在具体的application对象内，则需求的权限application作用范围为全部
                //但已认证权限的application作用范围为局部，则该权限符合不符合
                if (namespaceId == null) {
                    //权限不符合
                    continue;
                }
                //已认证权限作用的namespace范围为部分，但是需求权限作用范围是全都，则该权限符合不符合
                if (!required.getSpecifyNamespace()) {
                    //权限不符合
                    continue;
                }
                //已认证权限作用的namespace范围和需求权限作用范围不一致，则该权限符合不符合
                if (!authorized.getNamespaceId().equals(applicationId)) {
                    //权限不符合
                    continue;
                }
                log.error("""
                                Authorization exception
                                Detail:
                                class authorized = {}
                                class required = {}
                                Long applicationId = {}
                                Long namespaceId = {}
                                                                
                                StackTrace:
                                {}""",
                        authorized,
                        required,
                        applicationId,
                        namespaceId,
                        new RuntimeException().getStackTrace()
                );
            }
        }
        return true;
    }


}
