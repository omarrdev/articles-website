package com.omarrdev.ithra.dto.request;

import com.omarrdev.ithra.enums.Role;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class UpdateRoleRequest {
    @NotNull
    private Role role;
}
