package site.patrickshao.admin.common.utils;

import site.patrickshao.admin.common.exception.IllegalDataRelationException;
import site.patrickshao.admin.common.exception.http.Http400BadRequest;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

/**
 * 运行时异常工具类
 * 用于简化抛出运行时异常
 *
 * <pre>
 * void doSomething() {
 *      RuntimeExceptions.OnCondition(condition).throwIllegalState("Something wrong.");
 * }
 * </pre>
 * 等价于
 * <pre>
 * void doSomething() {
 *      if (condition) {
 *      throw new IllegalStateException("Something wrong.");
 *      }
 * }
 * </pre>
 *
 * @author Shao Yibo
 * @date 2024/4/3
 */


@ParametersAreNonnullByDefault
public final class Throwables {

    /**
     * 检查调用者类是否为指定类
     *
     * @param clazz 指定类
     * @throws IllegalCallerException 如果调用者不是指定类
     * @author Shao Yibo
     * @date 2024/4/3
     */
    public static <T> void checkCallerClass(Class<T> clazz) {
        checkCallerClass(clazz, "");
    }

    public static <T> void checkCallerClass(Class<T> clazz, String message) {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        Throwables.throwOnCondition(elements.length < 4).badCaller("This method should be called in a method.");
        Class<?> callerClazz = null;
        try {
            callerClazz = Class.forName(elements[2].getClassName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        Objects.requireNonNull(callerClazz);
        if (!clazz.isAssignableFrom(callerClazz)) {
            throw new IllegalCallerException(message);
        }
    }

    /**
     * 条件抛出异常
     *
     * @param condition 条件
     *                  如果为true，则抛出所指定的异常
     * @author Shao Yibo
     * @date 2024/4/3
     */
    public static Thrower throwOnCondition(boolean condition) {
        if (condition) {
            return new Unexpected();
        }
        return new Expected();
    }

    public static void throwOnCondition(boolean condition, Throwable e) {
        if (condition) {
            throw new RuntimeException(e);
        }
    }

    public static void throwOnNull(Object... NonNulObjects) {
        for (Object obj : NonNulObjects) {
            if (obj == null) throw new NullPointerException();
        }
    }

    public static void throwOnNotNull(Object... Null) {
        for (Object obj : Null) {
            if (obj != null) throw new NullPointerException();
        }
    }

    public static void validateRequest(boolean condition) {
        if (condition) {
            throw new Http400BadRequest();
        }
    }

    public static void validateRequest(boolean condition, String message) {
        if (condition) {
            throw new Http400BadRequest(message);
        }
    }


    public interface Thrower {
        void badCaller();

        void badCaller(String message);

        void badState();

        void badState(String message);

        void badDataRelation();

        void badDataRelation(String message);

        void illegalArgument();

        void illegalArgument(String message);

    }

    public static class Unexpected implements Thrower {
        private Unexpected() {

        }

        @Override
        public void badCaller() {
            throw new IllegalCallerException();
        }

        @Override
        public void badCaller(String message) {
            throw new IllegalCallerException(message);
        }

        @Override
        public void badState() {
            throw new IllegalStateException();
        }

        @Override
        public void badState(String message) {
            throw new IllegalStateException(message);
        }

        @Override
        public void badDataRelation() {
            throw new IllegalDataRelationException("Unexpected Data.");
        }

        @Override
        public void badDataRelation(String message) {
            throw new IllegalDataRelationException(message);
        }

        /**
         *
         */
        @Override
        public void illegalArgument() {
            throw new IllegalArgumentException();
        }

        /**
         * @param message
         */
        @Override
        public void illegalArgument(String message) {
            throw new IllegalArgumentException(message);
        }
    }

    public static class Expected implements Thrower {
        private Expected() {

        }

        @Override
        public void badCaller() {

        }

        @Override
        public void badCaller(String message) {

        }

        @Override
        public void badState() {

        }

        @Override
        public void badState(String message) {

        }

        @Override
        public void badDataRelation() {

        }

        @Override
        public void badDataRelation(String message) {

        }

        /**
         *
         */
        @Override
        public void illegalArgument() {

        }

        /**
         * @param message
         */
        @Override
        public void illegalArgument(String message) {

        }
    }
}
