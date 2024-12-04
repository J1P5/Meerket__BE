package org.j1p5.api.activityArea.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.j1p5.api.activityArea.exception.ActivityAreaAlreadyExistException;
import org.j1p5.api.user.exception.EmdAreaNotFoundException;
import org.j1p5.api.user.exception.UserNotFoundException;
import org.j1p5.common.dto.PageResult;
import org.j1p5.domain.activityArea.dto.ActivityAreaAddress;
import org.j1p5.domain.activityArea.dto.ActivityAreaFullAddress;
import org.j1p5.domain.activityArea.entity.ActivityArea;
import org.j1p5.domain.activityArea.repository.ActivityAreaRepository;
import org.j1p5.domain.user.entity.EmdArea;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.repository.EmdAreaRepository;
import org.j1p5.domain.user.repository.UserRepository;
import org.locationtech.jts.geom.Coordinate;
import org.locationtech.jts.geom.GeometryFactory;
import org.locationtech.jts.geom.Point;
import org.locationtech.jts.geom.PrecisionModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.j1p5.api.global.excpetion.WebErrorCode.*;

@Service
@RequiredArgsConstructor
public class ActivityAreaService {

    private final ActivityAreaRepository activityAreaRepository;
    private final UserRepository userRepository;
    private final EmdAreaRepository emdAreaRepository;

    public Point getPoint(Double longitude, Double latitude) {
        GeometryFactory geometryFactory = new GeometryFactory(new PrecisionModel(), 4326);
        return geometryFactory.createPoint(new Coordinate(longitude, latitude));
    }

    public PageResult<ActivityAreaFullAddress> getAreas(Point coordinate, int page, int size) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ActivityAreaAddress> activityAreaInfos = activityAreaRepository.getActivityAreas(coordinate, pageRequest);

        return convertToAreaName(activityAreaInfos);
    }

    public PageResult<ActivityAreaFullAddress> getAreasWithKeyword(int page, int size, String search) {
        PageRequest pageRequest = PageRequest.of(page, size);
        Page<ActivityAreaAddress> activityAreaInfos = activityAreaRepository.getActivityAreasWithKeyword(search, pageRequest);

        return convertToAreaName(activityAreaInfos);
    }

    private PageResult<ActivityAreaFullAddress> convertToAreaName(Page<ActivityAreaAddress> activityAreaInfos) {
        List<ActivityAreaAddress> activityAreaInfoList = activityAreaInfos.getContent();
        List<ActivityAreaFullAddress> activityAreaFullAddressList = new ArrayList<>();

        activityAreaInfoList.forEach(activityAreaInfo -> {
            String fullAddress = activityAreaInfo.sidoName() + " " + activityAreaInfo.sggName() + " " + activityAreaInfo.emdName();
            activityAreaFullAddressList.add(ActivityAreaFullAddress.of(fullAddress, activityAreaInfo.emdId()));
        });

        return new PageResult<>(activityAreaFullAddressList, activityAreaInfos.getTotalPages(), activityAreaInfos.hasNext());
    }

    @Transactional
    public void register(Long userId, Integer emdId) {
        UserEntity user = userRepository.findById(userId)
                        .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        EmdArea emdArea = emdAreaRepository.findById(emdId)
                        .orElseThrow(() -> new EmdAreaNotFoundException(EMD_AREA_NOT_FOUND));

        activityAreaValidation(user);

        activityAreaRepository.save(ActivityArea.create(user, emdArea));
    }

    private void activityAreaValidation(UserEntity user) {
        boolean isExist = activityAreaRepository.existsByUser(user);

        if (isExist) {
            throw new ActivityAreaAlreadyExistException(ACTIVITY_AREA_ALREADY_EXIST);
        }
    }

    @Transactional
    public void delete(Long userId, Integer emdId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        EmdArea emdArea = emdAreaRepository.findById(emdId)
                .orElseThrow(() -> new EmdAreaNotFoundException(EMD_AREA_NOT_FOUND));

        activityAreaRepository.deleteByUserAndEmdArea(user, emdArea);
    }

    public ActivityArea getActivityAreaByUser(Long userId) {
        UserEntity user = userRepository.findById((userId))
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        List<ActivityArea> activityAreas = activityAreaRepository.findByUser(user);
        return activityAreas.isEmpty() ? null : activityAreas.get(0);
    }
}
