package site.patrickshao.admin.biz.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.patrickshao.admin.biz.annotation.PreAuthorize;
import site.patrickshao.admin.biz.repository.DefaultRepository;
import site.patrickshao.admin.biz.secure.AuthorizationContext;
import site.patrickshao.admin.common.annotation.NotForController;
import site.patrickshao.admin.common.constants.DataBaseFields;
import site.patrickshao.admin.common.entity.dto.SpecifiedUserDTO;
import site.patrickshao.admin.common.entity.po.*;
import site.patrickshao.admin.common.utils.PojoUtils;
import site.patrickshao.admin.common.utils.Throwables;

import java.util.List;

import static site.patrickshao.admin.common.constants.DataBaseFields.User.USER_NAME;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/11
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class UserService {
    @Autowired
    private DefaultRepository<UserPO> userRepository;
    @Autowired
    private DefaultRepository<RolePO> roleRepository;
    @Autowired
    private DefaultRepository<UserRolePO> userRoleRepository;
    @Autowired
    private DefaultRepository<RolePermissionPO> rolePermissionRepository;
    @Autowired
    private DefaultRepository<PermissionPO> permissionRepository;
    @Autowired
    private RoleService roleService;

    @Transactional
    @PreAuthorize("User#Visit")
    public List<UserPO> getAll() {
        return userRepository.selectByWrapper(new QueryWrapper<>());
    }

    @Transactional
    @PreAuthorize("User#CreateUser")
    public Long createUser(UserPO userPO) {
        Throwables.validateRequest(!isUserNameUnique(userPO.getUserName()),
                "User name already exists");
        return userRepository.create(userPO, AuthorizationContext.getUsername()).getId();
    }

    @Transactional
    @PreAuthorize("User#DeleteUser")
    public void deleteUser(Long userId) {
        userRepository.deleteById(userId, AuthorizationContext.getUsername());
    }

    @SuppressWarnings("PointlessBooleanExpression")
    @Transactional
    @PreAuthorize("User#Enable/DisableUser")
    public void disableUser(Long userId) {
        Throwables.validateRequest(userRepository.exists(userId), "User not found");
        Throwables.validateRequest(userRepository.selectById(userId).getEnabled() == false,
                "User already disabled");
        UserPO userPO = new UserPO();
        userPO.setEnabled(false);
        userRepository.updateById(userId, userPO, AuthorizationContext.getUsername());
    }

    @SuppressWarnings("PointlessBooleanExpression")
    @Transactional
    @PreAuthorize("User#Enable/DisableUser")
    public void enableUser(Long userId) {
        Throwables.validateRequest(userRepository.exists(userId), "User not found");
        Throwables.validateRequest(userRepository.selectById(userId).getEnabled() == true,
                "User already enabled");
        UserPO userPO = new UserPO();
        userPO.setEnabled(true);
        userRepository.updateById(userId, userPO, AuthorizationContext.getUsername());
    }

    @Transactional
    @PreAuthorize("User#SelfEdit")
    public void selfEdit(UserPO userPO) {
        Throwables.validateRequest(!AuthorizationContext.getUserId().equals(userPO.getId()),
                "Cannot edit others info by this interface");
        userPO.setId(AuthorizationContext.getUserId());
        userPO.setUserName(null);
        userRepository.updateById(userPO, AuthorizationContext.getUsername());
    }

    @Transactional
    @PreAuthorize("User#Visit")
    public SpecifiedUserDTO getUserDTO(Long userId) {
        Throwables.validateRequest(!userRepository.exists(userId), "User not found");
        var roleDtoList = roleService.getRoleDTOByUserId(userId);
        return PojoUtils.makeSpecifiedUserDTO(userRepository.selectById(userId), roleDtoList);
    }

    @Transactional
    public boolean isUserNameUnique(String userName) {
        return userRepository.selectByWrapper(new QueryWrapper<UserPO>().eq(USER_NAME, userName)).isEmpty();
    }

    @Transactional
    @NotForController
    public void assignRole(Long userId, Long roleId, Long applicationId, Long namespaceId) {
        UserRolePO userRolePO = new UserRolePO();
        userRolePO.setUserId(userId);
        userRolePO.setRoleId(roleId);
        userRolePO.setApplicationSpecification(applicationId);
        userRolePO.setNamespaceSpecification(namespaceId);

        userRoleRepository.create(userRolePO, AuthorizationContext.getUsername());
    }

    public void deassignRole(Long userId, Long roleId) {
        userRoleRepository.deleteByWrapper(new QueryWrapper<UserRolePO>()
                        .eq(DataBaseFields.UserRole.USER_ID, userId)
                        .eq(DataBaseFields.UserRole.ROLE_ID, roleId),
                AuthorizationContext.getUsername());
    }
}
