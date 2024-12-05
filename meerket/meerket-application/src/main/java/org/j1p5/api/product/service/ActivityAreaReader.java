package org.j1p5.api.product.service;

import org.j1p5.domain.activityArea.entity.ActivityArea;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class ActivityAreaReader {
    public Point getActivityArea(List<ActivityArea> activityAreas) {

        List<Point> pointList = new ArrayList<>();
        for (ActivityArea activityArea : activityAreas) {
            Point userCoordinate = activityArea.getEmdArea().getCoordinate();
            pointList.add(userCoordinate);
        } // 일단은 활동지역 1개로 제한된 상태
        Point coordinate = pointList.get(0);
        return coordinate;
    }
}
// 사용자 activityArea를 조회하고 그 좌표 반환해주는 메소드
