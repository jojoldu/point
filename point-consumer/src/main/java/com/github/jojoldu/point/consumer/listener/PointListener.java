package com.github.jojoldu.point.consumer.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jojoldu.point.core.message.PointMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.aws.messaging.listener.Acknowledgment;
import org.springframework.cloud.aws.messaging.listener.SqsMessageDeletionPolicy;
import org.springframework.cloud.aws.messaging.listener.annotation.SqsListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * Created by jojoldu@gmail.com on 2018. 3. 21.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Slf4j
@AllArgsConstructor
@Component
public class PointListener {

    private ObjectMapper objectMapper;

    @SqsListener(value = "${sqs.queues.point.name}", deletionPolicy = SqsMessageDeletionPolicy.NEVER)
    public void receive(String message, @Header("SenderId") String senderId, Acknowledgment ack) throws IOException {
        log.info("senderId: {}, message: {}", senderId, message);
        PointMessage messageObject = objectMapper.readValue(message, PointMessage.class);
        try{
            ack.acknowledge().get();
        } catch (Exception e){
            log.error("Point Save Fail: "+ message, e);
        }
    }
}
