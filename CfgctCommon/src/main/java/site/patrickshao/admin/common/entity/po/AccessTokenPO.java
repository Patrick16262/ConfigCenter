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

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof AccessTokenPO that)) return false;
        if (!super.equals(object)) return false;
        return Objects.equals(applicationId, that.applicationId) && Objects.equals(applicationName, that.applicationName) && Objects.equals(token, that.token);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), applicationId, applicationName, token);
    }

    @Override
    public String toString() {
        return "AccessTokenPO{" +
                "applicationId=" + applicationId +
                ", applicationName='" + applicationName + '\'' +
                ", token='" + token + '\'' +
                "} " + super.toString();
    }
}
