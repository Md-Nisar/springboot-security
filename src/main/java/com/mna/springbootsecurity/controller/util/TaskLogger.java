package com.mna.springbootsecurity.controller.util;

import com.mna.springbootsecurity.base.model.Task;
import com.nimbusds.oauth2.sdk.util.MapUtils;
import net.logstash.logback.marker.Markers;
import org.apache.commons.math3.util.Pair;
import org.slf4j.Logger;
import org.slf4j.Marker;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class TaskLogger {

    private final long executionStartTime;
    private final Task task;

    public TaskLogger(String methodId, UserDetails userDetails) {
        this.task = Task.builder()
                .methodId(methodId)
                .userDetails(userDetails)
                .build();
        this.executionStartTime = System.currentTimeMillis();
    }

    public void logExecutionTime(Logger log, boolean isSuccessful, Map<String, Object> messages, Object... extraData) {
        task.setTotalDuration(System.currentTimeMillis() - executionStartTime);
        task.setSuccessful(isSuccessful);

        if (MapUtils.isNotEmpty(messages)) {
            List<Pair<String, Object>> pairs = messages.entrySet().stream()
                    .map(m -> new Pair<>(m.getKey(), m.getValue()))
                    .collect(Collectors.toList());
            task.setMessages(pairs);
        }

        task.setData(extraData);
        Marker marker = Markers.append("TASK", task);
        log.info(marker, task.toString());
    }

    public void addSubTaskDuration(String subTask, long taskStartTime) {
        task.addSubTask(subTask, taskStartTime);
    }




}
