package com.aprigio.pedidos.application.product.dto;

import com.aprigio.pedidos.domain.entity.Order;
import com.aprigio.pedidos.domain.entity.OrderStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderResponse(
        Long id,
        OrderStatus status,
        List<OrderItemResponse> items,
        BigDecimal totalPrice,
        LocalDateTime createdAt
) {
    // Factory que converte a ENTIDADE Order (com relacionamentos JPA) no DTO de saída.
    public static OrderResponse from(Order order) {
        return new OrderResponse(
                order.getId(),
                order.getStatus(),
                /*
                 * Cada OrderItem (entidade) precisa virar um OrderItemResponse (DTO).
                 * Como é uma LISTA, usamos .stream().map(...).toList() de novo.
                 * "OrderItemResponse::from" é method reference = item -> OrderItemResponse.from(item).
                 *
                 *   // TypeScript / NestJS equivalente:
                 *   items: order.items.map(OrderItemResponse.from)
                 *   // ou: order.items.map(item => OrderItemResponse.from(item))
                 *
                 * É o mesmo conceito de "mapear entidade -> DTO" que frameworks Node fazem
                 * com class-transformer (plainToInstance) ou manualmente com .map().
                 */
                order.getItems().stream()
                        .map(OrderItemResponse::from)
                        .toList(),
                order.getTotalPrice(),
                order.getCreatedAt()
        );
    }
}
