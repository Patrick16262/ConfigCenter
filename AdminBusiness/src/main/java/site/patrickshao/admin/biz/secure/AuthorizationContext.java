package site.patrickshao.admin.biz.secure;

import jakarta.annotation.Nullable;
import site.patrickshao.admin.common.entity.bo.AuthorizationContextBO;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/11
 */
public class AuthorizationContext {
    private static final ThreadLocal<AuthorizationContextBO> authorizationContext = new ThreadLocal<>();

    public static Long getPojoIdentifier() {
        return authorizationContext.get().getPojoIdentifier();
    }

    public static Long getUserId() {
        return authorizationContext.get().getUserId();
    }

    public static void setUserId(Long userId) {
        authorizationContext.get().setUserId(userId);
    }

    @Nullable
    public static Long getTargetApplicationId() {
        return authorizationContext.get().getTargetApplicationId();
    }

    public static void setTargetApplicationId(@Nullable Long targetApplicationId) {
        authorizationContext.get().setTargetApplicationId(targetApplicationId);
    }

    @Nullable
    public static Long getTargetNamespaceId() {
        return authorizationContext.get().getTargetNamespaceId();
    }

    public static void setTargetNamespaceId(@Nullable Long targetNamespaceId) {
        authorizationContext.get().setTargetNamespaceId(targetNamespaceId);
    }

    public static String getActionName() {
        return authorizationContext.get().getActionName();
    }

    public static void setActionName(String actionName) {
        authorizationContext.get().setActionName(actionName);
    }

    public static String getUsername() {
        return authorizationContext.get().getUsername();
    }

    public static void setUsername(String username) {
        authorizationContext.get().setUsername(username);
    }

    public static String debugString() {
        return authorizationContext.get().toString();
    }

    public static void destory() {
        authorizationContext.remove();
    }

    private AuthorizationContext() {
    }
}
