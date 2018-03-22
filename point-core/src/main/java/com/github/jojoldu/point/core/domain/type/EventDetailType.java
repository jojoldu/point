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
    SAVE_REVIEW("제품 리뷰"),
    SAVE_EVENT("이벤트 참여"),
    SAVE_REWARD("보상"),
    SAVE_CHARGING("충전"),

    DISAPPEAR_EXPIRATION("유효기간 만료"),
    DISAPPEAR_WITHDRAWAL("탈퇴");

    private String title;
}
