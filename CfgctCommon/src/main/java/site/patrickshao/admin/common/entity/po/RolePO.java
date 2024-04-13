package site.patrickshao.admin.common.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import site.patrickshao.admin.common.annotation.GenerateRepository;

import javax.annotation.Nullable;
import java.util.Objects;

import static site.patrickshao.admin.common.constants.DataBaseFields.Role.ROLE_NAME;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/9
 */
@TableName("`Role`")
@GenerateRepository
public class RolePO extends AbstractBasicFieldsObject {
    @TableField(ROLE_NAME)
    private String roleName;
    @Nullable
    private String parentRoleNames;
    private Boolean specifyApplication;
    private Boolean specifyNamespace;
    private Boolean systemDefault;

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @jakarta.annotation.Nullable
    public String getParentRoleNames() {
        return parentRoleNames;
    }

    public void setParentRoleNames(@jakarta.annotation.Nullable String parentRoleNames) {
        this.parentRoleNames = parentRoleNames;
    }

    public Boolean getSpecifyApplication() {
        return specifyApplication;
    }

    public void setSpecifyApplication(Boolean specifyApplication) {
        this.specifyApplication = specifyApplication;
    }

    public Boolean getSpecifyNamespace() {
        return specifyNamespace;
    }

    public void setSpecifyNamespace(Boolean specifyNamespace) {
        this.specifyNamespace = specifyNamespace;
    }

    public Boolean getSystemDefault() {
        return systemDefault;
    }

    public void setSystemDefault(Boolean systemDefault) {
        this.systemDefault = systemDefault;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof RolePO rolePO)) return false;
        if (!super.equals(object)) return false;
        return Objects.equals(roleName, rolePO.roleName) && Objects.equals(parentRoleNames, rolePO.parentRoleNames) && Objects.equals(specifyApplication, rolePO.specifyApplication) && Objects.equals(specifyNamespace, rolePO.specifyNamespace) && Objects.equals(systemDefault, rolePO.systemDefault);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), roleName, parentRoleNames, specifyApplication, specifyNamespace, systemDefault);
    }

    @Override
    public String toString() {
        return "RolePO{" +
                "roleName='" + roleName + '\'' +
                ", parentRoleNames='" + parentRoleNames + '\'' +
                ", specifyApplication=" + specifyApplication +
                ", specifyNamespace=" + specifyNamespace +
                ", systemDefault=" + systemDefault +
                "} " + super.toString();
    }
}
