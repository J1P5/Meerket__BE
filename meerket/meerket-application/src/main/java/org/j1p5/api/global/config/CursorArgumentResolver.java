package org.j1p5.api.global.config;

import org.j1p5.common.annotation.CursorDefault;
import org.j1p5.common.dto.Cursor;
import org.springframework.core.MethodParameter;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class CursorArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(CursorDefault.class)
                && parameter.getParameterType().equals(Cursor.class);
    }

    @Override
    public Object resolveArgument(
            MethodParameter parameter,
            ModelAndViewContainer mavContainer,
            NativeWebRequest webRequest,
            WebDataBinderFactory binderFactory)
            throws Exception {
        CursorDefault cursorDefault = parameter.getParameterAnnotation(CursorDefault.class);

        String cursorParam = webRequest.getParameter("cursor");
        long cursor = (cursorParam == null) ? cursorDefault.cursor() : Long.parseLong(cursorParam);

        String sizeParam = webRequest.getParameter("size");
        int size = (sizeParam == null) ? cursorDefault.size() : Integer.parseInt(sizeParam);

        return new Cursor(cursor, size);
    }
}
