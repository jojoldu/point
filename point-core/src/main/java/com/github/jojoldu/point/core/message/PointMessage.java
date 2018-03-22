package com.github.jojoldu.point.core.message;

import com.github.jojoldu.point.core.domain.active.PointActive;
import com.github.jojoldu.point.core.domain.history.PointHistory;
import com.github.jojoldu.point.core.domain.type.EventDetailType;
import com.github.jojoldu.point.core.domain.type.EventType;
import com.github.jojoldu.point.core.exception.GoToDeadLetterQueueException;
import com.github.jojoldu.point.core.util.KeyGenerator;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import javax.annotation.Nonnull;

/**
 * Created by jojoldu@gmail.com on 2018. 3. 21.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Slf4j
@Setter
@Getter
@NoArgsConstructor
public class PointMessage {
    private String messageId;
    private String tradeNo;
    private EventType eventType;
    private EventDetailType eventDetailType;
    private Long savePoint;
    private String description;
    private Long customerId;

    @Builder
    private PointMessage(String messageId, String tradeNo, EventType eventType, EventDetailType eventDetailType, Long savePoint, String description, Long customerId) {
        this.messageId = messageId;
        this.tradeNo = tradeNo;
        this.eventType = eventType;
        this.eventDetailType = eventDetailType;
        this.savePoint = savePoint;
        this.description = description;
        this.customerId = customerId;
    }

    public PointMessage create(@Nonnull String tradeNo, @Nonnull EventType eventType, @Nonnull EventDetailType eventDetailType, @Nonnull Long point, String description, @Nonnull Long customerId){
        try{
            return PointMessage.builder()
                    .messageId(KeyGenerator.generateUuid())
                    .tradeNo(tradeNo)
                    .eventType(eventType)
                    .eventDetailType(eventDetailType)
                    .savePoint(point)
                    .description(description)
                    .customerId(customerId)
                    .build();
        }catch (Exception e){
            log.error("PointMessage Create Exception", e);
            throw new GoToDeadLetterQueueException();
        }
    }

    public PointHistory toHistory(){
        return PointHistory.builder()
                .messageId(messageId)
                .tradeNo(tradeNo)
                .eventType(eventType)
                .eventDetailType(eventDetailType)
                .savePoint(savePoint)
                .description(description)
                .customerId(customerId)
                .build();
    }

    public PointActive toActive(){
        return PointActive.builder()
                .customerId(customerId)
                .savePoint(savePoint)
                .eventDetailType(eventDetailType)
                .description(description)
                .build();
    }
}
