package org.j1p5.common.dto;

import java.util.List;
import java.util.function.Function;

public record CursorResult<T>(List<T> content, Long nextCursor) {

    public static <T> CursorResult<T> of(List<T> content, Long nextCursor) {
        return new CursorResult<>(content, nextCursor);
    }

    public <R> CursorResult<R> map(Function<T, R> mapper) {
        List<R> mappedContent = content.stream().map(mapper).toList();
        return of(mappedContent, nextCursor);
    }
}
/*
* 커서기반 페이징처리를 효율적으로 표현하기 위해 만든 CursorResult자료형
* contentList: 조회된 데이터 리스트 (List<ProductResponseInfo>).
  nextCursor: 다음 페이지를 조회하기 위한 커서 값 (Long)
* of : CursorResult형 객체를 생성하기  위한 메소드
* map : content리스트의 데이터를 다른타입으로 변환하기 위한 메소드
* */
