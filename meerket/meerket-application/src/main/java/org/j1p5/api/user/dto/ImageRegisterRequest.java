package org.j1p5.api.user.dto;

import org.springframework.web.multipart.MultipartFile;

public record ImageRegisterRequest(MultipartFile file) {
}
