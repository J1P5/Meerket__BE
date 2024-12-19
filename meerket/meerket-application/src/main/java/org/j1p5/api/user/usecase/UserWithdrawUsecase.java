package org.j1p5.api.user.usecase;

import lombok.RequiredArgsConstructor;
import org.j1p5.api.activityArea.service.ActivityAreaService;
import org.j1p5.api.areaAuth.service.AreaAuthService;
import org.j1p5.api.auction.service.AuctionService;
import org.j1p5.api.block.service.BlockDeleteService;
import org.j1p5.api.comment.service.CommentService;
import org.j1p5.api.product.service.ProductService;
import org.j1p5.api.user.exception.UserNotFoundException;
import org.j1p5.api.user.service.UserWithdrawService;
import org.j1p5.domain.user.entity.UserEntity;
import org.j1p5.domain.user.repository.UserRepository;
import org.springframework.stereotype.Service;

import static org.j1p5.api.global.excpetion.WebErrorCode.USER_NOT_FOUND;

@Service
@RequiredArgsConstructor
public class UserWithdrawUsecase {

    private final UserRepository userRepository;
    private final ActivityAreaService activityAreaService;
    private final AreaAuthService areaAuthService;
    private final CommentService commentService;
    private final AuctionService auctionService;
    private final ProductService productService;
    private final BlockDeleteService blockDeleteService;
    private final UserWithdrawService userWithdrawService;

    public void execute(Long userId) {
        UserEntity user = userRepository.findById(userId)
                        .orElseThrow(() -> new UserNotFoundException(USER_NOT_FOUND));

        activityAreaService.withdraw(user);
        areaAuthService.withdraw(user);
        commentService.withdraw(user);
        auctionService.withdraw(user);
        productService.withdraw(user);
        blockDeleteService.withdraw(user);
        //TODO : report delete 처리
        userWithdrawService.withdraw(user);

    }
}
