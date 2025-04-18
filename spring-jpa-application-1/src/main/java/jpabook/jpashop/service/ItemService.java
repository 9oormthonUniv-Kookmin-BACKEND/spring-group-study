package jpabook.jpashop.service;

import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;


@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class ItemService {

    private final ItemRepository itemRepository;

    @Transactional
    public void saveItem(Item item) {
        itemRepository.save(item);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }

    @Transactional
    public Item updateItem(Long itemId, String name, BigDecimal price, int stockQuantity) {
        Item findItem = itemRepository.findOne(itemId); // 가능하면 merge 대신 변경 감지를 사용하자
        findItem.change(name, price, stockQuantity); // 엔티티 레벨에서 변경 지점 추적 가능하도록 하자

        return findItem;
    }
}
