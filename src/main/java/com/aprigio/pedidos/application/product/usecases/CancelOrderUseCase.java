package com.aprigio.pedidos.application.product.usecases;

import com.aprigio.pedidos.application.product.dto.OrderResponse;
import com.aprigio.pedidos.domain.entity.Order;
import com.aprigio.pedidos.domain.exception.OrderNotFoundException;
import com.aprigio.pedidos.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CancelOrderUseCase {

    private final OrderRepository repository;

    public OrderResponse execute(Long id) {
        // 1) Busca o pedido (ou 404 se não existir).
        Order order = repository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(id));

        // 2) Delega a REGRA para a própria entidade. O use case não sabe QUANDO um pedido
        //    pode ser cancelado — isso mora em Order.cancel(). O use case só orquestra o fluxo
        //    (buscar -> aplicar regra -> salvar). Boa separação de responsabilidades.
        order.cancel();

        // 3) Persiste o novo status e devolve o DTO atualizado.
        return OrderResponse.from(repository.save(order));
    }
}
