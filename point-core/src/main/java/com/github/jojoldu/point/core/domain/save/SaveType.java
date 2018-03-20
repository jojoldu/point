package com.github.jojoldu.point.core.domain.save;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by jojoldu@gmail.com on 2018. 3. 20.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Getter
@AllArgsConstructor
public enum SaveType {
    BUY("제품 구매"),
    REVIEW("제품 리뷰"),
    EVENT("이벤트 참여"),
    REWARD("보상"),
    CHARGING("충전");

    private String title;
}
