package site.patrickshao.admin.common.annotation;

import java.lang.annotation.*;

/**
 *
 * @author Shao Yibo
 * @date 2024/4/4
 */


@Target({ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface OrderedField {
}
