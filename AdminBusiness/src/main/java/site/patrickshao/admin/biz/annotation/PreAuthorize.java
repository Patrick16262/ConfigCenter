package site.patrickshao.admin.biz.annotation;

import java.lang.annotation.*;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/10
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreAuthorize {
    //行为名称
    String value();
}
