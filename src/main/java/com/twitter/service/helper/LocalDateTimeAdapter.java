package com.twitter.service.helper;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
public class LocalDateTimeAdapter implements JsonDeserializer<LocalDateTime> {
    private static final DateTimeFormatter deserializerFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSz");

    @Override
    public LocalDateTime deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        try {
            return LocalDateTime.parse(jsonElement.getAsString(), deserializerFormatter);
        }
        catch (Exception e) {
            log.error("Error during deserialization localDateTime " + e.getMessage(), e);
            return null;
        }
    }
}