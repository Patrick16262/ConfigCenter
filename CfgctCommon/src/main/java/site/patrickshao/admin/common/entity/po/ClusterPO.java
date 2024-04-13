package site.patrickshao.admin.common.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import site.patrickshao.admin.common.annotation.GenerateRepository;
import site.patrickshao.admin.common.annotation.ParentId;
import site.patrickshao.admin.common.entity.HaveApplicationParent;

import java.util.Date;
import java.util.Objects;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/9
 */
@TableName("`Cluster`")
@GenerateRepository
public class ClusterPO extends AbstractFullFieldsObject implements HaveApplicationParent {
    @ParentId
    private Long applicationId;
    @TableField("`Name`")
    private String name;
    private Boolean deferredDelete;
    private Date tobeDeletedAt;

    @Override
    public Long getApplicationId() {
        return applicationId;
    }

    @Override
    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getDeferredDelete() {
        return deferredDelete;
    }

    public void setDeferredDelete(Boolean deferredDelete) {
        this.deferredDelete = deferredDelete;
    }

    public Date getTobeDeletedAt() {
        return tobeDeletedAt;
    }

    public void setTobeDeletedAt(Date tobeDeletedAt) {
        this.tobeDeletedAt = tobeDeletedAt;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ClusterPO clusterPO)) return false;
        if (!super.equals(object)) return false;
        return Objects.equals(applicationId, clusterPO.applicationId) && Objects.equals(name, clusterPO.name) && Objects.equals(deferredDelete, clusterPO.deferredDelete) && Objects.equals(tobeDeletedAt, clusterPO.tobeDeletedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), applicationId, name, deferredDelete, tobeDeletedAt);
    }

    @Override
    public String toString() {
        return "ClusterPO{" +
                "applicationId=" + applicationId +
                ", name='" + name + '\'' +
                ", deferredDelete=" + deferredDelete +
                ", tobeDeletedAt=" + tobeDeletedAt +
                "} " + super.toString();
    }
}
