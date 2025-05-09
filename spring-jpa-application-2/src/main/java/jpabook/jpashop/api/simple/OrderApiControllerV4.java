package jpabook.jpashop.api.simple;

import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequestMapping("/api/simple/v4/orders/")
@RestController
@RequiredArgsConstructor
public class OrderApiControllerV4 {

    private final OrderSimpleQueryRepository orderRepository;


    // V3 -> V4 는 큰 개선 없는 경우가 많음 / Trade-off 존재
    @GetMapping
    public List<OrderSimpleQueryDto> getOrders() {
        return orderRepository.findOrderDtos();

        /*
        DTO projection 사용해 select절의 컬럼 수가 줄었음
        - 생성자 타입/순서/이름 정확히 일치해야 하고, 컴파일러 검증 불가 -> 오류 가능성
        - 재사용성 낮음
        - API 스펙에 의존하는 레포지토리 메서드 -> 쿼리용 별도 패키지로 관리

         vs Interface 기반 projection
         - getter 만 정의하면 되므로 빠르고 쉽다.
         - 복잡한 로직 매핑 불가능 (join 등)

        + open projection
        - 인터페이스에 @Value 어노테이션, SpEL 표현식 사용해 로직 추가
        - 지연/전체 로딩 유발 가능성
         */
    }
}
