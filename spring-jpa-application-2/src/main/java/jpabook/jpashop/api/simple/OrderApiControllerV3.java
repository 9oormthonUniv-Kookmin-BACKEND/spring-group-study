package jpabook.jpashop.api.simple;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/simple/v3/orders")
@RestController
@RequiredArgsConstructor
public class OrderApiControllerV3 {

    private final OrderRepository orderRepository;

    @GetMapping
    public List<OrderApiControllerV2.SimpleOrderResponse> getOrders() {
        // Fetch join 으로 1개 쿼리 실행 -> Lazy loading 이 아님
        List<Order> orders = orderRepository.findAllWithMemberDelivery();

        return orders.stream()
                .map(OrderApiControllerV2.SimpleOrderResponse::new)
                .toList();
    }
}
