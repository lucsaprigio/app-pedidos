package com.aprigio.pedidos.application.product.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record OrderRequest(
        @NotNull
        @Size(min = 1, message = "Pedido deve ter pelo menos 1 item")
        @Valid
        List<OrderItemRequest> items
) { }
