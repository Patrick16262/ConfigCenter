package site.patrickshao.admin.common.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import site.patrickshao.admin.common.annotation.GenerateRepository;
import site.patrickshao.admin.common.annotation.ParentId;

import java.util.Objects;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/9
 */
@TableName("`Modification`")
@GenerateRepository
public class ModificationPO extends AbstractBasicFieldsObject {
    @TableField("`value`")
    private String value;
    @TableField("`key`")
    private String key;
    @TableField("`operation`")
    private String operation;
    @ParentId
    private Long publishId;
    private Long applicationId;
    private Long namespaceId;

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public Long getPublishId() {
        return publishId;
    }

    public void setPublishId(Long publishId) {
        this.publishId = publishId;
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
        if (!(object instanceof ModificationPO that)) return false;
        if (!super.equals(object)) return false;
        return Objects.equals(value, that.value) && Objects.equals(key, that.key) && Objects.equals(operation, that.operation) && Objects.equals(publishId, that.publishId) && Objects.equals(applicationId, that.applicationId) && Objects.equals(namespaceId, that.namespaceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), value, key, operation, publishId, applicationId, namespaceId);
    }

    @Override
    public String toString() {
        return "ModificationPO{" +
                "value='" + value + '\'' +
                ", key='" + key + '\'' +
                ", operation='" + operation + '\'' +
                ", publishId=" + publishId +
                ", applicationId=" + applicationId +
                ", namespaceId=" + namespaceId +
                "} " + super.toString();
    }
}
