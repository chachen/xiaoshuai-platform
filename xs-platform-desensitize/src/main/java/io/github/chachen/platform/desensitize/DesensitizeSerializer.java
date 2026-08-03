package io.github.chachen.platform.desensitize;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

import java.io.IOException;

public class DesensitizeSerializer extends JsonSerializer<Object> implements ContextualSerializer {
    private DesensitizeType type = DesensitizeType.NAME;

    public DesensitizeSerializer() {
    }

    private DesensitizeSerializer(DesensitizeType t) {
        type = t;
    }

    static String mask(String s, DesensitizeType t) {
        if (s == null) return null;
        return switch (t) {
            case MOBILE -> s.length() < 7 ? s : s.substring(0, 3) + "****" + s.substring(s.length() - 4);
            case ID_CARD -> s.length() < 8 ? s : s.substring(0, 3) + "***********" + s.substring(s.length() - 4);
            case BANK_CARD -> s.length() < 8 ? s : "**** **** **** " + s.substring(s.length() - 4);
            case EMAIL -> {
                int i = s.indexOf('@');
                yield i <= 1 ? "***" : s.charAt(0) + "***" + s.substring(i);
            }
            case NAME -> s.length() <= 1 ? "*" : s.charAt(0) + "*".repeat(Math.max(1, s.length() - 1));
            case ADDRESS -> s.length() <= 4 ? "****" : s.substring(0, 3) + "****";
        };
    }

    @Override
    public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
        gen.writeString(mask(String.valueOf(value), type));
    }

    @Override
    public JsonSerializer<?> createContextual(SerializerProvider p, BeanProperty property) {
        Desensitize a = property == null ? null : property.getAnnotation(Desensitize.class);
        return new DesensitizeSerializer(a == null ? type : a.type());
    }
}
