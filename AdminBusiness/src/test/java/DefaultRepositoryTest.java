import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import site.patrickshao.admin.biz.AdminBusinessAutoConfiguration;
import site.patrickshao.admin.biz.repository.DefaultRepository;
import site.patrickshao.admin.biz.test.TestAutoConfiguration;
import site.patrickshao.admin.common.entity.po.RolePermissionPO;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/10
 */
@Configuration
@SpringBootTest(classes = AdminBusinessAutoConfiguration.class)
@Import(TestAutoConfiguration.class)
public class DefaultRepositoryTest {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private DefaultRepository<RolePermissionPO> repo;

    @Test
    void contentLoad() {


//        System.out.println(repo.getMapper());
    }


}

