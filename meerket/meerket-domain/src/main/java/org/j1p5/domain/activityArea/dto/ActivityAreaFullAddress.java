package org.j1p5.domain.activityArea.dto;


public record ActivityAreaFullAddress(String address, String emdId) {
    public static ActivityAreaFullAddress of(String address, String emdId) {
        return new ActivityAreaFullAddress(address, emdId);
    }
}
