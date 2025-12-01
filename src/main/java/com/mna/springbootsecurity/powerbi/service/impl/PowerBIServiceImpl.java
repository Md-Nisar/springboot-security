package com.mna.springbootsecurity.powerbi.service.impl;

import com.mna.springbootsecurity.powerbi.base.dto.*;
import com.mna.springbootsecurity.powerbi.base.response.DashboardListResponse;
import com.mna.springbootsecurity.powerbi.base.response.GroupListResponse;
import com.mna.springbootsecurity.powerbi.base.response.ReportListResponse;
import com.mna.springbootsecurity.powerbi.service.PowerBIAuthService;
import com.mna.springbootsecurity.powerbi.service.PowerBIService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.mna.springbootsecurity.powerbi.base.constants.PowerBIURL.POWER_BI_API_URL;

@Slf4j
@Service
@RequiredArgsConstructor
public class PowerBIServiceImpl implements PowerBIService {

    private final PowerBIAuthService authService;
    private final RestTemplate restTemplate = new RestTemplate();


    /**
     * Get authentication headers with the bearer token.
     */
    private HttpHeaders getAuthHeaders() {
        String accessToken = authService.getAccessToken();
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);
        return headers;
    }

    @Override
    public EmbedConfigData getEmbedConfig(String groupId, String reportId) {
        log.info("Fetching embed config for Report: {} in Group: {}", reportId, groupId);
        ReportData reportDetails = getReportDetails(groupId, reportId);
        EmbedTokenData embedToken = getEmbedToken(groupId, reportId);

        return EmbedConfigData.builder()
                .id(reportId)
                .name(reportDetails.getName())
                .embedUrl(reportDetails.getEmbedUrl())
                .embedToken(embedToken)
                .build();
    }

    public ReportData getReportDetails(String groupId, String reportId) {
        log.info("Fetching report details for Report: {} in Group: {}", reportId, groupId);
        String url = POWER_BI_API_URL + "/groups/" + groupId + "/reports/" + reportId;

        HttpEntity<String> entity = new HttpEntity<>(getAuthHeaders());

        ResponseEntity<ReportData> response = restTemplate.exchange(url, HttpMethod.GET, entity, ReportData.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            log.error("Failed to fetch report details: {}", response.getStatusCode());
            throw new RuntimeException("Failed to get report details");
        }
    }

    public EmbedTokenData getEmbedToken(String groupId, String reportId) {
        log.info("Fetching embed token for Report: {} in Group: {}", reportId, groupId);
        String url = POWER_BI_API_URL + "/groups/" + groupId + "/reports/" + reportId + "/GenerateToken";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("accessLevel", "View");
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, getAuthHeaders());

        ResponseEntity<EmbedTokenData> response = restTemplate.exchange(url, HttpMethod.POST, entity, EmbedTokenData.class);

        if (response.getStatusCode() == HttpStatus.OK) {
            return response.getBody();
        } else {
            log.error("Failed to fetch embed token: {}", response.getStatusCode());
            throw new RuntimeException("Failed to get embed token");
        }
    }


    @Override
    public List<GroupData> getAllGroups() {
        String url = POWER_BI_API_URL + "/groups";
        HttpEntity<Void> entity = new HttpEntity<>(getAuthHeaders());

        ResponseEntity<GroupListResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, GroupListResponse.class);
        return response.getBody() != null ? response.getBody().getValue() : Collections.emptyList();
    }

    public List<ReportData> getAllReports(String groupId) {
        String url = String.format(Locale.ROOT,"%s/groups/%s/reports", POWER_BI_API_URL, groupId);
        HttpEntity<Void> entity = new HttpEntity<>(getAuthHeaders());

        ResponseEntity<ReportListResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, ReportListResponse.class);
        return response.getBody() != null ? response.getBody().getValue() : Collections.emptyList();
    }

    @Override
    public List<DashboardData> getAllDashboards(String groupId) {
        String url = String.format(Locale.ROOT,"%s/groups/%s/dashboards", POWER_BI_API_URL, groupId);
        HttpEntity<Void> entity = new HttpEntity<>(getAuthHeaders());

        ResponseEntity<DashboardListResponse> response = restTemplate.exchange(url, HttpMethod.GET, entity, DashboardListResponse.class);
        return response.getBody() != null ? response.getBody().getValue() : Collections.emptyList();
    }


    @Override
    public DashboardData getDashboardDetails(String groupId, String dashboardId) {
        String url = String.format(Locale.ROOT,"%s/groups/%s/dashboards/%s", POWER_BI_API_URL, groupId, dashboardId);
        HttpEntity<Void> entity = new HttpEntity<>(getAuthHeaders());

        ResponseEntity<DashboardData> response = restTemplate.exchange(url, HttpMethod.GET, entity, DashboardData.class);
        return response.getBody();
    }


    @Override
    public EmbedTokenData getEmbedTokenForReport(String groupId, String reportId) {
        String url = String.format(Locale.ROOT,"%s/groups/%s/reports/%s/GenerateToken", POWER_BI_API_URL, groupId, reportId);

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("accessLevel", "View");
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, getAuthHeaders());

        ResponseEntity<EmbedTokenData> response = restTemplate.exchange(url, HttpMethod.POST, entity, EmbedTokenData.class);
        return response.getBody();
    }

    @Override
    public EmbedTokenData getEmbedTokenForDashboard(String groupId, String dashboardId) {
        String url = String.format(Locale.ROOT,"%s/groups/%s/dashboards/%s/GenerateToken", POWER_BI_API_URL, groupId, dashboardId);
        HttpEntity<Void> entity = new HttpEntity<>(getAuthHeaders());

        ResponseEntity<EmbedTokenData> response = restTemplate.exchange(url, HttpMethod.POST, entity, EmbedTokenData.class);
        return response.getBody();
    }

    @Override
    public EmbedConfigData getEmbedConfigForReport(String groupId, String reportId) {
        ReportData reportData = getReportDetails(groupId, reportId);
        EmbedTokenData embedTokenData = getEmbedTokenForReport(groupId, reportId);
        return EmbedConfigData.builder()
                .type("report")
                .id(reportData.getId())
                .embedUrl(reportData.getEmbedUrl())
                .embedToken(embedTokenData)
                .build();
    }

    @Override
    public EmbedConfigData getEmbedConfigForDashboard(String groupId, String dashboardId) {
        DashboardData dashboardData = getDashboardDetails(groupId, dashboardId);
        EmbedTokenData embedTokenData = getEmbedTokenForDashboard(groupId, dashboardId);
        return EmbedConfigData.builder()
                .type("report")
                .id(dashboardData.getId())
                .embedUrl(dashboardData.getEmbedUrl())
                .embedToken(embedTokenData)
                .build();
    }


}
