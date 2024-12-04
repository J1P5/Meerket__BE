package org.j1p5.api.activityArea.usecase;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.activityArea.service.ActivityAreaService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ActivityAreaRegisterUsecase {

    private final ActivityAreaService activityAreaService;

    public void execute(Long userId, Integer emdId) {
        activityAreaService.register(userId, emdId);
    }
}
