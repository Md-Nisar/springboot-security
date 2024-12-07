package com.mna.springbootsecurity.controller.core;

import com.mna.springbootsecurity.base.vo.UserData;
import com.mna.springbootsecurity.base.vo.response.ControllerResponse;
import com.mna.springbootsecurity.service.core.UserService;
import com.mna.springbootsecurity.util.ResponseUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ControllerResponse<UserData>> getUserDetails(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseUtil.success(userService.getUserByUsername(userDetails.getUsername()));
    }

    @PutMapping("")
    public ResponseEntity<ControllerResponse<UserData>> updateUser(@AuthenticationPrincipal UserDetails userDetails, @RequestBody UserData userData) {
        return ResponseUtil.success(userService.updateUser(userDetails.getUsername(), userData));
    }

    @DeleteMapping("")
    public ResponseEntity<ControllerResponse<Boolean>> deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
        return ResponseUtil.success(userService.deleteUserByUsername(userDetails.getUsername()));
    }
}
