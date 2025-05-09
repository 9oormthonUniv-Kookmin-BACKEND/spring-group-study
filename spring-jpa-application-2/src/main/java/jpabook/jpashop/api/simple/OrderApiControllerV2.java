package jpabook.jpashop.api.simple;

import jpabook.jpashop.api.MemberApiControllerV2;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/simple/v2/orders")
@RestController
@RequiredArgsConstructor
public class OrderApiControllerV2 {

    private final OrderRepository orderRepository;

    @GetMapping
    public List<SimpleOrderResponse> getOrders() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        return all.stream()
                .map(SimpleOrderResponse::new)
                .toList();
    }

    @Data
    static class SimpleOrderResponse {
        private Long id;
        private String orderDate;
        private String status;
        private MemberApiControllerV2.MemberResponse member;
//        private List<Item> items;

        public SimpleOrderResponse(Order order) {
            this.id = order.getId();
            this.orderDate = order.getOrderDate().toString();
            this.status = order.getStatus().name();
            this.member = MemberApiControllerV2.MemberResponse.of(order.getMember());
//            this.items = order.getOrderItems().stream()
//                    .map(OrderItem::getItem)
//                    .toList();
        }
    }
}
