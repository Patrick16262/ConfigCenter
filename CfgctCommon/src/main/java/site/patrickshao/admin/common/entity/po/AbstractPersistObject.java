package site.patrickshao.admin.common.entity.po;

import com.baomidou.mybatisplus.annotation.TableId;
import site.patrickshao.admin.common.entity.PojoWithIdentifier;

import java.io.Serializable;
import java.util.Objects;

import static site.patrickshao.admin.common.constants.DataBaseFields.ID;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/9
 */
public abstract class AbstractPersistObject implements Serializable, PojoWithIdentifier {
    @TableId(ID)
    private Long id;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof AbstractPersistObject that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "BasePersistObject{" +
                "id=" + id +
                '}';
    }

    /**
     * @return
     */
    @Override
    public Long getPojoIdentifier() {
        return id;
    }
}
