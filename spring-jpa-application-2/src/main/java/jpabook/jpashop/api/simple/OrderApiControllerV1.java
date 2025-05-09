package jpabook.jpashop.api.simple;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * xToOne 관계에서의 성능 최적화
 */
@RequestMapping("/api/simple/v1/orders")
@RestController
@RequiredArgsConstructor
public class OrderApiControllerV1 {

    private final OrderRepository orderRepository;

    @GetMapping
    public List<Order> getOrders() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        return all;

        /*
        1. Order <-> Member 로 무한 양방향 참조
        -> 한쪽에 @JsonIgnore 처리해 해결 가능
        */

        /*
        2. 프록시 객체 (lazy-loaded entity) 를 Jackson 이 직렬화 할 때 문제 발생
        - Type definition error (ByteBuddyInterceptor)
        - LazyInitializationException : 세션이 닫힌 경우

         -> Hibernate6Module 에서 FORCE_LAZY_LOADING 으로 강제 로딩 수행
          false : 프록시의 경우 로딩하지 않음. 에러 발생하지 않는 대신 null로 직렬화됨.
          true : 모두 강제로 로딩, 불필요한 필드 조회 쿼리 수행 가능성
        */

        // 결론 : 엔티티를 응답에 사용하지 않는다. DTO 사용한다.
    }
}
