package com.mna.springbootsecurity.powerbi.service;

import com.mna.springbootsecurity.powerbi.base.dto.*;

import java.util.List;

public interface PowerBIService {

    EmbedConfigData getEmbedConfig(String groupId, String reportId);

    List<GroupData> getAllGroups();

    List<ReportData> getAllReports(String groupId);

    ReportData getReportDetails(String groupId, String reportId);

    List<DashboardData> getAllDashboards(String groupId);

    DashboardData getDashboardDetails(String groupId, String dashboardId);

    EmbedTokenData getEmbedTokenForReport(String groupId, String reportId);

    EmbedTokenData getEmbedTokenForDashboard(String groupId, String dashboardId);

    EmbedConfigData getEmbedConfigForReport(String groupId, String reportId);

    EmbedConfigData getEmbedConfigForDashboard(String groupId, String dashboardId);

}
