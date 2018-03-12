# SQS를 이용한 비동기 포인트 시스템

## 기능

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
        
## 도메인

* 

## 모듈