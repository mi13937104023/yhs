package com.ddzn.dd.framework.common.util.converter;


import com.ddzn.dd.model.enums.BaseCodeEnum;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.beans.BeanInfo;
import java.beans.Introspector;
import java.beans.PropertyDescriptor;
import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 对象转换
 */
@Slf4j
public class ConvertUtils {

    /**
     * 将一个 Map 对象转化为一个 JavaBean
     *
     * @param javaBean 要转化的类型
     * @param map      包含属性值的 map
     * @return 转化出来的 JavaBean 对象
     */
    @SuppressWarnings("rawtypes")
    public static <T> T convertToBean(T javaBean, Map map) {

        try {

            // 获取javaBean属性
            BeanInfo beanInfo = Introspector.getBeanInfo(javaBean.getClass());
            // 创建 JavaBean 对象
            Object obj = javaBean.getClass().newInstance();

            // 给 JavaBean 对象的属性赋值
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            if (propertyDescriptors != null && propertyDescriptors.length > 0) {

                String propertyName = null; // javaBean属性名
                Object propertyValue = null; // javaBean属性值
                for (PropertyDescriptor descriptor : propertyDescriptors) {

                    propertyName = descriptor.getName();
                    if (map.containsKey(propertyName)) {

                        propertyValue = map.get(propertyName);
                        if (null == propertyValue) {
                            propertyValue = "";
                        }

                        try {

                            descriptor.getWriteMethod().invoke(obj, new Object[]{propertyValue});
                        } catch (InvocationTargetException e) {

                            //log.error("字段映射失败，属性：{}", propertyName);
                        }
                    }
                }
                return (T) obj;
            }
        } catch (Exception e) {
        }
        return null;
    }

    /**
     * 将一个 JavaBean 对象转化为一个 Map
     *
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的 Map 对象
     */
    @SuppressWarnings("rawtypes")
    public static <T> Map convertToMap(T bean) {

        try {

            Class<? extends Object> clazz = bean.getClass();
            Map<String, Object> returnMap = new HashMap<String, Object>();

            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : propertyDescriptors) {

                String propertyName = descriptor.getName();
                if (!"class".equals(propertyName)) {

                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(bean, new Object[0]);
                    if (null != result) {
                        if (result instanceof BaseCodeEnum) {
                            result = ((BaseCodeEnum) result).getCode();
                        } else {
                            result = result.toString();
                        }
                    }
                    returnMap.put(propertyName, result);
                }
            }
            return returnMap;
        } catch (Exception e) {
            log.error("convertToMap is error,ex:{}", e.getMessage(), e.getStackTrace());
        }
        return null;
    }

    /**
     * 将一个 JavaBean 对象转化为一个 Map
     *
     * @param bean 要转化的JavaBean 对象
     * @return 转化出来的 Map 对象
     */
    @SuppressWarnings("rawtypes")
    public static <T> Map<String, Object> convertToMapObj(T bean) {

        try {

            Class<? extends Object> clazz = bean.getClass();
            Map<String, Object> returnMap = new ConcurrentHashMap<>();

            BeanInfo beanInfo = Introspector.getBeanInfo(clazz);
            PropertyDescriptor[] propertyDescriptors = beanInfo.getPropertyDescriptors();
            for (PropertyDescriptor descriptor : propertyDescriptors) {

                String propertyName = descriptor.getName();
                if (!"class".equals(propertyName)) {
                    Method readMethod = descriptor.getReadMethod();
                    Object result = readMethod.invoke(bean, new Object[0]);
                    if (result != null) {
                        returnMap.put(propertyName, result);
                    }
                }
            }
            return returnMap;
        } catch (Exception e) {
            log.error("convertToMapObj is error,ex:{}", e.getMessage(), e.getStackTrace());
        }
        return null;
    }

    /**
     * 根据属性名获取属性值
     */
    public static Object getFieldValueByName(String fieldName, Object o) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String getter = "get" + firstLetter + fieldName.substring(1);
            Method method = o.getClass().getMethod(getter, new Class[]{});
            Object value = method.invoke(o, new Object[]{});
            return value;
        } catch (Exception e) {
            log.error("getFieldValueByName is error,ex:{}", e.getMessage(), e.getStackTrace());
            return null;
        }
    }

    /**
     * 执行set方法
     *
     * @param o         执行对象
     * @param fieldName 属性
     * @param value     值
     */
    public static void setFieldValueByName(String fieldName, Object o, Object value) {
        try {
            String firstLetter = fieldName.substring(0, 1).toUpperCase();
            String setter = "set" + firstLetter + fieldName.substring(1);
            Class[] parameterTypes = new Class[1];
            Field field = o.getClass().getDeclaredField(fieldName);
            parameterTypes[0] = field.getType();
            Method method = o.getClass().getMethod(setter, parameterTypes);
            method.invoke(o, new Object[]{value});
        } catch (Exception e) {
            log.error("setFieldValueByName is error,ex:{}", e.getMessage(), e.getStackTrace());
        }
    }

    /**
     * 将url参数转换成map
     *
     * @param param aa=11&bb=22&cc=33
     * @return
     */
    public static Map<String, Object> getUrlParams(String param) {

        Map<String, Object> map = new HashMap<String, Object>(0);
        if (StringUtils.isBlank(param)) {
            return map;
        }
        String[] params = param.split("&");
        for (int i = 0; i < params.length; i++) {
            String[] p = params[i].split("=");
            if (p.length == 2) {
                map.put(p[0], p[1]);
            }
        }
        return map;
    }

    /**
     * 将map转换成url
     *
     * @param map
     * @return
     */
    public static String getUrlParamsByMap(Map<String, String> map) {
        String s = "";
        if (map == null) {
            return "";
        }
        map = sortMapByKey(map);//按key排序
        Object value = null;
        if (map != null) {
            StringBuffer sb = new StringBuffer();
            for (Map.Entry<String, String> entry : map.entrySet()) {
                value = entry.getValue();//排除空值
                if (null != value && !"".equals(value.toString().trim())) {
                    sb.append(entry.getKey() + "=" + entry.getValue());
                    sb.append("&");
                }
            }
            s = sb.toString();
            if (s.endsWith("&")) {
                s = StringUtils.substringBeforeLast(s, "&");
            }
        }

        return s;
    }

    /**
     * 使用 Map按key进行排序
     *
     * @param map
     * @return
     */
    public static Map<String, String> sortMapByKey(Map<String, String> map) {

        if (map == null || map.isEmpty()) {

            return null;
        }

        Map<String, String> sortMap = new TreeMap<String, String>(new MapKeyComparator());
        sortMap.putAll(map);

        return sortMap;
    }

    public static <T> Optional<byte[]> objectToBytes(T obj) {
        byte[] bytes = null;
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ObjectOutputStream sOut;
        try {
            sOut = new ObjectOutputStream(out);
            sOut.writeObject(obj);
            sOut.flush();
            bytes = out.toByteArray();
        } catch (IOException e) {
            log.error("objectToBytes is error,ex:{}", e.getMessage(), e.getStackTrace());
        }
        return Optional.ofNullable(bytes);
    }

    public static <T> Optional<T> bytesToObject(byte[] bytes) {
        T t = null;
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        ObjectInputStream sIn;
        try {
            sIn = new ObjectInputStream(in);
            t = (T) sIn.readObject();
        } catch (Exception e) {
            log.error("bytesToObject is error,ex:{}", e.getMessage(), e.getStackTrace());
        }
        return Optional.ofNullable(t);

    }


    /**
     * 安全转换为Integer
     */
    public static Integer convertToInteger(Object obj) {
        if (obj == null) {
            return null;
        }
        if (obj instanceof Integer) {
            return (Integer) obj;
        }
        try {
            return Integer.parseInt(obj.toString());
        } catch (NumberFormatException e) {
            log.error("转换为Integer失败：{}", obj, e);
            return null;
        }
    }

    /**
     * 安全转换为Long
     */
    public static Long convertToLong(Object obj, Long defaultValue) {
        if (obj == null) {
            return defaultValue;
        }
        if (obj instanceof Long) {
            return (Long) obj;
        }
        try {
            return Long.parseLong(obj.toString());
        } catch (NumberFormatException e) {
            log.error("转换为Long失败：{}，使用默认值{}", obj, defaultValue, e);
            return defaultValue;
        }
    }

}

/**
 *
 */
//比较器类
class MapKeyComparator implements Comparator<String> {

    @Override
    public int compare(String str1, String str2) {

        return str1.compareTo(str2);
    }
}
