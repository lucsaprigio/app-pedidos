package com.aprigio.pedidos.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /*
     * @ManyToOne -> VÁRIOS OrderItem apontam para UM Product (o outro lado do @OneToMany).
     *   fetch = LAZY -> o Product só é buscado no banco quando você chamar getProduct().
     *                   Evita trazer o produto inteiro sem necessidade (otimização).
     * @JoinColumn(name="product_id") -> coluna de FK para a tabela products.
     *
     *   // TypeORM / NestJS equivalente:
     *   @ManyToOne(() => Product, { eager: false })
     *   @JoinColumn({ name: 'product_id' })
     *   product: Product;
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Integer quantity;

    // Preço "congelado" no momento da compra (por isso é guardado aqui, e não lido do Product
    // toda vez — se o produto mudar de preço depois, o pedido antigo mantém o valor original).
    @Column(nullable = false)
    private BigDecimal unitPrice;


    // Cálculo do subtotal com BigDecimal: .multiply() em vez de "*". BigDecimal é imutável,
    // então a operação DEVOLVE um novo valor (não altera o original) — igual a decimal.js no Node.
    // BigDecimal.valueOf(quantity) converte o Integer em BigDecimal pra poder multiplicar.
    public BigDecimal subtotal() {
        return unitPrice.multiply(BigDecimal.valueOf(quantity));
    }
}
