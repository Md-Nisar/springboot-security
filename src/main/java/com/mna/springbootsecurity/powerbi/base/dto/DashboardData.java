package com.mna.springbootsecurity.powerbi.base.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DashboardData {
    private String id;
    private String displayName;
    private String embedUrl;
    private String webUrl;
}
