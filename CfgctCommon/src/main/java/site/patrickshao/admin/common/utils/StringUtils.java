package site.patrickshao.admin.common.utils;

import com.google.common.base.Joiner;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;
import site.patrickshao.admin.common.constants.CommonStrings;
import site.patrickshao.admin.common.constants.SignConstants;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 字符串工具类
 */

@ParametersAreNonnullByDefault
public final class StringUtils {
    private static final Splitter SEMICOLON_SPLITTER = Splitter.on(SignConstants.SEMICOLON).trimResults().omitEmptyStrings();
    private static final Splitter EQUAL_SPLITTER = Splitter.on(SignConstants.EQUAL).trimResults().omitEmptyStrings();
    private static final Splitter COMMA_SPLITTER = Splitter.on(SignConstants.COMMA).trimResults().omitEmptyStrings();
    private static final Joiner SEMICOLON_JOINER = Joiner.on(SignConstants.SEMICOLON).skipNulls();
    private static final Joiner EQUAL_JOINER = Joiner.on(SignConstants.EQUAL).useForNull(CommonStrings.NULL);
    private static final Joiner COMMA_JOINER = Joiner.on(SignConstants.COMMA).skipNulls();

    private StringUtils() {
    }

    /**
     * 解析参数
     * 参数文本形式为 key1=value1;key2=value2;key3=value3
     *
     * @param text 参数文本
     * @return 参数
     */
    public static @NotNull Map<String, String> resolveArguments(@Nullable String text) {
        if (text == null || text.isEmpty()) {
            return java.util.Collections.emptyMap();
        }
        List<String> entryList = SEMICOLON_SPLITTER.splitToList(text);
        return entryList.stream().map(EQUAL_SPLITTER::splitToList)
                .collect(java.util.stream.Collectors.toMap(list -> list.get(0), list -> list.get(1)));
    }

    /*
     * 生成参数
     * 参数文本形式为 key1=value1;key2=value2;key3=value3
     *
     * @param arguments 参数
     * @return 参数文本
     */
    public static @NotNull String formatArguments(@NotNull Map<String, String> arguments) {
        Preconditions.checkNotNull(arguments);
        if (arguments.isEmpty()) {
            return "";
        }
        return SEMICOLON_JOINER.join(arguments.entrySet().stream()
                .map(entry -> EQUAL_JOINER.join(entry.getKey(), entry.getValue())).toList());
    }

    public static @NotNull List<String> splitByComma(String string) {
        return COMMA_SPLITTER.splitToList(string);
    }

    public static String concatByComma(List<String> stringList) {
        if (stringList.isEmpty()) {
            return "";
        }
        return COMMA_JOINER.join(stringList);
    }
 }
