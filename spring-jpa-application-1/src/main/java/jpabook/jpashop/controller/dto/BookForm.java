package jpabook.jpashop.controller.dto;

import jpabook.jpashop.domain.item.Book;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class BookForm {

    private Long id;
    private String name;
    private int price;
    private int stockQuantity;
    private String author;
    private String isbn;

    public Book toEntity() {
        Book book = new Book();
        book.setId(id);
        book.setName(name);
        book.setPrice(BigDecimal.valueOf(price));
        book.setStockQuantity(stockQuantity);
        book.setAuthor(author);
        book.setIsbn(isbn);

        return book;
    }
}
