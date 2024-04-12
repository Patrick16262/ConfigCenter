package site.patrickshao.admin.common.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import site.patrickshao.admin.common.annotation.GenerateRepository;
import site.patrickshao.admin.common.annotation.ParentId;

import java.util.Objects;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/9
 */
@TableName("`RolePermission`")
@GenerateRepository
public class RolePermissionPO extends AbstractBasicFieldsObject {
    @ParentId
    private Long roleId;
    private Long permissionId;
    private Boolean namespaceSpecification;
    private Boolean applicationSpecification;

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getPermissionId() {
        return permissionId;
    }

    public void setPermissionId(Long permissionId) {
        this.permissionId = permissionId;
    }

    public Boolean getNamespaceSpecification() {
        return namespaceSpecification;
    }

    public void setNamespaceSpecification(Boolean namespaceSpecification) {
        this.namespaceSpecification = namespaceSpecification;
    }

    public Boolean getApplicationSpecification() {
        return applicationSpecification;
    }

    public void setApplicationSpecification(Boolean applicationSpecification) {
        this.applicationSpecification = applicationSpecification;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof RolePermissionPO that)) return false;
        if (!super.equals(object)) return false;
        return Objects.equals(roleId, that.roleId) && Objects.equals(permissionId, that.permissionId) && Objects.equals(namespaceSpecification, that.namespaceSpecification) && Objects.equals(applicationSpecification, that.applicationSpecification);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), roleId, permissionId, namespaceSpecification, applicationSpecification);
    }

    @Override
    public String toString() {
        return "RolePermissionPO{" +
                "roleId=" + roleId +
                ", permissionId=" + permissionId +
                ", namespaceSpecification=" + namespaceSpecification +
                ", applicationSpecification=" + applicationSpecification +
                "} " + super.toString();
    }
}
