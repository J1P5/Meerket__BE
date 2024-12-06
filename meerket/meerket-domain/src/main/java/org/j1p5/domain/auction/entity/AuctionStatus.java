package org.j1p5.domain.auction.entity;

public enum AuctionStatus {
    BIDDING, // 입찰 진행 중
    CANCELLED, // 입찰 취소
    AWARDED, // 낙찰 성공
    DEAL_FAILED // 경매 불발
}
