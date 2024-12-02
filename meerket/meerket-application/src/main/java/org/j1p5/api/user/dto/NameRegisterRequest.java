
package org.j1p5.api.user.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;

public record NameRegisterRequest(@NotNull @Max(15) String name) {}
