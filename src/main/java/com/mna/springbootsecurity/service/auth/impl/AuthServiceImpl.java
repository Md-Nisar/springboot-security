package com.mna.springbootsecurity.service.auth.impl;

import com.mna.springbootsecurity.base.enums.Roles;
import com.mna.springbootsecurity.base.vo.message.EmailNotificationData;
import com.mna.springbootsecurity.base.vo.request.LoginRequest;
import com.mna.springbootsecurity.base.vo.request.RefreshTokenRequest;
import com.mna.springbootsecurity.base.vo.request.SignupRequest;
import com.mna.springbootsecurity.base.vo.response.AuthResponse;
import com.mna.springbootsecurity.base.vo.response.JwtResponse;
import com.mna.springbootsecurity.base.vo.response.SignupResponse;
import com.mna.springbootsecurity.cache.service.JwtTokenCacheService;
import com.mna.springbootsecurity.cache.service.UserCacheService;
import com.mna.springbootsecurity.cache.service.VerificationTokenCacheService;
import com.mna.springbootsecurity.domain.dao.RoleDao;
import com.mna.springbootsecurity.domain.dao.UserDao;
import com.mna.springbootsecurity.domain.entity.Role;
import com.mna.springbootsecurity.domain.entity.User;
import com.mna.springbootsecurity.mail.enums.EmailType;
import com.mna.springbootsecurity.pubsub.message.MessagePublisher;
import com.mna.springbootsecurity.security.util.JwtUtil;
import com.mna.springbootsecurity.service.auth.AuthService;
import com.mna.springbootsecurity.util.RandomStringUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collections;
import java.util.Date;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements AuthService {

    private final UserDao userDao;
    private final RoleDao roleDao;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final JwtTokenCacheService jwtTokenCacheService;
    private final UserCacheService userCacheService;
    private final VerificationTokenCacheService verificationTokenCacheService;
    private final MessagePublisher emailNotificationPublisher;

    @Override
    @Transactional
    public SignupResponse signup(SignupRequest signupRequest) {
        log.info("Processing signup request for username: {}", signupRequest.getUsername());

        if (Boolean.TRUE.equals(userDao.existsByUsername(signupRequest.getUsername()))) {
            log.warn("Username '{}' is already taken", signupRequest.getUsername());
            throw new IllegalArgumentException("Username is already taken");
        }

        User newUser = new User();
        newUser.setUsername(signupRequest.getUsername());
        newUser.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        newUser.setEnabled(false); // User is not enabled until verified
        newUser.setEmail(signupRequest.getEmail());


        userCacheService.storeUser(newUser.getUsername(), newUser);
        log.info("User '{}' registered successfully", newUser.getUsername());

        // Prepare verification token, add to the cache and send verification mail to the user
        String verificationToken = RandomStringUtil.generateRandomString();
        verificationTokenCacheService.storeToken(newUser.getUsername(), verificationToken);

        EmailNotificationData emailNotification = EmailNotificationData.builder()
                .emailAddress(newUser.getEmail())
                .type(EmailType.USER_VERIFICATION)
                .data(verificationToken)
                .build();
        emailNotificationPublisher.publish(emailNotification);

        return SignupResponse.builder()
                .username(newUser.getUsername())
                .email(newUser.getEmail())
                .message("User registered successfully")
                .success(true)
                .build();
    }

    @Override
    @Transactional
    public boolean verifyUser(String username, String verificationToken) {
        log.info("Processing User verification request for username: {}", username);
        boolean isVerified = verificationTokenCacheService.verifyToken(username, verificationToken);

        if (!isVerified) {
            log.error("User verification token {} is Invalid or expired for username: {}", verificationToken, username);
            return false;
        }

        User user = userCacheService.retrieveUser(username);

        if (user != null) {
            Role userRole = roleDao.findByName(Roles.USER.getValue())
                    .orElseThrow(() -> new RuntimeException("Role 'ROLE_USER' not found. Check database setup."));

            user.setRoles(Collections.singleton(userRole));
            user.setEnabled(true);
            User savedUser = userDao.save(user);
            log.info("User: {} saved into the database successfully", username);


            if (savedUser.getId() != null) {
                verificationTokenCacheService.deleteToken(username);
                userCacheService.deleteUser(username);
            }
            log.info("User with id:'{}', username: '{}' verified successfully", savedUser.getId(), savedUser.getUsername());
        } else {
            log.error("User not found with username : {}", username);
            return false;
        }
        return true;
    }

    @Override
    public JwtResponse login(LoginRequest loginRequest) {
        log.info("Processing login request for username: {}", loginRequest.getUsername());

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                loginRequest.getUsername(),
                loginRequest.getPassword()
        );

        Authentication authentication = authenticationManager.authenticate(authToken);
        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        String accessToken = jwtUtil.generateAccessToken(userDetails.getUsername());
        String refreshToken = jwtUtil.generateRefreshToken(userDetails.getUsername());

        Date expirationDate = jwtUtil.extractExpiration(accessToken);
        LocalDateTime expirationTime = LocalDateTime.ofInstant(expirationDate.toInstant(), ZoneId.systemDefault());

        log.info("User '{}' logged in successfully", userDetails.getUsername());
        return JwtResponse.builder()
                .username(userDetails.getUsername())
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .expiration(expirationTime)
                .authorities(userDetails.getAuthorities())
                .build();
    }

    @Override
    public void logout(String accessToken, String refreshToken) {
        try {

        } catch (Exception e) {

        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        log.info("Processing logout request for username: {}", username);

        jwtTokenCacheService.blacklistToken(username, accessToken);
        log.info("Access Token '{}' blacklisted successfully on logout for user '{}'", accessToken, username);

        jwtTokenCacheService.blacklistToken(username, refreshToken);
        log.info("Refresh Token '{}' blacklisted successfully on logout for user '{}'", refreshToken, username);

        SecurityContextHolder.clearContext();
        log.info("User '{}' logged out successfully", username);
    }

    public Optional<AuthResponse> refreshAccessToken(RefreshTokenRequest refreshTokenRequest) {
        String refreshToken = refreshTokenRequest.getRefreshToken();
        String username = jwtUtil.extractSubject(refreshToken);

        if (jwtUtil.validateToken(refreshToken, username)) {
            final String accessToken = jwtUtil.generateAccessToken(username);
            log.info("Access token refreshed successfully for username: {}", username);
            return Optional.of(AuthResponse.builder()
                    .accessToken(accessToken)
                    .refreshToken(refreshToken)
                    .build());
        }
        return Optional.empty();
    }

    @Override
    public boolean resetPassword(String username) {
         try {
             // Prepare reset password token, add to the cache and send reset password mail to the user
             String verificationToken = RandomStringUtil.generateRandomString();
             verificationTokenCacheService.storeToken(username, verificationToken);

             EmailNotificationData emailNotification = EmailNotificationData.builder()
                     .emailAddress(username)
                     .type(EmailType.RESET_PASSWORD)
                     .data(verificationToken)
                     .build();
             emailNotificationPublisher.publish(emailNotification);
             return true;
         } catch (Exception e) {
             log.error("Reset Password", e);
             return false;
         }
    }

}
