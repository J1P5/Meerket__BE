package org.j1p5.api.activityArea.dto;

import jakarta.validation.constraints.NotNull;

public record ActivityAreaSettingRequest(@NotNull(message = "읍면동 id 값은 필수입니다.") Integer emdId) {}
