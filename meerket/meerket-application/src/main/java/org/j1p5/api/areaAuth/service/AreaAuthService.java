package org.j1p5.api.areaAuth.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.j1p5.api.areaAuth.exception.AreaAuthNotFoundException;
import org.j1p5.api.global.converter.PointConverter;
import org.j1p5.api.user.exception.EmdAreaNotFoundException;
import org.j1p5.api.user.exception.UserNotFoundException;
import org.j1p5.domain.areaAuth.entity.AreaAuthEntity;
import org.j1p5.domain.areaAuth.repository.AreaAuthRepository;
import org.j1p5.domain.areaAuth.vo.AreaAuthInfo;
import org.j1p5.domain.user.entity.EmdArea;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.repository.EmdAreaRepository;
import org.j1p5.domain.user.repository.UserRepository;
import org.locationtech.jts.geom.Point;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

import static org.j1p5.api.global.excpetion.WebErrorCode.*;

@Service
@RequiredArgsConstructor
public class AreaAuthService {

    private final AreaAuthRepository areaAuthRepository;
    private final EmdAreaRepository emdAreaRepository;
    private final UserRepository userRepository;

    @Transactional
    public boolean isMatchMyAuthHistory(Long userId, Integer emdId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        AreaAuthEntity areaAuth = areaAuthRepository.findByUser(user)
                .orElse(null);

        if (areaAuth != null && emdId == areaAuth.getEmdArea().getId()) {
            areaAuth.updateHistory();
            return true;
        }

        return false;
    }

    public boolean checkMyCoordinate(AreaAuthInfo areaAuthInfo) {
        EmdArea emdArea = emdAreaRepository.findById(areaAuthInfo.emdId())
                .orElseThrow(() -> new EmdAreaNotFoundException(EMD_AREA_NOT_FOUND));
        Point coordinate = PointConverter.createPoint(areaAuthInfo.longitude(), areaAuthInfo.latitude());

        return areaAuthRepository.existsInRequestArea(coordinate, emdArea);
    }

    public void saveHistory(Long userId, Integer emdId) {
        EmdArea emdArea = emdAreaRepository.findById(emdId)
                .orElseThrow(() -> new EmdAreaNotFoundException(EMD_AREA_NOT_FOUND));

        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        AreaAuthEntity areaAuth = areaAuthRepository.findByUser(user)
                .orElse(null);

        if (areaAuth == null) {
            areaAuthRepository.save(AreaAuthEntity.of(LocalDateTime.now(), user, emdArea));
            return;
        }

        areaAuth.updateEmdArea(emdArea);
        areaAuth.updateHistory();

        areaAuthRepository.save(areaAuth);
    }
}
