package com.github.jojoldu.point.core.domain.event;

import org.springframework.data.jpa.repository.JpaRepository;

/**
 * Created by jojoldu@gmail.com on 2018. 3. 20.
 * Blog : http://jojoldu.tistory.com
 * Github : https://github.com/jojoldu
 */

public interface PointEventRepository extends JpaRepository<PointEvent, Long>{
}
