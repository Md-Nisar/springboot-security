package com.mna.springbootsecurity.powerbi.base.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ReportData {
    private String id;
    private String name;
    private String embedUrl;
    private String webUrl;
    private String datasetId;
}
