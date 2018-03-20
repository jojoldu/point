# SQS를 이용한 비동기 포인트 시스템

[![Build Status](https://travis-ci.org/jojoldu/point.svg?branch=master)](https://travis-ci.org/jojoldu/point) [![Coverage Status](https://coveralls.io/repos/github/jojoldu/point/badge.svg?branch=master)](https://coveralls.io/github/jojoldu/point?branch=master)

## Queue 시스템 소개



## 프로젝트 상세

### 기능

* 포인트 적립
  * **유효기간을 가진 포인트 적립**
  * 적립 유형 기록
      * 리뷰 남겼을시
      * 제품 구매시
      * 이벤트 참여시
      * 포인트 선물 수령
      * 기타 등등
* 포인트 소멸
  * 소멸 유형 기록
      * 유효기간이 지난 포인트는 소멸
      * 휴면처리된 계정은 포인트 소멸
      * 기타 등등
  
* 포인트 사용
  * **먼저 적립된 포인트부터 사용**됨
      * ex) 1000p/2018-02-01, 2000p/2018-01-31 이 있으면 2018-01-31 먼저 다 사용되고 2018-02-01 사용됨 
  * 사용 유형 기록
      * 제품 구매
      * 포인트 선물
      * 기타 등등

* 포인트 선물
  * 포인트를 타 계정으로 선물
  * 포인트 적립 / 사용이 함께 발생
  
* 롤백
  * 환불, 리뷰 삭제, 이벤트 참여 취소 등으로 인해 포인트 처리 롤백
  * 환불시에는 포인트 적립이 롤백
  * 제품 구매시 포인트를 사용했다면 환불시에 적립된 포인트도 롤백
        
### 도메인

* 사용자의 최종 포인트와 포인트 이벤트 이력만으로는 문제해결이 안됨
  * 이번에 사용할 포인트는 언제 적립된 포인트이며, 얼마가 남았으며, 부족하기 때문에 다음 적립포인트를 사용해야하는지 등등 확인이 어려움
  * 이번에 소멸할 포인트는 언제 적립된 포인트이며, 얼마가 남았는지 확인이 어려움 
* 적립/소멸/사용 테이블 분리하면 문제 발생
  * 이력조회시 10개 혹은 20개 단위 페이징 구현이 복잡해짐
  * 최대한 심플한 구조로 가야 유지보수가 하기 쉬움
  
그래서 아래와 같이 2개 도메인으로 설계

* point_save
  * 포인트 적립
  * 포인트 적립: insert
  * 포인트 사용/소멸: update
  * save_point, remain_point 컬럼 2개로 나누어 포인트 사용시 remain_point에서 차감
  * remain_point가 0이 될 경우 다음 최신 적립 포인트에서 차감
  * 유효기간 만료시 remain_point를 0으로 update
  * 조회의 경우 **remain_point가 0이 아닌 경우만** 조회
  * 컬럼
      * customer_id
      * save_type (제품구매, 제품리뷰, 이벤트참여, 보상, 충전)
      * save_point
      * remain_point 
      * created_date 
      * updated_date
  * RDS에서만 관리
  
* point_event
  * 포인트의 모든 이벤트
  * 포인트 적립/사용/소멸: insert
  * 컬럼
      * customer_id
      * event_type (적립/사용/소멸)
      * point
      * description
      * created_date
      * updated_date
  * RDS, DynamoDB 양쪽에서 모두 관리

### 모듈

포인트 관련 모든 이벤트는 **publisher를 통해서 처리**하는 것을 전제로 한다.  
(Batch를 통한 소멸/관리자페이지에서 수동 적립 등등 모두 publisher를 통해서 진행)

* point-core
* point-provider
  * SQS provide
* point-consumer
  * SQS consume
  * RDS와 DynamoDB에 저장
* point-batch
  * 포인트 소멸 관련 처리
      * 유효기간 만료된 포인트 소멸처리
  * 기존 데이터 마이그레이션
* point-external-api
  * 외부 제공용
  * DynamoDB의 내용을 조회
* point-internal-api
* point-admin
* point-test-web