package com.ll.exam.app_2022_09_22.app.product.entity;

import com.ll.exam.app_2022_09_22.app.base.entity.BaseEntity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class ProductOption extends BaseEntity {
    private String color;
    private String size;
    private String displayColor;
    private String displaySize;
    private int price;
    private int salePrice;
    private int wholesalePrice;
    @ManyToOne(fetch = LAZY)
    @ToString.Exclude
    private Product product;
    private boolean isSoldOut; // 사입처에서의 품절여부
    private int stockQuantity; // 쇼핑몰에서 보유한 물건 개수

    public ProductOption(String color, String size) {
        this.color = color;
        this.displayColor = color;
        this.size = size;
        this.displaySize = size;
    }

    public boolean isOrderable(int quantity) {
        // 품절 되지 않았다면 주문 가능.
        if (isSoldOut() == false) return true;
        // 품절이 되었으나 재고가 있는 경우 주문 가능.
        return getStockQuantity() >= quantity;
    }
}