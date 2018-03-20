package com.github.jojoldu.point.core.domain.save;

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

    public PointSaveCollection(List<PointSave> pointSaves) {
        this.pointSaves = pointSaves;
    }

    public void deductPoint(Long point){
        Long overflowPoint = point;
        for (PointSave pointSave : pointSaves) {
            overflowPoint = pointSave.deductPoint(overflowPoint);
            if(overflowPoint == 0L){
                break;
            }
        }
    }
}
