package com.github.jojoldu.point.core.domain.save;

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
public class PointSave extends BaseTimeEntity {
    @Id
    @GeneratedValue
    private Long id;

    private SaveType saveType;
    private Long savePoint;
    private Long remainPoint;
    private String description;

    private Long customerId;

    @Builder
    public PointSave(@Nonnull SaveType saveType, @Nonnull Long savePoint, @Nonnull Long customerId, String description) {
        this.saveType = saveType;
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
