package com.github.jojoldu.point.core.domain.save;

import com.github.jojoldu.point.core.domain.exception.PointOverflowException;
import lombok.Getter;

import java.util.List;

/**
 * Created by jojoldu@gmail.com on 2018. 3. 20.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Getter
public class PointSaveCollection {
    private List<PointSave> pointSaves;
    private Long totalRemainPoint;

    public PointSaveCollection(List<PointSave> pointSaves) {
        this.pointSaves = pointSaves;
        this.totalRemainPoint = pointSaves.stream()
                .mapToLong(PointSave::getRemainPoint)
                .sum();
    }

    public void deductPoint(Long point){
        verifyOverflowPoint(point);
        Long overflowPoint = point;
        for (PointSave pointSave : pointSaves) {
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
