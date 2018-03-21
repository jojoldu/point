package com.github.jojoldu.point.core.message;

import com.github.jojoldu.point.core.domain.event.EventType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.Nonnull;

/**
 * Created by jojoldu@gmail.com on 2018. 3. 21.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Setter
@Getter
@NoArgsConstructor
public class PointMessage {
    private EventType eventType;
    private Long savePoint;
    private String description;
    private Long customerId;

    @Builder
    public PointMessage(@Nonnull EventType eventType, @Nonnull Long savePoint, @Nonnull Long customerId, String description) {
        this.eventType = eventType;
        this.savePoint = savePoint;
        this.description = description;
        this.customerId = customerId;
    }
}
