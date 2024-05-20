package site.patrickshao.admin.common.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import site.patrickshao.admin.common.annotation.GenerateRepository;
import site.patrickshao.admin.common.annotation.ParentId;
import site.patrickshao.admin.common.entity.HaveApplicationParent;

import java.util.Objects;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/9
 */
@TableName("`AccessToken`")
@GenerateRepository
public class AccessTokenPO extends AbstractBasicFieldsObject implements HaveApplicationParent {
    @ParentId
    private Long applicationId;
    private String applicationName;
    @TableField("`Token`")
    private String token;
    private Boolean enabled;
    @Override
    public Long getApplicationId() {
        return applicationId;
    }
    @Override

    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
    }
    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof AccessTokenPO that)) return false;
        if (!super.equals(object)) return false;
        return Objects.equals(applicationId, that.applicationId) && Objects.equals(applicationName, that.applicationName) && Objects.equals(token, that.token) && Objects.equals(enabled, that.enabled);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), applicationId, applicationName, token, enabled);
    }

    @Override
    public String toString() {
        return "AccessTokenPO{" +
                "applicationId=" + applicationId +
                ", applicationName='" + applicationName + '\'' +
                ", token='" + token + '\'' +
                ", enabled=" + enabled +
                "} " + super.toString();
    }
}
