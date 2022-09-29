package com.ll.exam.app_2022_09_22.app.order.entity;

import com.ll.exam.app_2022_09_22.app.base.entity.BaseEntity;
import com.ll.exam.app_2022_09_22.app.product.entity.ProductOption;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

import java.time.LocalDateTime;

import static javax.persistence.FetchType.LAZY;

@Entity
@Getter
@Setter
@NoArgsConstructor
@SuperBuilder
@ToString(callSuper = true)
public class RebateOrderItem extends BaseEntity {
    @OneToOne(fetch = LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ToString.Exclude
    private OrderItem orderItem;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ToString.Exclude
    private Order order;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    private ProductOption productOption;

    private int quantity;

    // 가격
    private int price; // 권장판매가
    private int salePrice; // 실제판매가
    private int wholesalePrice; // 도매가
    private int pgFee; // 결제대행사 수수료
    private int payPrice; // 결제금액
    private int refundPrice; // 환불금액
    private int refundQuantity; // 환불한 개수
    private boolean isPaid; // 결제여부

    // 상품
    private String productName;

    private LocalDateTime payDate; // 결제날짜

    // 상품옵션
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "color", column = @Column(name = "product_option_color")),
            @AttributeOverride(name = "size", column = @Column(name = "product_option_size")),
            @AttributeOverride(name = "displayColor", column = @Column(name = "product_option_display_color")),
            @AttributeOverride(name = "displaySize", column = @Column(name = "product_option_display_size"))
    })
    private RebateOrderItem.EmbProductOption embProductOption;

    // 주문 했을 때의 생성 날짜
    private LocalDateTime orderItem_create_date;

    public RebateOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
        order = orderItem.getOrder();
        productOption = orderItem.getProductOption();
        quantity = orderItem.getQuantity();
        price = orderItem.getPrice();
        salePrice = orderItem.getSalePrice();
        wholesalePrice = orderItem.getWholesalePrice();
        pgFee = orderItem.getPgFee();
        payPrice = orderItem.getPayPrice();
        refundPrice = orderItem.getRefundPrice();
        refundQuantity = orderItem.getRefundQuantity();
        isPaid = orderItem.isPaid();

        // 상품
        productName = orderItem.getProductOption().getProduct().getName();

        // 상품 옵션 추가 데이터
        embProductOption = new EmbProductOption(orderItem.getProductOption());

        // 주문 아이템 추가 데이터
        orderItem_create_date = orderItem.getCreateDate();

        payDate = orderItem.getPayDate();// 결제 날짜
    }
    @Embeddable
    @NoArgsConstructor
    public static class EmbProductOption {
        private String color;
        private String size;
        private String displayColor;
        private String displaySize;

        public EmbProductOption(ProductOption productOption) {
            color = productOption.getColor();
            size = productOption.getSize();
            displayColor = productOption.getDisplayColor();
            displaySize = productOption.getDisplaySize();
        }
    }
}