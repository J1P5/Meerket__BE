package org.j1p5.api.report.service;

import lombok.RequiredArgsConstructor;
import org.j1p5.domain.report.ReportInfo;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportRegisterService {

    public void register(Long userId, ReportInfo reportInfo, List<File> images) {

    }
}
