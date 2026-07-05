package com.aprigio.pedidos.application.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderItemRequest(
        @NotNull(message = "ID do produto é obrigatório")
        Long productId,

        @NotNull
        @Min(value = 1, message = "Quantidade mínima deve ser 1")
        Integer quantity
) { }
