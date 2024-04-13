package site.patrickshao.admin.biz.aop;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import site.patrickshao.admin.biz.annotation.PreAuthorize;
import site.patrickshao.admin.biz.secure.AuthorizationContext;
import site.patrickshao.admin.biz.service.AuthorizationService;
import site.patrickshao.admin.common.exception.InvalidAuthorizationContextException;
import site.patrickshao.admin.common.exception.http.Http401Unauthorized;
import site.patrickshao.admin.common.utils.ReflectUtils;
import site.patrickshao.admin.common.utils.Throwables;

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

    @Autowired
    private AuthorizationService authorizationService;

    @Pointcut("@annotation(site.patrickshao.admin.biz.annotation.PreAuthorize)")
    private void pointcut() {
    }

    @Before("pointcut()")
    public void validatePermission(JoinPoint joinPoint) {
        Field field = ReflectUtils.getField(joinPoint.getTarget().getClass(),
                joinPoint.getSignature().getName());
        String actionName = field.getAnnotation(PreAuthorize.class).value();
        AuthorizationContext.setActionName(actionName);
        if (!authorizationService.checkPermission()) {
            throw new Http401Unauthorized();
        }
    }

    public static void removeCurrentAuthorizationContext() {
        AuthorizationContext.destroy();
    }

}
