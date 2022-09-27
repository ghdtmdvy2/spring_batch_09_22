package com.ll.exam.app_2022_09_22.app.order.service;

import com.ll.exam.app_2022_09_22.app.cart.entity.CartItem;
import com.ll.exam.app_2022_09_22.app.cart.service.CartService;
import com.ll.exam.app_2022_09_22.app.member.entity.Member;
import com.ll.exam.app_2022_09_22.app.member.service.MemberService;
import com.ll.exam.app_2022_09_22.app.order.entity.Order;
import com.ll.exam.app_2022_09_22.app.order.entity.OrderItem;
import com.ll.exam.app_2022_09_22.app.order.repository.OrderRepository;
import com.ll.exam.app_2022_09_22.app.product.entity.ProductOption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {
    private final MemberService memberService;
    private final CartService cartService;
    private final OrderRepository orderRepository;

    // 주문 하기 메서드
    @Transactional
    public Order createFromCart(Member member) {
        // 입력된 회원의 장바구니 아이템들을 전부 가져온다.

        // 만약에 특정 장바구니의 상품옵션이 판매불능이면 삭제
        // 만약에 특정 장바구니의 상품옵션이 판매가능이면 주문품목으로 옮긴 후 삭제

        // 모든 장바구니에 들어가있는 것을 가져옴.
        List<CartItem> cartItems = cartService.getItemsByMember(member);

        List<OrderItem> orderItems = new ArrayList<>();

        // 카트에 있는 상품들을 가져오기 위한 반복문
        for (CartItem cartItem : cartItems) {
            // 카트에 있는 상품들을 가져옴.
            ProductOption productOption = cartItem.getProductOption();

            // 카트를 통해 주문 가능하는지 확인하는 부분.
            if (productOption.isOrderable(cartItem.getQuantity())) {
                // 카트에 있는 용량과 상품의 재고가 있는 경우 주문 List에 추가.
                orderItems.add(new OrderItem(productOption, cartItem.getQuantity()));
            }
            // 주문 List에 다 추가하면 주문이 성공이 될 것이기 때문에 장바구니에서 제거
            cartService.deleteItem(cartItem);
        }
        // 주문하기 위해 유저와 주문 list를 파라미터로 받아옴.
        return create(member, orderItems);
    }

    @Transactional
    public Order create(Member member, List<OrderItem> orderItems) {
        // 주문 객체를 생성
        Order order = Order
                .builder()
                .member(member)
                .build();
        // 주문 리스트를 주문 객체에 넣어줌.
        for (OrderItem orderItem : orderItems) {
            order.addOrderItem(orderItem);
        }
        // 주문 객체를 저장 (주문 하기)
        orderRepository.save(order);

        return order;
    }

    @Transactional
    public void payByRestCashOnly(Order order) {
        // 주문자의 가지고 있는 Cash를 통해 계산하기 때문에 주문자를 찾아온다.
        Member orderer = order.getMember();

        // 주문자의 Cash를 가져옴.
        long restCash = orderer.getRestCash();

        // 주문을 통한 가격 계산
        int payPrice = order.calculatePayPrice();

        // 만약 자기가 가지고 있는 캐시보다 결제 할 금액(주문 할 금액)보다 크다면 RuntimeException
        if (payPrice > restCash) {
            throw new RuntimeException("예치금이 부족합니다.");
        }
        // 정상적으로 주문이 진행이 되었다면 Cashlog를 찍어줌.
        memberService.addCash(orderer, payPrice * -1, "주문결제__예치금결제");

        // 주문자의 주문 리스트를 저장해줌.
        order.setPaymentDone();
        // 주문 저장. ( 주문이랑 주문리스트가 관계가 맺어져 있기 때문에 주문을 저장하면 알아서 주문 리스트도 저장이 된다. )
        orderRepository.save(order);
    }
}