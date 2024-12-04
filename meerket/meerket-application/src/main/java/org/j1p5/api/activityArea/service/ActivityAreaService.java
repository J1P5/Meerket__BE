package org.j1p5.api.activityArea.service;

import lombok.RequiredArgsConstructor;
import org.j1p5.common.dto.PageResult;
import org.j1p5.domain.activityArea.dto.ActivityAreaAddress;
import org.j1p5.domain.activityArea.dto.ActivityAreaFullAddress;
import org.j1p5.domain.activityArea.repository.ActivityAreaRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ActivityAreaService {

    private final ActivityAreaRepository activityAreaRepository;

    public Point getPoint(Double longitude, Double latitude) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        return geometryFactory.createPoint(new Coordinate(longitude, latitude));
    }

    public PageResult<ActivityAreaFullAddress> getAreas(Point coordinate, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ActivityAreaAddress> activityAreaInfos = activityAreaRepository.getActivityAreas(coordinate, pageRequest);

        return convertToAreaName(activityAreaInfos);
    }

    public PageResult<ActivityAreaFullAddress> convertToAreaName(Page<ActivityAreaAddress> activityAreaInfos) {
        List<ActivityAreaAddress> activityAreaInfoList = activityAreaInfos.getContent();
        List<ActivityAreaFullAddress> activityAreaFullAddressList = new ArrayList<>();

        activityAreaInfoList.forEach(activityAreaInfo -> {
            String fullAddress = activityAreaInfo.sidoName() + " " + activityAreaInfo.sggName() + " " + activityAreaInfo.emdName();
            activityAreaFullAddressList.add(ActivityAreaFullAddress.of(fullAddress, activityAreaInfo.emdId()));
        });

        return new PageResult<>(activityAreaFullAddressList, activityAreaInfos.getTotalPages(), activityAreaInfos.hasNext());
    }
}
