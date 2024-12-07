package org.j1p5.api.auction.job;

import org.j1p5.api.auction.service.AuctionService;
import org.j1p5.api.chat.service.ChatRoomService;
import org.j1p5.api.fcm.FcmService;
import org.j1p5.api.product.service.ProductService;
import org.j1p5.domain.auction.entity.AuctionEntity;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AuctionClosingJob implements Job {

    @Autowired
    private ProductService productService;

    @Autowired
    private ChatRoomService chatRoomService;

    @Autowired
    private FcmService fcmService;

    @Autowired
    private AuctionService auctionService;

    @Override
    public void execute(JobExecutionContext context) throws JobExecutionException {
        JobDataMap dataMap = context.getMergedJobDataMap();
        Long productId = dataMap.getLong("productId");

        // 최고 입찰자 찾기
        AuctionEntity auctionEntity = auctionService.findByHighestBidder(productId);

        // 상품의 낙찰 가격 업데이트
        productService.updateWinningPrice(productId, auctionEntity.getPrice());

        // 채팅방 생성
        chatRoomService.createChatRoom(auctionEntity, productId);

        // FCM 알림 전송
//        fcmService.sendAuctionWonNotification(highestBid.getUserId(), productId);
//        fcmService.sendAuctionResultToSeller(highestBid.getProduct().getSellerId(), productId, highestBid.getAmount());
//        fcmService.notifyLosingBidders(productId, highestBid.getUserId());


    }
}
