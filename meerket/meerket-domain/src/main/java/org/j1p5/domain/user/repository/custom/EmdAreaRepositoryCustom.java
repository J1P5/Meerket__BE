package org.j1p5.domain.areaAuth.repository.custom;

import org.j1p5.domain.user.entity.EmdArea;

public interface AreaAuthRepositoryCustom {
    boolean existsInRequestArea(Double longitude, Double latitude, EmdArea emdArea);
}
