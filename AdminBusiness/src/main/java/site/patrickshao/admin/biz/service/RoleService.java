package site.patrickshao.admin.biz.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.patrickshao.admin.biz.annotation.PreAuthorize;
import site.patrickshao.admin.biz.repository.DefaultRepository;
import site.patrickshao.admin.biz.secure.AuthorizationContext;
import site.patrickshao.admin.common.annotation.NotForController;
import site.patrickshao.admin.common.entity.bo.RoleDTO;
import site.patrickshao.admin.common.entity.bo.SpecifiedPermissionBO;
import site.patrickshao.admin.common.entity.dto.SpecifiedRoleDTO;
import site.patrickshao.admin.common.entity.po.*;
import site.patrickshao.admin.common.utils.PojoUtils;
import site.patrickshao.admin.common.utils.StringUtils;
import site.patrickshao.admin.common.utils.Throwables;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import static site.patrickshao.admin.common.constants.DataBaseFields.Role.ROLE_NAME;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/12
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
@ParametersAreNonnullByDefault
public class RoleService {
    @Autowired
    private DefaultRepository<RolePO> roleRepository;
    @Autowired
    private DefaultRepository<UserRolePO> userRoleRepository;
    @Autowired
    private DefaultRepository<RolePermissionPO> rolePermissionRepository;
    @Autowired
    private PermissionService permissionService;
    @Autowired
    private DefaultRepository<RoleAssignRequirePO> roleAssignRequireRepository;
    @Autowired
    private DefaultRepository<PermissionPO> permissionRepository;


    @Transactional
    @PreAuthorize("User#Visit")
    public List<SpecifiedRoleDTO> getRoleDTOByUserId(Long userId) {
        List<UserRolePO> userRolePOList = userRoleRepository.selectByParentId(userId)
                .stream()
                .sorted(Comparator.comparing(UserRolePO::getRoleId))
                .toList();
        List<Long> roleIds = userRolePOList.stream().map(UserRolePO::getRoleId).toList();
        List<RolePO> roles = roleRepository.selectByIds(roleIds)
                .stream()
                .sorted(Comparator.comparing(RolePO::getId))
                .toList();
        List<SpecifiedRoleDTO> specifiedRoleDTOS = new ArrayList<>();
        for (int i = 0; i < userRolePOList.size(); i++) {
            UserRolePO userRolePO = userRolePOList.get(i);
            RolePO role = roles.get(i);
            List<SpecifiedPermissionBO> specifiedPermissionBOS = getSpecifiedPermissionBOByRoleIdAndUserRole(role.getId(), userRolePO);
            specifiedRoleDTOS.add(PojoUtils.makeSpecifiedRoleDTO(userRolePO, role, specifiedPermissionBOS));
        }
        return specifiedRoleDTOS;
    }


    @Transactional
    public List<RoleDTO> viewAllRoles() {
        List<RolePO> rolePOS = roleRepository.selectByWrapper(new QueryWrapper<>());
        return rolePOS.stream().map(role -> {
            List<PermissionPO> permissionPOS = permissionService.getPermissionByRoleId(role.getId());
            RoleDTO roleDTO = new RoleDTO();
            roleDTO.setId(role.getId());
            roleDTO.setRoleName(role.getRoleName());
            roleDTO.setParentRoleNames(role.getParentRoleNames());
            roleDTO.setSpecifyApplication(role.getSpecifyApplication());
            roleDTO.setSpecifyNamespace(role.getSpecifyNamespace());
            roleDTO.setPermissions(permissionPOS);
            return roleDTO;
        }).toList();
    }

    @Transactional
    @PreAuthorize("Role#Create")
    public Long createRole(RolePO rolePO) {
        Throwables.validateRequest(!isRoleNameUnique(rolePO.getRoleName()),
                "Role name already exists");
        return roleRepository.create(rolePO, AuthorizationContext.getUsername()).getId();
    }

    @Transactional
    @PreAuthorize("Role#Delete")
    public void deleteRole(Long roleId) {
        Throwables.validateRequest(!roleRepository.exists(roleId), "Role not found");
        Throwables.validateRequest(roleRepository.selectById(roleId).getSystemDefault(),
                "System default role cannot be deleted");
        roleRepository.deleteById(roleId, AuthorizationContext.getUsername());
    }

    @Transactional
    @PreAuthorize("Role#Editname")
    public void editRoleName(Long roleId, String roleName) {
        RolePO po = roleRepository.selectById(roleId);
        Throwables.validateRequest(po == null, "Role not found");
        Throwables.validateRequest(po.getSystemDefault(), "System default role name cannot be edited");
        Throwables.validateRequest(!isRoleNameUnique(roleName), "Role name already exists");
        RolePO rolePO = new RolePO();
        rolePO.setRoleName(roleName);
        roleRepository.updateById(roleId, rolePO, AuthorizationContext.getUsername());
    }

    @Transactional
    @PreAuthorize("Role#Editname")
    public void editComment(Long roleId, String newComment) {
        RolePO po = roleRepository.selectById(roleId);
        Throwables.validateRequest(po == null, "Role not found");
        Throwables.validateRequest(po.getSystemDefault(), "System default role name cannot be edited");
        RolePO rolePO = new RolePO();
        rolePO.setComment(newComment);
        roleRepository.updateById(roleId, rolePO, AuthorizationContext.getUsername());
    }

    @Transactional
    @PreAuthorize("Role#EditRolePermission")
    public void editRolePermission(Long roleId, List<Long> permissionIds) {
        Throwables.validateRequest(!roleRepository.exists(roleId), "Role not found");
        List<RolePermissionPO> rolePermissionPOList = permissionIds.stream().map(permit -> {
            RolePermissionPO rolePermissionPO = new RolePermissionPO();
            rolePermissionPO.setRoleId(roleId);
            rolePermissionPO.setPermissionId(permit);
            return rolePermissionPO;
        }).toList();
        rolePermissionRepository.deleteByParentId(roleId, AuthorizationContext.getUsername());
        rolePermissionRepository.create(rolePermissionPOList, AuthorizationContext.getUsername());
    }

    @Transactional
    public boolean isRoleNameUnique(String roleName) {
        return roleRepository.selectByWrapper(new QueryWrapper<RolePO>().eq(ROLE_NAME, roleName)).isEmpty();
    }

    /**
     * Get the permissions that are required to assign a role
     * ActionRequire虽然有comment字段，但是不支持读取，创建，修改，删除comment
     * 仅作为数据库维护人员的提示
     * 任何关系类实体的comment字段都是这样
     *
     * @param roleId the role id
     * @return the list of permissions
     */

    @Transactional
    public List<PermissionPO> getRoleAssignRequiredPermissions(Long roleId) {
        Throwables.validateRequest(!roleRepository.exists(roleId), "Role not found");
        List<RoleAssignRequirePO> roleAssignRequirePOS = roleAssignRequireRepository.selectByParentId(roleId);
        List<Long> permissionIds = roleAssignRequirePOS.stream().map(RoleAssignRequirePO::getPermissionId).toList();
        return permissionRepository.selectByIds(permissionIds);
    }

    @Transactional
    @NotForController
    public List<SpecifiedPermissionBO> getSpecifiedPermissionBOByRoleIdAndUserRole(Long roleId, UserRolePO userRolePO) {
        List<Long> roleIds = new ArrayList<>();
        roleIds.add(roleId);
        List<SpecifiedPermissionBO> specifiedPermissionBOS = new ArrayList<>();
        while (!roleIds.isEmpty()) {
            Long curRoleId = roleIds.get(0);

            specifiedPermissionBOS
                    .addAll(permissionService.getSpecifiedPermissionBOByRoleId(
                            curRoleId,
                            userRolePO)
                    );

            roleIds.remove(0);
            roleIds.addAll(getParentRoleIds(roleId));
        }
        return specifiedPermissionBOS;
    }

    @Transactional
    @NotForController
    public List<Long> getParentRoleIds(Long roleId) {
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