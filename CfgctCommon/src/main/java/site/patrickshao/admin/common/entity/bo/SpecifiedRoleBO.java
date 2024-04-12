package site.patrickshao.admin.common.entity.bo;

import site.patrickshao.admin.common.entity.PojoWithIdentifier;
import site.patrickshao.admin.common.entity.po.RolePO;
import site.patrickshao.admin.common.entity.po.UserRolePO;

import java.util.Objects;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/11
 */
public class SpecifiedRoleBO implements PojoWithIdentifier {
    private Long id;
    private String name;
    private String parentRoleNames;
    private Long applicationId;
    private Long namespaceId;

    public SpecifiedRoleBO() {
    }

    public SpecifiedRoleBO(UserRolePO userRole, RolePO role) {
        id = userRole.getId();
        applicationId = userRole.getApplicationSpecification();
        namespaceId = userRole.getNamespaceSpecification();
        name = role.getRoleName();
        parentRoleNames = role.getParentRoleNames();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getParentRoleNames() {
        return parentRoleNames;
    }

    public void setParentRoleNames(String parentRoleNames) {
        this.parentRoleNames = parentRoleNames;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public Long getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Long namespaceId) {
        this.namespaceId = namespaceId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof SpecifiedRoleBO that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(parentRoleNames, that.parentRoleNames) && Objects.equals(applicationId, that.applicationId) && Objects.equals(namespaceId, that.namespaceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, parentRoleNames, applicationId, namespaceId);
    }

    @Override
    public String toString() {
        return "SpecifiedRoleBO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", parentRoleNames='" + parentRoleNames + '\'' +
                ", applicationId=" + applicationId +
                ", namespaceId=" + namespaceId +
                '}';
    }

    /**
     * @return
     */
    @Override
    public Long getPojoIdentifier() {
        return null;
    }
}
