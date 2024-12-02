package org.j1p5.api.auth.dto;

import jakarta.validation.constraints.NotNull;

public record LoginRequest(
        @NotNull String code,
        @NotNull String provider) {
}