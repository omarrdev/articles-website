package com.omarrdev.ithra.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class TagRequest {
    @NotBlank @Size(max = 50)
    private String name;
}
