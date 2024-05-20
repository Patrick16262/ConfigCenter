import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.event.annotation.BeforeTestClass;
import site.patrickshao.admin.biz.AdminBusinessAutoConfiguration;
import site.patrickshao.admin.biz.secure.AuthorizationContext;
import site.patrickshao.admin.biz.service.AuthorizationService;
import site.patrickshao.admin.biz.service.PermissionService;
import site.patrickshao.admin.biz.service.UserService;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/18
 */
@SpringBootTest(classes = AdminBusinessAutoConfiguration.class)
public class ApplicationServiceTest {
    //认证和授权信息信息
    @Autowired
    public UserService userService;
    @BeforeTestClass
    public void init() {
        AuthorizationContext.setUsername("root");
        AuthorizationContext.setUserId(1L);
        AuthorizationContext.setSpecifiedUserDTO(userService.getUserDTO(1L));
        AuthorizationContext.s
    }
}
