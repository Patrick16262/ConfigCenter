package site.patrickshao.admin.common.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableLogic;

import java.util.Date;
import java.util.Objects;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/9
 */
public abstract class AbstractBasicFieldsObject extends AbstractPersistObject {
    @TableField("`comment`")
    private String comment;
    @TableLogic
    @TableField("`deleted`")
    private Boolean deleted;
    @TableField("`deletedAt`")
    private Date deletedAt;
    @TableField("`createdBy`")
    private String createdBy;
    @TableField("`createTime`")
    private Date createTime;

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof AbstractBasicFieldsObject that)) return false;
        if (!super.equals(object)) return false;
        return Objects.equals(comment, that.comment) && Objects.equals(deleted, that.deleted) && Objects.equals(deletedAt, that.deletedAt) && Objects.equals(createdBy, that.createdBy) && Objects.equals(createTime, that.createTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), comment, deleted, deletedAt, createdBy, createTime);
    }

    @Override
    public String toString() {
        return "BaseCommonPersistObject{" +
                "comment='" + comment + '\'' +
                ", deleted=" + deleted +
                ", deletedAt=" + deletedAt +
                ", createdBy='" + createdBy + '\'' +
                ", createTime='" + createTime + '\'' +
                "} " + super.toString();
    }
}
