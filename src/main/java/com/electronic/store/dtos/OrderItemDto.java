package com.electronic.store.dtos;

import com.electronic.store.entities.Order;
import com.electronic.store.entities.Product;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class OrderItemDto {

    private int orderItemId;
    private int quantity;
    private int totalPrice;
    private ProductDto product;
}
