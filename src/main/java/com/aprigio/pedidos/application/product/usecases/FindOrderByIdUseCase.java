package com.aprigio.pedidos.application.product.usecases;

import com.aprigio.pedidos.application.product.dto.OrderResponse;
import com.aprigio.pedidos.domain.exception.OrderNotFoundException;
import com.aprigio.pedidos.domain.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FindOrderByIdUseCase {

    private final OrderRepository repository;

    public OrderResponse execute(Long id) {
        /*
         * ATENÇÃO: aqui o .map() é do OPTIONAL, não do Stream — mas a ideia é a mesma:
         * "se tiver valor dentro, transforma; se estiver vazio, não faz nada".
         *
         *  findById(id) -> Optional<Order>  (uma "caixinha" que pode conter 0 ou 1 Order)
         *  .map(OrderResponse::from) -> se tiver Order, converte pra Optional<OrderResponse>
         *  .orElseThrow(...) -> se a caixinha estava vazia, lança 404
         *
         *   // TypeScript / NestJS equivalente (sem o wrapper Optional):
         *   const order = await this.repo.findById(id);
         *   if (!order) throw new NotFoundException(...);
         *   return OrderResponse.from(order);
         *
         * O Optional é a forma do Java te OBRIGAR a tratar o "não encontrado", em vez de
         * deixar um null escapar e estourar um NullPointerException lá na frente. É parecido
         * com o cuidado de checar "if (!order)" no TS com strictNullChecks ligado.
         */
        return repository.findById(id)
                .map(OrderResponse::from)
                .orElseThrow(() -> new OrderNotFoundException(id));
    }
}
