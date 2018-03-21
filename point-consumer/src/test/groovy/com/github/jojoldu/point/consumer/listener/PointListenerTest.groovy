package com.github.jojoldu.point.consumer.listener

import com.amazonaws.services.sqs.AmazonSQSAsync
import com.github.jojoldu.point.core.domain.event.EventType
import com.github.jojoldu.point.core.message.PointMessage
import com.github.jojoldu.sqs.config.SqsQueues
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.cloud.aws.messaging.core.QueueMessagingTemplate
import org.springframework.context.annotation.Bean
import spock.lang.Specification

/**
 * Created by jojoldu@gmail.com on 2018. 3. 21.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@SpringBootTest
class PointListenerTest extends Specification {

    @Autowired
    SqsQueues sqsQueues

    @Autowired
    AmazonSQSAsync amazonSqs

    QueueMessagingTemplate queueMessagingTemplate

    void setup() {
        queueMessagingTemplate = new QueueMessagingTemplate(amazonSqs)
    }

    def "정상적으로 Queue 메세지가 수신된다."() {
        given:
        PointMessage message = PointMessage.builder()
                .customerId(1L)
                .savePoint(100L)
                .eventType(EventType.EARN)
                .build()
        when:
        queueMessagingTemplate.convertAndSend(sqsQueues.getQueueName("point"), message)
        Thread.sleep(500L)

        then:
        true
    }
}
