package org.j1p5.api.global.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.AbstractJackson2HttpMessageConverter;
import org.springframework.stereotype.Component;

import java.lang.reflect.Type;

@Component
public class OctetStreamMsgConverter extends AbstractJackson2HttpMessageConverter {
    @Autowired
    public OctetStreamMsgConverter(ObjectMapper objectMapper) {
        super(objectMapper, MediaType.APPLICATION_OCTET_STREAM);
    }

    // 기존 application/octet-stream 타입을 쓰기로 다루는 메시지 컨버터가 이미 존재 (ByteArrayHttpMessageConverter)
    // 따라서 해당 컨버터는 쓰기 작업에는 이용하면 안됨
    @Override
    public boolean canWrite(Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    public boolean canWrite(Type type, Class<?> clazz, MediaType mediaType) {
        return false;
    }

    @Override
    protected boolean canWrite(MediaType mediaType) {
        return false;
    }
}
