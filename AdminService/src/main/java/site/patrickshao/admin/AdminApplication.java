package site.patrickshao.admin;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import site.patrickshao.admin.biz.annotation.EnableAdminBusiness;

@EnableAdminBusiness
@SpringBootApplication
public class AdminApplication  {
    public static void main(String[] args) {
        ApplicationContext context = SpringApplication.run(AdminApplication.class, args);
    }
}
