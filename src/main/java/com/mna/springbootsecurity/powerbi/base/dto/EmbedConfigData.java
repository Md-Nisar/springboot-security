package com.mna.springbootsecurity.powerbi.base.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmbedConfigData {
    private String type; // Report or Dashboard
    private String id;
    private String datasetId;
    private String name;
    private String embedUrl;
    private EmbedTokenData embedToken;
}