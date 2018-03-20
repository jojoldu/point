package com.github.jojoldu.point.core.domain

import com.github.jojoldu.point.core.domain.save.PointSave
import com.github.jojoldu.point.core.domain.save.PointSaveCollection
import com.github.jojoldu.point.core.domain.save.PointSaveRepository
import com.github.jojoldu.point.core.domain.save.SaveType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

/**
 * Created by jojoldu@gmail.com on 2018. 3. 20.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@SpringBootTest
class PointSaveRepositoryTest extends Specification {

    @Autowired
    private PointSaveRepository pointSaveRepository

    void cleanup() {
        pointSaveRepository.deleteAllInBatch()
    }

    def "Audit가 Core만으로 가능한지 테스트"() {
        given:
        pointSaveRepository.save(PointSave.builder()
                .savePoint(100L)
                .customerId(1L)
                .saveType(SaveType.BUY)
                .build())
        when:
        PointSave savedPoint = pointSaveRepository.findAll().get(0)

        then:
        savedPoint.getCreatedDate() != null
        savedPoint.getModifiedDate() != null
    }

    def "PointSave에서 -금액 전환될 경우 Exception 발생 검증" () {
        given:
        PointSave pointSave = PointSave.builder()
                .savePoint(100L)
                .customerId(1L)
                .saveType(SaveType.BUY)
                .build()
        when:
        Long overflowPoint = pointSave.deductPoint(-200L)

        then:
        overflowPoint == -100L
    }

    def "PointSave가 Overflow발생시 다음 엔티티 금액에서 차감된다"() {
        given:
        PointSaveCollection collection = new PointSaveCollection(Arrays.asList(
                PointSave.builder().savePoint(100L).saveType(SaveType.BUY).customerId(1L).build(),
                PointSave.builder().savePoint(200L).saveType(SaveType.BUY).customerId(1L).build(),
                PointSave.builder().savePoint(300L).saveType(SaveType.BUY).customerId(1L).build()
        ))

        when:
        collection.deductPoint(-200L)

        then:
        collection.getPointSaves().get(0).getRemainPoint() == 0L
        collection.getPointSaves().get(1).getRemainPoint() == 100L
        collection.getPointSaves().get(2).getRemainPoint() == 300L
    }
}
