package site.patrickshao.admin.biz.utils;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import site.patrickshao.admin.common.annotation.ParentId;
import site.patrickshao.admin.common.entity.dto.SpecifiedRoleDTO;
import site.patrickshao.admin.common.entity.po.RolePO;
import site.patrickshao.admin.common.entity.po.UserRolePO;
import site.patrickshao.admin.common.utils.ArrayUtils;
import site.patrickshao.admin.common.utils.Throwables;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/10
 */
public class QuaryUtils {

    public static <T> Wrapper<T> generateByParentIdQueryWrapper(Class<T> childrenClass, Long parentId) {
        String columName = ArrayUtils.getOnlyOne(getParentIdColumNames(childrenClass).keySet());
        return new QueryWrapper<T>().eq(columName, parentId);
    }

    public static  <T, V> Wrapper<T> generateByParentIdQueryWrapper(Class<T> chidrenClass, Map<Class<V>, Long> parentIds) {
        Map<String, Field> map = getParentIdColumNames(chidrenClass);
        Map<String, Long> queryMap = new HashMap<>();
        QueryWrapper<T> wrapper = new QueryWrapper<T>();
        for (var entry : map.entrySet()) {
            for (var srcEntry: parentIds.entrySet()) {
                if (entry.getValue().getAnnotation(ParentId.class).value() == srcEntry.getKey()) {
                    wrapper.eq(entry.getKey(), srcEntry.getValue());
                }
            }
        }
        Throwables.throwOnCondition(wrapper.isEmptyOfWhere()).illegalArgument();
        return wrapper;
    }

    public static <T> Map<String, Field> getParentIdColumNames(Class<T> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.getAnnotation(ParentId.class) != null)
                .collect(Collectors.toMap(Field::getName, field -> field));

    }



    private QuaryUtils() {

    }
}
