package site.patrickshao.admin.common.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import site.patrickshao.admin.common.annotation.GenerateRepository;
import site.patrickshao.admin.common.annotation.PartitionField;

import java.util.Objects;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/9
 */
@TableName("`Namespace`")
@GenerateRepository
public class NamespacePO extends AbstractFullFieldsObject {
    @PartitionField
    private Long applicationID;
    @TableField("`name`")
    private String name;
    @TableField("`associate`")
    private Long associate;
    private Long orderNum;
    @TableField("`type`")
    private String type;

    public Long getApplicationID() {
        return applicationID;
    }

    public void setApplicationID(Long applicationID) {
        this.applicationID = applicationID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getAssociate() {
        return associate;
    }

    public void setAssociate(Long associate) {
        this.associate = associate;
    }

    public Long getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Long orderNum) {
        this.orderNum = orderNum;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof NamespacePO that)) return false;
        if (!super.equals(object)) return false;
        return Objects.equals(applicationID, that.applicationID) && Objects.equals(name, that.name) && Objects.equals(associate, that.associate) && Objects.equals(orderNum, that.orderNum) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), applicationID, name, associate, orderNum, type);
    }

    @Override
    public String toString() {
        return "NamespacePO{" +
                "applicationID=" + applicationID +
                ", name='" + name + '\'' +
                ", associate=" + associate +
                ", orderNum=" + orderNum +
                ", type='" + type + '\'' +
                "} " + super.toString();
    }
}
