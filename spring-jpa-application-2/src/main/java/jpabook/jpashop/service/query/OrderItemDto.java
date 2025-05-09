package jpabook.jpashop.service.query;

import jpabook.jpashop.domain.OrderItem;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OrderItemDto {
    private String itemName;
    private int orderPrice;
    private int count;

    public static OrderItemDto from(OrderItem orderItem) {
        return new OrderItemDto(
                orderItem.getItem().getName(),
                orderItem.getOrderPrice(),
                orderItem.getCount()
        );
    }
}
