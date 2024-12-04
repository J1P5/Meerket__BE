package org.j1p5.domain.activityArea.dto;


public record ActivityAreaFullAddress(String address, Integer emdId) {
    public static ActivityAreaFullAddress of(String address, Integer emdId) {
        return new ActivityAreaFullAddress(address, emdId);
    }
}
