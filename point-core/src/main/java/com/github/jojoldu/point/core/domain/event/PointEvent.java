package com.github.jojoldu.point.core.domain.event;

import com.github.jojoldu.point.core.domain.BaseTimeEntity;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.Nonnull;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

/**
 * Created by jojoldu@gmail.com on 2018. 3. 20.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Getter
@NoArgsConstructor
@Entity
public class PointEvent extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    private EventType eventType;
    private Long savePoint;
    private String description;

    private Long customerId;

    @Builder
    public PointEvent(@Nonnull EventType eventType, @Nonnull Long savePoint, @Nonnull Long customerId, String description) {
        this.eventType = eventType;
        this.savePoint = savePoint;
        this.description = description;
        this.customerId = customerId;
    }
}
