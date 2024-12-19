package org.j1p5.api.report.service;

import lombok.RequiredArgsConstructor;
import org.j1p5.domain.comment.entity.CommentEntity;
import org.j1p5.domain.report.entity.ReportEntity;
import org.j1p5.domain.report.repository.ReportRepository;
import org.j1p5.domain.user.entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    @Transactional
    public void withdraw(UserEntity user) {
        List<ReportEntity> reports = reportRepository.findByUser(user);

        if (reports.isEmpty()) {
            return;
        }

        reports.forEach(ReportEntity::withdraw);
    }
}
