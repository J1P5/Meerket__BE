package org.j1p5.common.dto;

import java.util.List;
import java.util.function.Function;

public record PageResult<T>(List<T> content, Integer totalPages, Boolean hasNext) {
    public static <T> PageResult<T> of(List<T> content, Integer totalPages, Boolean hasNext) {
        return new PageResult<>(content, totalPages, hasNext);
    }

    public <R> PageResult<R> map(Function<T, R> mapper) {
        List<R> mappedContent = content.stream().map(mapper).toList();
        return of(mappedContent, totalPages, hasNext);
    }
}
