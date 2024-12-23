package org.j1p5.api.auth.dto;

public record SessionInfo(Long pk, String role) {
    public static SessionInfo of(Long pk, String role) {
        return new SessionInfo(pk, role);
    }
}
