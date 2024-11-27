package org.j1p5.common.dto;

public record Cursor(Long cursor, Integer size) {
}
//cursor : 마지막으로 조회된 id or timestamp
//size 한번에 몇개 가져올건지