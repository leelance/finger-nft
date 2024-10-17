package com.fingerchar.core.config.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.LocalTime;


/**
 * TimeDeserializer
 *
 * @author Black_Dragon
 * @since 2021/6/2
 */
public class TimeDeserializer extends JsonDeserializer<LocalTime> {

  @Override
  public LocalTime deserialize(JsonParser p, DeserializationContext ctx) throws IOException {
    long value = p.getLongValue();
    if (value > 0) {
      return LocalTime.ofSecondOfDay(value);
    }
    return null;
  }
}
