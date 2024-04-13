package site.patrickshao.admin.biz.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import site.patrickshao.admin.biz.annotation.PreAuthorize;
import site.patrickshao.admin.biz.repository.DefaultRepository;
import site.patrickshao.admin.biz.secure.AuthorizationContext;
import site.patrickshao.admin.common.annotation.NotForController;
import site.patrickshao.admin.common.entity.bo.SpecifiedPermissionBO;
import site.patrickshao.admin.common.entity.po.PermissionPO;
import site.patrickshao.admin.common.entity.po.RolePO;
import site.patrickshao.admin.common.entity.po.RolePermissionPO;
import site.patrickshao.admin.common.entity.po.UserRolePO;
import site.patrickshao.admin.common.utils.PojoUtils;
import site.patrickshao.admin.common.utils.Throwables;

import java.util.List;
import java.util.stream.Collectors;

import static site.patrickshao.admin.common.constants.DataBaseFields.Permission.PERMISSION_NAME;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/11
 */
@SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
@Service
public class PermissionService {
    @Autowired
    private DefaultRepository<RolePO> roleRepository;
    @Autowired
    private DefaultRepository<RolePermissionPO> rolePermissionRepository;
    @Autowired
    private DefaultRepository<PermissionPO> permissionRepository;

    /**
     * 注意：不会查询父role的权限
     *
     * @author Shao Yibo
     * @date 2024/4/12
     */
    @NotForController
    public List<SpecifiedPermissionBO> getSpecifiedPermissionBOByRoleId(Long roleId, UserRolePO userRole) {
        List<RolePermissionPO> rolePermissionPOList = rolePermissionRepository.selectByParentId(roleId);
        List<Long> permissionIds = rolePermissionPOList.stream().map(RolePermissionPO::getPermissionId).collect(Collectors.toList());
        return permissionRepository.selectByIds(permissionIds)
                .stream()
                .map(permissionPO -> PojoUtils.makeSpecifiedPermissionBO(userRole, permissionPO))
                .collect(Collectors.toList());
    }

    @Transactional
    public List<PermissionPO> getPermissionByRoleId(Long roleId) {
        Throwables.validateRequest(roleRepository.exists(roleId), "Role not found");
        List<RolePermissionPO> rolePermissionPOList = rolePermissionRepository.selectByParentId(roleId);
        List<Long> permissionIds = rolePermissionPOList.stream()
                .map(RolePermissionPO::getPermissionId)
                .collect(Collectors.toList());
        return permissionRepository.selectByIds(permissionIds);
    }

    @Transactional
    @PreAuthorize("Permission#Create")
    public Long createPermission(PermissionPO permissionPO) {
        Throwables.validateRequest(!permissionPO.getSystemDefault(),
                "System default permission cannot be created");
        Throwables.validateRequest(permissionRepository
                        .selectByWrapper(new QueryWrapper<PermissionPO>()
                                .eq(PERMISSION_NAME, permissionPO.getPermissionName()))
                        .isEmpty(),
                "Permission already exists");
        return permissionRepository.create(permissionPO, AuthorizationContext.getUsername()).getId();
    }

    @SuppressWarnings("DataFlowIssue")
    @Transactional
    @PreAuthorize("Permission#Delete")
    public void deletePermission(Long permissionId) {
        Throwables.validateRequest(permissionRepository.exists(permissionId), "Permission not found");
        PermissionPO permissionPO = permissionRepository.selectById(permissionId);
        Throwables.validateRequest(!permissionPO.getSystemDefault(),
                "System default permission cannot be deleted");
        permissionRepository.deleteById(permissionId, AuthorizationContext.getUsername());
    }
}
