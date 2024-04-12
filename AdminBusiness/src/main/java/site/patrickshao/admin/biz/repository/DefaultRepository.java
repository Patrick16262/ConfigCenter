package site.patrickshao.admin.biz.repository;


import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import site.patrickshao.admin.biz.utils.QuaryUtils;
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
import java.util.Map;

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
    public List<T> selectByParentId(Long parentId) {
        Wrapper<T> wrapper = QuaryUtils.generateByParentIdQueryWrapper(entityType, parentId);
        return selectByWrapper(wrapper);
    }

    @NotNull
    public List<T> selectByParentIds(Map<Class<?>, Long> parentIds) {
        Wrapper<T> wrapper = QuaryUtils.generateByParentIdQueryWrapper(entityType, parentIds);
        return selectByWrapper(wrapper);
    }

    public T create(T entity, String Operator) {
        processEntityBeforeCreate(entity, Operator);
        mapper.insert(entity);

        return entity;
    }

    public List<T> create(List<T> entities, String Operator) {
        entities.forEach(entity -> processEntityBeforeCreate(entity, Operator));
        entities.forEach(mapper::insert);
        return entities;
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

    public void updateByParentId(T entity, Long parentId, String operator) {
        Wrapper<T> wrapper = QuaryUtils.generateByParentIdQueryWrapper(entityType, parentId);
        Throwables.throwOnCondition(wrapper.isEmptyOfWhere()).illegalArgument();
        processEntityBeforeUpdate(entity, operator);

        mapper.update(entity, wrapper);
    }

    public void updateByParentIds(T entity, Map<Class<?>, Long> parentIds, String operator) {
        Wrapper<T> wrapper = QuaryUtils.generateByParentIdQueryWrapper(entityType, parentIds);
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

    public void deleteByParentId(Long parentId, String operator) {
        T t = ReflectUtils.newInstance(entityType);
        processEntityBeforeDelete(t, operator);
        Wrapper<T> wrapper = QuaryUtils.generateByParentIdQueryWrapper(entityType, parentId);

        mapper.update(t, wrapper);
        mapper.delete(wrapper);
    }

    public void deleteByParentIds(Map<Class<?>, Long> parentIds, String operator) {
        T t = ReflectUtils.newInstance(entityType);
        processEntityBeforeDelete(t, operator);
        Wrapper<T> wrapper = QuaryUtils.generateByParentIdQueryWrapper(entityType, parentIds);

        mapper.update(t, wrapper);
        mapper.delete(wrapper);
    }

    public boolean exists(Long id) {
        return mapper.selectCount(new QueryWrapper<T>().eq(DataBaseFields.ID, id)) > 0;
    }

    public Class<T> getEntityType() {
        return entityType;
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
