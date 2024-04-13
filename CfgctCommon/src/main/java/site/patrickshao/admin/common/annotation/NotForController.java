package site.patrickshao.admin.common.annotation;

import java.lang.annotation.*;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/12
 */
@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface NotForController {
}
