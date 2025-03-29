package com.digipay.rest;

import com.digipay.rest.dtos.UserDto;
import com.digipay.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserDto>> createUser(@RequestBody UserDto userDto) {
        UserDto createdUser = userService.createUser(userDto);
        ApiResponse<UserDto> response = new ApiResponse<>(true, "User created successfully", createdUser);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<ApiResponse<UserDto>> getUser(@PathVariable String userId) {
        UserDto user = userService.getUserById(userId);
        ApiResponse<UserDto> response = new ApiResponse<>(true, "User retrieved successfully", user);
        return ResponseEntity.ok(response);
    }
}