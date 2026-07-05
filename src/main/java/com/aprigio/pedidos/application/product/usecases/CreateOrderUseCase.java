package com.aprigio.pedidos.application.product.usecases;

import com.aprigio.pedidos.application.product.dto.OrderRequest;
import com.aprigio.pedidos.application.product.dto.OrderResponse;
import com.aprigio.pedidos.domain.entity.Order;
import com.aprigio.pedidos.domain.entity.OrderItem;
import com.aprigio.pedidos.domain.entity.OrderStatus;
import com.aprigio.pedidos.domain.entity.Product;
import com.aprigio.pedidos.domain.exception.ProductNotFoundException;
import com.aprigio.pedidos.domain.repository.OrderRepository;
import com.aprigio.pedidos.domain.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.awt.*;
import java.math.BigDecimal;
import java.util.List;

@Component
@RequiredArgsConstructor
public class CreateOrderUseCase {

    private final OrderRepository orderRepository;
    private final ProdutoRepository productRepository;

    public OrderResponse execute(OrderRequest request) {
        /*
         * ==== .stream() + .map() : POR QUE PRECISA DO .map NO PRODUTO? ====
         *
         * O request chega com uma lista de itens "crus" (só productId + quantity).
         * Precisamos TRANSFORMAR cada um desses num OrderItem completo — e, pra isso,
         * buscar o Product no banco pra pegar o preço atual. "Transformar cada elemento
         * de uma lista em outra coisa" é exatamente o trabalho do .map().
         *
         * Em Java, uma List não tem .map() direto. Você abre um "fluxo" com .stream()
         * (uma esteira lazy), aplica as operações, e no fim "materializa" com .toList().
         * No TypeScript/JS o Array já tem .map() nativo, então não existe esse .stream():
         *
         *   // TypeScript / NestJS equivalente:
         *   const items = await Promise.all(
         *     request.items.map(async (itemRequest) => {
         *       const product = await this.productRepo.findById(itemRequest.productId);
         *       if (!product) throw new NotFoundException(...);   // <- o orElseThrow
         *       return { product, quantity: itemRequest.quantity, unitPrice: product.preco };
         *     })
         *   );
         *
         * Diferenças-chave vs. TS:
         *  - Java: .stream().map(...).toList()   |  JS: array.map(...) direto.
         *  - Aqui o findById é SÍNCRONO (bloqueante). No Node seria assíncrono (await +
         *    Promise.all), porque I/O no Node não trava a thread. Por isso o JS precisa do
         *    Promise.all em volta e o Java não precisa de nada.
         */
        List<OrderItem> items = request.items().stream()
                // Para CADA itemRequest, esta lambda devolve um OrderItem novo.
                // (equivale ao callback do .map() no JS: itemRequest => { ... return ... })
                .map(itemRequest -> {
                    // findById devolve Optional<Product> (pode existir ou não).
                    // .orElseThrow(...) = "se veio vazio, lança a exceção". No JS/TS seria
                    // um "if (!product) throw ...". O Optional evita o null solto.
                    Product product = productRepository.findById(itemRequest.productId())
                            .orElseThrow(() -> new ProductNotFoundException(itemRequest.productId()));
                    // Congela o preço no momento da compra (unitPrice = preço do produto agora).
                    return OrderItem.builder()
                            .product(product)
                            .quantity(itemRequest.quantity())
                            .unitPrice(product.getPreco())
                            .build();

                })
                // .toList() fecha o stream e junta tudo numa List<OrderItem> imutável.
                // (no JS o .map() já devolve o array pronto — não existe esse passo extra)
                .toList();

        /*
         * ==== .reduce() : SOMAR TUDO NUM VALOR SÓ ====
         *
         * Aqui pegamos a lista de itens e "reduzimos" a UM único BigDecimal (o total).
         *  1) .map(OrderItem::subtotal)  -> vira um stream de subtotais (preço * qtd de cada).
         *     "OrderItem::subtotal" é um METHOD REFERENCE, atalho de "item -> item.subtotal()".
         *  2) .reduce(inicial, acumulador) -> começa em ZERO e vai somando um a um.
         *     "BigDecimal::add" é o acumulador: reduce((a, b) => a.add(b)).
         *
         *   // TypeScript / JS equivalente:
         *   const total = items
         *     .map(item => item.subtotal())
         *     .reduce((acc, sub) => acc + sub, 0);   // 0 = valor inicial, igual ao BigDecimal.ZERO
         *
         * Obs: usa-se BigDecimal (e não double/number) pra dinheiro, evitando erro de ponto
         * flutuante. No JS puro "0.1 + 0.2 === 0.30000000000000004" — por isso libs como
         * decimal.js existem no mundo Node. BigDecimal é o "decimal.js nativo" do Java.
         */
        BigDecimal total = items.stream()
                .map(OrderItem::subtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        // Monta o pedido já com status inicial PENDING e o total calculado.
        Order order = Order.builder()
                .status(OrderStatus.PENDING)
                .items(items)
                .totalPrice(total)
                .build();

        // Salva (o cascade salva os itens junto) e converte a entidade em DTO de resposta.
        return OrderResponse.from(orderRepository.save(order));

    };
}
