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
public enum EventType {
    SAVE("적립"),
    USE("사용"),
    DISAPPEAR("소멸");

    private String title;
}
