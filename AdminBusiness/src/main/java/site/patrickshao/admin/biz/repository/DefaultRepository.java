package site.patrickshao.admin.biz.repository;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import site.patrickshao.admin.biz.utils.PersistUtils;
import site.patrickshao.admin.common.constants.DataBaseFields;
import site.patrickshao.admin.common.entity.po.AbstractBasicFieldsObject;
import site.patrickshao.admin.common.entity.po.AbstractFullFieldsObject;
import site.patrickshao.admin.common.entity.po.AbstractPersistObject;
import site.patrickshao.admin.common.utils.ReflectUtils;
import site.patrickshao.admin.common.utils.Throwables;

import javax.annotation.ParametersAreNonnullByDefault;
import java.time.Instant;
import java.util.Date;
import java.util.List;

/**
 * @author Shao Yibo
 * @description 所有Repository的模板类
 * 在启动时生成相应实体的Repository
 * 由于某些bug，Repository和其子类不能进行任何代理
 * @date 2024/4/10
 */

@ParametersAreNonnullByDefault
public class DefaultRepository<T extends AbstractPersistObject> {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    private BaseMapper<T> mapper;

    private final Class<T> entityType;

    public DefaultRepository() {
        this.entityType = ReflectUtils.getSuperClassGenericArgument(this.getClass());
    }

    public T selectById(Long id) {
        return mapper.selectById(id);
    }

    @NotNull
    public List<T> selectByIds(List<Long> ids) {
        return mapper.selectBatchIds(ids);
    }

    @NotNull
    public List<T> selectByWrapper(Wrapper<T> wrapper) {
        return mapper.selectList(wrapper);
    }

    @NotNull
    public List<T> selectByPartition(T entity) {
        Wrapper<T> wrapper = PersistUtils.generatePartitionQueryWrapper(entity);
        Throwables.throwOnCondition(wrapper.isEmptyOfWhere()).illegalArgument();

        return selectByWrapper(wrapper);
    }

    public T create(T entity, String Operator) {
        processEntityBeforeCreate(entity, Operator);
        mapper.insert(entity);

        return entity;
    }

    public void updateById(Long id, T entity, String operator) {
        Throwables.throwOnNotNull(entity.getId());
        entity.setId(id);
        processEntityBeforeUpdate(entity, operator);

        updateById(entity, operator);
    }

    public void updateById(T entity, String operator) {
        Throwables.throwOnNull(entity.getId());
        processEntityBeforeUpdate(entity, operator);

        mapper.updateById(entity);
    }


    public void updateByWrapper(Wrapper<T> wrapper, T entity, String operator) {
        Throwables.throwOnNull(entity.getId());
        processEntityBeforeUpdate(entity, operator);

        mapper.update(entity, wrapper);
    }

    public void updateByPartition(T entity, String operator) {
        Wrapper<T> wrapper = PersistUtils.generatePartitionQueryWrapper(entity);
        Throwables.throwOnCondition(wrapper.isEmptyOfWhere()).illegalArgument();
        processEntityBeforeUpdate(entity, operator);

        mapper.update(entity, wrapper);
    }

    public void deleteById(Long id, String operator) {
        T t = ReflectUtils.newInstance(entityType);
        t.setId(id);
        processEntityBeforeDelete(t, operator);

        mapper.updateById(t);
        mapper.deleteById(id);
    }

    public void deleteByIds(List<Long> ids, String operator) {
        T t = ReflectUtils.newInstance(entityType);

        processEntityBeforeDelete(t, operator);

        mapper.update(new QueryWrapper<T>().in(DataBaseFields.ID, ids));
        mapper.deleteBatchIds(ids);
    }

    public void deleteByWrapper(Wrapper<T> wrapper, String operator) {
        T t = ReflectUtils.newInstance(entityType);
        processEntityBeforeDelete(t, operator);

        mapper.update(t, wrapper);
        mapper.delete(wrapper);
    }

    public void deleteByPartition(T entity, String operator) {
        T t = ReflectUtils.newInstance(entityType);
        processEntityBeforeDelete(t, operator);
        Wrapper<T> wrapper = PersistUtils.generatePartitionQueryWrapper(t);

        mapper.update(t, wrapper);
        mapper.delete(wrapper);
    }

    private void processEntityBeforeDelete(T t, String operator) {
        processEntityBeforeUpdate(t, operator);
        if (t instanceof AbstractBasicFieldsObject object) {
            object.setDeleted(true);
            object.setDeletedAt(Date.from(Instant.now()));
        }
    }

    private void processEntityBeforeUpdate(T t, String operator) {
        if (t instanceof AbstractBasicFieldsObject object) {
            Throwables.throwOnNotNull(
                    object.getCreatedBy(),
                    object.getCreateTime(),
                    object.getDeleted(),
                    object.getDeletedAt()
            );
        }
        if (t instanceof AbstractFullFieldsObject object) {
            Throwables.throwOnNotNull(
                    object.getCreatedBy(),
                    object.getCreateTime()
            );
            object.setLastModifiedBy(operator);
            object.setLastModifyTime(Date.from(Instant.now()));
        }
    }

    private void processEntityBeforeCreate(T t, String operator) {
        if (t instanceof AbstractBasicFieldsObject object) {
            Throwables.throwOnNotNull(
                    object.getCreatedBy(),
                    object.getCreateTime(),
                    object.getDeleted(),
                    object.getDeletedAt());
            object.setCreatedBy(operator);
            object.setCreateTime(Date.from(Instant.now()));
        }
        if (t instanceof AbstractFullFieldsObject object) {
            Throwables.throwOnNotNull(
                    object.getCreatedBy(),
                    object.getCreateTime()
            );
        }
    }
}
