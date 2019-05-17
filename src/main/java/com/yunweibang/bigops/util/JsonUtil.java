package com.yunweibang.bigops.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

/**
 *  JSON工具
 *  @author lpp
 *  @since 2017-08-22 15:19:11
 */
public class JsonUtil {

    private static Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    private static final SerializerFeature[] features = {SerializerFeature.WriteMapNullValue, // 输出空置字段
            SerializerFeature.WriteNullListAsEmpty, // list字段如果为null，输出为[]，而不是null
            SerializerFeature.WriteNullNumberAsZero, // 数值字段如果为null，输出为0，而不是null
            SerializerFeature.WriteNullBooleanAsFalse, // Boolean字段如果为null，输出为false，而不是null
            SerializerFeature.WriteNullStringAsEmpty // 字符类型字段如果为null，输出为""，而不是null
    };

    /**
     * 对象转成json字符串
     */
    public static String toJson(Object obj) {
        if (obj == null) {
            return null;
        }
        return JSON.toJSONString(obj);
    }

    /**
     * 对象转成带null的json字符串
     */
    public static String toJsonWithNull(Object obj) {
        if (obj == null) {
            return null;
        }
        return JSON.toJSONString(obj, SerializerFeature.WriteMapNullValue);
    }

    /**
     * json字符串转成对象
     */
    public static Object fromJson(String text) {
        return JSON.parse(text);
    }

    /**
     * json字符串转成对象
     */
    public static <T> T fromJson(String text, Class<T> clazz) {
        return JSON.parseObject(text, clazz);
    }


    /**
     * jackson实现方法
     */
//    private static final ObjectMapper JSON_NON_EMPTY_MAPPER = new ObjectMapper()
//            .setSerializationInclusion(JsonInclude.Include.NON_EMPTY);
//    private static final ObjectMapper JSON_NON_DEFAULT_MAPPER = new ObjectMapper();
//
//    /**
//     * Object可以是POJO，也可以是Collection或数组。
//     *
//     * 如果对象为Null, 返回"null".
//     *
//     * 如果集合为空集合, 返回"[]".
//     */
//    public static String toJson(Object object) {
//        if (object == null) {
//            return null;
//        }
//
//        try {
//            return JSON_NON_DEFAULT_MAPPER.writeValueAsString(object);
//        } catch (IOException e) {
//            logger.error("write json to string [{}] error: {}", object, ToolUtil.getExceptionMsg(e));
//            return null;
//        }
//    }
//
//    /**
//     * Object可以是POJO，也可以是Collection或数组。
//     *
//     * 如果对象为Null, 则不返回此对象.
//     *
//     * 如果集合为空集合, 返回"[]".
//     */
//    public static String toJsonNoNull(Object object) {
//        if (object == null) {
//            return null;
//        }
//
//        try {
//            return JSON_NON_EMPTY_MAPPER.writeValueAsString(object);
//        } catch (IOException e) {
//            logger.error("write json to string [{}] error: {}", object, ToolUtil.getExceptionMsg(e));
//            return null;
//        }
//    }
//
//    /**
//     * 反序列化POJO或简单Collection如List<String>.
//     *
//     * 如果JSON字符串为Null或"null"字符串, 返回Null. 如果JSON字符串为"[]", 返回空集合.
//     *
//     * 如需反序列化复杂Collection如List<MyBean>, 请使用fromJson(String, JavaType)
//     *
//     */
//    public static <T> T fromJson(String jsonString, Class<T> clazz) {
//        if (ToolUtil.isEmpty(jsonString)) {
//            return null;
//        }
//
//        try {
//            return JSON_NON_DEFAULT_MAPPER.readValue(jsonString, clazz);
//        } catch (IOException e) {
//            logger.error("parse json from string [{}] error: {}", jsonString, ToolUtil.getExceptionMsg(e));
//            return null;
//        }
//    }
//
//    /**
//     * 反序列化复杂Collection如List<MyBean>.
//     *
//     * 如果JSON字符串为Null或"null"字符串, 返回Null. 如果JSON字符串为"[]", 返回空集合.
//     *
//     */
//    public static <T> T fromJson(String jsonString, ParameterizedTypeReference<T> type) {
//        if (ToolUtil.isEmpty(jsonString)) {
//            return null;
//        }
//
//        try {
//            TypeFactory typeFactory = JSON_NON_DEFAULT_MAPPER.getTypeFactory();
//            JavaType javaType = typeFactory.constructType(type.getType());
//            return JSON_NON_DEFAULT_MAPPER.readValue(jsonString, javaType);
//        } catch (IOException e) {
//            logger.error("parse json from string [{}] error: {}", jsonString, ToolUtil.getExceptionMsg(e));
//            return null;
//        }
//    }

    /**
     * GSON实现方法
     */
    // private static final Gson gson = new
    // GsonBuilder().serializeNulls().setDateFormat("yyyy-MM-dd HH:mm:ss")
    // .registerTypeAdapter(new TypeToken<Map<String, Object>>() {
    // }.getType(), new JsonDeserializer<Map<String, Object>>() {
    // @Override
    // public Map<String, Object> deserialize(JsonElement json, Type typeOfT,
    // JsonDeserializationContext context) throws JsonParseException {
    // Map<String, Object> Map = new HashMap<>();
    // JsonObject jsonObject = json.getAsJsonObject();
    // Set<Map.Entry<String, JsonElement>> entrySet = jsonObject.entrySet();
    // for (Map.Entry<String, JsonElement> entry : entrySet) {
    // Map.put(entry.getKey(), entry.getValue());
    // }
    // return Map;
    // }
    // }).create();
    //
    // public static String toJson(Object obj) {
    // return gson.toJson(obj);
    // }
    //
    // public static <T> T fromJson(String json, Class<T> classOfT) {
    // return gson.fromJson(json, classOfT);
    // }
    //
    // public static <T> T fromJson(String json, Type typeOfT) {
    // return gson.fromJson(json, typeOfT);
    // }
    // 避免int long转Double的使用方法
    // List<Map<String, Object>> list = JsonUtil.fromJson(str, new
    // TypeToken<List<Map<String, Object>>>() {
    // }.getType());

    public static Map<String, Object> objectToMap(Object obj) {
        if(obj==null) return null;
        Map<String, Object> map = new HashMap<>();
        Class<?> clazz = obj.getClass();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            String fieldName = field.getName();
            Object value = null;
            try {
                value = field.get(obj);
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
            map.put(fieldName, value);
        }
        return map;
    }

}
