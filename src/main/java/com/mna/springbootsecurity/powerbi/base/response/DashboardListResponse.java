package com.mna.springbootsecurity.powerbi.base.response;

import com.mna.springbootsecurity.powerbi.base.dto.DashboardData;
import lombok.Data;

import java.util.List;

@Data
public class DashboardListResponse {
    private List<DashboardData> value;
}

