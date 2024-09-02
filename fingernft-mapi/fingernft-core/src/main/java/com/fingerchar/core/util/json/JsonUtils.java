package com.fingerchar.core.util.json;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fingerchar.core.util.StringConst;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * object <-> json
 *
 * @author lance
 * @since 2024/8/17 19:44
 */
@Slf4j
@UtilityClass
public class JsonUtils {
  /**
   * 实例化ObjectMapper对象
   */
  private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

  static {
    SimpleModule simpleModule = new SimpleModule();
    //JSON Long ==> String 前端Long精度丢失
    simpleModule.addSerializer(Long.class, ToStringSerializer.instance);
    simpleModule.addSerializer(BigInteger.class, ToStringSerializer.instance);
    simpleModule.addSerializer(BigDecimal.class, ToStringSerializer.instance);

    OBJECT_MAPPER
        // 设置允许序列化空的实体类（否则会抛出异常）
        .disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
        // 设置在遇到未知属性的时候不抛出异常
        .disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES)
        // 设置date -> timestamp
        .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS)
        // 强制JSON 空字符串("")转换为null对象值
        .enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT)
        // 设置数字丢失精度问题
        .enable(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS)
        // 设置没有引号的字段名
        .enable(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES)
        // 设置允许单引号
        .enable(JsonParser.Feature.ALLOW_SINGLE_QUOTES)
        // 设置接受只有一个元素的数组的反序列化
        .configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true)
        // 处理java1.8 LocalDate
        .registerModules(new JavaTimeModule(), simpleModule)
    ;
  }

  /**
   * 对象转json字符串
   *
   * @param obj object
   * @return string
   */
  public static String toJsonString(Object obj) {
    try {
      return OBJECT_MAPPER.writeValueAsString(obj);
    } catch (JsonProcessingException e) {
      log.warn("object -> json fail: ", e);
    }

    return StringConst.EMPTY;
  }

  /**
   * json字符串转换bean
   *
   * @param json  json string
   * @param clazz class<T>
   * @return T
   */
  public static <T> T parseObject(String json, Class<T> clazz) {
    try {
      return OBJECT_MAPPER.readValue(json, clazz);
    } catch (IOException e) {
      log.warn("json -> object fail: ", e);
    }
    return null;
  }

  /**
   * json file -> bean
   *
   * @param jsonPath json file
   * @param clazz    bean
   * @return T
   */
  public static <T> T file2Object(String jsonPath, Class<T> clazz) {
    try {
      return OBJECT_MAPPER.readValue(Paths.get(jsonPath).toFile(), clazz);
    } catch (IOException e) {
      log.warn("json file -> object fail: ", e);
    }
    return null;
  }

  /**
   * json字符串转Map
   *
   * @param json string
   * @return map<String, Object>
   */
  public static Map<String, Object> parseMap(String json) {
    MapTypeReference reference = new MapTypeReference();
    try {
      return OBJECT_MAPPER.readValue(json, reference);
    } catch (JsonProcessingException e) {
      log.warn("json -> Map fail: ", e);
    }

    return Collections.emptyMap();
  }

  /**
   * Java bean -> map<String, Object>
   *
   * @param object bean
   * @return map<String, Object>
   */
  @SuppressWarnings("unchecked")
  public static Map<String, Object> object2Map(Object object) {
    try {
      return OBJECT_MAPPER.convertValue(object, Map.class);
    } catch (IllegalArgumentException e) {
      log.warn("object -> Map fail: ", e);
    }

    return Collections.emptyMap();
  }

  /**
   * Map -> Java bean
   *
   * @param map map<String, Object>
   * @return T
   */
  public static <T> T map2Object(Map<String, Object> map, Class<T> clazz) {
    try {
      return OBJECT_MAPPER.convertValue(map, clazz);
    } catch (IllegalArgumentException e) {
      log.warn("object -> Map fail: ", e);
    }

    return null;
  }

  /**
   * json字符串转对象集合
   *
   * @param json  string
   * @param clazz class
   * @return List<T>
   */
  public static <T> List<T> parseArray(String json, Class<T> clazz) {
    JavaType javaType = OBJECT_MAPPER.getTypeFactory().constructCollectionType(List.class, clazz);
    try {
      return OBJECT_MAPPER.readValue(json, javaType);
    } catch (IOException e) {
      log.warn("json -> list<T> fail: ", e);
    }

    return Collections.emptyList();
  }

  /**
   * 字符串转换对象, 指定TypeReference
   *
   * @param json      json字符串
   * @param reference TypeReference
   * @param <T>       T
   * @return T
   */
  public static <T> T parseObject(String json, TypeReference<T> reference) {
    try {
      return OBJECT_MAPPER.readValue(json, reference);
    } catch (JsonProcessingException var3) {
      log.warn("json -> failed to parseObject: ", var3);
      return null;
    }
  }
}
