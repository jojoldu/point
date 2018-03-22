package com.github.jojoldu.point.core.domain.history

import com.github.jojoldu.point.core.domain.type.EventDetailType
import com.github.jojoldu.point.core.domain.type.EventType
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import spock.lang.Specification

/**
 * Created by jojoldu@gmail.com on 2018. 3. 22.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@SpringBootTest
class PointHistoryRepositoryTest extends Specification {

    @Autowired
    PointHistoryRepository pointHistoryRepository

    void cleanup() {
        pointHistoryRepository.deleteAllInBatch()
    }

    def "exist메소드는 boolean을 정상 반환한다"() {
        given:
        def messageId = "messageId"
        def customerId = 1L
        pointHistoryRepository.save(PointHistory.builder()
                .messageId(messageId)
                .tradeNo("tradeNo1")
                .eventType(EventType.SAVE)
                .eventDetailType(EventDetailType.SAVE_BUY)
                .savePoint(100L)
                .customerId(customerId)
                .build())
        when:
        boolean result = pointHistoryRepository.existsByMessageIdAndAndCustomerId(messageId, customerId)

        then:
        result
    }

}
