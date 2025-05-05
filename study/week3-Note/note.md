# N + 1 문제 해결과 DTO 직접 조회 전략

## 1. N+1 문제란?

- N + 1 문제는 연관된 데이터를 지연 로딩(Lazy)로 불러올 때 발생한다.
- 예: 회원과 주문 (1:N) 관계에서 회원 1명을 조회할 때마다 추가 쿼리 발생.

```sql
SELECT * FROM member -- 1번
SELECT * FROM order WHERE member_id = ? -- N번 (회원 수만큼)
```
- 총 1+N번의 쿼리가 발생하므로 성능 저하의 원인이 된다.

## 2. N + 1 탐지 방법
| 방법                | 설명                                             |
|---------------------|--------------------------------------------------|
| Hibernate SQL 로그  | JPA가 날리는 쿼리를 로그로 확인                 |
| p6spy               | SQL 로그를 보기 좋게 출력하는 오픈소스           |
| JPA Inspector       | 인텔리제이 플러그인으로 지연 로딩 추적 가능     |

- application yml 설정 예시
```
spring:
  jpa:
    properties:
      hibernate:
        format_sql: true
logging.level:
  org.hibernate.SQL: debug
  org.hibernate.type.descriptor.sql: trace
```

## N + 1 해결 전략
### 1. Fetch Join
- 연관 엔티티를 즉시 로딩하면서 **한 번의 쿼리**로 JOIN
- JPQL
  `SELECT m FROM Member m JOIN FETCH m.orders`

### 2. @Entity Graph
- JPQL 없이 선언적으로 fetch join 효과
```java
@EntityGraph(attributePaths = {"orders"})
List<Member> findAll(); 
```

### 3. @BatchSize
- Lazy 로딩이더라도 IN 쿼리로 묶어서 한번에 조회
```java
@BatchSize(size = 100)
private List<Order> orders;
```

또는

`hibernate.default_batch_fetch_size: 100`

### 4. DTO 직접 조회(V4) 방식

## DTO 직접 조회 전략(V4)의 장단점
### 장점
- 성능 최적화: 필요한 컬럼만 select
- 엔티티 관리 X → 메모리 사용량 ↓

### 단점
- 재사용성 떨어짐
- 쿼리 변경 시 코드 수정 필요함.
### 5. 💡 실무에서는 어떻게 : Projection (Spring Data JPA)
- DTO 대신 interface 기반 결과 매핑도 가능
```java
public interface OrderView {
    Long getId();
    String getMemberName();
}

@Query("SELECT o.id AS id, m.name AS memberName FROM Order o JOIN o.member m")
List<OrderView> findAllProjection();
```

- 코드 간결, Setter 필요 없음, 성능도 DTO 직접 조회처럼 좋음
