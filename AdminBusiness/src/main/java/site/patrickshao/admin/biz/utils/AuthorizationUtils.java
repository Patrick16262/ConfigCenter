package site.patrickshao.admin.biz.utils;

import site.patrickshao.admin.common.entity.po.ActionPO;
import site.patrickshao.admin.common.entity.po.ActionRequirePO;
import site.patrickshao.admin.common.entity.po.RolePermissionPO;
import site.patrickshao.admin.common.entity.po.UserRolePO;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/11
 */
public class AuthorizationUtils {
    public static ActionRequirePO convertToActionPOPartition(ActionPO actionPO) {
        ActionRequirePO po = new ActionRequirePO();
        po.setActionID(actionPO.getId());
        return po;
    }

    public static RolePermissionPO convertToRolePermissionPOPartition(Long roleID) {
        RolePermissionPO po = new RolePermissionPO();
        po.setRoleId(roleID);
        return po;
    }

    public static UserRolePO convertToUserRolePOPartition(Long userId) {
        UserRolePO po = new UserRolePO();
        po.setUserId(userId);
        return po;
    }

}
