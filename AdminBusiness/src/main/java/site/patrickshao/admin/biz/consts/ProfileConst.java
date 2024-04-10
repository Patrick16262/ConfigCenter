package site.patrickshao.admin.biz.consts;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class ProfileConst {
    @Value("${config.admin.default.namespace.name : AppNameSpace}")
    public static String DEFAULT_NAME_SPACE_NAME;

    @Value("${config.admin.default.cluster.name : Default}")
    public static String DEFAULT_CLUSTER_NAME;

    @Value("${config.admin.env.name : DEV}")
    public static String ENV_NAME;
}
