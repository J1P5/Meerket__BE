
package org.j1p5.api.user.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NameRegisterRequest(@NotNull @Size(max = 15) String name) {}
