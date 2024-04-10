package site.patrickshao.admin.common.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import site.patrickshao.admin.common.annotation.GenerateRepository;
import site.patrickshao.admin.common.annotation.PartitionField;

import java.util.Objects;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/9
 */
@TableName("`ActionRequire`")
@GenerateRepository
public class ActionRequirePO extends AbstractBasicFieldsObject {
    @PartitionField
    private Long actionID;
    private Long permissionID;

    public Long getActionID() {
        return actionID;
    }

    public void setActionID(Long actionID) {
        this.actionID = actionID;
    }

    public Long getPermissionID() {
        return permissionID;
    }

    public void setPermissionID(Long permissionID) {
        this.permissionID = permissionID;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ActionRequirePO that)) return false;
        if (!super.equals(object)) return false;
        return Objects.equals(actionID, that.actionID) && Objects.equals(permissionID, that.permissionID);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), actionID, permissionID);
    }

    @Override
    public String toString() {
        return "ActionRequirePO{" +
                "actionID=" + actionID +
                ", permissionID=" + permissionID +
                "} " + super.toString();
    }
}
