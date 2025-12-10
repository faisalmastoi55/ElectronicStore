package com.electronic.store.services.impl;

import com.electronic.store.dtos.CartDto;
import com.electronic.store.entities.Cart;
import com.electronic.store.entities.CartItem;
import com.electronic.store.entities.Product;
import com.electronic.store.entities.User;
import com.electronic.store.exceptions.BadApiRequestException;
import com.electronic.store.exceptions.ResourceNoteFoundException;
import com.electronic.store.payloads.AddItemToCartRequest;
import com.electronic.store.repositories.CartItemRepository;
import com.electronic.store.repositories.CartRepository;
import com.electronic.store.repositories.ProductRepository;
import com.electronic.store.repositories.UserRepository;
import com.electronic.store.services.CartService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ModelMapper mapper;

    @Override
    public CartDto addItemToCart(String userId, AddItemToCartRequest request) {

        String productId = request.getProductId();
        int quantity = request.getQuantity();

        if (quantity <= 0) {
            throw new BadApiRequestException("Requested quantity is not valid !!");
        }

        //fetch the product from db:
        Product product = productRepository.findById(productId).orElseThrow(() -> new ResourceNoteFoundException("Product not found in database"));
        //fetch thw user from db:
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNoteFoundException("User not found in database"));

        //cart is not available then create the cart
        Cart cart=null;
        try {
            cart = cartRepository.findByUser(user).get();
        }catch (NoSuchElementException e) {
            cart = new Cart();
            cart.setCartId(UUID.randomUUID().toString());
            cart.setCreatedAt(new Date());
        }

        //perform cart operation
        //if cart items already present; then update
        AtomicReference<Boolean> updated = new AtomicReference<>(false);
        List<CartItem> items = cart.getItems();
        items = items.stream().map(item -> {

            if (item.getProduct().getProductId().equals(productId)) {
                item.setQuantity(quantity);
                item.setTotalPrice(quantity * product.getDiscountedPrice());
                updated.set(true);
            }
            return item;
        }).collect(Collectors.toList());

        //cart.setItems(updatedItems);

        //create items
        if (!updated.get()) {
            CartItem cartItem = CartItem.builder()
                    .quantity(quantity)
                    .totalPrice(quantity * product.getDiscountedPrice())
                    .cart(cart)
                    .product(product)
                    .build();

            cart.getItems().add(cartItem);
        }

        cart.setUser(user);
        Cart updatedCart = cartRepository.save(cart);

        return mapper.map(updatedCart, CartDto.class);
    }

    @Override
    public void removeItemFromCart(String userId, int cartItem) {
        CartItem cartItem1 = cartItemRepository.findById(cartItem).orElseThrow(() -> new ResourceNoteFoundException("Cart item not found !!"));
        cartItemRepository.delete(cartItem1);
    }

    @Override
    public void clearCart(String userId) {
        //fetch thw user from db:
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNoteFoundException("User not found in database"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNoteFoundException("cart of given user not found !!"));
        cart.getItems().clear();
        cartRepository.save(cart);
    }

    @Override
    public CartDto getCartByUser(String userId) {
        //fetch thw user from db:
        User user = userRepository.findById(userId).orElseThrow(() -> new ResourceNoteFoundException("User not found in database"));
        Cart cart = cartRepository.findByUser(user).orElseThrow(() -> new ResourceNoteFoundException("cart of given user not found !!"));
        return mapper.map(cart, CartDto.class);
    }
}
