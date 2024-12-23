package org.j1p5.api.report.service;

import java.util.List;
import lombok.RequiredArgsConstructor;
import org.j1p5.domain.report.entity.ReportEntity;
import org.j1p5.domain.report.repository.ReportRepository;
import org.j1p5.domain.user.entity.UserEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ReportService {

    private final ReportRepository reportRepository;

    // 유저 회원 탈퇴 시 신고 관련 삭제 설정
    @Transactional
    public void withdraw(UserEntity user) {
        List<ReportEntity> reports = reportRepository.findByUser(user);

        if (reports.isEmpty()) {
            return;
        }

        reports.forEach(ReportEntity::withdraw);
    }
}
