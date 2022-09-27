package com.ll.exam.app_2022_09_22.app.order.entity;

import com.ll.exam.app_2022_09_22.app.base.entity.BaseEntity;
import com.ll.exam.app_2022_09_22.app.product.entity.ProductOption;
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
public class OrderItem extends BaseEntity {
    @ManyToOne(fetch = LAZY)
    @ToString.Exclude
    private Order order;

    @ManyToOne(fetch = LAZY)
    private ProductOption productOption;

    private int quantity;

    // 가격
    private int price; // 권장판매가
    private int salePrice; // 실제판매가
    private int wholesalePrice; // 도매가

    private int payPrice; // 권장판매가(실제 판매가) <- 10만원 이상 구매시 할인 쿠폰 같은 경우 ( 즉 할인 쿠폰 같은 경우 )

    private int refundPrice; // 5개를 샀는데 4개를 환불하는 경우 가격 ( 정책에 따라 환불 받았을 때 이벤트에 할인이 적용이 안되는 경우를 대비 )

    private int refundQuantity; // 5개를 샀는데 4개를 환불하는 경우 용량

    private int pgFee; // pg(결제 대행사)를 이용할 때 비용. ( 결제 대행사 수수료 )
    private boolean isPaid; // 결제 여부

    public OrderItem(ProductOption productOption, int quantity) {
        this.productOption = productOption;
        this.quantity = quantity;
        this.price = productOption.getPrice();
        this.salePrice = productOption.getSalePrice();
        this.wholesalePrice = productOption.getWholesalePrice();
    }

    public int calculatePayPrice() {
        return salePrice * quantity;
    }

    public void setPaymentDone() {
        this.pgFee = 0;
        this.payPrice = calculatePayPrice();
        this.isPaid = true;
    }
}