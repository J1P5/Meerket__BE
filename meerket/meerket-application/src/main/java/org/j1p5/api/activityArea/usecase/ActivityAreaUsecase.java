package org.j1p5.api.activityArea.usecase;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.activityArea.service.ActivityAreaService;
import org.j1p5.domain.activityArea.dto.ActivityAreaFullAddress;
import org.locationtech.jts.geom.Point;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityAreaUsecase {

    private final ActivityAreaService activityAreaService;

    public Page<ActivityAreaFullAddress> getAreas(Double longitude, Double latitude, int page, int size) {
        Point coordinate = activityAreaService.getPoint(longitude, latitude);

        return activityAreaService.getAreas(coordinate, page, size);
    }
}
