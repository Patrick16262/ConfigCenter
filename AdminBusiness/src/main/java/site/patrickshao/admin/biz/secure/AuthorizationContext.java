package site.patrickshao.admin.biz.secure;

import jakarta.annotation.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.patrickshao.admin.biz.service.AuthorizationService;
import site.patrickshao.admin.common.entity.bo.AuthorizationContextBO;
import site.patrickshao.admin.common.entity.dto.SpecifiedUserDTO;
import site.patrickshao.admin.common.entity.po.AccessTokenPO;

/**
 * 几乎所有业务类/控制器/鉴权授权类都会用到的一个类，用于存储当前请求的上下文信息
 * 通过ThreadLocal实现
 *
 * @author Shao Yibo
 * @description
 * @date 2024/4/11
 */
public class AuthorizationContext {
    private static final Logger log = LoggerFactory.getLogger(AuthorizationService.class);
    private static final ThreadLocal<AuthorizationContextBO> authorizationContext = new ThreadLocal<>();

    public static void init() {
        log.debug("init authorization context");
        authorizationContext.set(new AuthorizationContextBO());
    }

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

    public static SpecifiedUserDTO getSpecifiedUserDTO() {
        return authorizationContext.get().getSpecifiedUserDTO();
    }

    public static void setTargetApplicationId(@Nullable Long targetApplicationId) {
        authorizationContext.get().setTargetApplicationId(targetApplicationId);
    }

    @Nullable
    public static Long getTargetNamespaceId() {
        return authorizationContext.get().getTargetNamespaceId();
    }

    public static void setTargetNamespaceId(@Nullable Long targetNamespaceId) {
        log.debug("set target namespace id: " + targetNamespaceId);
        authorizationContext.get().setTargetNamespaceId(targetNamespaceId);
    }

    public static String getActionName() {
        return authorizationContext.get().getActionName();
    }

    public static void setActionName(String actionName) {
        log.debug("set action name: " + actionName);
        authorizationContext.get().setActionName(actionName);
    }

    public static String getUsername() {
        return authorizationContext.get().getUsername();
    }

    public static void setUsername(String username) {
        log.debug("set username: " + username);
        authorizationContext.get().setUsername(username);
    }

    public static String debugString() {
        return authorizationContext.get().toString();
    }

    public static void setSpecifiedUserDTO(SpecifiedUserDTO specifiedUserDTO) {
        log.debug("set specified user dto: " + specifiedUserDTO);
        authorizationContext.get().setSpecifiedUserDTO(specifiedUserDTO);
    }

    public static AccessTokenPO getAuthorizationContextBO() {
        return authorizationContext.get().getAccessTokenPO();
    }

    public static void setAccessTokenPO (AccessTokenPO accessTokenPO) {
        log.debug("set access token po: " + accessTokenPO);
        authorizationContext.get().setAccessTokenPO(accessTokenPO);
    }

    public static void destroy() {
        authorizationContext.remove();
    }

    private AuthorizationContext() {
    }
}
