package org.j1p5.api.activityArea.usecase;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.activityArea.service.ActivityAreaService;
import org.j1p5.common.dto.PageResult;
import org.j1p5.domain.activityArea.dto.ActivityAreaFullAddress;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityAreaReadUsecase {

    private final ActivityAreaService activityAreaService;

    public PageResult<ActivityAreaFullAddress> getAreas(Double longitude, Double latitude, int page, int size) {
        Point coordinate = activityAreaService.getPoint(longitude, latitude);

        return activityAreaService.getAreas(coordinate, page, size);
    }

    public PageResult<ActivityAreaFullAddress> getAreasWithKeyword(int page, int size, String keyword) {
        return activityAreaService.getAreasWithKeyword(page, size, keyword);
    }
}
