# varchar(32) tiny string
# varchar(64) small string
# varchar(127) medium string
#varchar(255) large string

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

UNLOCK TABLES;

DROP TABLE IF EXISTS `Application`;
DROP TABLE IF EXISTS `AccessToken`;
DROP TABLE IF EXISTS `Cluster`;
DROP TABLE IF EXISTS `Namespace`;
DROP TABLE IF EXISTS `Branch`;
DROP TABLE IF EXISTS `Item`;
DROP TABLE IF EXISTS `Publish`;
DROP TABLE IF EXISTS `Modification`;
DROP TABLE IF EXISTS `Instance`;
DROP TABLE IF EXISTS `GrayReleaseRule`;

DROP TABLE IF EXISTS `ModificationMessage`;

DROP TABLE IF EXISTS `User`;
DROP TABLE IF EXISTS `Role`;
DROP TABLE IF EXISTS `UserRole`;
DROP TABLE IF EXISTS `Permission`;
DROP TABLE IF EXISTS `RoleAssignRequire`;
DROP TABLE IF EXISTS `RolePermission`;
DROP TABLE IF EXISTS `Action`;
DROP TABLE IF EXISTS `ActionRequire`;

DROP TABLE IF EXISTS `Audit`;
DROP TABLE IF EXISTS `ServerConfig`;

SET @@FOREIGN_KEY_CHECKS = 1;

CREATE TABLE `Application`
(
    `ID`               int unsigned PRIMARY KEY AUTO_INCREMENT COMMENT '每张表的主键，int unsigned，自增',

    `ApplicationName`  varchar(64) UNIQUE                                    NOT NULL COMMENT '应用名',
    `NickName`         varchar(127) DEFAULT null                             NULL COMMENT '应用在界面上显示的名称',
    `DeferredDelete`   boolean      DEFAULT false                            NOT NULL COMMENT '是否将要被删除',
    `ToBeDeletedAt`    boolean      DEFAULT null                             NULL COMMENT '将于何时移除',
    `RequireAccessKey` boolean      DEFAULT false                            NOT NULL COMMENT '是否需要AccessKey',

    `Comment`          text(65525)  DEFAULT null                             NULL COMMENT '备注',
    `Deleted`          boolean      DEFAULT false                            NOT NULL COMMENT '是否被逻辑删除',
    `DeletedAt`        timestamp    DEFAULT null                             NULL COMMENT '删除时间',
    `CreatedBy`        varchar(127)                                          NOT NULL COMMENT '谁创建的',
    `CreateTime`       timestamp    DEFAULT CURRENT_TIMESTAMP                NOT NULL COMMENT '创建时间',
    `LastModifiedBy`   varchar(127) DEFAULT 'Not Modified'                   NOT NUll COMMENT '谁最后一次修改的',
    `LastModifyTime`   timestamp    DEFAULT null ON UPDATE CURRENT_TIMESTAMP NULL COMMENT '修改时间'
) DEFAULT CHARSET = utf8mb4 COMMENT '应用';

CREATE INDEX `DeleteAndApplicationNameIndex` ON `Application` (Deleted, ApplicationName);
CREATE INDEX `DeleteAndDeferredDeleteAndToBeDeletedAtIndex` ON `Application` (Deleted, DeferredDelete, ToBeDeletedAt);

/*
AccessToken
应用访问Token
ApplicationID
应用ID
ApplicationName
应用名
Token
Token
公共字段1
*/

CREATE TABLE `AccessToken`
(
    `ID`              int unsigned PRIMARY KEY AUTO_INCREMENT COMMENT '每张表的主键，int unsigned，自增',

    `ApplicationID`   int unsigned                          NOT NULL COMMENT '应用ID',
    `ApplicationName` varchar(64)                           NOT NULL COMMENT '应用名',
    `Token`           varchar(255)                          NOT NULL COMMENT 'Token',

    `Comment`         text(65525) DEFAULT null              NULL COMMENT '备注',
    `Deleted`         boolean     DEFAULT false             NOT NULL COMMENT '是否被逻辑删除',
    `DeletedAt`       timestamp   DEFAULT null              NULL COMMENT '删除时间',
    `CreatedBy`       varchar(127)                          NOT NULL COMMENT '谁创建的',
    `CreateTime`      timestamp   DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',

    CONSTRAINT FOREIGN KEY ApplicationIDKey (`ApplicationID`) REFERENCES Application (ID)
) DEFAULT CHARSET = utf8mb4 COMMENT '应用访问Token';

CREATE INDEX `DeleteAndApplicationIDIndex` ON `AccessToken` (Deleted, ApplicationID);
CREATE INDEX `DeleteAndApplicationNameIndex` ON `AccessToken` (Deleted, ApplicationName);

/*
Namespace
命名空间
ApplicationID
应用ID
Name
名称
Associate
关联的命名空间
OrderNum
序号
Type
命名空间类型
公共字段1
*/

CREATE TABLE `Namespace`
(
    `ID`             int unsigned PRIMARY KEY AUTO_INCREMENT COMMENT '每张表的主键，int unsigned，自增',

    `ApplicationID`  int unsigned                                          NOT NULL COMMENT '应用ID',
    `Name`           varchar(64)                                           NOT NULL COMMENT '名称',
    `Associate`      int unsigned DEFAULT null                             NULL COMMENT '关联的命名空间',
    `OrderNum`       int unsigned                                          NOT NULL COMMENT '序号',
    `Type`           varchar(32)                                           NOT NULL COMMENT '命名空间类型',

    `Comment`        text(65525)  DEFAULT null                             NULL COMMENT '备注',
    `Deleted`        boolean      DEFAULT false                            NOT NULL COMMENT '是否被逻辑删除',
    `DeletedAt`      timestamp    DEFAULT null                             NULL COMMENT '删除时间',
    `CreatedBy`      varchar(127)                                          NOT NULL COMMENT '谁创建的',
    `CreateTime`     timestamp    DEFAULT CURRENT_TIMESTAMP                NOT NULL COMMENT '创建时间',
    `LastModifiedBy` varchar(127) DEFAULT 'Not Modified'                   NOT NUll COMMENT '谁最后一次修改的',
    `LastModifyTime` timestamp    DEFAULT null ON UPDATE CURRENT_TIMESTAMP NULL COMMENT '修改时间',

    CONSTRAINT FOREIGN KEY ApplicationIDKey (`ApplicationID`) REFERENCES Application (ID)
) DEFAULT CHARSET = utf8mb4 COMMENT '命名空间';

CREATE INDEX `DeleteAndApplicationIDIndex` ON `Namespace` (Deleted, ApplicationID, OrderNum);
CREATE INDEX `DeleteAndNameIndex` ON `Namespace` (Deleted, Name);

/*
Cluster
集群
ApplicationID
应用ID
Name
名称
DeferredDelete
是否被延迟删除
公共字段1
*/

CREATE TABLE `Cluster`
(
    `ID`             int unsigned PRIMARY KEY AUTO_INCREMENT COMMENT '每张表的主键，int unsigned，自增',

    `ApplicationID`  int unsigned                                          NOT NULL COMMENT '应用ID',
    `Name`           varchar(64)                                           NOT NULL COMMENT '名称',
    `DeferredDelete` bool         DEFAULT FALSE                            NOT NULL COMMENT '是否被延迟删除',
    `ToBeDeletedAt`  boolean      DEFAULT null                             NULL COMMENT '将于何时移除',


    `Comment`        text(65525)  DEFAULT null                             NULL COMMENT '备注',
    `Deleted`        boolean      DEFAULT false                            NOT NULL COMMENT '是否被逻辑删除',
    `DeletedAt`      timestamp    DEFAULT null                             NULL COMMENT '删除时间',
    `CreatedBy`      varchar(127)                                          NOT NULL COMMENT '谁创建的',
    `CreateTime`     timestamp    DEFAULT CURRENT_TIMESTAMP                NOT NULL COMMENT '创建时间',
    `LastModifiedBy` varchar(127) DEFAULT 'Not Modified'                   NOT NUll COMMENT '谁最后一次修改的',
    `LastModifyTime` timestamp    DEFAULT null ON UPDATE CURRENT_TIMESTAMP NULL COMMENT '修改时间',

    CONSTRAINT FOREIGN KEY ApplicationIDKey (`ApplicationID`) REFERENCES Application (ID)
) DEFAULT CHARSET = utf8mb4 COMMENT '集群';

CREATE INDEX `DeleteAndApplicationIDIndex` ON `Cluster` (Deleted, ApplicationID);
CREATE INDEX `DeleteAndNameIndex` ON `Cluster` (Deleted, Name);

/*
Branch
分支
Name
名称
ClusterID
哪个下的分支
NamespaceID
哪个下的分支
BranchHead
分支头
BranchType
分支类型
公共字段1
 */

CREATE TABLE `Branch`
(
    `ID`             int unsigned PRIMARY KEY AUTO_INCREMENT COMMENT '每张表的主键，int unsigned，自增',

    `Name`           varchar(127)                                          NOT NULL COMMENT '名称',
    `ClusterID`      int unsigned                                          NOT NULL COMMENT '哪个下的分支',
    `NamespaceID`    int unsigned                                          NOT NULL COMMENT '哪个下的分支',
    `BranchHead`     int unsigned                                          NOT NULL COMMENT '分支头, 所处于的发布的ID',
    `BranchType`     varchar(32)                                           NOT NULL COMMENT '分支类型',
    `ApplicationID`  int unsigned                                          NOT NULL COMMENT '应用ID',

    `Comment`        text(65525)  DEFAULT null                             NULL COMMENT '备注',
    `Deleted`        boolean      DEFAULT false                            NOT NULL COMMENT '是否被逻辑删除',
    `DeletedAt`      timestamp    DEFAULT null                             NULL COMMENT '删除时间',
    `CreatedBy`      varchar(127)                                          NOT NULL COMMENT '谁创建的',
    `CreateTime`     timestamp    DEFAULT CURRENT_TIMESTAMP                NOT NULL COMMENT '创建时间',
    `LastModifiedBy` varchar(127) DEFAULT 'Not Modified'                   NOT NUll COMMENT '谁最后一次修改的',
    `LastModifyTime` timestamp    DEFAULT null ON UPDATE CURRENT_TIMESTAMP NULL COMMENT '修改时间',

    CONSTRAINT FOREIGN KEY ApplicationIDKey (`ApplicationID`) REFERENCES Application (ID),
    CONSTRAINT FOREIGN KEY NamespaceIDKey (`NamespaceID`) REFERENCES Namespace (ID),
    CONSTRAINT FOREIGN KEY ClusterIDKey (`ClusterID`) REFERENCES Cluster (ID)
) DEFAULT CHARSET = utf8mb4 COMMENT '分支';

CREATE INDEX `DeleteAndNamespaceIDIndex` ON `Branch` (Deleted, NamespaceID);

/*
Instance 实例
IP
IP
LastConnectTime
最后一次连接断开的时间, 连接尚未断开为null
ApplicationName
应用名
ClusterName
集群名
*/

CREATE TABLE `Instance`
(
    `ID`              int unsigned PRIMARY KEY AUTO_INCREMENT COMMENT '每张表的主键，int unsigned，自增',

    `IP`              varchar(64)                           NOT NULL COMMENT 'IPv4或IPv6',
    `LastConnectTime` timestamp   DEFAULT null              NULL COMMENT '最后一次连接断开的时间, 连接尚未断开为null',
    `ApplicationName` varchar(64)                           NOT NULL COMMENT '应用名',
    `ClusterName`     varchar(64)                           NOT NULL COMMENT '集群名',


    `Comment`         text(65525) DEFAULT null              NULL COMMENT '备注',
    `Deleted`         boolean     DEFAULT false             NOT NULL COMMENT '是否被逻辑删除',
    `DeletedAt`       timestamp   DEFAULT null              NULL COMMENT '删除时间',
    `CreatedBy`       varchar(127)                          NOT NULL COMMENT '谁创建的',
    `CreateTime`      timestamp   DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间'
) DEFAULT CHARSET = utf8mb4 COMMENT '实例';

CREATE INDEX `DeleteAndIPIndex` ON `Instance` (Deleted, IP);

/*
GrayReleaseRule
灰度发布规则
BranchID
分支ID
Rule
规则
Enabled
是否已启用
公共字段2
*/

CREATE TABLE `GrayReleaseRule`
(
    `ID`            int unsigned PRIMARY KEY AUTO_INCREMENT COMMENT '每张表的主键，int unsigned，自增',

    `BranchID`      int unsigned                          NOT NULL COMMENT '分支ID',
    `Rule`          varchar(255)                          NOT NULL COMMENT '规则',
    `Enabled`       bool        DEFAULT true              NOT NULL COMMENT 'Enabled',
    `ApplicationID` int unsigned                          NOT NULL COMMENT '应用ID',
    `NamespaceID`   int unsigned                          NOT NULL COMMENT '哪个下的分支',

    `Comment`       text(65525) DEFAULT null              NULL COMMENT '备注',
    `Deleted`       boolean     DEFAULT false             NOT NULL COMMENT '是否被逻辑删除',
    `DeletedAt`     timestamp   DEFAULT null              NULL COMMENT '删除时间',
    `CreatedBy`     varchar(127)                          NOT NULL COMMENT '谁创建的',
    `CreateTime`    timestamp   DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',

    CONSTRAINT FOREIGN KEY ApplicationIDKey (`ApplicationID`) REFERENCES Application (ID),
    CONSTRAINT FOREIGN KEY NamespaceIDKey (`NamespaceID`) REFERENCES Namespace (ID),
#     CONSTRAINT FOREIGN KEY ClusterIDKey (`ClusterID`) REFERENCES Cluster (ID),
    CONSTRAINT FOREIGN KEY BranchIDey (`BranchID`) REFERENCES Branch (ID)

) DEFAULT CHARSET = utf8mb4 COMMENT '灰度发布规则';

CREATE INDEX `DeleteAndBranchID` ON `GrayReleaseRule` (Deleted, BranchID);
CREATE INDEX `DeleteAndApplicationIDAndNamespaceID` ON `GrayReleaseRule` (Deleted, ApplicationID, NamespaceID);

/*
Publish
发布日志
Name
名称
BranchID
分支ID
Workspace
是否为工作区
Authorizer
审核人ID
PreviousID
前一个发布日志ID
NextID
下一个发布日志ID
公共字段2
 */

CREATE TABLE `Publish`
(
    `ID`            int unsigned PRIMARY KEY AUTO_INCREMENT COMMENT '每张表的主键，int unsigned，自增',

    `Name`          varchar(127)                          NOT NULL COMMENT '名称',
    `BranchID`      int unsigned                          NOT NULL COMMENT '分支ID',
    `WorkSpace`     bool        default false             NOT NULL COMMENT '是否为工作区',
    `Authorizer`    varchar(64)                           NOT NULL COMMENT '审核人ID',
    `PreviousID`    int unsigned                          NULL COMMENT '前一个发布日志ID',
    `NextID`        int unsigned                          NULL COMMENT '下一个发布日志ID',
    `ApplicationID` int unsigned                          NOT NULL COMMENT '应用ID',
    `NamespaceID`   int unsigned                          NOT NULL COMMENT '哪个下的分支',

    `Comment`       text(65525) DEFAULT null              NULL COMMENT '备注',
    `Deleted`       boolean     DEFAULT false             NOT NULL COMMENT '是否被逻辑删除',
    `DeletedAt`     timestamp   DEFAULT null              NULL COMMENT '删除时间',
    `CreatedBy`     varchar(127)                          NOT NULL COMMENT '谁创建的',
    `CreateTime`    timestamp   DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',

    CONSTRAINT FOREIGN KEY ApplicationIDKey (`ApplicationID`) REFERENCES Application (ID),
    CONSTRAINT FOREIGN KEY NamespaceIDKey (`NamespaceID`) REFERENCES Namespace (ID),
    CONSTRAINT FOREIGN KEY BranchIDey (`BranchID`) REFERENCES Branch (ID)
) DEFAULT CHARSET = utf8mb4 COMMENT '发布日志';

CREATE INDEX `DeleteAndBranchIDIndex` ON `Publish` (Deleted, BranchID);
CREATE INDEX `DeleteAndAuthorizerAndIsWorkspaceIndex` ON `Publish` (Deleted, Authorizer, WorkSpace);

ALTER TABLE Publish
    ADD CONSTRAINT FOREIGN KEY PreviousIDKey (`PreviousID`) REFERENCES Publish (ID);
ALTER TABLE Publish
    ADD CONSTRAINT FOREIGN KEY NextIDKey (`NextID`) REFERENCES Publish (ID);

/*
Modification
变更
PublishID
哪个发布下的
Key
键
Value
值
Operation
操作
公共字段2
*/

CREATE TABLE `Modification`
(
    `ID`            int unsigned PRIMARY KEY AUTO_INCREMENT COMMENT '每张表的主键，int unsigned，自增',

    `Value`         varchar(255) DEFAULT null              NULL COMMENT '值',
    `Key`           varchar(255) DEFAULT null              NULL COMMENT '键',
    `Operation`     varchar(32)                            NOT NULL COMMENT '操作',
    `PublishID`     int unsigned                           NOT NULL COMMENT '哪个发布下的',
    `ApplicationID` int unsigned                           NOT NULL COMMENT '应用ID',
    `NamespaceID`   int unsigned                           NOT NULL COMMENT '哪个下的分支',

    `Comment`       text(65525)  DEFAULT null              NULL COMMENT '备注',
    `Deleted`       boolean      DEFAULT false             NOT NULL COMMENT '是否被逻辑删除',
    `DeletedAt`     timestamp    DEFAULT null              NULL COMMENT '删除时间',
    `CreatedBy`     varchar(127)                           NOT NULL COMMENT '谁创建的',
    `CreateTime`    timestamp    DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',

    CONSTRAINT FOREIGN KEY ApplicationIDKey (`ApplicationID`) REFERENCES Application (ID),
    CONSTRAINT FOREIGN KEY NamespaceIDKey (`NamespaceID`) REFERENCES Namespace (ID)
) DEFAULT CHARSET = utf8mb4 COMMENT '变更';

CREATE INDEX `DeleteAndPublishIDAndKeyIndex` ON `Modification` (`Deleted`, `PublishID`, `Key`);

/*
Item
条目
BranchID
哪个分支下的条目
Value
值
Key
键
公共字段1
*/

CREATE TABLE `Item`
(
    `ID`             int unsigned PRIMARY KEY AUTO_INCREMENT COMMENT '每张表的主键，int unsigned，自增',

    `BranchID`       int unsigned                                          NOT NULL COMMENT '哪个分支下的条目',
    `Value`          varchar(255) DEFAULT null                             NULL COMMENT '值',
    `Key`            varchar(255) DEFAULT null                             NULL COMMENT '键',
    `ApplicationID`  int unsigned                                          NOT NULL COMMENT '应用ID',
    `NamespaceID`    int unsigned                                          NOT NULL COMMENT '哪个下的分支',

    `Comment`        text(65525)  DEFAULT null                             NULL COMMENT '备注',
    `Deleted`        boolean      DEFAULT false                            NOT NULL COMMENT '是否被逻辑删除',
    `DeletedAt`      timestamp    DEFAULT null                             NULL COMMENT '删除时间',
    `CreatedBy`      varchar(127)                                          NOT NULL COMMENT '谁创建的',
    `CreateTime`     timestamp    DEFAULT CURRENT_TIMESTAMP                NOT NULL COMMENT '创建时间',
    `LastModifiedBy` varchar(127) DEFAULT 'Not Modified'                   NOT NUll COMMENT '谁最后一次修改的',
    `LastModifyTime` timestamp    DEFAULT null ON UPDATE CURRENT_TIMESTAMP NULL COMMENT '修改时间',
    CONSTRAINT FOREIGN KEY ApplicationIDKey (`ApplicationID`) REFERENCES Application (ID),
    CONSTRAINT FOREIGN KEY NamespaceIDKey (`NamespaceID`) REFERENCES Namespace (ID),
    CONSTRAINT FOREIGN KEY BranchIDKey (`BranchID`) REFERENCES Application (ID)
) DEFAULT CHARSET = utf8mb4 COMMENT '条目';

CREATE INDEX `DeleteAndPublishIDAndKeyIndex` ON `Item` (`Deleted`, `BranchID`);

/*
ModificationMessage
配置变更消息
OperationType
操作类型
OperationArgument
操作描述参数
*/

CREATE TABLE `ModificationMessage`
(
    `ID`                int unsigned PRIMARY KEY AUTO_INCREMENT COMMENT '每张表的主键，int unsigned，自增',

    `OperationType`     varchar(32)                            NOT NULL COMMENT '操作类型',
    `OperationArgument` varchar(255) DEFAULT ''                NOT NULL COMMENT '操作描述参数',

    `Comment`           text(65525)  DEFAULT null              NULL COMMENT '备注',
    `Deleted`           boolean      DEFAULT false             NOT NULL COMMENT '是否被逻辑删除',
    `DeletedAt`         timestamp    DEFAULT null              NULL COMMENT '删除时间',
    `CreatedBy`         varchar(127)                           NOT NULL COMMENT '谁创建的',
    `CreateTime`        timestamp    DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间'
) DEFAULT CHARSET = utf8mb4 COMMENT '配置变更消息';

CREATE INDEX `DeleteIndex` ON `ModificationMessage` (`Deleted`);

/*
Audit
审计日志
OperationTime
操作时间
OperatorID
操作人ID
OperatorName
操作人名
AppID
AppName
NamespaceID
NamespaceName
Describe
修改描述
*/

CREATE TABLE `Audit`
(
    `ID`            int unsigned PRIMARY KEY AUTO_INCREMENT COMMENT '每张表的主键，int unsigned，自增',

    `OperationTime` timestamp DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '操作时间',
    `OperatorID`    int unsigned                        NOT NULL COMMENT '操作人ID',
    `OperatorName`  varchar(64)                         NOT NULL COMMENT '操作人名',
    `AppID`         int unsigned                        NOT NULL,
    `AppName`       varchar(64)                         NOT NULL,
    `NamespaceID`   int unsigned                        NOT NULL,
    `NamespaceName` varchar(64)                         NOT NULL
) DEFAULT CHARSET = utf8mb4 COMMENT '修改描述';

CREATE INDEX `AppIDAndNamespaceIDIndex` ON `Audit` (`AppID`, `NamespaceID`);

/*
ServerConfig
服务配置
Key
Value
公共字段1
*/

CREATE TABLE `ServerConfig`
(
    `ID`             int unsigned PRIMARY KEY AUTO_INCREMENT COMMENT '每张表的主键，int unsigned，自增',

    `Key`            varchar(255)                                          NOT NULL,
    `Value`          varchar(255)                                          NOT NULL,

    `Comment`        text(65525)  DEFAULT null                             NULL COMMENT '备注',
    `Deleted`        boolean      DEFAULT false                            NOT NULL COMMENT '是否被逻辑删除',
    `DeletedAt`      timestamp    DEFAULT null                             NULL COMMENT '删除时间',
    `CreatedBy`      varchar(127)                                          NOT NULL COMMENT '谁创建的',
    `CreateTime`     timestamp    DEFAULT CURRENT_TIMESTAMP                NOT NULL COMMENT '创建时间',
    `LastModifiedBy` varchar(127) DEFAULT 'Not Modified'                   NOT NUll COMMENT '谁最后一次修改的',
    `LastModifyTime` timestamp    DEFAULT null ON UPDATE CURRENT_TIMESTAMP NULL COMMENT '修改时间'
) DEFAULT CHARSET = utf8mb4 COMMENT '服务端配置';

CREATE INDEX `DeletedIndex` ON `ServerConfig` (`Deleted`);

/*
User
Username
用户名
Email
邮箱
NickName
昵称
PassWord
密码(加密后)
Enabled
是否启用
公共字段2
*/

CREATE TABLE `User`
(
    `ID`             int unsigned PRIMARY KEY AUTO_INCREMENT COMMENT '每张表的主键，int unsigned，自增',

    `UserName`       varchar(64) UNIQUE                                    NOT NULL COMMENT '用户名',
    `Email`          varchar(64)                                           NOT NULL COMMENT '邮箱',
    `NickName`       varchar(127) DEFAULT null                             NULL COMMENT '昵称',
    `Password`       varchar(127)                                          NOT NULL COMMENT '密码(加密后)',
    `Enabled`        bool         DEFAULT true                             NOT NULL COMMENT '是否启用',


    `Comment`        text(65525)  DEFAULT null                             NULL COMMENT '备注',
    `Deleted`        boolean      DEFAULT false                            NOT NULL COMMENT '是否被逻辑删除',
    `DeletedAt`      timestamp    DEFAULT null                             NULL COMMENT '删除时间',
    `CreatedBy`      varchar(127)                                          NOT NULL COMMENT '谁创建的',
    `CreateTime`     timestamp    DEFAULT CURRENT_TIMESTAMP                NOT NULL COMMENT '创建时间',
    `LastModifiedBy` varchar(127) DEFAULT 'Not Modified'                   NOT NUll COMMENT '谁最后一次修改的',
    `LastModifyTime` timestamp    DEFAULT null ON UPDATE CURRENT_TIMESTAMP NULL COMMENT '修改时间'
) DEFAULT CHARSET = utf8mb4 COMMENT '用户';

CREATE INDEX `DeletedUserNameIndex` ON `User` (`Deleted`, `UserName`);

INSERT INTO `User`(`UserName`, `Email`, `Password`, `CreatedBy`)
VALUES ('root', '', 'Temporarily Cannot Login', 'System');

/*
Role
角色
RoleName
角色名称
ParentRoleName
角色名称
SpecifyApplication
是否有目标App
SpecifyNamespace
是否有目标命名空间
SystemDefault
是否为系统自带角色
*/

CREATE TABLE `Role`
(
    `ID`                 int unsigned PRIMARY KEY AUTO_INCREMENT COMMENT '每张表的主键，int unsigned，自增',

    `RoleName`           varchar(64) UNIQUE                    NOT NULL COMMENT '角色名称',
    `ParentRoleNames`    varchar(64) DEFAULT null              NULL COMMENT '父角色名称，继承父角色所有权限',
    `SpecifyApplication` bool                                  NOT NULL COMMENT '是否有目标App',
    `SpecifyNamespace`   bool                                  NOT NULL COMMENT '是否有目标命名空间',
    `SystemDefault`      bool        DEFAULT false             NOT NULL COMMENT '是否为系统自带角色，系统自带角色不可更改和删除',


    `Comment`            text(65525) DEFAULT null              NULL COMMENT '备注',
    `Deleted`            boolean     DEFAULT false             NOT NULL COMMENT '是否被逻辑删除',
    `DeletedAt`          timestamp   DEFAULT null              NULL COMMENT '删除时间',
    `CreatedBy`          varchar(127)                          NOT NULL COMMENT '谁创建的',
    `CreateTime`         timestamp   DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间'


) DEFAULT CHARSET = utf8mb4 COMMENT '角色，权限的集合';

CREATE INDEX `DeletedRoleNameIndex` ON `Role` (`Deleted`, `RoleName`);

INSERT INTO Role(`RoleName`, `ParentRoleNames`, `SpecifyApplication`, `SpecifyNamespace`, `SystemDefault`, `CreatedBy`)
VALUES ('NamespaceVisitor', null, true, true, true, 'System'),
       ('NamespacePublisher', 'NamespaceVisitor', true, true, true, 'System'),
       ('NamespaceEditor', 'NamespaceVisitor', true, true, true, 'System'),
       ('NamespaceAdmin', 'NamespaceEditor, NamespacePublisher', true, true, true, 'System'),
       ('ApplicationVisitor', 'NamespaceVisitor', true, false, true, 'System'),
       ('ApplicationManager', 'ApplicationVisitor', true, false, true, 'System'),
       ('ApplicationAdmin', 'ApplicationManager', true, false, true, 'System'),
       ('ServerManager', 'ApplicationAdmin', false, false, true, 'System'),
       ('ServerAdmin', 'ServerManager', false, false, true, 'System'),
       ('SuperUser', 'ServerAdmin', false, false, true, 'System'),
       ('SensitiveAdmin', 'SensitiveManager', false, false, true, 'System'),
       ('SensitiveManager', null, true, true, true, 'System');


/*
UserRole
UserID
RoleID
公共字段2
*/

SET FOREIGN_KEY_CHECKS = true;

CREATE TABLE `UserRole`
(
    `ID`                       int unsigned PRIMARY KEY AUTO_INCREMENT COMMENT '每张表的主键，int unsigned，自增',

    `UserID`                   int unsigned                          NOT NULL,
    `RoleID`                   int unsigned                          NOT NULL,
    `NamespaceSpecification`   int unsigned                          NULL COMMENT '授权的namespace',
    `ApplicationSpecification` int unsigned                          NULL COMMENT '授权的App',

    `Comment`                  text(65525) DEFAULT null              NULL COMMENT '备注',
    `Deleted`                  boolean     DEFAULT false             NOT NULL COMMENT '是否被逻辑删除',
    `DeletedAt`                timestamp   DEFAULT null              NULL COMMENT '删除时间',
    `CreatedBy`                varchar(127)                          NOT NULL COMMENT '谁创建的',
    `CreateTime`               timestamp   DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',


    CONSTRAINT FOREIGN KEY UserIDKey (`UserID`) REFERENCES User (ID),
    CONSTRAINT FOREIGN KEY RoleIDKey (`RoleID`) REFERENCES Role (ID)
) DEFAULT CHARSET = utf8mb4 COMMENT 'User和Role的关系表，用来表示哪些User有哪些Role';

CREATE INDEX `DeletedUserIDIndex` ON `UserRole` (`Deleted`, `UserID`);

INSERT INTO `UserRole`(`UserID`, `RoleID`, `CreatedBy`)
VALUES ((SELECT ID FROM User WHERE UserName = 'root'), (SELECT ID FROM Role WHERE RoleName = 'SuperUser'), 'System');

/*
Permission
权限
PermissionName
权限名称
SpecifyApplication
目标App
SpecifyNamespace
目标Namespace
SystemDefault
是否为系统自带权限
公共字段1
*/

CREATE TABLE `Permission`
(
    `ID`                 int unsigned PRIMARY KEY AUTO_INCREMENT COMMENT '每张表的主键，int unsigned，自增',

    `PermissionName`     varchar(64) UNIQUE                    NOT NULL COMMENT '权限名称',
    `SpecifyApplication` bool                                  NOT NULL COMMENT '是否可以声明目标App',
    `SpecifyNamespace`   bool                                  NOT NULL COMMENT '是否可以声明目标Namespace',
    `SystemDefault`      bool        DEFAULT false             NOT NULL COMMENT '是否为系统自带',

    `Comment`            text(65525) DEFAULT null              NULL COMMENT '备注',
    `Deleted`            boolean     DEFAULT false             NOT NULL COMMENT '是否被逻辑删除',
    `DeletedAt`          timestamp   DEFAULT null              NULL COMMENT '删除时间',
    `CreatedBy`          varchar(127)                          NOT NULL COMMENT '谁创建的',
    `CreateTime`         timestamp   DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间'

) DEFAULT CHARSET = utf8mb4 COMMENT '权限';

CREATE INDEX `DeletedAndPermissionNameIndex` ON `Permission` (`Deleted`, `PermissionName`);

INSERT INTO `Permission`(PermissionName, SpecifyApplication, SpecifyNamespace, CreatedBy, SystemDefault)
VALUES ('Super', false, false, 'System', true),
       ('SuperAuthorization', false, false, 'System', true),
       ('ServerAuthorization', false, false, 'System', true),
       ('SeverConfigManage', false, false, 'System', true),
       ('UserManage', false, false, 'System', true),
       ('ApplicationAuthorization', true, false, 'System', true),
       ('ApplicationDelete', false, false, 'System', true),
       ('ApplicationCreate', false, false, 'System', true),
       ('ApplicationVisit', true, false, 'System', true),
       ('ApplicationEdit', true, false, 'System', true),
       ('NamespaceCreate', true, false, 'System', true),
       ('ClusterCreate', true, false, 'System', true),
       ('ClusterDelete', true, false, 'System', true),
       ('NamespaceAuthorization', true, true, 'System', true),
       ('NamespaceVisit', true, true, 'System', true),
       ('NamespaceEdit', true, true, 'System', true),
       ('NamespacePublish', true, true, 'System', true),
       ('NamespaceDelete', true, true, 'System', true),
       ('SensitiveAuthorization', false, false, 'System', true),
       ('SensitiveManage', true, true, 'System', true),
       ('UserAuthorization', false, false, 'System', true);


/*
RoleAssignRequire
RoleID
PermissionID
公共字段2
*/

CREATE TABLE `RoleAssignRequire`
(
    `ID`                       int unsigned PRIMARY KEY AUTO_INCREMENT COMMENT '每张表的主键，int unsigned，自增',

    `RoleID`                   int unsigned                          NOT NULL,
    `PermissionID`             int unsigned                          NOT NULL,
    `ApplicationSpecification` bool                                  NULL COMMENT '授权的App',
    `NamespaceSpecification`   bool                                  NULL COMMENT '授权的namespace',

    `Comment`                  text(65525) DEFAULT null              NULL COMMENT '备注',
    `Deleted`                  boolean     DEFAULT false             NOT NULL COMMENT '是否被逻辑删除',
    `DeletedAt`                timestamp   DEFAULT null              NULL COMMENT '删除时间',
    `CreatedBy`                varchar(127)                          NOT NULL COMMENT '谁创建的',
    `CreateTime`               timestamp   DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',

    CONSTRAINT FOREIGN KEY PermissionIDKey (`PermissionID`) REFERENCES Permission (ID),
    CONSTRAINT FOREIGN KEY RoleIDKey (`RoleID`) REFERENCES Role (ID)
) DEFAULT CHARSET = utf8mb4 COMMENT '授予他人权限时自己需要什么角色，多个Permission的关系为‘且';

CREATE INDEX `DeletedRoleIDIndex` ON `RoleAssignRequire` (`Deleted`, `RoleID`);

INSERT INTO `RoleAssignRequire`(`RoleID`, `PermissionID`, `NamespaceSpecification`, `ApplicationSpecification`,
                                `CreatedBy`)
VALUES ((SELECT ID FROM Role WHERE RoleName = 'SuperUser'),
        (SELECT ID FROM Permission WHERE PermissionName = 'SuperAuthorization'), false, false, 'System'),
       ((SELECT ID FROM Role WHERE RoleName = 'ServerAdmin'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ServerAuthorization'), false, false, 'System'),
       ((SELECT ID FROM Role WHERE RoleName = 'ServerManager'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ServerAuthorization'), false, false, 'System'),
       ((SELECT ID FROM Role WHERE RoleName = 'ApplicationAdmin'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationAuthorization'), true, false, 'System'),
       ((SELECT ID FROM Role WHERE RoleName = 'ApplicationManager'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationAuthorization'), true, false, 'System'),
       ((SELECT ID FROM Role WHERE RoleName = 'ApplicationVisitor'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationAuthorization'), true, false, 'System'),
       ((SELECT ID FROM Role WHERE RoleName = 'NamespaceAdmin'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceAuthorization'), true, true, 'System'),
       ((SELECT ID FROM Role WHERE RoleName = 'NamespaceEditor'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceAuthorization'), true, true, 'System'),
       ((SELECT ID FROM Role WHERE RoleName = 'NamespacePublisher'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceAuthorization'), true, true, 'System'),
       ((SELECT ID FROM Role WHERE RoleName = 'NamespaceVisitor'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceAuthorization'), true, true, 'System'),
       ((SELECT ID FROM Role WHERE RoleName = 'SensitiveAdmin'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ServerAuthorization'), false, false, 'System'),
       ((SELECT ID FROM Role WHERE RoleName = 'SensitiveManager'),
        (SELECT ID FROM Permission WHERE PermissionName = 'SeverConfigManage'), true, true, 'System');


/*
RolePermission
RoleID
PermissionID
公共字段2
*/
CREATE TABLE `RolePermission`
(
    `ID`                       int unsigned PRIMARY KEY AUTO_INCREMENT COMMENT '每张表的主键，int unsigned，自增',

    `RoleID`                   int unsigned                          NOT NULL,
    `PermissionID`             int unsigned                          NOT NULL,
    `NamespaceSpecification`   bool                                  NULL COMMENT '授权的namespace',
    `ApplicationSpecification` bool                                  NULL COMMENT '授权的App',


    `Comment`                  text(65525) DEFAULT null              NULL COMMENT '备注',
    `Deleted`                  boolean     DEFAULT false             NOT NULL COMMENT '是否被逻辑删除',
    `DeletedAt`                timestamp   DEFAULT null              NULL COMMENT '删除时间',
    `CreatedBy`                varchar(127)                          NOT NULL COMMENT '谁创建的',
    `CreateTime`               timestamp   DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',

    CONSTRAINT FOREIGN KEY PermissionIDKey (`PermissionID`) REFERENCES Permission (ID),
    CONSTRAINT FOREIGN KEY RoleIDKey (`RoleID`) REFERENCES Role (ID)
) DEFAULT CHARSET = utf8mb4 COMMENT 'Role有哪些Permission';

CREATE INDEX `DeletedRoleIDIndex` ON `RolePermission` (`Deleted`, `RoleID`);

INSERT INTO `RolePermission` (RoleID, PermissionID, NamespaceSpecification, ApplicationSpecification, CreatedBy)
VALUES ((SELECT ID FROM Role where RoleName = 'SuperUser'),
        (SELECT ID FROM Permission WHERE PermissionName = 'SuperAuthorization'), false, false, 'System');
INSERT INTO `RolePermission` (RoleID, PermissionID, NamespaceSpecification, ApplicationSpecification, CreatedBy)
VALUES ((SELECT ID FROM Role where RoleName = 'ServerAdmin'),
        (SELECT ID FROM Permission WHERE PermissionName = 'UserAuthorization'), false, false, 'System');
INSERT INTO `RolePermission` (RoleID, PermissionID, NamespaceSpecification, ApplicationSpecification, CreatedBy)
VALUES ((SELECT ID FROM Role where RoleName = 'ServerAdmin'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ServerAuthorization'), false, false, 'System');
INSERT INTO `RolePermission` (RoleID, PermissionID, NamespaceSpecification, ApplicationSpecification, CreatedBy)
VALUES ((SELECT ID FROM Role where RoleName = 'ServerManager'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationDelete'), false, false, 'System');
INSERT INTO `RolePermission` (RoleID, PermissionID, NamespaceSpecification, ApplicationSpecification, CreatedBy)
VALUES ((SELECT ID FROM Role where RoleName = 'ServerManager'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationCreate'), false, false, 'System');
INSERT INTO `RolePermission` (RoleID, PermissionID, NamespaceSpecification, ApplicationSpecification, CreatedBy)
VALUES ((SELECT ID FROM Role where RoleName = 'ServerManager'),
        (SELECT ID FROM Permission WHERE PermissionName = 'UserManage'), false, false, 'System');
INSERT INTO `RolePermission` (RoleID, PermissionID, NamespaceSpecification, ApplicationSpecification, CreatedBy)
VALUES ((SELECT ID FROM Role where RoleName = 'ServerManager'),
        (SELECT ID FROM Permission WHERE PermissionName = 'SeverConfigManage'), false, false, 'System');
INSERT INTO `RolePermission` (RoleID, PermissionID, NamespaceSpecification, ApplicationSpecification, CreatedBy)
VALUES ((SELECT ID FROM Role where RoleName = 'ApplicationAdmin'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationAuthorization'), false, true, 'System');
INSERT INTO `RolePermission` (RoleID, PermissionID, NamespaceSpecification, ApplicationSpecification, CreatedBy)
VALUES ((SELECT ID FROM Role where RoleName = 'ApplicationManager'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationEdit'), false, true, 'System');
INSERT INTO `RolePermission` (RoleID, PermissionID, NamespaceSpecification, ApplicationSpecification, CreatedBy)
VALUES ((SELECT ID FROM Role where RoleName = 'ApplicationManager'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceCreate'), false, true, 'System');
INSERT INTO `RolePermission` (RoleID, PermissionID, NamespaceSpecification, ApplicationSpecification, CreatedBy)
VALUES ((SELECT ID FROM Role where RoleName = 'ApplicationManager'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ClusterCreate'), false, true, 'System');
INSERT INTO `RolePermission` (RoleID, PermissionID, NamespaceSpecification, ApplicationSpecification, CreatedBy)
VALUES ((SELECT ID FROM Role where RoleName = 'ApplicationManager'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ClusterDelete'), false, true, 'System');
INSERT INTO `RolePermission` (RoleID, PermissionID, NamespaceSpecification, ApplicationSpecification, CreatedBy)
VALUES ((SELECT ID FROM Role where RoleName = 'ApplicationVisitor'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), false, true, 'System');
INSERT INTO `RolePermission` (RoleID, PermissionID, NamespaceSpecification, ApplicationSpecification, CreatedBy)
VALUES ((SELECT ID FROM Role where RoleName = 'ApplicationVisitor'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceVisit'), false, true, 'System');
INSERT INTO `RolePermission` (RoleID, PermissionID, NamespaceSpecification, ApplicationSpecification, CreatedBy)
VALUES ((SELECT ID FROM Role where RoleName = 'NamespaceAdmin'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceAuthorization'), true, true, 'System');
INSERT INTO `RolePermission` (RoleID, PermissionID, NamespaceSpecification, ApplicationSpecification, CreatedBy)
VALUES ((SELECT ID FROM Role where RoleName = 'NamespaceEditor'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceEdit'), true, true, 'System');
INSERT INTO `RolePermission` (RoleID, PermissionID, NamespaceSpecification, ApplicationSpecification, CreatedBy)
VALUES ((SELECT ID FROM Role where RoleName = 'NamespacePublisher'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespacePublish'), true, true, 'System');
INSERT INTO `RolePermission` (RoleID, PermissionID, NamespaceSpecification, ApplicationSpecification, CreatedBy)
VALUES ((SELECT ID FROM Role where RoleName = 'NamespaceVisitor'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceVisit'), true, true, 'System');
/*
Action
行为，系统中的每个操作都在这里。只读。
ActionName
行为名称
CheckApplicationPermission
检查app权限
CheckNamespacePermission
检查Namespace权限
*/


CREATE TABLE `Action`
(
    `ID`                       int unsigned PRIMARY KEY AUTO_INCREMENT COMMENT '每张表的主键，int unsigned，自增',

    ActionName                 varchar(64) NOT NULL COMMENT '行为名称',
    CheckApplicationPermission bool        NOT NULL COMMENT '该行为是否会检查App权限',
    CheckNamespacePermission   bool        NOT NULL COMMENT '该行为是否检查Namespace权限'
) DEFAULT CHARSET = utf8mb4 COMMENT '行为，系统中的每个操作的名称都在这里。此表只读，不可更改。';

CREATE INDEX `ActionNameIndex` ON `Action` (`ActionName`);

# INSERT INTO Action(ActionName, CheckApplicationPermission, CheckNamespacePermission, CreatedBy)
# VALUES (, 'System');

INSERT INTO Action(ActionName, CheckApplicationPermission, CheckNamespacePermission)
VALUES ('Application#Visible', true, false),
       ('Application#Create', false, false),
       ('Application#Delete', false, false),
       ('Application#Enable/DisableRequireAcessKey', true, false),
       ('Application#GenerateAceessKey', true, false),
       ('Application#Enable/DisableAcessKey', true, false),
       ('Application#DeleteAceessKey', true, false),
       ('Application#EditApplicationName', true, false),
       ('Application#EditNickName', true, false),
       ('Application#EditComment', true, false),
       ('Application#SyncAppToEnv', true, false),
       ('Cluster#Visit', true, false),
       ('Cluster#CreateCluster', true, false),
       ('Cluster#DeleteClusterDeferred', true, false),
       ('Cluster#EditClusterName', true, false),
       ('Cluster#EditClusterComment', true, false),
       ('Namespace#Visit', true, true),
       ('Namespace#CreateNamespace', true, false),
       ('Namespace#DeleteNamespace', true, false),
       ('Namespace#EditNamespaceName', true, true),
       ('Namespace#EditNamespaceOrder', true, true),
       ('Namespace#EditNamespaceComment', true, true),
       ('Namespace#SyncNamespaceToEnv', true, true),
       ('Sensitive#CreateSensitiveNamespace', true, true),
       ('Sensitive#SensitiveNamespace/ItemCRUB', true, true),
       ('Branch#Visit', true, true),
       ('Branch#PublishChanges', true, true),
       ('Branch#Rollback', true, true),
       ('Branch#ReRollback', true, true),
       ('Branch#MakeGrayBranch', true, true),
       ('Branch#DeleteGrayBranch', true, true),
       ('Branch#MergeGrayBranch', true, true),
       ('Branch#ManageGrayReleaseRule', true, true),
       ('Item#Visit', true, true),
       ('Item#AddItem', true, true),
       ('Item#DeleteItem', true, true),
       ('Item#EditItemKeyValue', true, true),
       ('Item#EditItemComment', true, true),
       ('Item#SyncItemsToCluster', true, true),
       ('User#Visit', false, false),
       ('User#CreateUser', false, false),
       ('User#DeleteUser', false, false),
       ('User#Enable/DisableUser', false, false),
       ('User#EditUser', false, false),
       ('Audit#VisitAplplicationAudit', true, true),
       ('Audit#VisitNamespaceAudit', true, true),
       ('ServerConfig#ChangeKeyValue', false, false),
       ('Role#Create', false, false),
       ('Role#Delete', false, false),
       ('Role#Editname', false, false),
       ('Role#EditRolePermission', false, false),
       ('Permission#Create', false, false),
       ('Permission#Delete', false, false),
       ('Permission#CreateActionRequire', false, false),
       ('Permission#DeleteActionRequire', false, false),
       ('Permission#Editname', false, false),
       ('Permission#EditAssginRequirement', false, false),
       #第一次修改2024年4月10日
       ('Branch#EditBranchName', true, true),
       ('Branch#EditBranchComment', true, true),
       ('User#SelfEdit', false, false);


/*
ActionRequire
PermissionName
ActionName
*/

CREATE TABLE `ActionRequire`
(
    `ID`           int unsigned PRIMARY KEY AUTO_INCREMENT COMMENT '每张表的主键，int unsigned，自增',

    `ActionID`     int unsigned                          NOT NULL COMMENT '行为名',
    `PermissionID` int unsigned                          NOT NULL COMMENT '许可名',

    `Comment`      text(65525) DEFAULT null              NULL COMMENT '备注',
    `Deleted`      boolean     DEFAULT false             NOT NULL COMMENT '是否被逻辑删除',
    `DeletedAt`    timestamp   DEFAULT null              NULL COMMENT '删除时间',
    `CreatedBy`    varchar(127)                          NOT NULL COMMENT '谁创建的',
    `CreateTime`   timestamp   DEFAULT CURRENT_TIMESTAMP NOT NULL COMMENT '创建时间',

    CONSTRAINT FOREIGN KEY ActionNameKey (`ActionID`) REFERENCES Action (ID),
    CONSTRAINT FOREIGN KEY PermissionNameKey (`PermissionID`) REFERENCES Permission (ID)
) DEFAULT CHARSET = utf8mb4 COMMENT '每个行为需要的权限';

CREATE INDEX `DeletedActionIDIndex` ON `ActionRequire` (`Deleted`, `ActionID`);

INSERT INTO `ActionRequire` (`ActionID`, `PermissionID`, `CreatedBy`)
VALUES ((SELECT ID FROM Action WHERE ActionName = 'Application#Visible'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Application#Create'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Application#Create'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationCreate'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Application#Delete'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Application#Delete'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationDelete'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Application#Enable/DisableRequireAcessKey'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Application#Enable/DisableRequireAcessKey'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationAuthorization'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Application#GenerateAceessKey'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Application#GenerateAceessKey'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationAuthorization'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Application#Enable/DisableAcessKey'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Application#Enable/DisableAcessKey'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationAuthorization'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Application#DeleteAceessKey'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Application#DeleteAceessKey'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationAuthorization'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Application#EditApplicationName'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Application#EditApplicationName'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationEdit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Application#EditNickName'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Application#EditNickName'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationEdit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Application#EditComment'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Application#EditComment'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationEdit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Application#SyncAppToEnv'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Application#SyncAppToEnv'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationEdit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Cluster#Visit'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Cluster#CreateCluster'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Cluster#CreateCluster'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ClusterCreate'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Cluster#DeleteClusterDeferred'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Cluster#DeleteClusterDeferred'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ClusterDelete'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Cluster#EditClusterName'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Cluster#EditClusterComment'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Namespace#Visit'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Namespace#Visit'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Namespace#CreateNamespace'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Namespace#CreateNamespace'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceCreate'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Namespace#DeleteNamespace'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Namespace#DeleteNamespace'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Namespace#DeleteNamespace'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceDelete'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Namespace#EditNamespaceName'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Namespace#EditNamespaceName'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Namespace#EditNamespaceName'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceEdit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Namespace#EditNamespaceOrder'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Namespace#EditNamespaceOrder'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Namespace#EditNamespaceOrder'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceEdit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Namespace#EditNamespaceComment'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Namespace#EditNamespaceComment'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Namespace#EditNamespaceComment'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceEdit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Namespace#SyncNamespaceToEnv'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Namespace#SyncNamespaceToEnv'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Namespace#SyncNamespaceToEnv'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceEdit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Sensitive#CreateSensitiveNamespace'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Sensitive#CreateSensitiveNamespace'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Sensitive#CreateSensitiveNamespace'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceCreate'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Sensitive#CreateSensitiveNamespace'),
        (SELECT ID FROM Permission WHERE PermissionName = 'SensitiveAuthorization'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Sensitive#SensitiveNamespace/ItemCRUB'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Sensitive#SensitiveNamespace/ItemCRUB'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Sensitive#SensitiveNamespace/ItemCRUB'),
        (SELECT ID FROM Permission WHERE PermissionName = 'SensitiveManage'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Branch#Visit'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Branch#Visit'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Branch#PublishChanges'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Branch#PublishChanges'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Branch#PublishChanges'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespacePublish'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Branch#Rollback'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Branch#Rollback'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Branch#Rollback'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespacePublish'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Branch#ReRollback'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Branch#ReRollback'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Branch#ReRollback'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespacePublish'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Branch#MakeGrayBranch'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Branch#MakeGrayBranch'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Branch#MakeGrayBranch'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceEdit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Branch#DeleteGrayBranch'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Branch#DeleteGrayBranch'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Branch#DeleteGrayBranch'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceEdit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Branch#MergeGrayBranch'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Branch#MergeGrayBranch'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Branch#MergeGrayBranch'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceEdit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Branch#ManageGrayReleaseRule'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Branch#ManageGrayReleaseRule'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Branch#ManageGrayReleaseRule'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespacePublish'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Item#Visit'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Item#Visit'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Item#AddItem'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Item#AddItem'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Item#AddItem'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceEdit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Item#DeleteItem'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Item#DeleteItem'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Item#DeleteItem'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceEdit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Item#EditItemKeyValue'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Item#EditItemKeyValue'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Item#EditItemKeyValue'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceEdit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Item#EditItemComment'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Item#EditItemComment'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Item#EditItemComment'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceEdit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Item#SyncItemsToCluster'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Item#SyncItemsToCluster'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Item#SyncItemsToCluster'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceEdit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'User#CreateUser'),
        (SELECT ID FROM Permission WHERE PermissionName = 'UserManage'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'User#DeleteUser'),
        (SELECT ID FROM Permission WHERE PermissionName = 'UserManage'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'User#Enable/DisableUser'),
        (SELECT ID FROM Permission WHERE PermissionName = 'UserManage'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'User#EditUser'),
        (SELECT ID FROM Permission WHERE PermissionName = 'UserManage'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Audit#VisitAplplicationAudit'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Audit#VisitNamespaceAudit'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Audit#VisitNamespaceAudit'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'ServerConfig#ChangeKeyValue'),
        (SELECT ID FROM Permission WHERE PermissionName = 'SeverConfigManage'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Role#Create'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ServerAuthorization'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Role#Delete'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ServerAuthorization'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Role#Editname'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ServerAuthorization'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Role#EditRolePermission'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ServerAuthorization'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Permission#Create'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ServerAuthorization'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Permission#Delete'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ServerAuthorization'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Permission#CreateActionRequire'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ServerAuthorization'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Permission#DeleteActionRequire'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ServerAuthorization'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Permission#Editname'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ServerAuthorization'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Permission#EditAssginRequirement'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ServerAuthorization'), 'System'),
       #第一次修改2024年4月10日
       ((SELECT ID FROM Action WHERE ActionName = 'Branch#EditBranchName'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Branch#EditBranchName'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Branch#EditBranchName'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceEdit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Branch#EditBranchComment'),
        (SELECT ID FROM Permission WHERE PermissionName = 'ApplicationVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Branch#EditBranchComment'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceVisit'), 'System'),
       ((SELECT ID FROM Action WHERE ActionName = 'Branch#EditBranchComment'),
        (SELECT ID FROM Permission WHERE PermissionName = 'NamespaceEdit'), 'System');


FLUSH TABLES Action WITH READ LOCK;

/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;
/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;