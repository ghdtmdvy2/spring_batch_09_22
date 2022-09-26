package com.ll.exam.app_2022_09_22.app.cart.service;

import com.ll.exam.app_2022_09_22.app.cart.entity.CartItem;
import com.ll.exam.app_2022_09_22.app.cart.repository.CartItemRepository;
import com.ll.exam.app_2022_09_22.app.member.entity.Member;
import com.ll.exam.app_2022_09_22.app.product.entity.ProductOption;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartItemRepository cartItemRepository;

    public void addItem(Member member, ProductOption productOption, int quantity) {
        Optional<CartItem> cartItem_ = cartItemRepository.findByMember_idAndProductOption_id(member.getId(),productOption.getId());

        if (cartItem_.isPresent()){
            CartItem cartItem = cartItem_.get();
            int PrevQuantity = cartItem.getQuantity();
            PrevQuantity = PrevQuantity + quantity;
            cartItem.setQuantity(PrevQuantity);
            cartItemRepository.save(cartItem);
        } else {
            CartItem cartItem = CartItem.builder()
                    .member(member)
                    .productOption(productOption)
                    .quantity(quantity)
                    .build();
            cartItemRepository.save(cartItem);
        }

    }
}