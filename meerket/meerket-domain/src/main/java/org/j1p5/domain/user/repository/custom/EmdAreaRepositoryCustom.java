package org.j1p5.domain.user.repository.custom;

import org.j1p5.domain.user.entity.EmdArea;
import org.locationtech.jts.geom.Point;

public interface EmdAreaRepositoryCustom {
    boolean existsInRequestArea(Point coordinate, EmdArea emdArea);
}
