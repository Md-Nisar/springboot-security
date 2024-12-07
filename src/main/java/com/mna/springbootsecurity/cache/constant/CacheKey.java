package com.mna.springbootsecurity.cache.constant;

import lombok.Getter;

/**
 * This class defines constants for cache keys used in the application.
 * These keys are used to uniquely identify cache entries for various purposes.
 */
@Getter
public final class CacheKey {

    // Application Key
    private static final String APP_PREFIX = "app:";

    // JWT Keys
    public static final String JWT_PREFIX = APP_PREFIX + "jwt:";
    public static final String JWT_BLACKLIST = JWT_PREFIX + "blacklist:";
    public static final String JWT_ACCESS_TOKEN = JWT_PREFIX + "access_token:";
    public static final String JWT_REFRESH_TOKEN = JWT_PREFIX + "refresh_token:";

    // Signup Keys
    public static final String SIGNUP_PREFIX = APP_PREFIX + "signup:";
    public static final String SIGNUP_REGISTRATION = SIGNUP_PREFIX + "registration:";
    public static final String SIGNUP_REGISTRATION_USER = SIGNUP_PREFIX + "registration:user:";

    // Verification Token Keys
    public static final String SIGNUP_VERIFICATION = SIGNUP_PREFIX + "verification:";
    public static final String SIGNUP_VERIFICATION_TOKEN = SIGNUP_VERIFICATION + "token:";

    private CacheKey() {
        throw new AssertionError("Cannot instantiate utility class.");
    }
}
