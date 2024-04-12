package site.patrickshao.admin.common.constants;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/10
 */
public interface DataBaseFields {
    String ID = "ID";

    interface ActionPO {
        String ACTION_NAME = "ActionName";
    }
    interface RolePO {
        String ROLE_NAME = "RoleName";
    }

    interface UserPO {
        String USER_NAME = "UserName";
    }

    interface UserRolePO {
        String USER_ID = "UserID";
        String ROLE_ID = "RoleID";
    }
}
