package org.j1p5.api.areaAuth.usecase;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.areaAuth.exception.PointNotMatchException;
import org.j1p5.api.areaAuth.service.AreaAuthService;
import org.j1p5.domain.areaAuth.entity.AreaAuthEntity;
import org.j1p5.domain.areaAuth.vo.AreaAuthInfo;
import org.springframework.stereotype.Service;

import static org.j1p5.api.global.excpetion.WebErrorCode.CURRENT_POINT_NOT_MATCH;

@Service
@RequiredArgsConstructor
public class AreaAuthUsecase {

    private final AreaAuthService areaAuthService;

    public void execute(Long userId, AreaAuthInfo areaAuthInfo) {
        boolean isMatch = areaAuthService.isMatchMyAuthHistory(userId, areaAuthInfo.emdId());
        if (isMatch) {
            return;
        }

        boolean isValidCoordinate = areaAuthService.checkMyCoordinate(areaAuthInfo);
        if (isValidCoordinate) {
            areaAuthService.saveHistory(userId, areaAuthInfo.emdId());
            return;
        }

        throw new PointNotMatchException(CURRENT_POINT_NOT_MATCH);
    }
}
