package com.fingerchar.core.config.serializer;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

import java.io.IOException;
import java.time.LocalTime;

/**
 * TimeSerializer
 *
 * @author Black_Dragon
 * @since 2021/6/2
 */
public class TimeSerializer extends JsonSerializer<LocalTime> {

  @Override
  public void serialize(LocalTime value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
    if (value != null) {
      gen.writeNumber(LocalTime.parse(value.toString()).toSecondOfDay());
    }
  }
}
