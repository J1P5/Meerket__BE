package org.j1p5.api.product.service;

import org.j1p5.domain.user.entity.UserEntity;

public class EmdNameReader {
    public static String getEmdName(UserEntity user){
        String emdName = user.getActivityAreas().get(0).getEmdArea().getEmdName();

        return emdName;
    }
}
