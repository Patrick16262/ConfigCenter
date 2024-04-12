import database.DataBaseTestAutoConfiguration;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import site.patrickshao.admin.biz.AdminBusinessAutoConfiguration;
import site.patrickshao.admin.biz.service.AuthorizationService;
import site.patrickshao.admin.common.entity.bo.AuthorizationContextBO;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/11
 */
@SpringBootTest(classes = AdminBusinessAutoConfiguration.class)
@Import(DataBaseTestAutoConfiguration.class)
public class AuthorizationServiceTest {
    @Autowired
    private AuthorizationService authorizationService;
    @Test
    public void test() {

//        System.out.println(authorizationService.checkPermission());
    }

}
