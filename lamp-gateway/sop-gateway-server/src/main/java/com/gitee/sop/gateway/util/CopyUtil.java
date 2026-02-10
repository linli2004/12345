package com.gitee.sop.gateway.util;

import com.alibaba.fastjson2.JSON;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.FatalBeanException;
import org.springframework.util.Assert;
import org.springframework.util.ClassUtils;

import java.beans.PropertyDescriptor;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * 属性拷贝工具类
 *
 * @author 六如
 */
public class CopyUtil extends BeanUtils {

    /**
     * 属性拷贝,第一个参数中的属性值拷贝到第二个参数中<br>
     * 注意:当第一个参数中的属性有null值时,不会拷贝进去
     *
     * @param from             源对象
     * @param to               目标对象
     * @param ignoreProperties 忽略的字段
     * @throws BeansException
     */
    public static void copyPropertiesIgnoreNull(Object from, Object to, String... ignoreProperties)
            throws BeansException {
        Assert.notNull(from, "Source must not be null");
        Assert.notNull(to, "Target must not be null");

        Class<?> actualEditable = to.getClass();
        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);
        List<String> ignoreList = (ignoreProperties != null ? Arrays.asList(ignoreProperties) : Collections.emptyList());

        for (PropertyDescriptor targetPd : targetPds) {
            if (ignoreList.contains(targetPd.getName())) {
                continue;
            }
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(from.getClass(), targetPd.getName());
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null &&
                        ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                        try {
                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            Object value = readMethod.invoke(from);
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            // 这里判断value是否为空 当然这里也能进行一些特殊要求的处理
                            // 例如绑定时格式转换等等
                            if (value != null) {
                                writeMethod.invoke(to, value);
                            }
                        } catch (Throwable ex) {
                            throw new FatalBeanException(
                                    "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                        }
                    }
                }
            }
        }
    }

    /**
     * 拷贝指定的字段
     *
     * @param from          源对象
     * @param to            目标对象
     * @param includeFields 指定字段
     */
    public static void copyPropertiesInclude(Object from, Object to, Set<String> includeFields) {
        Objects.requireNonNull(includeFields, "includeFields can not null");
        Assert.notNull(from, "Source must not be null");
        Assert.notNull(to, "Target must not be null");
        if (includeFields.isEmpty()) {
            return;
        }
        Class<?> actualEditable = to.getClass();
        PropertyDescriptor[] targetPds = getPropertyDescriptors(actualEditable);

        for (PropertyDescriptor targetPd : targetPds) {
            if (!includeFields.contains(targetPd.getName())) {
                continue;
            }
            Method writeMethod = targetPd.getWriteMethod();
            if (writeMethod != null) {
                PropertyDescriptor sourcePd = getPropertyDescriptor(from.getClass(), targetPd.getName());
                if (sourcePd != null) {
                    Method readMethod = sourcePd.getReadMethod();
                    if (readMethod != null &&
                        ClassUtils.isAssignable(writeMethod.getParameterTypes()[0], readMethod.getReturnType())) {
                        try {
                            if (!Modifier.isPublic(readMethod.getDeclaringClass().getModifiers())) {
                                readMethod.setAccessible(true);
                            }
                            Object value = readMethod.invoke(from);
                            if (!Modifier.isPublic(writeMethod.getDeclaringClass().getModifiers())) {
                                writeMethod.setAccessible(true);
                            }
                            writeMethod.invoke(to, value);
                        } catch (Throwable ex) {
                            throw new FatalBeanException(
                                    "Could not copy property '" + targetPd.getName() + "' from source to target", ex);
                        }
                    }
                }
            }
        }
    }

    /**
     * 拷贝属性
     *
     * @param from 被拷贝类
     * @param to   目标类
     */
    public static void copyProperties(Object from, Object to) {
        BeanUtils.copyProperties(from, to);
    }

    /**
     * 拷贝bean成为一个新类
     *
     * @param from     被拷贝类
     * @param supplier 新的类获取回调
     * @param <T>      新的类
     * @return 返回新的类实例，from为null时，返回null
     */
    public static <T> T copyBean(Object from, Supplier<T> supplier) {
        if (from == null) {
            return null;
        }
        T to = supplier.get();
        BeanUtils.copyProperties(from, to);
        return to;
    }

    /**
     * 拷贝实例
     *
     * @param from     被拷贝类
     * @param supplier 新的类获取回调
     * @param after    对新的类最后续处理回调
     * @param <T>      新的类
     * @return 返回新的类
     */
    public static <T> T copyBean(Object from, Supplier<T> supplier, Consumer<T> after) {
        if (from == null) {
            return null;
        }
        T to = supplier.get();
        BeanUtils.copyProperties(from, to);
        after.accept(to);
        return to;
    }

    /**
     * 拷贝List，将list中的类转换成新的对象
     *
     * @param collection 被拷贝的集合
     * @param toElement  List新元素
     * @param <T>        新元素类型
     * @return 返回新的List
     */
    public static <T> List<T> copyList(Collection<?> collection, Supplier<T> toElement) {
        if (collection == null || collection.isEmpty()) {
            return new ArrayList<>();
        }
        return collection.stream()
                .map(source -> {
                    T target = toElement.get();
                    BeanUtils.copyProperties(source, target);
                    return target;
                })
                .collect(Collectors.toList());
    }

    public static <E, R> List<R> copyList(Collection<E> fromList, Function<E, R> function) {
        if (fromList == null) {
            return new ArrayList<>();
        }
        return fromList.stream()
                .map(source -> {
                    R target = function.apply(source);
                    BeanUtils.copyProperties(source, target);
                    return target;
                })
                .collect(Collectors.toList());
    }

    /**
     * 拷贝List，并做后续处理
     *
     * @param fromList  被拷贝的list
     * @param toElement 新元素
     * @param after     对新元素做后续处理
     * @param <T>       新类型
     * @return 返回新的List
     */
    public static <T> List<T> copyList(Collection<?> fromList, Supplier<T> toElement, Consumer<T> after) {
        if (fromList == null) {
            return new ArrayList<>();
        }
        return fromList.stream()
                .map(source -> {
                    T target = toElement.get();
                    BeanUtils.copyProperties(source, target);
                    after.accept(target);
                    return target;
                })
                .collect(Collectors.toList());
    }

    /**
     * 拷贝List，并做后续处理
     *
     * @param fromList  被拷贝的list
     * @param toElement 新元素
     * @param after     对新元素做后续处理
     * @param <T>       新类型
     * @return 返回新的List
     */
    public static <T, F> List<T> copyList(Collection<F> fromList, Supplier<T> toElement, CopyConsumer<F, T> after) {
        if (fromList == null) {
            return new ArrayList<>();
        }
        return fromList.stream()
                .map(source -> {
                    T target = toElement.get();
                    BeanUtils.copyProperties(source, target);
                    after.apply(source, target);
                    return target;
                })
                .collect(Collectors.toList());
    }

    /**
     * 深层次拷贝，通过json转换的方式实现
     *
     * @param from    待转换的类
     * @param toClass 目标类class
     * @param <T>     目标类
     * @return 返回目标类
     */
    public static <T> T deepCopy(Object from, Class<T> toClass) {
        String json = JSON.toJSONString(from);
        return JSON.parseObject(json, toClass);
    }

    /**
     * 深层次拷贝，通过json转换的方式实现
     *
     * @param from    待转换的类
     * @param toClass 目标类class
     * @param <T>     目标类
     * @return 返回目标类
     */
    public static <T> List<T> deepCopyList(Object from, Class<T> toClass) {
        String json = JSON.toJSONString(from);
        return JSON.parseArray(json, toClass);
    }


    /**
     * 拷贝map
     *
     * @param srcMap      原map
     * @param valueGetter 值转换
     * @param <K>         Key类型
     * @param <V>         Value类型
     * @return 返回新map
     */
    public static <K, V> Map<K, V> copyMap(Map<K, ?> srcMap, Supplier<V> valueGetter) {
        Map<K, V> ret = new LinkedHashMap<>(srcMap.size() * 2);
        for (Map.Entry<K, ?> entry : srcMap.entrySet()) {
            V value = copyBean(entry.getValue(), valueGetter);
            ret.put(entry.getKey(), value);
        }
        return ret;
    }

    /**
     * 拷贝map
     *
     * @param srcMap   原map
     * @param function 值转换
     * @param <K>      Key类型
     * @param <V>      Value类型
     * @return 返回新map
     */
    public static <K, V, V0> Map<K, V> copyMap(Map<K, V0> srcMap, Function<V0, V> function) {
        Map<K, V> ret = new LinkedHashMap<>(srcMap.size() * 2);
        for (Map.Entry<K, V0> entry : srcMap.entrySet()) {
            V value = function.apply(entry.getValue());
            ret.put(entry.getKey(), value);
        }
        return ret;
    }

    /**
     * 拷贝map,value是list
     *
     * @param srcMap      原map
     * @param valueGetter 值转换
     * @param <K>         Key类型
     * @param <V>         Value类型
     * @return 返回新map
     */
    public static <K, V, V2> Map<K, List<V2>> copyMapList(Map<K, List<V>> srcMap, Function<List<V>, List<V2>> valueGetter) {
        Map<K, List<V2>> ret = new LinkedHashMap<>(srcMap.size() * 2);
        for (Map.Entry<K, List<V>> entry : srcMap.entrySet()) {
            List<V2> value = valueGetter.apply(entry.getValue());
            ret.put(entry.getKey(), value);
        }
        return ret;
    }

    public interface CopyConsumer<F, T> {
        void apply(F from, T to);
    }

}
