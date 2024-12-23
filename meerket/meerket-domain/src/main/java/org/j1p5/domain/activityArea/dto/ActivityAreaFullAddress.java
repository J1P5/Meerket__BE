package org.j1p5.domain.activityArea.dto;

public record ActivityAreaFullAddress(String fullAddress, Integer emdId) {
    public static ActivityAreaFullAddress of(ActivityAreaAddress address) {
        String fullAddress = address.sidoName() + " " + address.sggName() + " " + address.emdName();
        return new ActivityAreaFullAddress(fullAddress, address.emdId());
    }
}
