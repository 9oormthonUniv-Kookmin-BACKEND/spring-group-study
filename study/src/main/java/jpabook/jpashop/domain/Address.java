package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.Getter;

// Address는 내장 타입으로 사용하기에 Embeddable 적용
@Embeddable
@Getter
public class Address {

    private String city;
    private String street;
    private String zipcode;

    protected Address() {
    }

    public Address(String city, String street, String zipcode) {
        this.city = city;
        this.street = street;
        this.zipcode = zipcode;
    }
}
