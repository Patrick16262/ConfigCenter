package site.patrickshao.admin.biz.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import site.patrickshao.admin.biz.service.AuthorizationService;
import site.patrickshao.admin.common.entity.bo.SpecifiedPermissionBO;
import site.patrickshao.admin.common.entity.po.PermissionPO;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/11
 */
public class AuthorizationBusiness {
    private static final Logger log = LoggerFactory.getLogger(AuthorizationService.class);

    public static boolean checkIfPermissionSatisfied(
            List<SpecifiedPermissionBO> authorizedPermissions,
            List<PermissionPO> requiredPermissions,
            @Nullable Long applicationId,
            @Nullable Long namespaceId) {
        //按Id排序权限
        authorizedPermissions = new ArrayList<>(authorizedPermissions);
        requiredPermissions = new ArrayList<>(requiredPermissions);
        authorizedPermissions.sort(Comparator.comparing(SpecifiedPermissionBO::getId));
        requiredPermissions.sort(Comparator.comparing(PermissionPO::getId));

        //验证权限
        int i = 0;
        for (int j = 0; j < requiredPermissions.size(); j++) {
            PermissionPO required = requiredPermissions.get(j);

            for (; i < authorizedPermissions.size() + 1; i++) {
                if (i == authorizedPermissions.size()) {
                    //认证失败
                    return false;
                }
                var authorized = authorizedPermissions.get(i);
                //没有权限满足该要求，认证失败，直接返回false
                if (required.getId() < required.getId()) {
                    //认证失败
                    return false;
                }
                //不是一类权限
                if (!required.getId().equals(authorized.getId())) {
                    //权限不符合
                    continue;
                }
                //已认证权限的application作用范围为全部，则该权限符合
                if (authorized.getTargetApplication() == null) {
                    //权限符合
                    break;
                }
                //操作不在具体的application对象内，则需求的权限application作用范围为全部
                //但已认证权限的application作用范围为局部，则该权限符合不符合
                if (applicationId == null) {
                    //权限不符合
                    continue;
                }
                //已认证权限作用的application范围为部分，但是需求权限作用范围是全都，则该权限符合不符合
                if (!required.getSpecifyApplication()) {
                    //权限不符合
                    continue;
                }
                //已认证权限作用的application范围和需求权限作用范围不一致，则该权限符合不符合
                if (!authorized.getTargetApplication().equals(applicationId)) {
                    //权限不符合
                    continue;
                }
                //已认证权限的namespace作用范围为全部，则该权限符合
                if (authorized.getTargetNamespace() == null) {
                    //权限符合
                    break;
                }
                //操作不在具体的application对象内，则需求的权限application作用范围为全部
                //但已认证权限的application作用范围为局部，则该权限符合不符合
                if (namespaceId == null) {
                    //权限不符合
                    continue;
                }
                //已认证权限作用的namespace范围为部分，但是需求权限作用范围是全都，则该权限符合不符合
                if (!required.getSpecifyNamespace()) {
                    //权限不符合
                    continue;
                }
                //已认证权限作用的namespace范围和需求权限作用范围不一致，则该权限符合不符合
                if (!authorized.getTargetNamespace().equals(applicationId)) {
                    //权限不符合
                    continue;
                }
                log.error("""
                                Authorization exception
                                Detail:
                                class authorized = {}
                                class required = {}
                                Long applicationId = {}
                                Long namespaceId = {}
                                                                
                                StackTrace:
                                {}""",
                        authorized,
                        required,
                        applicationId,
                        namespaceId,
                        new RuntimeException().getStackTrace()
                );
            }
            //在他之后没有权限满足该要求，认证失败，直接返回false

        }
        return true;
    }
}
