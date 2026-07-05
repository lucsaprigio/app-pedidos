package com.aprigio.pedidos.infra.web;

import com.aprigio.pedidos.application.product.dto.OrderRequest;
import com.aprigio.pedidos.application.product.dto.OrderResponse;
import com.aprigio.pedidos.application.product.usecases.CancelOrderUseCase;
import com.aprigio.pedidos.application.product.usecases.CreateOrderUseCase;
import com.aprigio.pedidos.application.product.usecases.FindAllOrdersUseCase;
import com.aprigio.pedidos.application.product.usecases.FindOrderByIdUseCase;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    private final CreateOrderUseCase createOrder;
    private final FindOrderByIdUseCase findOrderById;
    private final FindAllOrdersUseCase findAllOrders;
    private final CancelOrderUseCase cancelOrder;

    @GetMapping
    public ResponseEntity<List<OrderResponse>> findAll() {
        return ResponseEntity.ok(findAllOrders.execute());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(findOrderById.execute(id));
    }

    @PostMapping
    public ResponseEntity<OrderResponse> create(
            @Valid @RequestBody OrderRequest request) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createOrder.execute(request));
    }

    // @PatchMapping -> PATCH em /api/v1/orders/{id}/cancel.
    // Usa-se PATCH (e não PUT) porque é uma alteração PARCIAL/de estado (só o status muda),
    // não a substituição do recurso inteiro. É uma "ação" sobre o pedido.
    // No NestJS seria: @Patch(':id/cancel') cancel(@Param('id') id) { ... }
    @PatchMapping("/{id}/cancel")
    public ResponseEntity<OrderResponse> cancel(@PathVariable Long id) {
        return ResponseEntity.ok(cancelOrder.execute(id));
    }
}
