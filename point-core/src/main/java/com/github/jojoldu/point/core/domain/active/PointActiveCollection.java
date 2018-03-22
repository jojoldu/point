package com.github.jojoldu.point.core.domain.active;

import com.github.jojoldu.point.core.domain.exception.PointOverflowException;
import lombok.Getter;

import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2018. 3. 20.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Getter
public class PointActiveCollection {
    private List<PointActive> pointActives;
    private Long totalRemainPoint;

    public PointActiveCollection(List<PointActive> pointActives) {
        this.pointActives = pointActives;
        this.totalRemainPoint = pointActives.stream()
                .mapToLong(PointActive::getRemainPoint)
                .sum();
    }

    public void deductPoint(Long point){
        verifyOverflowPoint(point);
        Long overflowPoint = point;
        for (PointActive pointSave : pointActives) {
            overflowPoint = pointSave.deductPoint(overflowPoint);
            if(overflowPoint == 0L){
                break;
            }
        }
    }

    private void verifyOverflowPoint(Long point){
        if(point + totalRemainPoint < 0){
            throw new PointOverflowException();
        }
    }
}
