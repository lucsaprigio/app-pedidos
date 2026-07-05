package com.aprigio.pedidos.application.product.dto;

import com.aprigio.pedidos.domain.entity.OrderItem;

import java.math.BigDecimal;

public record OrderItemResponse(
        Long id,
        String productName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {
    // Converte a entidade OrderItem no DTO. Repare que ela "achata" o objeto: em vez de
    // devolver o Product inteiro, pega só o nome (item.getProduct().getNome()) e já calcula
    // o subtotal. É o DTO controlando exatamente o formato que sai na API.
    // No NestJS isso seria um método/mapper equivalente, ou @Expose/@Transform do class-transformer.
    public static OrderItemResponse from (OrderItem item) {
        return new OrderItemResponse(
                item.getId(),
                item.getProduct().getNome(),
                item.getQuantity(),
                item.getUnitPrice(),
                item.subtotal()
        );
    }
}
