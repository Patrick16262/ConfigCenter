package site.patrickshao.admin.biz;

import jakarta.annotation.PostConstruct;
import org.mybatis.spring.annotation.MapperScan;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

// 启用AdminBusiness自动配置

// AdminBusiness模块是AdminService的业务模块
// 在开发时，需要在AdminService的启动类上加上@EnableAdminBusiness注解
// 因为SpringBoot不会扫描还没有打包的项目下的spring.factories文件
@ComponentScan
//@ComponentScan(excludeFilters = @ComponentScan.Filter(type = FilterType.REGEX, pattern = "site.patrickshao.admin.biz.mapper..*"))
@Configuration
@EnableAutoConfiguration
@MapperScan("site.patrickshao.admin.biz.mapper")
public class AdminBusinessAutoConfiguration {
    private static final Logger logger = LoggerFactory.getLogger(AdminBusinessAutoConfiguration.class);

    @PostConstruct
    public void init() {
        logger.debug("AdminBusiness has been loaded.");
    }
}
