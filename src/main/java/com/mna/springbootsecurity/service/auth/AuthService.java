package com.mna.springbootsecurity.service.auth;

import com.mna.springbootsecurity.base.vo.request.LoginRequest;
import com.mna.springbootsecurity.base.vo.request.RefreshTokenRequest;
import com.mna.springbootsecurity.base.vo.request.SignupRequest;
import com.mna.springbootsecurity.base.vo.response.AuthResponse;
import com.mna.springbootsecurity.base.vo.response.JwtResponse;
import com.mna.springbootsecurity.base.vo.response.SignupResponse;

import java.util.Optional;

public interface AuthService {

    SignupResponse signup(SignupRequest signupRequest);

    boolean verifyUser(String username, String verificationToken);

    JwtResponse login(LoginRequest loginRequest);

    void logout(String accessToken, String refreshToken);

    Optional<AuthResponse> refreshAccessToken(RefreshTokenRequest refreshTokenRequest);

    boolean resetPassword(String username);
}
