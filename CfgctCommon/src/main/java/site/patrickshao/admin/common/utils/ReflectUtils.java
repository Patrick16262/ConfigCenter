package site.patrickshao.admin.common.utils;

import jakarta.validation.constraints.NotNull;
import site.patrickshao.admin.common.exception.RuntimeErrors;

import javax.annotation.ParametersAreNonnullByDefault;
import java.io.File;
import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ParametersAreNonnullByDefault
public final class ReflectUtils {

    private static final Map<String, Class<?>> extraClassMap = new HashMap<>();

    public static void registerClass(Class<?> clazz) {
        extraClassMap.put(clazz.getName(), clazz);
    }

    public static Class<?> forName(String name) {
        var clazz = extraClassMap.get(name);
        if (clazz != null) return clazz;

        try {
            return Class.forName(name);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <T> Class<T> getSuperClassGenericArgument(Class<?> clazz) {
        ParameterizedType type = (ParameterizedType) clazz.getGenericSuperclass();
        Type[] types = type.getActualTypeArguments();
        try {
            return (Class<T>) Class.forName(ArrayUtils.getOnlyOne(types).getTypeName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    @NotNull
    public static <T> Class<T> getCurrentSuperClassGenericArgument() {
        StackTraceElement[] elements = Thread.currentThread().getStackTrace();
        RuntimeExceptions.throwOnCondition(elements.length < 3).badCaller("Illegal caller state");
        Class<?> clazz = ReflectUtils.forName(elements[2].getClassName());
        ParameterizedType type = (ParameterizedType) clazz.getGenericSuperclass();
        Type[] types = type.getActualTypeArguments();
        try {
            return (Class<T>) Class.forName(ArrayUtils.getOnlyOne(types).getTypeName());
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public static Field getField(Class<?> clazz, String fieldName) {
        try {
            Field field = clazz.getDeclaredField(fieldName);
            field.setAccessible(true);
            return clazz.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            return RuntimeErrors.emit("Field not found.");
        }
    }

    public static void setFieldValue(Object object, Field field, Object value) {
        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static void setFieldValue(Object object, String fieldName, Object value) {
        Field field = getField(object.getClass(), fieldName);
        try {
            field.setAccessible(true);
            field.set(object, value);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Object object, Field field) {
        try {
            field.setAccessible(true);
            return (T) field.get(object);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes) {
        try {
            return clazz.getDeclaredMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public static Field[] getAnnotationFields(Class<?> clazz, Class<? extends Annotation> annotationClass) {
        ArrayList<Field> fields = new ArrayList<>();
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getAnnotation(annotationClass) != null) {
                field.setAccessible(true);
                fields.add(field);
            }
        }
        RuntimeExceptions.throwOnCondition(fields.isEmpty(), new NoSuchFieldException());
        return fields.toArray(new Field[0]);
    }

    public static List<Class<?>> scan(String packageName) {
        List<Class<?>> classes = new ArrayList<>();
        String packagePath = packageName.replace('.', '/');
        File directory = new File(Thread.currentThread().getContextClassLoader().getResource(packagePath).getFile());
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        classes.addAll(scan(packageName + "." + file.getName()));
                    } else if (file.getName().endsWith(".class")) {
                        String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                        try {
                            classes.add(Class.forName(className));
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }
            }
        }
        return classes;
    }

    public static <T> T newInstance(Class<T> clazz) {
        try {
            Constructor<T> constructor = clazz.getDeclaredConstructor();
            return constructor.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    private ReflectUtils() {
    }
}
