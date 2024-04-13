package site.patrickshao.admin.biz.consts;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/13
 */
public interface ProfileValues {
    String ENV_NAME = "DEV";
    boolean IS_SENSITIVE_NAMESPACE_ENCRYPTION_ENABLED = false;
    long APPLICATIONS_DELETED_DELAY = 1209600;
    long CLUSTER_DELETED_DELAY = 604800;
    long DELETED_EXPIRE_TIME = -1;
    long AUDIT_EXPIRE_TIME = -1;
}
