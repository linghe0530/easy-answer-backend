package com.crane.answer.utils;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Type;
import java.util.List;

/**
 * @author crane
 * @date 2025.06.25 下午10:15
 * @description
 **/
@Slf4j
public class JsonUtils {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    static {
        // 2. 忽略未知字段（JSON 中有多余字段时不报错）
        OBJECT_MAPPER.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    public static <T> Page<T> parsePage(String json) {
        try {
            return OBJECT_MAPPER.readValue(json, new TypeReference<>() {
            });
        } catch (Exception e) {
            log.error("json parse error:", e);
            throw new RuntimeException();

        }
    }

    public static <T> T parse(String json, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(json, clazz);
        } catch (Exception e) {
            log.error("json parse error:", e);
            throw new RuntimeException();

        }
    }

    public static String toJson(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("to json error:", e);
            throw new RuntimeException();
        }
    }

    public static Object parse(String json, Type type) throws JsonProcessingException {
        if (json == null || json.isEmpty()) {
            return null;
        }
        JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructType(type);
        return OBJECT_MAPPER.readValue(json, javaType);
    }


    /**
     * 将JSON字符串转为List集合
     */
    public static <T> List<T> parseList(String json) {
        // 转换为List<User>
        try {
            if (StringUtils.isBlank(json)) {
                return CollUtils.emptyList();
            }
            return OBJECT_MAPPER.readValue(json, new TypeReference<>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> List<T> parseList(String json, Class<T> clazz) {
        try {
            if (StringUtils.isBlank(json)) {
                return CollUtils.emptyList();
            }
            JavaType type = OBJECT_MAPPER.getTypeFactory().constructParametricType(List.class, clazz);
            return OBJECT_MAPPER.readValue(json, type);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public static String toLongJson(Object obj) {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();

        // 自定义序列化器：只对字段名包含"id"的Long类型进行转换
        module.addSerializer(Long.class, ToStringSerializer.instance);
        module.addSerializer(Long.TYPE, ToStringSerializer.instance);
        objectMapper.registerModule(module);
        try {
            return objectMapper.writeValueAsString(obj);
        } catch (Exception e) {
            log.error("to json error:", e);
            throw new RuntimeException();
        }
    }
}
