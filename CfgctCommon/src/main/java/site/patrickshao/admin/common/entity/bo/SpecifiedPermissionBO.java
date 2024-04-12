package site.patrickshao.admin.common.entity.bo;

import site.patrickshao.admin.common.entity.PojoWithIdentifier;
import site.patrickshao.admin.common.entity.po.PermissionPO;

import java.util.Objects;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/11
 */
public class SpecifiedPermissionBO implements PojoWithIdentifier {
    private Long id;
    private String name;
    private Long applicationId;
    private Long namespaceId;

    public SpecifiedPermissionBO() {
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
    public String toString() {
        return "SpecifiedPermissionBO{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", applicationId=" + applicationId +
                ", namespaceId=" + namespaceId +
                '}';
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof SpecifiedPermissionBO that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name) && Objects.equals(applicationId, that.applicationId) && Objects.equals(namespaceId, that.namespaceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, applicationId, namespaceId);
    }

    /**
     * @return
     */
    @Override
    public Long getPojoIdentifier() {
        return id;
    }
}
