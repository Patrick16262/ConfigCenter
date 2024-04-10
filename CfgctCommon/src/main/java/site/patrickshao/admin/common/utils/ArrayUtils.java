package site.patrickshao.admin.common.utils;

import com.google.common.base.Preconditions;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

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
}
