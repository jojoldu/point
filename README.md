# SQS를 이용한 비동기 포인트 시스템

[![Build Status](https://travis-ci.org/jojoldu/point.svg?branch=master)](https://travis-ci.org/jojoldu/point) [![Coverage Status](https://coveralls.io/repos/github/jojoldu/point/badge.svg?branch=master)](https://coveralls.io/github/jojoldu/point?branch=master)

## Queue 시스템 소개


### Skill Stack

* AWS SQS
* AWS RDS
* AWS DynamoDB
  * 데이터 내구성을 위해 Elastic Cache보다는 DynamoDB 선택
  * [DynamoDB vs Elastic Cache](https://www.quora.com/How-does-one-choose-DynamoDB-versus-Redis-ElastiCache-on-AWS)

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

### SQS

* consumer가 동일 메세지를 2번 수신하는것 방지하기 위해 provider에선 uuid를 제공
  * consumer는 uuid가 이미 들어가있는지 확인후 save과정 진행
          
### 도메인

* 사용자의 **최종 포인트와 포인트 이벤트 이력만으로는 문제해결이 안됨**
  * 이번에 **사용**할 포인트는 언제 적립된 포인트이며, 얼마가 남았으며, 부족하기 때문에 다음 적립포인트를 사용해야하는지 등등 확인이 어려움
  * 이번에 **소멸**할 포인트는 언제 적립된 포인트이며, 얼마가 남았는지 확인이 어려움 
* 적립/소멸/사용 테이블 분리하면 문제 발생
  * 이력조회시 10개 혹은 20개 단위 페이징 구현이 복잡해짐
  * 최대한 심플한 구조로 가야 유지보수가 하기 쉬움
  
그래서 아래와 같이 3개 도메인으로 설계

* point_active
  * 활성화된 포인트
  * 포인트 적립: insert
  * 포인트 사용/소멸: update
  * save_point, remain_point 컬럼 2개로 나누어 포인트 사용시 remain_point에서 차감
  * remain_point가 0이 될 경우 다음 최신 적립 포인트에서 차감
  * 유효기간 만료시 remain_point를 0으로 update
  * 조회의 경우 **remain_point가 0이 아닌 경우만** 조회
  * 컬럼
      * customer_id
      * event_detail_type (제품구매, 제품리뷰, 이벤트참여, 보상, 충전)
      * save_point (제일 처음 등록된 포인트)
      * remain_point (남은 포인트)
      * created_date 
      * updated_date
  * RDS에서만 관리
  
* point_history
  * 포인트의 모든 이벤트
  * 포인트 적립/사용/소멸: insert
  * 컬럼
      * customer_id
      * message_id //queue uuid
      * trade_no
      * event_type (적립/사용/소멸)
      * event_detail_type 
      * point
      * description
      * created_date
      * updated_date
  * RDS, DynamoDB 양쪽에서 모두 관리
  * uuid+customer_id가 유니크키

* point_sum
  * 고객의 현재 총 포인트만 갖고 있음
  * point_active에 insert/update가 발생시마다 remain_point 의 총합계로 overwrite
  * 컬럼
      * customer_id
      * point_sum
  * DynamoDB에서만 관리     

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
  * 외부용
  * DynamoDB의 내용을 조회
* point-internal-api
  * 내부용
  * 
* point-admin
* point-test-web

## 남은 문제

* 포인트 계산 로직의 주체는 누구?
  * 포인트 어드민 생기는것 보니 우리가 해야하나보다
  * 그럼 주문 금액과 포인트 키를 받아서 직접 포인트 계산해야하나?
  * 아니면 거기서 계산된 포인트를 주나?
  * 부분 결제 취소가 있으면 부분 포인트 취소도 있어야하는데? 
      * 얼만큼 적립 취소를 시켜야하는지 포인트 시스템에서 어떻게 알지?