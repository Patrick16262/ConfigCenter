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
@TableName("`UserRole`")
@GenerateRepository
public class UserRolePO extends AbstractBasicFieldsObject {
    @PartitionField
    private Long userId;
    private Long roleId;
    private Long namespaceSpecification;
    private Long applicationSpecification;

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getRoleId() {
        return roleId;
    }

    public void setRoleId(Long roleId) {
        this.roleId = roleId;
    }

    public Long getNamespaceSpecification() {
        return namespaceSpecification;
    }

    public void setNamespaceSpecification(Long namespaceSpecification) {
        this.namespaceSpecification = namespaceSpecification;
    }

    public Long getApplicationSpecification() {
        return applicationSpecification;
    }

    public void setApplicationSpecification(Long applicationSpecification) {
        this.applicationSpecification = applicationSpecification;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof UserRolePO that)) return false;
        if (!super.equals(object)) return false;
        return Objects.equals(userId, that.userId) && Objects.equals(roleId, that.roleId) && Objects.equals(namespaceSpecification, that.namespaceSpecification) && Objects.equals(applicationSpecification, that.applicationSpecification);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), userId, roleId, namespaceSpecification, applicationSpecification);
    }

    @Override
    public String toString() {
        return "UserRolePO{" +
                "userId=" + userId +
                ", roleId=" + roleId +
                ", namespaceSpecification=" + namespaceSpecification +
                ", applicationSpecification=" + applicationSpecification +
                "} " + super.toString();
    }
}
