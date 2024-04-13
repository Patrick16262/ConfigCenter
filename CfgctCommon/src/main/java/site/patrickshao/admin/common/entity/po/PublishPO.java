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
@TableName("`Publish`")
@GenerateRepository
public class PublishPO extends AbstractBasicFieldsObject implements HaveApplicationParent, HaveNamespaceParent {
    @TableField("`name`")
    private String name;
    @ParentId
    private Long branchId;
    private Boolean workspace;
    @TableField("`authorizer`")
    private String authorizer;
    private Long previousId;
    private Long nextId;
    private Long applicationId;
    private Long namespaceId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public Boolean getWorkspace() {
        return workspace;
    }

    public void setWorkspace(Boolean workspace) {
        this.workspace = workspace;
    }

    public String getAuthorizer() {
        return authorizer;
    }

    public void setAuthorizer(String authorizer) {
        this.authorizer = authorizer;
    }

    public Long getPreviousId() {
        return previousId;
    }

    public void setPreviousId(Long previousId) {
        this.previousId = previousId;
    }

    public Long getNextId() {
        return nextId;
    }

    public void setNextId(Long nextId) {
        this.nextId = nextId;
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
    public Long getNamespaceId() {
        return namespaceId;
    }

    @Override
    public void setNamespaceId(Long namespaceId) {
        this.namespaceId = namespaceId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof PublishPO publishPO)) return false;
        if (!super.equals(object)) return false;
        return Objects.equals(name, publishPO.name) && Objects.equals(branchId, publishPO.branchId) && Objects.equals(workspace, publishPO.workspace) && Objects.equals(authorizer, publishPO.authorizer) && Objects.equals(previousId, publishPO.previousId) && Objects.equals(nextId, publishPO.nextId) && Objects.equals(applicationId, publishPO.applicationId) && Objects.equals(namespaceId, publishPO.namespaceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, branchId, workspace, authorizer, previousId, nextId, applicationId, namespaceId);
    }

    @Override
    public String toString() {
        return "PublishPO{" +
                "name='" + name + '\'' +
                ", branchId=" + branchId +
                ", workSpace=" + workspace +
                ", authorizer='" + authorizer + '\'' +
                ", previousId=" + previousId +
                ", nextId=" + nextId +
                ", applicationId=" + applicationId +
                ", namespaceId=" + namespaceId +
                "} " + super.toString();
    }
}
