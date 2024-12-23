package org.j1p5.api.report.service;

import static org.j1p5.api.comment.exception.CommentErrorCode.COMMENT_NOT_FOUND;
import static org.j1p5.api.global.excpetion.WebErrorCode.USER_NOT_FOUND;
import static org.j1p5.api.product.exception.ProductException.PRODUCT_NOT_FOUND;

import java.io.File;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.j1p5.api.comment.exception.CommentNotFoundException;
import org.j1p5.api.product.exception.ProductNotFoundException;
import org.j1p5.api.user.exception.UserNotFoundException;
import org.j1p5.domain.comment.repository.CommentRepository;
import org.j1p5.domain.product.repository.ProductRepository;
import org.j1p5.domain.report.ReportImageClient;
import org.j1p5.domain.report.ReportInfo;
import org.j1p5.domain.report.entity.ReportEntity;
import org.j1p5.domain.report.entity.ReportType;
import org.j1p5.domain.report.repository.ReportRepository;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReportRegisterService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final ProductRepository productRepository;
    private final ReportImageClient reportImageClient;
    private final ReportRepository reportRepository;

    /**
     * 신고 등록 함수. reportInfo 속 target type에 달라 신고 카테고리가 달라짐.
     *
     * @author icecoff22
     * @param userId
     * @param reportInfo
     * @param images
     */
    public void register(Long userId, ReportInfo reportInfo, List<File> images) {
        UserEntity user =
                userRepository
                        .findById(userId)
                        .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        ReportType reportType = ReportType.get(reportInfo.reportType());
        // targetId 검증
        validateTargetId(reportType, reportInfo.targetId());

        // TODO : 이미지 테이블에 저장 기능 구현할 것.
        if (!images.isEmpty()) {
            reportImageClient.upload(images);
        }
        reportRepository.save(
                ReportEntity.create(reportType, reportInfo.targetId(), reportInfo.content(), user));
    }

    /**
     * report type에 따라 신고 대상이 있는지 확인
     *
     * @author icecoff22
     * @param reportType
     * @param targetId
     */
    private void validateTargetId(ReportType reportType, Long targetId) {
        // TODO : 추상화시키고 if-else문을 없앨지 논의 필요.
        if (reportType == ReportType.COMMENT) {
            commentRepository
                    .findById(targetId)
                    .orElseThrow(() -> new CommentNotFoundException(COMMENT_NOT_FOUND));
        } else if (reportType == ReportType.POST) {
            productRepository
                    .findById(targetId)
                    .orElseThrow(() -> new ProductNotFoundException(PRODUCT_NOT_FOUND));
        } else { // user
            userRepository
                    .findById(targetId)
                    .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));
        }
    }
}
