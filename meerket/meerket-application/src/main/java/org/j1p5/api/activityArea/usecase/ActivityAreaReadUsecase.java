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

    /**
     * 위경도 기반 동네 조회 기능
     *
     * @author Icecoff22
     * @param longitude 경도 값
     * @param latitude 위도 값
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @return pagination된 dto
     */
    public PageResult<ActivityAreaFullAddress> getAreas(
            Double longitude, Double latitude, int page, int size) {
        Point coordinate = activityAreaService.getPoint(longitude, latitude);

        return activityAreaService.getAreas(coordinate, page, size);
    }

    /**
     * 키워드 기반 동네 검색 기능
     *
     * @author Icecoff22
     * @param page 페이지 번호
     * @param size 페이지 크기
     * @param keyword 키워드
     * @return pagination된 dto
     */
    public PageResult<ActivityAreaFullAddress> getAreasWithKeyword(
            int page, int size, String keyword) {
        return activityAreaService.getAreasWithKeyword(page, size, keyword);
    }
}
