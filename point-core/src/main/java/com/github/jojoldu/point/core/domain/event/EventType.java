package com.github.jojoldu.point.core.domain.event;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * Created by jojoldu@gmail.com on 2018. 3. 20.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Getter
@AllArgsConstructor
public enum EventType {
    EARN("적립"),
    USE("사용"),
    DISAPPEAR("소멸");

    private String title;
}
