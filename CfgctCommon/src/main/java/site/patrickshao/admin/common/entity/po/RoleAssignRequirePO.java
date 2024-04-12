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
@TableName("`RoleAssignRequire`")
@GenerateRepository
public class RoleAssignRequirePO extends AbstractBasicFieldsObject {
    @ParentId
    private Long roleId;
    private Long permissionId;
    private Boolean applicationSpecification;
    private Boolean namespaceSpecification;

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

    public Boolean getApplicationSpecification() {
        return applicationSpecification;
    }

    public void setApplicationSpecification(Boolean applicationSpecification) {
        this.applicationSpecification = applicationSpecification;
    }

    public Boolean getNamespaceSpecification() {
        return namespaceSpecification;
    }

    public void setNamespaceSpecification(Boolean namespaceSpecification) {
        this.namespaceSpecification = namespaceSpecification;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof RoleAssignRequirePO that)) return false;
        if (!super.equals(object)) return false;
        return Objects.equals(roleId, that.roleId) && Objects.equals(permissionId, that.permissionId) && Objects.equals(applicationSpecification, that.applicationSpecification) && Objects.equals(namespaceSpecification, that.namespaceSpecification);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), roleId, permissionId, applicationSpecification, namespaceSpecification);
    }

    @Override
    public String toString() {
        return "RoleAssignRequirePO{" +
                "roleId=" + roleId +
                ", permissionId=" + permissionId +
                ", applicationSpecification=" + applicationSpecification +
                ", namespaceSpecification=" + namespaceSpecification +
                "} " + super.toString();
    }
}
