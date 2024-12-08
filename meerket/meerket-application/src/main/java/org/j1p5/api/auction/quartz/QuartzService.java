package org.j1p5.api.auction.quartz;

import lombok.extern.slf4j.Slf4j;
import org.j1p5.api.auction.job.AuctionClosingJob;
import org.j1p5.domain.product.entity.ProductEntity;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Service
public class QuartzService {

    @Autowired
    private Scheduler scheduler;


    // 물품 등록시 job 등록
    public void scheduleAuctionClosingJob(ProductEntity product) {
        log.info("product잘들어옴?{}", product);

        JobDetail jobDetail = JobBuilder.newJob(AuctionClosingJob.class)
                .withIdentity("auctionJob_" + product.getId(), "auctionGroup")
                .usingJobData("productId", product.getId())
                .build();


        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("auctionTrigger_" + product.getId(), "auctionGroup")
                .startAt(Date.from(product.getExpiredTime().atZone(ZoneId.systemDefault()).toInstant()))
                .build();

        log.info("job + trigger{}{}", jobDetail, trigger);

        try {
            scheduler.scheduleJob(jobDetail, trigger);
            log.info("작업 완료");
        } catch (SchedulerException e) {
            log.error("스케줄 등록 과정 중 에러 발생 ",e);
        }
    }


    // 물품 마감 시간이 변경되었을 때 job 수정
    public void rescheduleAuctionClosing(Long productId, LocalDateTime newExpiredTime) {
        TriggerKey triggerKey = new TriggerKey("auctionTrigger_" + productId, "auctionGroup");

        Trigger newTrigger = TriggerBuilder.newTrigger()
                .withIdentity("auctionTrigger_" + productId, "auctionGroup")
                .startAt(Date.from(newExpiredTime.atZone(ZoneId.systemDefault()).toInstant()))
                .build();

        try {
            scheduler.rescheduleJob(triggerKey, newTrigger);
            log.info("경매 마감 시간을 재설정. productId = {}",productId);
        } catch (SchedulerException e) {
            log.error("스케줄 재설정 과정 중 에러 발생",e);
        }
    }


    // 물품 제거시 job 삭제
    public void cancelAuctionJob(Long productId) {
        JobKey jobKey = new JobKey("auctionJob_" + productId, "auctionGroup");
        try {
            boolean result = scheduler.deleteJob(jobKey);
            if (result) {
                log.info("경매 job 삭제 성공. productId={}", productId);
            } else {
                log.warn("경매 job을 찾지 못함. productId={}", productId);
            }
        } catch (SchedulerException e) {
            log.error("job 삭제 중 에러 발생", e);
        }
    }


}
