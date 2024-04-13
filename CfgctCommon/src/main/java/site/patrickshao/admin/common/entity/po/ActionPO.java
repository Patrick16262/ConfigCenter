package site.patrickshao.admin.common.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import site.patrickshao.admin.common.annotation.GenerateRepository;

import java.util.Objects;

import static site.patrickshao.admin.common.constants.DataBaseFields.Action.ACTION_NAME;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/9
 */
@TableName("`Action`")
@GenerateRepository
public class ActionPO extends AbstractPersistObject {
    @TableField(ACTION_NAME)
    private String actionName;
    private Boolean checkApplicationPermission;
    private Boolean checkNamespacePermission;

    public String getActionName() {
        return actionName;
    }

    public void setActionName(String actionName) {
        this.actionName = actionName;
    }

    public Boolean getCheckApplicationPermission() {
        return checkApplicationPermission;
    }

    public void setCheckApplicationPermission(Boolean checkApplicationPermission) {
        this.checkApplicationPermission = checkApplicationPermission;
    }

    public Boolean getCheckNamespacePermission() {
        return checkNamespacePermission;
    }

    public void setCheckNamespacePermission(Boolean checkNamespacePermission) {
        this.checkNamespacePermission = checkNamespacePermission;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ActionPO actionPO)) return false;
        if (!super.equals(object)) return false;
        return Objects.equals(actionName, actionPO.actionName) && Objects.equals(checkApplicationPermission, actionPO.checkApplicationPermission) && Objects.equals(checkNamespacePermission, actionPO.checkNamespacePermission);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), actionName, checkApplicationPermission, checkNamespacePermission);
    }

    @Override
    public String toString() {
        return "ActionPO{" +
                "actionName='" + actionName + '\'' +
                ", checkApplicationPermission=" + checkApplicationPermission +
                ", checkNamespacePermission=" + checkNamespacePermission +
                "} " + super.toString();
    }
}
