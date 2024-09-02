package com.fingerchar.core.util.json;

import com.fasterxml.jackson.core.type.TypeReference;

import java.util.Map;

/**
 * MapTypeReference
 *
 * @author lance
 * @since 2024/8/17 19:44
 */
class MapTypeReference extends TypeReference<Map<String, Object>> {
  MapTypeReference() {
  }
}
