package site.patrickshao.admin.common.entity.po;

import java.util.Date;
import java.util.Objects;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/9
 */
public abstract class AbstractFullFieldsObject extends AbstractBasicFieldsObject {
    private String lastModifiedBy;
    private Date lastModifyTime;

    public String getLastModifiedBy() {
        return lastModifiedBy;
    }

    public void setLastModifiedBy(String lastModifiedBy) {
        this.lastModifiedBy = lastModifiedBy;
    }

    public Date getLastModifyTime() {
        return lastModifyTime;
    }

    public void setLastModifyTime(Date lastModifyTime) {
        this.lastModifyTime = lastModifyTime;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof AbstractFullFieldsObject that)) return false;
        if (!super.equals(object)) return false;
        return Objects.equals(lastModifiedBy, that.lastModifiedBy) && Objects.equals(lastModifyTime, that.lastModifyTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), lastModifiedBy, lastModifyTime);
    }

    @Override
    public String toString() {
        return "BaseUpdatablePersistObject{" +
                "lastModifiedBy='" + lastModifiedBy + '\'' +
                ", lastModifyTime=" + lastModifyTime +
                "} " + super.toString();
    }
}
