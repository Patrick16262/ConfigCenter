package site.patrickshao.admin.common.constants;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/10
 */
public interface DataBaseFields {
    String ID = "ID";
    String LAST_MODIFIED_BY = "LastModifiedBy";
    String LAST_MODIFY_TIME = "LastModifyTime";
    String CREATE_TIME = "CreateTime";
    String CREATED_BY = "CreatedBy";

    interface Action {
        String ACTION_NAME = "ActionName";
    }

    interface Role {
        String ROLE_NAME = "RoleName";
    }

    interface User {
        String USER_NAME = "UserName";
    }

    interface UserRole {
        String USER_ID = "UserID";
        String ROLE_ID = "RoleID";
    }

    interface Permission {
        String PERMISSION_NAME = "PermissionName";
    }

    interface ActionRequire {
        String ACTION_ID = "ActionID";
        String PERMISSION_ID = "PermissionID";
    }

    interface Application {
        String APPLICATION_NAME = "ApplicationName";
        String NICK_NAME = "NickName";
        String DEFERRED_DELETE = "DeferredDelete";
        String TOBE_DELETED_AT = "TobeDeletedAt";
    }

    interface Namespace {
        String NAMESPACE_NAME = "Name";
        String APPLICATION_ID = "ApplicationID";
        String ASSOCIATE = "Associate";
        String TYPE = "Type";
    }

    interface Item {
        String KEY = "`Key`";
        String VALUE = "`Value`";
        String NAMESPACE_ID = "NamespaceID";
        String APPLICATION_ID = "ApplicationID";
        String BRANCH_ID = "BranchID";
    }

    interface Branch{
        String NAME = "Name";
        String CLUSTER_ID = "ClusterID";
        String BRANCH_HEAD = "BranchHead";
        String BRANCH_TYPE = "BranchType";
        String NAMESPACE_ID = "NamespaceID";
        String APPLICATION_ID = "ApplicationID";
    }

    interface Publish {
        String NAME = "Name";
        String BRANCH_ID = "BranchID";
        String AUTHORIZER = "Authorizer";
        String PREVIOUS_ID = "PreviousID";
        String NEXT_ID = "NextID";
        String WORKSPACE =  "Workspace";
        String APPLICATION_ID = "ApplicationID";
        String NAMESPACE_ID = "NamespaceID";
    }

    interface Modification {
        String VALUE = "`Value`";
        String KEY = "`Key`";
        String OPERATION = "Operation";
        String PUBLISH_ID = "PublishID";
        String APPLICATION_ID = "ApplicationID";
        String NAMESPACE_ID = "NamespaceID";
    }

    interface Cluster {
        String APPLICATION_ID = "ApplicationID";
        String NAME = "Name";
    }
}
