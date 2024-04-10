package site.patrickshao.admin.common.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import site.patrickshao.admin.common.annotation.GenerateRepository;
import site.patrickshao.admin.common.annotation.PartitionField;

import java.util.Objects;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/9
 */
@TableName("`Branch`")
@GenerateRepository
public class BranchPO extends AbstractFullFieldsObject {
    @TableField("Name")
    private Long name;
    @PartitionField
    private Long clusterId;
    @PartitionField
    private Long namespaceId;
    private Long branchHead;
    private String branchType;
    private Long applicationId;

    public Long getName() {
        return name;
    }

    public void setName(Long name) {
        this.name = name;
    }

    public Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(Long clusterId) {
        this.clusterId = clusterId;
    }

    public Long getNamespaceId() {
        return namespaceId;
    }

    public void setNamespaceId(Long namespaceId) {
        this.namespaceId = namespaceId;
    }

    public Long getBranchHead() {
        return branchHead;
    }

    public void setBranchHead(Long branchHead) {
        this.branchHead = branchHead;
    }

    public String getBranchType() {
        return branchType;
    }

    public void setBranchType(String branchType) {
        this.branchType = branchType;
    }

    public Long getApplicationId() {
        return applicationId;
    }

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof BranchPO branchPO)) return false;
        if (!super.equals(object)) return false;
        return Objects.equals(name, branchPO.name) && Objects.equals(clusterId, branchPO.clusterId) && Objects.equals(namespaceId, branchPO.namespaceId) && Objects.equals(branchHead, branchPO.branchHead) && Objects.equals(branchType, branchPO.branchType) && Objects.equals(applicationId, branchPO.applicationId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, clusterId, namespaceId, branchHead, branchType, applicationId);
    }

    @Override
    public String toString() {
        return "BranchPO{" +
                "name=" + name +
                ", clusterId=" + clusterId +
                ", namespaceId=" + namespaceId +
                ", branchHead=" + branchHead +
                ", branchType='" + branchType + '\'' +
                ", applicationId=" + applicationId +
                "} " + super.toString();
    }
}
