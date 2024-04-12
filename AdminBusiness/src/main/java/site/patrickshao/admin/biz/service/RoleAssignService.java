package site.patrickshao.admin.biz.service;

import org.checkerframework.checker.units.qual.A;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.patrickshao.admin.biz.repository.DefaultRepository;
import site.patrickshao.admin.common.entity.po.AbstractPersistObject;
import site.patrickshao.admin.common.entity.po.RoleAssignRequirePO;
import site.patrickshao.admin.common.entity.po.UserRolePO;
import site.patrickshao.admin.common.exception.http.Http400BadRequest;
import site.patrickshao.admin.common.exception.http.Http401Unauthorized;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/12
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@ParametersAreNonnullByDefault
public class RoleAssignService {
    @Autowired
    private UserService userService;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private DefaultRepository<RoleAssignRequirePO> roleAssignRequireRepository;
    @Autowired
    private AuthorizationService authorizationService;
    @Autowired
    private DefaultRepository<UserRolePO> userRoleRepository;


    @Transactional
    public void authorize(Long userId, Long roleId, @Nullable Long applicationId, @Nullable Long namespaceId) {
        var currentRoles = userRoleRepository.selectByParentId(userId);
        for (UserRolePO rolePO : currentRoles) {
            if (rolePO.getRoleId().equals(roleId)) {
                throw new Http400BadRequest("Role already assigned");
            }
        }
        if (!authorizationService.checkIfHavePermissionToAssignRole(userId, roleId, applicationId, namespaceId)) {
            throw new Http401Unauthorized("You don't have permission to assign this role");
        }

        userService.assignRole(userId, roleId, applicationId, namespaceId);
    }

    @Transactional
    public void deauthorize(Long userId, Long roleId) {
        var currentRoles = userRoleRepository.selectByParentId(userId);
        for (UserRolePO rolePO : currentRoles) {
            if (rolePO.getRoleId().equals(roleId)) {
                if (!authorizationService.checkIfHavePermissionToAssignRole(
                        userId,
                        roleId,
                        rolePO.getApplicationSpecification(),
                        rolePO.getNamespaceSpecification())) {
                    throw new Http401Unauthorized("You don't have permission to assign this role");
                }
                userService.deassignRole(userId, roleId);
                return;
            }
        }
        throw new Http400BadRequest("Role not assigned");
    }
}
