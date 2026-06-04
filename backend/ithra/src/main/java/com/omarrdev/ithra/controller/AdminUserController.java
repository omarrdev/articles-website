package com.omarrdev.ithra.controller;

import com.omarrdev.ithra.dto.request.UpdateRoleRequest;
import com.omarrdev.ithra.dto.response.PageResponse;
import com.omarrdev.ithra.dto.response.UserResponse;
import com.omarrdev.ithra.service.AdminUserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Tag(name = "Admin — Users", description = "Admin user management")
@SecurityRequirement(name = "bearerAuth")
public class AdminUserController {

    private final AdminUserService adminUserService;

    @GetMapping
    @Operation(summary = "List all users (paginated)")
    public PageResponse<UserResponse> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return adminUserService.getUsers(PageRequest.of(page, size));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get user by ID")
    public UserResponse getUser(@PathVariable Long id) {
        return adminUserService.getUser(id);
    }

    @PatchMapping("/{id}/role")
    @Operation(summary = "Update user role")
    public UserResponse updateRole(@PathVariable Long id, @Valid @RequestBody UpdateRoleRequest request) {
        return adminUserService.updateRole(id, request);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Soft delete user")
    public void deleteUser(@PathVariable Long id) {
        adminUserService.deleteUser(id);
    }
}
