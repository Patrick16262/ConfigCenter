package site.patrickshao.admin.biz.aop;


import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.ApplicationContext;
import site.patrickshao.admin.biz.annotation.NeedInjectMapper;
import site.patrickshao.admin.common.utils.ReflectUtils;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 这个切面类是为了解决Bean A需要Bean B，但是Bean B需要Bean A造成的循环依赖问题
 * 这个切面会代理所有被@NeedInject注解的类
 * 在代理类被第一次调用时，将会检查这个类中的所有字段，如果字段的类型是所需要的类型，那么将会注入这个类的实例
 * 以下样例代码将自动注入basicRepository字段：
 * <pre>
 * `@NeedInject({BasicRepository.class})
 *  public class BasicService {
 *     private BasicRepository basicRepository;
 * }
 * </pre>
 *
 * @author Shao Yibo
 * @date 2024/4/3
 */

@Aspect
//@Component
public class InjectionAspect {

    private final Map<Object, Object> classSet = new ConcurrentHashMap<>();
    private final ApplicationContext applicationContext;

    public InjectionAspect(ApplicationContext applicationContext) {
        this.applicationContext = applicationContext;
    }

    @Pointcut("@within(site.patrickshao.admin.biz.annotation.NeedInjectMapper)")
    public void needInjectMapper() {
    }

    @Before("needInjectMapper()")
    public void checkAndInject(JoinPoint joinPoint) {
        if (classSet.containsKey(joinPoint.getTarget())) {
            return;
        }
        Object target = joinPoint.getTarget();
        classSet.put(target, new Object());
        NeedInjectMapper annotation = target.getClass().getAnnotation(NeedInjectMapper.class);
        Field[] fields = target.getClass().getDeclaredFields();
        for (Field field : fields) {
            Class<?> type = field.getType();
            if (ReflectUtils.getFieldValue(target, field) != null) {
                continue;
            }
            field.setAccessible(true);
            Object bean = applicationContext.getBean(type);
            ReflectUtils.setFieldValue(target, field, bean);
        }
    }
}
