package org.j1p5.api.auth.dto;

public record LoginRequest(String code, String provider) {
}
