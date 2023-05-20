package com.my.selfimprovement.config;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;

import java.io.IOException;

public class StringTrimmingDeserializer extends StdDeserializer<String> {

    protected StringTrimmingDeserializer(Class<String> vc) {
        super(vc);
    }

    @Override
    public String deserialize(JsonParser p, DeserializationContext context) throws IOException {
        return p.getText() != null ? p.getText().trim() : null;
    }

}
