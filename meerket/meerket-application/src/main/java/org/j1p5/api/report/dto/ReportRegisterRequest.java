package org.j1p5.api.report.dto;

import org.j1p5.domain.report.ReportInfo;

public record ReportRegisterRequest(
        String title, String content, String reportType, Long targetId) {
    public static ReportInfo toInfo(ReportRegisterRequest request) {
        return new ReportInfo(
                request.title(), request.content(), request.reportType(), request.targetId());
    }
}
