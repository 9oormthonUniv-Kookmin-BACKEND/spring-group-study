package jpabook.jpashop.service.query;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional(readOnly = true)
@Service
@RequiredArgsConstructor
public class OrderQueryService {

    /**
     * OSIV false 설정시 응답 반환 시점 (트랜잭션 종료 후)에 지연로딩 필드 접근 불가
     * 여기로 지연로딩 필드 조회 로직 모두 이동해 해결
     * Command 와 Query 분리
     */

    private final OrderRepository orderRepository;

    public List<OrderDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithItem();
        return orders.stream()
                .map(OrderDto::from)
                .toList();
    }
}
