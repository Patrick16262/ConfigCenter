package site.patrickshao.admin.common.entity.po;

import com.baomidou.mybatisplus.annotation.TableName;
import site.patrickshao.admin.common.annotation.GenerateRepository;

import java.util.Objects;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/9
 */
@TableName("`Permission`")
@GenerateRepository
public class PermissionPO extends AbstractBasicFieldsObject {
    private String permissionName;
    private Boolean specifyApplication;
    private Boolean specifyNamespace;
    private Boolean systemDefault;

    public String getPermissionName() {
        return permissionName;
    }

    public void setPermissionName(String permissionName) {
        this.permissionName = permissionName;
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
        if (!(object instanceof PermissionPO that)) return false;
        if (!super.equals(object)) return false;
        return Objects.equals(permissionName, that.permissionName) && Objects.equals(specifyApplication, that.specifyApplication) && Objects.equals(specifyNamespace, that.specifyNamespace) && Objects.equals(systemDefault, that.systemDefault);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), permissionName, specifyApplication, specifyNamespace, systemDefault);
    }

    @Override
    public String toString() {
        return "PermissionPO{" +
                "permissionName='" + permissionName + '\'' +
                ", specifyApplication=" + specifyApplication +
                ", specifyNamespace=" + specifyNamespace +
                ", systemDefault=" + systemDefault +
                "} " + super.toString();
    }
}
