package com.github.jojoldu.point.consumer.service;

import com.github.jojoldu.point.core.domain.active.PointActiveRepository;
import com.github.jojoldu.point.core.domain.history.PointHistoryRepository;
import com.github.jojoldu.point.core.message.PointMessage;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Created by jojoldu@gmail.com on 2018. 3. 21.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

@Slf4j
@AllArgsConstructor
@Service
public class PointConsumerService {

    private PointActiveRepository pointActiveRepository;
    private PointHistoryRepository pointHistoryRepository;


    /**
     * 적립
     * 1. point_history에서 uuid+customerId로 이미 등록된게 있는지 확인
     * 2. 없을 경우
     *  - point_history rds insert
     *  - point_history dynamodb insert
     *  - point_active insert
     *  - ack
     * 2-1. 있을 경우 => pass
     *
     * 적립취소
     * 1. point_history에서 trade_no + event_type.SAVE 로 적립된 내역이 있는지 확인
     * 2. 있을 경우
     *  - point_history rds insert
     *  - point_history dynamodb insert
     *  - point_active update
     *  - ack
     * 2-1. 없을 경우 => pass
     */

    @Transactional
    public void savePoint(PointMessage message){

    }
    /**
     * 사용
     */

    /**
     * 소멸
     */
}
