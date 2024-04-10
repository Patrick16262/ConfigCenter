package site.patrickshao.admin.biz.annotation;

import org.springframework.context.annotation.Import;
import site.patrickshao.admin.biz.aop.InjectionAspect;

import java.lang.annotation.*;


/**
 * 需要注入的注解
 * 在需要注入的类上加上此注解
 * value为需要注入的字段的类
 * 例如以下代码会自动注入BasicRepository：
 * <pre>
 * `@NeedInject({BasicRepository.class})
 * public class BasicService {
 * private BasicRepository basicRepository;
 * }
 * </pre>
 *
 * @author Shao Yibo
 * @date 2024/4/3
 */

@Inherited
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Import(InjectionAspect.class)
public @interface NeedInjectMapper {
}
