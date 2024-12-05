package org.j1p5.api.product.service;

import org.j1p5.domain.activityArea.entity.ActivityArea;
import org.springframework.stereotype.Component;

@Component
public class UserLocationNameReader {
    public String getLocationName(ActivityArea area) {
        return area.getEmdArea().getEmdName();
    }
}
