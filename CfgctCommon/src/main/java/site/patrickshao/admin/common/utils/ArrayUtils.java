package site.patrickshao.admin.common.utils;

import com.google.common.base.Preconditions;
import site.patrickshao.admin.common.entity.PojoWithIdentifier;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.*;

@ParametersAreNonnullByDefault
public final class ArrayUtils {
    public static <T> T getOnlyOne(T[] array) {
        Preconditions.checkArgument(array.length == 1);
        return array[0];
    }

    public static <T> T getOnlyOne(List<T> list) {
        Preconditions.checkArgument(list.size() == 1);
        return list.get(0);
    }
    @SuppressWarnings("unchecked")
    public static <T> T getOnlyOne(Collection<T> list) {
        Preconditions.checkArgument(list.size() == 1);
        return (T) list.toArray()[0];
    }

    @SuppressWarnings("unchecked")
    public static <T extends PojoWithIdentifier> List<T> distinct(List<T> list) {
        Map<Long, PojoWithIdentifier> map = new HashMap<>();
        list.forEach(pojo ->map.putIfAbsent(pojo.getPojoIdentifier(), pojo));
        return (List<T>) new ArrayList<>(map.values());
    }

    public static boolean contains(List<PojoWithIdentifier> parentList, List<PojoWithIdentifier> child) {
        Map<Long, PojoWithIdentifier> map = new HashMap<>();
        parentList.forEach(pojo -> map.put(pojo.getPojoIdentifier(), pojo));
        return child.stream().allMatch(pojo -> map.containsKey(pojo.getPojoIdentifier()));
    }


}
