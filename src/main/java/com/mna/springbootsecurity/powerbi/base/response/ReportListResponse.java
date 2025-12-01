package com.mna.springbootsecurity.powerbi.base.response;

import com.mna.springbootsecurity.powerbi.base.dto.ReportData;
import lombok.Data;

import java.util.List;

@Data
public class ReportListResponse {
    private List<ReportData> value;
}
