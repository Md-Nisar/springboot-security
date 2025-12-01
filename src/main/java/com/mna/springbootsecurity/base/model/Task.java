package com.mna.springbootsecurity.base.model;

import lombok.*;
import org.apache.commons.math3.util.Pair;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.List;


@Getter
@Setter
@Builder
public class Task {
    private final String methodId;
    private final UserDetails userDetails;
    private final List<SubTask> subTasks = new ArrayList<>();
    private List<Pair<String, Object>> messages;
    private long totalDuration;
    private Object[] data;
    private boolean isSuccessful;

    public void addSubTask(String name, long taskStartTime) {
        subTasks.add(new SubTask(name, System.currentTimeMillis() - taskStartTime));
    }
}
