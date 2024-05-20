package site.patrickshao.admin.common.entity.bo;

import site.patrickshao.admin.common.entity.PojoWithIdentifier;
import site.patrickshao.admin.common.entity.dto.SpecifiedUserDTO;
import site.patrickshao.admin.common.entity.po.AccessTokenPO;

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
    private SpecifiedUserDTO specifiedUserDTO;
    private AccessTokenPO accessTokenPO;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Nullable
    public Long getTargetApplicationId() {
        return targetApplicationId;
    }

    public void setTargetApplicationId(@Nullable Long targetApplicationId) {
        this.targetApplicationId = targetApplicationId;
    }

    @Nullable
    public Long getTargetNamespaceId() {
        return targetNamespaceId;
    }

    public void setTargetNamespaceId(@Nullable Long targetNamespaceId) {
        this.targetNamespaceId = targetNamespaceId;
    }

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public SpecifiedUserDTO getSpecifiedUserDTO() {
        return specifiedUserDTO;
    }

    public void setSpecifiedUserDTO(SpecifiedUserDTO specifiedUserDTO) {
        this.specifiedUserDTO = specifiedUserDTO;
    }

    public void setAccessTokenPO(AccessTokenPO accessTokenPO) {
        this.accessTokenPO = accessTokenPO;
    }

    public AccessTokenPO getAccessTokenPO() {
        return accessTokenPO;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof AuthorizationContextBO that)) return false;
        return Objects.equals(userId, that.userId) && Objects.equals(username, that.username) && Objects.equals(targetApplicationId, that.targetApplicationId) && Objects.equals(targetNamespaceId, that.targetNamespaceId) && Objects.equals(actionName, that.actionName) && Objects.equals(specifiedUserDTO, that.specifiedUserDTO);
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, username, targetApplicationId, targetNamespaceId, actionName, specifiedUserDTO, accessTokenPO);
    }

    @Override
    public String toString() {
        return "AuthorizationContextBO{" +
                "userId=" + userId +
                ", username='" + username + '\'' +
                ", targetApplicationId=" + targetApplicationId +
                ", targetNamespaceId=" + targetNamespaceId +
                ", actionName='" + actionName + '\'' +
                ", specifiedUserDTO=" + specifiedUserDTO +
                ", accessTokenPO=" + accessTokenPO +
                '}';
    }

    /**
     * @return
     */
    @Override
    public Long getPojoIdentifier() {
        return userId;
    }
}
