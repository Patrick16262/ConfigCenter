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
@TableName("`GrayReleaseRule`")
@GenerateRepository
public class GrayReleaseRulePO extends AbstractBasicFieldsObject {
    @ParentId
    private Long branchId;
    @TableField("`rule`")
    private String rule;
    @TableField("`enabled`")
    private Boolean enabled;
    private Long applicationId;
    private Long namespaceId;

    public Long getBranchId() {
        return branchId;
    }

    public void setBranchId(Long branchId) {
        this.branchId = branchId;
    }

    public String getRule() {
        return rule;
    }

    public void setRule(String rule) {
        this.rule = rule;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
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
        if (!(object instanceof GrayReleaseRulePO that)) return false;
        if (!super.equals(object)) return false;
        return Objects.equals(branchId, that.branchId) && Objects.equals(rule, that.rule) && Objects.equals(enabled, that.enabled) && Objects.equals(applicationId, that.applicationId) && Objects.equals(namespaceId, that.namespaceId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), branchId, rule, enabled, applicationId, namespaceId);
    }

    @Override
    public String toString() {
        return "GrayReleaseRulePO{" +
                "branchId=" + branchId +
                ", rule='" + rule + '\'' +
                ", enabled=" + enabled +
                ", applicationId=" + applicationId +
                ", namespaceId=" + namespaceId +
                "} " + super.toString();
    }
}
