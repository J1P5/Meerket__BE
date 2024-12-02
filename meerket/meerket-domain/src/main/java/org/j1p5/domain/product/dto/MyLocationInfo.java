package org.j1p5.domain.product.dto;

import org.j1p5.domain.user.entity.ActivityArea;

import java.util.List;

public record MyLocationInfo(
        String address// 현재는 내 활동지역 하나만 설정하면댐
) {

    public static MyLocationInfo of(String name){
        return new MyLocationInfo(name);
    }
}

