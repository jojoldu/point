package com.github.jojoldu.point.core.domain.history;

import com.github.jojoldu.point.core.domain.BaseTimeEntity;
import com.github.jojoldu.point.core.domain.type.EventDetailType;
import com.github.jojoldu.point.core.domain.type.EventType;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.annotation.Nonnull;
import javax.persistence.*;

/**
 * Created by jojoldu@gmail.com on 2018. 3. 20.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

/**
 * 이벤트 발생시 모두 insert만 하는 도메인
 * 1) 정산처리
 * 2) 포인트 내역
 * 3) 기타 모든 내부에서 포인트 관련된 처리를 담당
 */
@Getter
@NoArgsConstructor
@Entity
@Table(
        uniqueConstraints = @UniqueConstraint(name = "UNI_EVENT_UUID", columnNames = {"messageId", "customerId"}),
        indexes = {
                @Index(name = "IDX_EVENT_TRADE_NO", columnList = "tradeNo"),
                @Index(name = "IDX_EVENT_CUSTOMER_ID", columnList = "customerId")
        }
)
public class PointHistory extends BaseTimeEntity {

    @Id
    @GeneratedValue
    private Long id;

    @Column(nullable = false)
    private String messageId; // queueMessage uuid

    @Column(nullable = false)
    private String tradeNo;  // 거래번호

    @Column(nullable = false)
    private EventType eventType;

    @Column(nullable = false)
    private EventDetailType eventDetailType;

    @Column(nullable = false)
    private Long savePoint;
    private String description;

    private Long customerId; // FK 이지만, 회원관리를 여기서하지 않기 때문에 조회용 Index만 설

    @Builder
    public PointHistory(@Nonnull String messageId, @Nonnull String tradeNo, @Nonnull EventType eventType, @Nonnull EventDetailType eventDetailType, @Nonnull Long savePoint, String description, @Nonnull Long customerId) {
        this.messageId = messageId;
        this.tradeNo = tradeNo;
        this.eventType = eventType;
        this.eventDetailType = eventDetailType;
        this.savePoint = savePoint;
        this.description = description;
        this.customerId = customerId;
    }
}
