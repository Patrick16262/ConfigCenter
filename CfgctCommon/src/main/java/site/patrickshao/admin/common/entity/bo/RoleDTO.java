package site.patrickshao.admin.common.entity.bo;

import site.patrickshao.admin.common.entity.PojoWithIdentifier;
import site.patrickshao.admin.common.entity.po.PermissionPO;

import java.util.List;
import java.util.Objects;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/12
 */
public class RoleDTO implements PojoWithIdentifier {
    private Long id;
    private String roleName;
    private String parentRoleNames;
    private Boolean applicationSpecification;
    private Boolean namespaceSpecification;
    private List<PermissionPO> permissions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getParentRoleNames() {
        return parentRoleNames;
    }

    public void setParentRoleNames(String parentRoleNames) {
        this.parentRoleNames = parentRoleNames;
    }

    public Boolean getApplicationSpecification() {
        return applicationSpecification;
    }

    public void setSpecifyApplication(Boolean applicationSpecification) {
        this.applicationSpecification = applicationSpecification;
    }

    public Boolean getNamespaceSpecification() {
        return namespaceSpecification;
    }

    public void setSpecifyNamespace(Boolean namespaceSpecification) {
        this.namespaceSpecification = namespaceSpecification;
    }

    public List<PermissionPO> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<PermissionPO> permissions) {
        this.permissions = permissions;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof RoleDTO roleDTO)) return false;
        return Objects.equals(id, roleDTO.id) && Objects.equals(roleName, roleDTO.roleName) && Objects.equals(parentRoleNames, roleDTO.parentRoleNames) && Objects.equals(applicationSpecification, roleDTO.applicationSpecification) && Objects.equals(namespaceSpecification, roleDTO.namespaceSpecification) && Objects.equals(permissions, roleDTO.permissions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roleName, parentRoleNames, applicationSpecification, namespaceSpecification, permissions);
    }

    @Override
    public String toString() {
        return "RoleBO{" +
                "id=" + id +
                ", roleName='" + roleName + '\'' +
                ", parentRoleNames='" + parentRoleNames + '\'' +
                ", applicationSpecification=" + applicationSpecification +
                ", namespaceSpecification=" + namespaceSpecification +
                ", permissions=" + permissions +
                '}';
    }

    /**
     * @return
     */
    @Override
    public Long getPojoIdentifier() {
        return id;
    }
}
