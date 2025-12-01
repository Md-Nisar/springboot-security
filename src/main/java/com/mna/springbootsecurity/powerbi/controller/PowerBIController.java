package com.mna.springbootsecurity.powerbi.controller;

import com.mna.springbootsecurity.powerbi.base.dto.*;
import com.mna.springbootsecurity.powerbi.service.PowerBIAuthService;
import com.mna.springbootsecurity.powerbi.service.PowerBIService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/v1/powerbi")
public class PowerBIController {

    private final PowerBIService powerBIService;
    private final PowerBIAuthService powerBIAuthService;

    public PowerBIController(@Qualifier("powerBIAuthServiceMSALImpl") PowerBIAuthService powerBIAuthService,
                             PowerBIService powerBIService) {
        this.powerBIAuthService = powerBIAuthService;
        this.powerBIService = powerBIService;
    }

    @GetMapping("/accessToken")
    public ResponseEntity<String> getAccessToken() {
        String accessToken = powerBIAuthService.getAccessToken();
        return ResponseEntity.ok(accessToken);
    }

    @GetMapping("/embedToken/{groupId}/{reportId}")
    public ResponseEntity<EmbedConfigData> getReportEmbedConfig(@PathVariable String groupId, @PathVariable String reportId) {
        EmbedConfigData response = powerBIService.getEmbedConfig(groupId, reportId);
        return ResponseEntity.ok(response);
    }

    /**
     * Get all Power BI workspaces (groups).
     */
    @GetMapping("/groups")
    public ResponseEntity<List<GroupData>> getAllGroups() {
        List<GroupData> groups = powerBIService.getAllGroups();
        return ResponseEntity.ok(groups);
    }

    /**
     * Get all reports in a workspace.
     */
    @GetMapping("groups/{groupId}/reports")
    public ResponseEntity<List<ReportData>> getAllReports(@PathVariable String groupId) {
        List<ReportData> reports = powerBIService.getAllReports(groupId);
        return ResponseEntity.ok(reports);
    }

    /**
     * Get report details.
     */
    @GetMapping("groups/{groupId}/reports/{reportId}")
    public ResponseEntity<ReportData> getReportDetails(@PathVariable String groupId, @PathVariable String reportId) {
        ReportData report = powerBIService.getReportDetails(groupId, reportId);
        return ResponseEntity.ok(report);
    }

    /**
     * Get all dashboards in a workspace.
     */
    @GetMapping("groups/{groupId}/dashboards")
    public ResponseEntity<List<DashboardData>> getAllDashboards(@PathVariable String groupId) {
        List<DashboardData> dashboards = powerBIService.getAllDashboards(groupId);
        return ResponseEntity.ok(dashboards);
    }

    /**
     * Get dashboard details.
     */
    @GetMapping("groups/{groupId}/dashboards/{dashboardId}")
    public ResponseEntity<DashboardData> getDashboardDetails(@PathVariable String groupId, @PathVariable String dashboardId) {
        DashboardData dashboard = powerBIService.getDashboardDetails(groupId, dashboardId);
        return ResponseEntity.ok(dashboard);
    }


    /**
     * Get an embed token for a report.
     */
    @GetMapping("/embed/token/reports/{groupId}/{reportId}")
    public ResponseEntity<EmbedTokenData> getReportEmbedToken(@PathVariable String groupId, @PathVariable String reportId) {
        EmbedTokenData token = powerBIService.getEmbedTokenForReport(groupId, reportId);
        return ResponseEntity.ok(token);
    }

    /**
     * Get an embed token for a dashboard.
     */
    @GetMapping("/embed/token/dashboards/{groupId}/{dashboardId}")
    public ResponseEntity<EmbedTokenData> getDashboardEmbedToken(@PathVariable String groupId, @PathVariable String dashboardId) {
        EmbedTokenData token = powerBIService.getEmbedTokenForDashboard(groupId, dashboardId);
        return ResponseEntity.ok(token);
    }

}
