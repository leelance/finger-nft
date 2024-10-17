package com.fingerchar.core.config.serializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.util.Date;

/**
 * DateDeserializer
 *
 * @author Black_Dragon
 * @since 2021/5/21
 */
public class DateDeserializer extends JsonDeserializer<Date> {

  @Override
  public Date deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    long value = jsonParser.getLongValue();
    if (value > 0) {
      return new Date(value);
    }
    return null;
  }
}
