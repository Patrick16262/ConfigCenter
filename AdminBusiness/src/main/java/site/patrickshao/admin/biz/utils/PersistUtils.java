package site.patrickshao.admin.biz.utils;

import com.baomidou.mybatisplus.core.conditions.Wrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import site.patrickshao.admin.common.annotation.PartitionField;
import site.patrickshao.admin.common.utils.ReflectUtils;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author Shao Yibo
 * @description
 * @date 2024/4/10
 */
public class PersistUtils {

    public static  <T> Wrapper<T> generatePartitionQueryWrapper(T t) {
        var fieldMap = getPartitionColumNames(t.getClass());
        QueryWrapper<T> wrapper = new QueryWrapper<T>();
        for (var entry : fieldMap.entrySet()) {
            Object val = ReflectUtils.getFieldValue(t, entry.getValue());
            wrapper.eq(entry.getKey(), val);
        }
        return wrapper;
    }

    public static  <T> Map<String, Field> getPartitionColumNames(Class<T> clazz) {
        return Arrays.stream(clazz.getDeclaredFields())
                .filter(field -> field.getAnnotation(PartitionField.class) != null)
                .collect(Collectors.toMap(Field::getName, field -> field));

    }

    private PersistUtils() {

    }
}
