package org.j1p5.domain.activityArea.dto;

import org.j1p5.domain.activityArea.entity.ActivityArea;

public record SimpleAddress(Integer emdId, String emdName) {
    public static SimpleAddress from(ActivityArea activityArea) {
        return new SimpleAddress(activityArea.getEmdArea().getId(), activityArea.getEmdArea().getEmdName());
    }
}
