package jpabook.jpashop.api;

import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderItem;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.query.OrderFlatDto;
import jpabook.jpashop.repository.order.query.OrderItemQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryDto;
import jpabook.jpashop.repository.order.query.OrderQueryRepository;
import jpabook.jpashop.service.query.OrderDto;
import jpabook.jpashop.service.query.OrderQueryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RequestMapping("/api")
@RestController
@RequiredArgsConstructor
public class OrderApiController {

    private final OrderRepository orderRepository;
    private final OrderQueryRepository orderQueryRepository;
    private final OrderQueryService orderQueryService;

    @GetMapping("/v1/orders")
    public List<Order> 엔티티직접노출() {
        List<Order> orders = orderRepository.findAllByQueryDsl(new OrderSearch());
        for (Order order : orders) {
            // 1to1 Lazy 강제 초기화
            order.getMember().getName();
            order.getDelivery().getAddress();

            // 1toN Lazy 강제 초기화
            List<OrderItem> orderItems = order.getOrderItems();
            orderItems.forEach(o -> o.getItem().getName());
        }
        return orders;
    }

    @GetMapping("/v2/orders")
    public List<OrderDto> DTO로변환() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        return orders.stream()
                .map(OrderDto::from)
                .toList();
    }

    /**
     * 컬렉션 페치 조인으로 쿼리 수 최적화
     */
    @GetMapping("/v3/orders")
    public List<OrderDto> 페치조인최적화() {
        return orderQueryService.ordersV3();
    }

    /**
     * 컬렉션 페치 조인 제거하고 Batchsize로 최적화
     */
    @GetMapping("/v3.1/orders")
    public List<OrderDto> 페치조인페이징(
            @RequestParam(value = "offset", defaultValue = "0") int offset,
            @RequestParam(value = "limit", defaultValue = "100") int limit
    ) {
        List<Order> orders = orderRepository.findAllWithMemberDelivery(offset, limit);
        return orders.stream()
                .map(OrderDto::from)
                .toList();
    }

    /**
     * DTO로 직접 조회
     * 쿼리 수 : 1 + N (N + 1 문제 발생)
     */
    @GetMapping("/v4/orders")
    public List<OrderQueryDto> DTO직접조회() {
        return orderQueryRepository.findOrderQueryDtos();
    }

    /**
     * N + 1 문제 해결
     * OrderItem을 In 절로 묶어서 한번에 조회
     * 쿼리 수 : 1 + 1 = 2
     */
    @GetMapping("/v5/orders")
    public List<OrderQueryDto> DTO직접조회_최적화() {
        return orderQueryRepository.findOrderQueryDtos_optimization();
    }


    /**
     * 쿼리는 한번이지만 조인으로 인해 중복 데이터 조회됨
     * 어플리케이션에서 중복 제거 추가 작업 필요
     * 따라서 페이징 불가능
     */
    @GetMapping("/v6/orders")
    public List<OrderQueryDto> 플랫데이터() {
        List<OrderFlatDto> orderFlats = orderQueryRepository.findOrderQueryDtos_flat();
        Map<OrderQueryDto, List<OrderItemQueryDto>> grouped = groupOrderItems(orderFlats);

        return grouped.entrySet().stream() // 중복 제거/변환된 dto Map -> List
                .map(e -> new OrderQueryDto(
                        e.getKey().getOrderId(),
                        e.getKey().getName(),
                        e.getKey().getOrderDate(),
                        e.getKey().getOrderStatus(),
                        e.getKey().getAddress(),
                        e.getValue()))
                .toList();
    }

    private Map<OrderQueryDto, List<OrderItemQueryDto>> groupOrderItems(List<OrderFlatDto> flats) {
        return flats.stream()
                .collect(Collectors.groupingBy( // orderId 기준으로 중복 제거
                        o -> new OrderQueryDto(
                                o.getOrderId(),
                                o.getName(),
                                o.getOrderDate(),
                                o.getOrderStatus(),
                                o.getAddress()),
                        Collectors.mapping( // OrderItem 리스트 매핑
                                o -> new OrderItemQueryDto(
                                        o.getOrderId(),
                                        o.getItemName(),
                                        o.getOrderPrice(),
                                        o.getCount()),
                                Collectors.toList()
                        )
                ));
    }

}
