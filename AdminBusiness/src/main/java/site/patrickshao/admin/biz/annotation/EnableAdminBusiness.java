package site.patrickshao.admin.biz.annotation;


import org.springframework.context.annotation.Import;
import site.patrickshao.admin.biz.AdminBusinessAutoConfiguration;

import java.lang.annotation.*;

/**
 *  启用AdminBusiness自动配置
 *  在把项目打包成jar包前，需要在项目的启动类上加上@EnableAdminBusiness注解
 *  因为SpringBoot不会扫描还没有打包的项目下的spring.factories文件
 * @author Shao Yibo
 * @date 2024/4/4
 */
@Inherited
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(AdminBusinessAutoConfiguration.class)
public @interface EnableAdminBusiness {
}
