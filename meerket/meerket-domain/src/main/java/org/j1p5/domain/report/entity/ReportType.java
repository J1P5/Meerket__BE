package org.j1p5.domain.report.entity;

import org.j1p5.domain.auth.exception.InvalidProviderException;

import java.util.Arrays;

import static org.j1p5.domain.global.exception.DomainErrorCode.INVALID_REPORT_TYPE;

public enum ReportType {
    USER,
    POST,
    COMMENT;

    public static ReportType get(String reportType) {
        boolean isValid =
                Arrays.stream(ReportType.values())
                        .map(ReportType::name)
                        .anyMatch(name -> name.equals(reportType));

        if (!isValid) {
            throw new InvalidProviderException(INVALID_REPORT_TYPE);
        }

        return ReportType.valueOf(reportType);
    }
}
