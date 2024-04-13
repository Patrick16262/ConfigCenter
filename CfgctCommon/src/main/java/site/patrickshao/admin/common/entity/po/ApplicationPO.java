package site.patrickshao.admin.common.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import jakarta.validation.constraints.NotNull;
import site.patrickshao.admin.common.annotation.GenerateRepository;
import site.patrickshao.admin.common.entity.HaveApplicationParent;

import java.util.Date;
import java.util.Objects;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/9
 */
@TableName("`Application`")
@GenerateRepository
public class ApplicationPO extends AbstractFullFieldsObject implements HaveApplicationParent {
    private String applicationName;
    private String nickName;
    @NotNull
    private Boolean deferredDelete;
    private Date tobeDeletedAt;

    public String getApplicationName() {
        return applicationName;
    }

    public void setApplicationName(String applicationName) {
        this.applicationName = applicationName;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
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
        if (!(object instanceof ApplicationPO that)) return false;
        if (!super.equals(object)) return false;
        return Objects.equals(applicationName, that.applicationName) && Objects.equals(nickName, that.nickName) && Objects.equals(deferredDelete, that.deferredDelete) && Objects.equals(tobeDeletedAt, that.tobeDeletedAt);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), applicationName, nickName, deferredDelete, tobeDeletedAt);
    }

    @Override
    public String toString() {
        return "ApplicationPO{" +
                "applicationName='" + applicationName + '\'' +
                ", nickName='" + nickName + '\'' +
                ", deferredDelete=" + deferredDelete +
                ", tobeDeletedAt=" + tobeDeletedAt +
                "} " + super.toString();
    }

    /**
     * @return
     */
    @Override
    public Long getApplicationId() {
        return getId();
    }

    /**
     * @param applicationId
     */
    @Override
    public void setApplicationId(Long applicationId) {
        setId(applicationId);
    }
}
