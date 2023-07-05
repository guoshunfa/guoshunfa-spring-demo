package com.panda.utils;

import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.*;

/**
 * @ClassName PandaReflectUtils
 * @Description 反射工具类
 * @Author guoshunfa
 * @Date 2021/11/23 下午1:58
 * @Version 1.0
 **/
public class PandaReflectUtils {

    public static Class<?> getClassForName(String className) {
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(String className) {
        try {
            return (T) Class.forName(className).newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T newInstance(Class<?> clazz) {
        try {
            return (T) clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static void setFieldValue(Object vo, String key, Object value) {
        try {
            Field fieldx = getField(vo.getClass(), key);
            fieldx.setAccessible(true);
            fieldx.set(vo, value);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 获取类上带注解的字段
     *
     * @param clz             指定类
     * @param annotationClass 带注解的字段
     * @return not null
     */
    @SuppressWarnings("rawtypes")
    public static <T extends Annotation> List<Field> getFieldsByAnnotation(Class clz, Class<T> annotationClass) {
        List<Field> descFields = new ArrayList<Field>();
        Field[] fields = clz.getDeclaredFields();
        for (Field fd : fields) {
            T desc = fd.getAnnotation(annotationClass);
            if (desc != null) {
                descFields.add(fd);
            }
        }
        return descFields;
    }

    /**
     * 获取类上指定注解的方法
     *
     * @param clz
     * @param annotationClass
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static <T extends Annotation> Method getMethodByAnnotation(Class clz, Class<T> annotationClass) {
        Method[] methods = clz.getDeclaredMethods();
        for (Method m : methods) {
            T annotation = m.getAnnotation(annotationClass);
            if (annotation != null) {
                return m;
            }
        }
        return null;
    }

    /**
     * 带注解的多个方法
     *
     * @param clz
     * @param annotationClass
     * @return
     */
    public static <T extends Annotation> List<Method> getMethodByAnnotations(Class clz, Class<T> annotationClass) {
        List<Method> result = new ArrayList<>();
        Method[] methods = clz.getDeclaredMethods();
        for (Method m : methods) {
            T annotation = m.getAnnotation(annotationClass);
            if (annotation != null) {
                result.add(m);
            }
        }
        return result;
    }

    /**
     * @param vo
     * @param field
     * @return 属性必须存在
     */
    @SuppressWarnings("unchecked")
    public static <T> T getFieldValue(Object vo, String field) {
        try {

            Field fieldx = getField(vo.getClass(), field);
            fieldx.setAccessible(true);
            return (T) fieldx.get(vo);
        } catch (Exception e) {
            throw new RuntimeException("字段获取失败class:" + vo.getClass() + "--field:" + field, e);
        }
    }

    /**
     * @param vo
     * @param field
     * @param defaultValue 字段没有值时的默认值
     * @return 属性必须存在
     */
    public static <T> T getFieldValue(Object vo, String field, T defaultValue) {
        T t = getFieldValue(vo, field);
        return t != null ? t : defaultValue;
    }

    /**
     * 通过属性名获取类上的属性  A.a
     * 支持多层获取，如workDetail.materialBill.materialId，直接获取到materialId属性的Field
     *
     * @param clazz     类
     * @param fieldName 完整的属性名，多级的包含.
     * @return 属性不存在返回null
     */
    public static Field getField(Class<?> clazz, String fieldName) {
        String[] split = fieldName.split("\\.");
        // 递归获取到最结尾属性field
        Field field = getMultilayerField(clazz, split, 0);
        if (field == null) {
//            LogUtils.errTemp.debug("字段不存在 classz:{},fieldName:{}"
//                    ,clazz.getName(),fieldName);
        }
        return field;
    }

    /**
     * 获取指定类型的字段，只返回一个
     *
     * @param clazz
     * @param fieldClasses
     * @return 可null
     */
    public static Field getField(Class<?> clazz, Class[] fieldClasses) {
        for (Field field : clazz.getDeclaredFields()) {
            for (Class onefieldClass : fieldClasses) {
                if (field.getType().equals(onefieldClass)) {
                    return field;
                }
            }
        }
        return null;
    }

    public static Field getFieldByClass(Class<?> clazz, String fieldClass) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType().getName().equals(fieldClass)) {
                return field;
            }
        }
        return null;
    }

    public static Field getFieldByClass(Class<?> clazz, Class fieldClass) {
        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType().equals(fieldClass)) {
                return field;
            }
        }
        return null;
    }

    public static List<Field> getFieldsByClasses(Class<?> clazz, Class fieldClass, List<Field> result) {

        for (Field field : clazz.getDeclaredFields()) {
            if (field.getType().equals(fieldClass)) {
                result.add(field);
            }
        }
        while (clazz.getSuperclass() != null) {
            getFieldsByClasses(clazz.getSuperclass(), fieldClass, result);
            return result;
        }
        return result;
    }

    /**
     * 内部使用，不直接对外。
     *
     * @param clazz      当前类class
     * @param fieldNames 多级属性
     * @param index      按层级递归时的索引，满足索引条件跳出
     * @return
     */
    private static Field getMultilayerField(Class<?> clazz, String[] fieldNames, int index) {
        //当前级别的属性
        Field field = PandaReflectUtils.getSuperclassField(clazz).stream().filter(a -> a.getName().equals(fieldNames[index]))
                .findAny().orElse(null);
        if (field == null) {
            return null;
        }
        if (fieldNames.length - 1 == index) {
            return field;
        } else {
            return getMultilayerField(PandaReflectUtils.getType(field), fieldNames, index + 1);
        }
    }

    /**
     * 获取所有字段
     *
     * @param cls
     * @return
     */
    @SuppressWarnings({"rawtypes", "unchecked"})
    public static List<Field> getFieldList(Class cls) {
        List fields = new ArrayList<Field>();
        try {

            for (Class acls = cls; acls != null; acls = acls.getSuperclass()) {
                for (Field field : acls.getDeclaredFields()) {
                    fields.add(field);
                }
            }
            for (Field field : cls.getFields()) {
                fields.add(field);
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return fields;
    }

    /**
     * 根据注解获得字段
     *
     * @param clazz
     * @param annotationClass
     * @return
     */
    @SuppressWarnings({"rawtypes"})
    public static <T extends Annotation> Field getFieldByAnnotation(Class clazz, Class<T> annotationClass) {
        try {
            List<Field> fields = getFieldList(clazz);
            for (Field f : fields) {
                T annotation = f.getAnnotation(annotationClass);
                if (annotation != null) {
                    return f;
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    /**
     * 执行void方法
     *
     * @param service
     * @param methodName
     * @param params     参数中有空值会报错
     */
    public static void invokeVoidMethod(Object service, String methodName, Object... params) {
        Class[] pclzs = getParamsClaz(params);
        Method m = null;
        try {
            m = service.getClass().getDeclaredMethod(methodName, pclzs);
        } catch (NoSuchMethodException | SecurityException e1) {
//            throw new RuntimeException("不存在方法" + service.getClass().getName() + "." + methodName + " ,参数："
//                    + CollectionsUtils.listToString(Arrays.asList(pclzs)), e1.getCause());
        }
        try {
            m.invoke(service, params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static Class[] getParamsClaz(Object... params) {
        if (params == null) {
            return new Class[0];
        }
        Class[] pclzs = new Class[params.length];
        for (int i = 0; i < params.length; i++) {
            pclzs[i] = params[i].getClass();
        }
        return pclzs;
    }

    /**
     * 执行带返回值方法
     *
     * @param service
     * @param methodName
     * @param params     参数中有空值会报错
     * @return
     */
    public static Object invokeMethod(Object service, String methodName, Object... params) {
        Class[] pclzs = getParamsClaz(params);
        Method m = null;
        try {
            m = service.getClass().getDeclaredMethod(methodName, pclzs);
        } catch (NoSuchMethodException | SecurityException e1) {
//            throw new RuntimeException("不存在方法" + service.getClass().getName() + "." + methodName + " ,参数："
//                    + CollectionsUtils.listToString(Arrays.asList(pclzs)), e1.getCause());
        }
        try {
            return m.invoke(service, params);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 是否存在对应的方法。方法名和参数完全匹配
     *
     * @param service
     * @param methodName
     * @param params
     * @return
     */
    public static boolean hasMethod(Object service, String methodName, Object... params) {
        try {
            Class[] pclzs = new Class[params.length];
            for (int i = 0; i < params.length; i++) {
                pclzs[i] = params[i].getClass();
            }

            Method m = service.getClass().getDeclaredMethod(methodName, pclzs);
            return m != null;
        } catch (java.lang.NoSuchMethodException e) {
            return false;
        }

    }

    /**
     * 查找类上的指定注解
     *
     * @param clazz
     * @param annotationClass
     * @return
     */
    public static <T extends Annotation> T getAnnotation(Class<?> clazz, Class<T> annotationClass) {
        Annotation[] as = clazz.getAnnotations();
        for (Annotation a : as) {
            if (a.annotationType().equals(annotationClass)) {
                return (T) a;
            }
        }
        return null;
    }

    public static <T extends Annotation> boolean hasAnnotation(Class<?> clazz, Class<T> annotationClass) {
        T a = getAnnotation(clazz, annotationClass);
        return a != null;
    }

    /**
     * 获取类型，如果是集合则取出他的泛型
     *
     * @param field
     * @return
     */
    public static Class<?> getType(Field field) {
        if (field.getType() == java.util.List.class) {
            return PandaReflectUtils.getGenericityByList(field);
        } else {
            return field.getType();
        }
    }

    /**
     * 获取List的泛型。 如果当前field的类型不是List，则返回他的当前类型。
     *
     * @param field
     * @return
     */
    public static Class<?> getGenericityByList(Field field) {
        if (field == null) {
            return null;
        }
        if (java.util.Collection.class.isAssignableFrom(field.getType())) {
            // 如果是List类型，得到其Generic的类型
            Type genericType = field.getGenericType();
            if (genericType == null)
                return null;
            if (genericType instanceof ParameterizedType) {
                ParameterizedType pt = (ParameterizedType) genericType;
                // 得到泛型里的class类型对象
                return (Class<?>) pt.getActualTypeArguments()[0];
            }
        }
        return field.getType();
    }

    /**
     * 获取当前类和父类(继承)中所有的属性Field
     *
     * @param clazz
     * @return
     */
    public static List<Field> getSuperclassField(Class clazz) {
        List<Field> fieldList = new ArrayList<>();
        Class tempClass = clazz;
        while (tempClass != null) {// 当父类为null的时候说明到达了最上层的父类(Object类).
            fieldList.addAll(Arrays.asList(tempClass.getDeclaredFields()));
            tempClass = tempClass.getSuperclass(); // 得到父类,然后赋给自己
        }
        return fieldList;
    }

    /**
     * 获得泛型的class的方法
     *
     * @return
     */
    @SuppressWarnings("rawtypes")
    public static Class getTypeClass(Class clazz) {
        // 如果是List类型，得到其Generic的类型
        Type genericType = clazz.getGenericSuperclass();
        if (genericType == null)
            return null;
        if (genericType instanceof ParameterizedType) {
            ParameterizedType pt = (ParameterizedType) genericType;
            // 得到泛型里的class类型对象
            return (Class<?>) pt.getActualTypeArguments()[0];
        }
        return null;
    }

    public static boolean isAssignableFrom(Class fatherClazz, Class clazz) {
        return fatherClazz.isAssignableFrom(clazz);
    }

}
