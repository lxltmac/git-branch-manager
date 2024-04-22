package com.geostar.geostack.git_branch_manager.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import lombok.NonNull;
import lombok.SneakyThrows;

import java.io.IOException;

/**
 * json工具类
 */
public class JsonUtil {

  public JsonUtil() {
    objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    objectMapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
    objectMapper.configure(DeserializationFeature.USE_BIG_DECIMAL_FOR_FLOATS, true);
  }

  public static String toJSONString(Object req) {
    return getInstance().toJsonStr(req);
  }

  @SneakyThrows
  public <T> String toJsonStr(final T data) {
      return this.objectMapper.writeValueAsString(data);
  }

  @SneakyThrows
  public <T> String prettyJsonStr(final T data) {
    return this.objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(data);
  }

  public <T> T toObj(@NonNull final String jsonStr, Class<T> clazz) throws JsonProcessingException, IOException {
      return this.objectMapper.readValue(jsonStr.getBytes(), clazz);
  }

  public <T> T toObj(@NonNull byte[] bytes, Class<T> clazz) throws IOException {
      return this.objectMapper.readValue(bytes, clazz);
  }

  public <T> T toGenericObj(@NonNull final String jsonStr, TypeReference valueTypeRef) throws JsonProcessingException, IOException {
      return (T) this.objectMapper.readValue(jsonStr.getBytes(), valueTypeRef);
  }

  public <T> T convertValue(Object fromValue, Class<T> clazz) {
    return objectMapper.convertValue(fromValue, clazz);
  }

  public JsonNode readTree(String content) throws IOException {
    return objectMapper.readTree(content);
  }

  public static JsonUtil getInstance() {
    return INSTANCE;
  }

  private static final JsonUtil INSTANCE = new JsonUtil();

  private final ObjectMapper objectMapper = new ObjectMapper();
}
