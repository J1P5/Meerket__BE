package org.j1p5.api.auction.quartz;

import lombok.extern.slf4j.Slf4j;
import org.j1p5.api.auction.job.AuctionClosingJob;
import org.j1p5.domain.product.entity.ProductEntity;
import org.quartz.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.ZoneId;
import java.util.Date;

@Slf4j
@Service
public class QuartzService {

    @Autowired
    private Scheduler scheduler;

    public void scheduleAuctionClosingJob(ProductEntity product) {
        JobDetail jobDetail = JobBuilder.newJob(AuctionClosingJob.class)
                .withIdentity("auctionJob_" + product.getId(), "auctionGroup")
                .usingJobData("productId", product.getId())
                .build();

        Trigger trigger = TriggerBuilder.newTrigger()
                .withIdentity("auctionTrigger_" + product.getId(), "auctionGroup")
                .startAt(Date.from(product.getExpiredTime().atZone(ZoneId.systemDefault()).toInstant()))
                .build();

        try {
            scheduler.scheduleJob(jobDetail, trigger);
        } catch (SchedulerException e) {
            log.error("스케줄 등록 과정 중 에러 발생 ",e);
        }

    }

}
