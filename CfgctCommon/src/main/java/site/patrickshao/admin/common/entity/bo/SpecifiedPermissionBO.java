package site.patrickshao.admin.common.entity.bo;

import site.patrickshao.admin.common.entity.PojoWithIdentifier;

import java.io.Serializable;
import java.util.Objects;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/10
 */
public class SpecifiedPermissionBO implements Serializable, PojoWithIdentifier {
    private Long id;
    private Long targetApplication;
    private Long targetNamespace;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getTargetApplication() {
        return targetApplication;
    }

    public void setTargetApplication(Long targetApplication) {
        this.targetApplication = targetApplication;
    }

    public Long getTargetNamespace() {
        return targetNamespace;
    }

    public void setTargetNamespace(Long targetNamespace) {
        this.targetNamespace = targetNamespace;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof SpecifiedPermissionBO that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(targetApplication, that.targetApplication) && Objects.equals(targetNamespace, that.targetNamespace);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, targetApplication, targetNamespace);
    }

    @Override
    public String toString() {
        return "SpecifiedPermissionBO{" +
                "id=" + id +
                ", targetApplication=" + targetApplication +
                ", targetNamespace=" + targetNamespace +
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
