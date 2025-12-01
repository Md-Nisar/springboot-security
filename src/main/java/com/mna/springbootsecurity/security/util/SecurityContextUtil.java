package com.mna.springbootsecurity.security.util;

import com.mna.springbootsecurity.security.model.CustomUserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Map;

public class SecurityContextUtil {

    public static SecurityContext getSecurityContext() {
        return SecurityContextHolder.getContext();
    }

    public static Authentication getAuthentication() {
        return getSecurityContext().getAuthentication();
    }

    public static Object getPrincipal() {
        return getAuthentication().getPrincipal();
    }

    public static CustomUserDetails getUser() {
        return (CustomUserDetails) getPrincipal();
    }

    public static String getUsername() {
        CustomUserDetails userDetails = getUser();
        return userDetails.getUsername();
    }

    public static boolean isSecurityContextAvailable() {
        return SecurityContextHolder.getContext() != null
                && SecurityContextHolder.getContext().getAuthentication() != null
                && SecurityContextHolder.getContext().getAuthentication().getPrincipal() != null;
    }

    public static void removeAuthentication() {
        SecurityContextHolder.getContext().setAuthentication(null);
    }
}

