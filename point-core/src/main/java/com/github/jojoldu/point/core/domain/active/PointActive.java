package com.github.jojoldu.point.core.domain.active;

import com.github.jojoldu.point.core.domain.BaseTimeEntity;
import com.github.jojoldu.point.core.domain.type.EventDetailType;
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
 * 이벤트 발생시 실제 계산처리될 도메인
 * 1) 정산처리
 * 2) 포인트 내역
 * 3) 기타 모든 내부에서 포인트 관련된 처리를 담당
 */

@Getter
@NoArgsConstructor
@Entity
@Table(
        indexes = {
                @Index(name = "IDX_ACTIVE_CUSTOMER_ID", columnList = "customerId")
        }
)
public class PointActive extends BaseTimeEntity {
    @Id
    @GeneratedValue
    private Long id;

    private EventDetailType eventDetailType;
    private Long savePoint;
    private Long remainPoint;
    private String description;

    private Long customerId;

    @Builder
    public PointActive(@Nonnull EventDetailType eventDetailType, @Nonnull Long savePoint, @Nonnull Long customerId, String description) {
        this.eventDetailType = eventDetailType;
        this.savePoint = savePoint;
        this.remainPoint = savePoint;
        this.description = description;
        this.customerId = customerId;
    }

    public Long deductPoint(Long point){
        this.remainPoint += point;
        if(this.remainPoint < 0){
            Long overflowPoint = this.remainPoint;
            this.remainPoint = 0L;
            return overflowPoint;
        }
        return 0L;
    }
}
