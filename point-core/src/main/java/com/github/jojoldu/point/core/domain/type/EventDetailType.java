package com.github.jojoldu.point.core.domain.type;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by jojoldu@gmail.com on 2018. 3. 20.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Getter
@AllArgsConstructor
public enum EventDetailType {
    SAVE_BUY("제품 구매"),
    SAVE_COUPON("쿠폰 등록"),
    SAVE_REVIEW("리뷰 등록"),
    SAVE_EVENT("이벤트 참여"),
    SAVE_REWARD("보상"),
    SAVE_CHARGE("충전"),
    SAVE_CANCEL("적립 취소"),

    USE_BUY("제품 구매"),
    USE_CANCEL("포인트 사용 취소"),

    DISAPPEAR_EXPIRATION("유효기간 만료"),
    DISAPPEAR_WITHDRAWAL("탈퇴");

    private String title;
}
