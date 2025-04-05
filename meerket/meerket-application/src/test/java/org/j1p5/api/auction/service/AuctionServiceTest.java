package org.j1p5.api.auction.service;

import org.j1p5.api.auction.dto.response.BidHistoryResponse;
import org.j1p5.api.auction.exception.AuctionException;
import org.j1p5.api.auction.fixture.AuctionFixture;
import org.j1p5.api.auction.fixture.ProductFixture;
import org.j1p5.api.auction.fixture.UserFixture;
import org.j1p5.api.global.excpetion.WebException;
import org.j1p5.api.product.service.EmdNameReader;
import org.j1p5.domain.auction.entity.AuctionEntity;
import org.j1p5.domain.auction.entity.AuctionStatus;
import org.j1p5.domain.auction.repository.AuctionRepository;
import org.j1p5.domain.product.entity.ProductEntity;
import org.j1p5.domain.product.repository.ProductRepository;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.swing.text.html.Option;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuctionServiceTest {

    @Mock
    private AuctionRepository auctionRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuctionService auctionService;


    @Test
    @DisplayName("입찰 사용자가 본인이 맞는 경우")
    void verifyUserBidOwnership_success() {
        // given
        Long userId = 1L;
        Long auctionId = 100L;
        UserEntity user = UserFixture.withId(userId);
        AuctionEntity auction = AuctionFixture.withUserId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));

        // when & then
        assertThatCode(() -> auctionService.verifyUserBidOwnership(userId, auctionId))
                .doesNotThrowAnyException();
    }


    @Test
    @DisplayName("입찰 사용자가 본인이 아닌 경우")
    void verifyUserBidOwnership_fail() {
        // given
        Long requestUserId = 1L;
        Long realUserId = 2L;
        Long auctionId = 100L;
        UserEntity user = UserFixture.withId(requestUserId);
        AuctionEntity auction = AuctionFixture.withUserId(realUserId);

        when(userRepository.findById(requestUserId)).thenReturn(Optional.of(user));
        when(auctionRepository.findById(auctionId)).thenReturn(Optional.of(auction));

        // when & then
        assertThatThrownBy(() -> auctionService.verifyUserBidOwnership(requestUserId, auctionId))
                .isInstanceOf(WebException.class)
                .satisfies(ex -> {
                    WebException we = (WebException) ex;
                    assertThat(we.getBaseErrorCode()).isEqualTo(AuctionException.BID_USER_NOT_AUTHORIZED);
                });
    }


    @Test
    @DisplayName("조기 마감 상태인지 확인하는데 조기마감 맞을때")
    void isEarlyClosure_true() {
        // given
        Long productId = 1L;
        ProductEntity product = mock(ProductEntity.class);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(product.isEarly()).thenReturn(true);

        // when & then
        assertThat(auctionService.isEarlyClosure(productId)).isTrue();
    }


    @Test
    @DisplayName("조기 마감 상태인지 확인하는데 조기마감이 아닐때")
    void isEarlyClosure_false() {
        // given
        Long productId = 1L;
        ProductEntity product = mock(ProductEntity.class);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(product.isEarly()).thenReturn(false);

        // when & then
        assertThat(auctionService.isEarlyClosure(productId)).isFalse();
    }

    @Test
    @DisplayName("정상적으로 BidHistoryResponse 생성")
    void getAuctionHistoryResponses_success() {
        // given
        ProductEntity product = ProductFixture.withId(1L);
        AuctionEntity auction = AuctionFixture.withProductAndAuctionId(product, 1L);
        List<AuctionEntity> auctions = List.of(auction);
        MockedStatic<EmdNameReader> emdNameReaderMockedStatic = mockStatic(EmdNameReader.class);

        emdNameReaderMockedStatic.when(() -> EmdNameReader.getEmdName(any())).thenReturn("test동");
        when(productRepository.findById(any())).thenReturn(Optional.of(product));

        // when
        List<BidHistoryResponse> responses = auctionService.getAuctionHistoryResponses(auctions);

        // then
        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).auctionId()).isEqualTo(auction.getId());

        emdNameReaderMockedStatic.close();
    }


    @Test
    @DisplayName("최고 입찰자 정상 반환")
    void findByHighestBidder(){
        // given
        Long productId = 1L;
        AuctionEntity auction = AuctionFixture.withPrice(10000);

        when(auctionRepository.findHighestBidder(productId)).thenReturn(Optional.of(auction));
        // when
        AuctionEntity result = auctionService.findByHighestBidder(productId);

        // then
        assertThat(result.getPrice()).isEqualTo(auction.getPrice());
    }


    @Test
    @DisplayName("거래 완료시 Auction의 status 변경")
    void updateAuctionStatusToAwarded() {
        // given
        Long productId = 1L;
        AuctionEntity auction = AuctionFixture.withPrice(1000);
        when(auctionRepository.findHighestBidder(productId)).thenReturn(Optional.of(auction));

        // when
        auctionService.updateAuctionStatusToAwarded(productId);

        // then
        assertThat(auction.getStatus()).isEqualTo(AuctionStatus.AWARDED);
    }


    @Test
    @DisplayName("입찰 내역이 존재할 경우 모두 withdraw() 호출")
    void withdraw_withAuctions() {
        // given
        UserEntity user = UserFixture.withId(1L);
        AuctionEntity auction1 = mock(AuctionEntity.class);
        AuctionEntity auction2 = mock(AuctionEntity.class);
        when(auctionRepository.findByUser(user)).thenReturn(List.of(auction1, auction2));

        // when
        auctionService.withdraw(user);

        // then
        verify(auction1, times(1)).withdraw();
        verify(auction2, times(1)).withdraw();

    }










}