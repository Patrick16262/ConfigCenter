package site.patrickshao.admin.biz.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import site.patrickshao.admin.biz.annotation.PreAuthorize;
import site.patrickshao.admin.biz.service.AuthorizationService;
import site.patrickshao.admin.common.exception.Http401Unauthorized;
import site.patrickshao.admin.common.utils.ReflectUtils;

import java.lang.reflect.Field;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/10
 */
@SuppressWarnings("SpringJavaAutowiredFieldsWarningInspection")
@Component
@Aspect
public class AuthorizeAspect {
    private static final ThreadLocal<Long> currentUserId = new ThreadLocal<>();
    @Autowired
    private AuthorizationService authorizationService;

    @Pointcut("@annotation(site.patrickshao.admin.biz.annotation.PreAuthorize)")
    private void pointcut() {
    }

    @Before("pointcut()")
    public void validatePermission(JoinPoint joinPoint) {
        Field field = ReflectUtils.getField(joinPoint.getTarget().getClass(),
                joinPoint.getSignature().getName());
        assert field != null;
        String actionName = field.getAnnotation(PreAuthorize.class).value();
        if (!authorizationService.checkIfHavePermission(currentUserId.get(), actionName)) {
            throw new Http401Unauthorized();
        }
    }

    public static void removeCurrentAuthorizationContext() {
        currentUserId.remove();
    }

}
