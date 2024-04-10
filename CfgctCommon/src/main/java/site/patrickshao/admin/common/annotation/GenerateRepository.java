package site.patrickshao.admin.common.annotation;

import java.lang.annotation.*;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/10
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
public @interface GenerateRepository {
}
