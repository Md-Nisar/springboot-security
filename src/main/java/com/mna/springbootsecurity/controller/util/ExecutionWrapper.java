package com.mna.springbootsecurity.controller.util;

import com.mna.springbootsecurity.security.model.CustomUserDetails;
import com.mna.springbootsecurity.security.util.SecurityContextUtil;
import com.mna.springbootsecurity.util.ExecutionContextHolder;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ExecutionWrapper {

    private static void setupExecutionContext() {
        CustomUserDetails userDetails = SecurityContextUtil.getUser();
        ExecutionContextHolder.put(ExecutionContextHolder.USER, userDetails);
        ExecutionContextHolder.put(ExecutionContextHolder.ROLE, userDetails);
        ExecutionContextHolder.put(ExecutionContextHolder.ADDITIONAL_DATA, userDetails.getData());
        ExecutionContextHolder.put(ExecutionContextHolder.ADDITIONAL_DATA, new HashMap<String, Object>());
    }

    public static <T> T executeWithContext(Logger log, Supplier<T> task, Object... extraData) {
        setupExecutionContext();
        return execute(log, task, extraData);
    }

    public static void executeWithContext(Logger log, Runnable task, Object... extraData) {
        setupExecutionContext();
        execute(log, task, extraData);
    }

    public static <T> T execute(Logger log, Supplier<T> task, Object... extraData) {
        preExecution();
        boolean success = false;
        try {
            T result = task.get();
            success = true;
            return result;
        } finally {
            postExecution(log, success, extraData);
        }
    }

    public static void execute(Logger log, Runnable task, Object... extraData) {
        preExecution();
        boolean success = false;
        try {
            task.run();
            success = true;
        } finally {
            postExecution(log, success, extraData);
        }
    }

    private static void preExecution() {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        String URI = request.getMethod() + ":" + request.getRequestURI();

        if (StringUtils.isNotBlank(request.getQueryString())) {
            URI += "?" + request.getQueryString();
        }

        TaskLogger taskLogger = new TaskLogger(URI, (UserDetails) ExecutionContextHolder.get(ExecutionContextHolder.USER));

        ExecutionContextHolder.put(ExecutionContextHolder.TASK_LOGGER, taskLogger);
    }

    private static void postExecution(Logger log, boolean success, Object... extraData) {
        Map<String, Object> messages = (Map<String, Object>) ExecutionContextHolder.get(ExecutionContextHolder.MESSAGES);
        TaskLogger logger = (TaskLogger) ExecutionContextHolder.get(ExecutionContextHolder.TASK_LOGGER);
        if (logger != null) {
            logger.logExecutionTime(log, success, messages, extraData);
        }
        ExecutionContextHolder.clear();
    }
}
