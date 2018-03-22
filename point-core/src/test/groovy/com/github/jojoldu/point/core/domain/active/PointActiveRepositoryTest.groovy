package com.github.jojoldu.point.core.domain.active

import com.github.jojoldu.point.core.domain.type.EventDetailType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

/**
 * Created by jojoldu@gmail.com on 2018. 3. 20.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@SpringBootTest
class PointActiveRepositoryTest extends Specification {

    @Autowired
    private PointActiveRepository pointActiveRepository

    void cleanup() {
        pointActiveRepository.deleteAllInBatch()
    }

    def "Audit가 Core만으로 가능한지 테스트"() {
        given:
        pointActiveRepository.save(PointActive.builder()
                .savePoint(100L)
                .customerId(1L)
                .eventDetailType(EventDetailType.SAVE_BUY)
                .build())
        when:
        PointActive savedPoint = pointActiveRepository.findAll().get(0)

        then:
        savedPoint.getCreatedDate() != null
        savedPoint.getModifiedDate() != null
    }

    def "PointActive에서 -금액 전환될 경우 Exception 발생 검증" () {
        given:
        PointActive pointActive = PointActive.builder()
                .savePoint(100L)
                .customerId(1L)
                .eventDetailType(EventDetailType.SAVE_BUY)
                .build()
        when:
        Long overflowPoint = pointActive.deductPoint(-200L)

        then:
        overflowPoint == -100L
    }

    def "PointActive가 Overflow발생시 다음 엔티티 금액에서 차감된다"() {
        given:
        PointActiveCollection collection = new PointActiveCollection(Arrays.asList(
                PointActive.builder().savePoint(100L).eventDetailType(EventDetailType.SAVE_BUY).customerId(1L).build(),
                PointActive.builder().savePoint(200L).eventDetailType(EventDetailType.SAVE_BUY).customerId(1L).build(),
                PointActive.builder().savePoint(300L).eventDetailType(EventDetailType.SAVE_BUY).customerId(1L).build()
        ))

        when:
        collection.deductPoint(-200L)

        then:
        collection.getPointActives().get(0).getRemainPoint() == 0L
        collection.getPointActives().get(1).getRemainPoint() == 100L
        collection.getPointActives().get(2).getRemainPoint() == 300L
    }
}
