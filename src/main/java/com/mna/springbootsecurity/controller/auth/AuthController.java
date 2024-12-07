package com.mna.springbootsecurity.controller.auth;

import com.mna.springbootsecurity.base.constant.HTTPHeaders;
import com.mna.springbootsecurity.base.property.JwtProperties;
import com.mna.springbootsecurity.util.ResponseUtil;
import com.mna.springbootsecurity.base.vo.request.LoginRequest;
import com.mna.springbootsecurity.base.vo.request.RefreshTokenRequest;
import com.mna.springbootsecurity.base.vo.request.SignupRequest;
import com.mna.springbootsecurity.base.vo.response.AuthResponse;
import com.mna.springbootsecurity.base.vo.response.ControllerResponse;
import com.mna.springbootsecurity.base.vo.response.JwtResponse;
import com.mna.springbootsecurity.base.vo.response.SignupResponse;
import com.mna.springbootsecurity.service.auth.AuthService;
import com.mna.springbootsecurity.service.core.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
@Validated
@Slf4j
public class AuthController {

    private final AuthService authService;
    private final UserService userService;
    private final JwtProperties jwtProperties;

    @PostMapping("/signup")
    public ResponseEntity<ControllerResponse<SignupResponse>> signup(@Valid @RequestBody SignupRequest signupRequest) {
        return ResponseUtil.success(authService.signup(signupRequest), "Please verify your account to complete Signup");
    }

    @GetMapping("/verify")
    public ResponseEntity<ControllerResponse<Void>> verifyUser(@RequestParam String username, @RequestParam String token) {
        if (authService.verifyUser(username, token)) {
            return ResponseUtil.success("User verified successfully");
        }
        return ResponseUtil.error(HttpStatus.BAD_REQUEST, List.of("Invalid or expired verification token"));
    }

    @PostMapping("/login")
    public ResponseEntity<ControllerResponse<JwtResponse>> login(@Valid @RequestBody LoginRequest loginRequest) {
        return ResponseUtil.success(authService.login(loginRequest), "Login successful");
    }

    @PostMapping("/logout")
    public ResponseEntity<ControllerResponse<Void>> logout(HttpServletRequest request) {
        String accessTokenHeader = request.getHeader(HTTPHeaders.AUTHORIZATION);
        String refreshTokenHeader = request.getHeader(HTTPHeaders.X_REFRESH_TOKEN);

        if (accessTokenHeader == null || !accessTokenHeader.startsWith(jwtProperties.getTokenPrefix())) {
            return ResponseUtil.error(HttpStatus.BAD_REQUEST, List.of("Invalid Authorization header"));
        }
        if (refreshTokenHeader == null || !refreshTokenHeader.startsWith(jwtProperties.getTokenPrefix())) {
            return ResponseUtil.error(HttpStatus.BAD_REQUEST, List.of("Invalid Refresh token header"));
        }

        String accessToken = accessTokenHeader.replace(jwtProperties.getTokenPrefix(), "");
        String refreshToken = refreshTokenHeader.replace(jwtProperties.getTokenPrefix(), "");

        authService.logout(accessToken, refreshToken);

        return ResponseUtil.success("Logout successful");
    }

    @PostMapping("/refresh")
    public ResponseEntity<ControllerResponse<AuthResponse>> refreshAccessToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {
        return authService.refreshAccessToken(refreshTokenRequest)
                .map(authResponse -> ResponseUtil.success(authResponse, "Access token refreshed successfully"))
                .orElseGet(() -> ResponseUtil.error(null, HttpStatus.UNAUTHORIZED, List.of("Invalid refresh token")));
    }

    @PostMapping("/resetPassword")
    public ResponseEntity<ControllerResponse<Void>> resetPassword(@RequestParam("username") String username) {
        if (userService.existsByUsername(username)) {
            return ResponseUtil.error(HttpStatus.BAD_REQUEST, List.of("Invalid username"));
        }

        if (authService.resetPassword(username)) {
            return ResponseUtil.success("Please check your mail inbox to reset password");
        }

        return ResponseUtil.error(HttpStatus.INTERNAL_SERVER_ERROR, List.of("Unable to reset password. Please try again later"));
    }

}

