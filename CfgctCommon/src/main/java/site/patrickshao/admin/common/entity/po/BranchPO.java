package site.patrickshao.admin.common.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import site.patrickshao.admin.common.annotation.GenerateRepository;
import site.patrickshao.admin.common.annotation.ParentId;
import site.patrickshao.admin.common.entity.HaveApplicationParent;
import site.patrickshao.admin.common.entity.HaveNamespaceParent;

import java.util.Objects;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/9
 */
@TableName("`Branch`")
@GenerateRepository
public class BranchPO extends AbstractFullFieldsObject implements HaveApplicationParent, HaveNamespaceParent {
    @TableField("Name")
    private String name;
    @ParentId(ClusterPO.class)
    private Long clusterId;
    @ParentId(NamespacePO.class)
    private Long namespaceId;
    private Long branchHead;
    private String branchType;
    private Long applicationId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getClusterId() {
        return clusterId;
    }

    public void setClusterId(Long clusterId) {
        this.clusterId = clusterId;
    }

    @Override
    public Long getNamespaceId() {
        return namespaceId;
    }

    @Override
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

    @Override
    public Long getApplicationId() {
        return applicationId;
    }

    @Override
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
