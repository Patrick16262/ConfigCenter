package site.patrickshao.admin.biz.annotation;

import java.lang.annotation.*;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/10
 */
@Inherited
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface PreAuthorize {
    //行为名称, actionName
    //具体行为名称见数据库表action
    String value();
}
