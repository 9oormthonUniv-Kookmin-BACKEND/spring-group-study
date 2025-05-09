package jpabook.jpashop.service.query;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.List;

@Getter
@AllArgsConstructor
public class OrderDto {
    private Long orderId;
    private String name;
    private String orderDate;
    private OrderStatus orderStatus;
    private Address address;
    private List<OrderItemDto> orderItems;

    public static OrderDto from(Order order) {
        return new OrderDto(
                order.getId(),
                order.getMember().getName(),
                order.getOrderDate().toString(),
                order.getStatus(),
                order.getDelivery().getAddress(),
                order.getOrderItems().stream()
                        .map(OrderItemDto::from)
                        .toList()
        );
    }
}
