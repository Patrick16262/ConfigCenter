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
@TableName("`Item`")
@GenerateRepository
public class ItemPO extends AbstractFullFieldsObject {
    @ParentId
    private Long branchId;
    @TableField("`value`")
    private String value;
    @TableField("`key`")
    private String key;
    private Long applicationId;
    private Long namespaceId;

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

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
        if (!(object instanceof ItemPO itemPO)) return false;
        if (!super.equals(object)) return false;
        return Objects.equals(branchId, itemPO.branchId) && Objects.equals(value, itemPO.value) && Objects.equals(key, itemPO.key) && Objects.equals(applicationId, itemPO.applicationId) && Objects.equals(namespaceId, itemPO.namespaceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), branchId, value, key, applicationId, namespaceId);
    }

    @Override
    public String toString() {
        return "ItemPO{" +
                "branchId=" + branchId +
                ", value='" + value + '\'' +
                ", key='" + key + '\'' +
                ", applicationId=" + applicationId +
                ", namespaceId=" + namespaceId +
                "} " + super.toString();
    }
}
