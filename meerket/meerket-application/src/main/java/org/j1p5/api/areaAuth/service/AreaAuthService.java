package org.j1p5.api.areaAuth.service;

import static org.j1p5.api.global.excpetion.WebErrorCode.EMD_AREA_NOT_FOUND;
import static org.j1p5.api.global.excpetion.WebErrorCode.USER_NOT_FOUND;

import jakarta.transaction.Transactional;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
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

@Service
@RequiredArgsConstructor
public class AreaAuthService {

    private final AreaAuthRepository areaAuthRepository;
    private final EmdAreaRepository emdAreaRepository;
    private final UserRepository userRepository;

    /**
     * 지역인증 하려는 동네가 유저가 현재 인증한 동네와 같은지 확인
     *
     * @author Icecoff22
     * @param userId 유저 Id
     * @param emdId 읍면동 Id
     * @return
     */
    @Transactional
    public boolean isMatchMyAuthHistory(Long userId, Integer emdId) {
        UserEntity user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        AreaAuthEntity areaAuth = areaAuthRepository.findByUser(user).orElse(null);

        if (areaAuth != null && emdId == areaAuth.getEmdArea().getId()) {
            areaAuth.updateHistory();
            return true;
        }

        return false;
    }

    /**
     * 요청으로 들어온 위경도 좌표가 인증받으려는 읍면동 지역 내에 있는지 확인
     *
     * @author Icecoff22
     * @param areaAuthInfo 위경도 값, 읍면동 Id
     * @return
     */
    public boolean checkMyCoordinate(AreaAuthInfo areaAuthInfo) {
        EmdArea emdArea =
                emdAreaRepository
                        .findById(areaAuthInfo.emdId())
                        .orElseThrow(() -> new EmdAreaNotFoundException(EMD_AREA_NOT_FOUND));
        Point coordinate =
                PointConverter.createPoint(areaAuthInfo.longitude(), areaAuthInfo.latitude());

        return areaAuthRepository.existsInRequestArea(coordinate, emdArea);
    }

    /**
     * 인증 이후 인증 테이블에 인증 내역 기록
     *
     * @author Icecoff22
     * @param userId 유저 Id
     * @param emdId 읍면동 Id
     */
    public void saveHistory(Long userId, Integer emdId) {
        EmdArea emdArea =
                emdAreaRepository
                        .findById(emdId)
                        .orElseThrow(() -> new EmdAreaNotFoundException(EMD_AREA_NOT_FOUND));

        UserEntity user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        AreaAuthEntity areaAuth = areaAuthRepository.findByUser(user).orElse(null);

        if (areaAuth == null) {
            areaAuthRepository.save(AreaAuthEntity.of(LocalDateTime.now(), user, emdArea));
            return;
        }

        areaAuth.updateEmdArea(emdArea);
        areaAuth.updateHistory();

        areaAuthRepository.save(areaAuth);
    }

    /**
     * 회원 탈퇴 시 삭제 컬럼 업데이트
     *
     * @author Icecoff22
     * @param user 유저 엔티티
     */
    public void withdraw(UserEntity user) {
        AreaAuthEntity areaAuth = areaAuthRepository.findByUser(user).orElse(null);

        if (areaAuth == null) {
            return;
        }

        areaAuth.withdraw();
    }
}
