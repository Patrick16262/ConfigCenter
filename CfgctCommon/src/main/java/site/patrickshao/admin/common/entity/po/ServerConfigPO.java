package site.patrickshao.admin.common.entity.po;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import site.patrickshao.admin.common.annotation.GenerateRepository;

import java.util.Objects;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/9
 */
@TableName("`ServerConfig`")
@GenerateRepository
public class ServerConfigPO extends AbstractFullFieldsObject {
    @TableField("`key`")
    private String key;
    @TableField("`value`")
    private String value;

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof ServerConfigPO that)) return false;
        if (!super.equals(object)) return false;
        return Objects.equals(key, that.key) && Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), key, value);
    }

    @Override
    public String toString() {
        return "ServerConfigPO{" +
                "key='" + key + '\'' +
                ", value='" + value + '\'' +
                "} " + super.toString();
    }
}
