package site.patrickshao.admin.common.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import site.patrickshao.admin.common.annotation.GenerateRepository;
import site.patrickshao.admin.common.annotation.ParentId;
import site.patrickshao.admin.common.entity.HaveApplicationParent;
import site.patrickshao.admin.common.entity.HaveNamespaceParent;

import java.util.Objects;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/9
 */
@TableName("`Namespace`")
@GenerateRepository
public class NamespacePO extends AbstractFullFieldsObject implements HaveApplicationParent, HaveNamespaceParent {
    @ParentId
    private Long applicationId;
    @TableField("`name`")
    private String name;
    @TableField("`associate`")
    private Long associate;
    //此字段废弃
    private Long orderNum;
    @TableField("`type`")
    private String type;

    @Override
    public Long getApplicationId() {
        return applicationId;
    }

    @Override
    public void setApplicationId(Long applicationId) {
        this.applicationId = applicationId;
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

    @Deprecated
    public Long getOrderNum() {
        return orderNum;
    }

    @Deprecated
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
        return Objects.equals(applicationId, that.applicationId) && Objects.equals(name, that.name) && Objects.equals(associate, that.associate) && Objects.equals(orderNum, that.orderNum) && Objects.equals(type, that.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), applicationId, name, associate, orderNum, type);
    }

    @Override
    public String toString() {
        return "NamespacePO{" +
                "applicationID=" + applicationId +
                ", name='" + name + '\'' +
                ", associate=" + associate +
                ", orderNum=" + orderNum +
                ", type='" + type + '\'' +
                "} " + super.toString();
    }

    /**
     * @return
     */
    @Override
    public Long getNamespaceId() {
        return getId();
    }

    /**
     * @param namespaceId
     */
    @Override
    public void setNamespaceId(Long namespaceId) {
        setId(namespaceId);
    }
}
