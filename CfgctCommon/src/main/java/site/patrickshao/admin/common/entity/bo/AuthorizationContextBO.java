package site.patrickshao.admin.common.entity.bo;

import site.patrickshao.admin.common.entity.PojoWithIdentifier;

import javax.annotation.Nullable;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/11
 */
public class AuthorizationContextBO implements Serializable, PojoWithIdentifier {
    private Long userId;
    private String username;
    @Nullable
    private Long targetApplicationId;
    @Nullable
    private Long targetNamespaceId;
    private String actionName;

    public AuthorizationContextBO() {
    }

    public AuthorizationContextBO(Long userId, @jakarta.annotation.Nullable Long targetApplicationId, @jakarta.annotation.Nullable Long targetNamespaceId) {
        this.userId = userId;
        this.targetApplicationId = targetApplicationId;
        this.targetNamespaceId = targetNamespaceId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    @jakarta.annotation.Nullable
    public Long getTargetApplicationId() {
        return targetApplicationId;
    }

    public void setTargetApplicationId(@jakarta.annotation.Nullable Long targetApplicationId) {
        this.targetApplicationId = targetApplicationId;
    }

    @jakarta.annotation.Nullable
    public Long getTargetNamespaceId() {
        return targetNamespaceId;
    }

    public void setTargetNamespaceId(@jakarta.annotation.Nullable Long targetNamespaceId) {
        this.targetNamespaceId = targetNamespaceId;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * @return
     */
    @Override
    public Long getPojoIdentifier() {
        return userId;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof AuthorizationContextBO that)) return false;
        return Objects.equals(userId, that.userId) && Objects.equals(username, that.username) && Objects.equals(targetApplicationId, that.targetApplicationId) && Objects.equals(targetNamespaceId, that.targetNamespaceId) && Objects.equals(actionName, that.actionName);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, targetApplicationId, targetNamespaceId, actionName);
    }

    @Override
    public String toString() {
        return "AuthorizationContextBO{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", targetApplicationId=" + targetApplicationId +
                ", targetNamespaceId=" + targetNamespaceId +
                ", actionName='" + actionName + '\'' +
                '}';
    }
}
