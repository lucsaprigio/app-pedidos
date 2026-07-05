package com.aprigio.pedidos.domain.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // @Enumerated -> guarda um enum (OrderStatus) numa coluna. Estuda isto: SEM argumento,
    // o padrão é EnumType.ORDINAL, ou seja, salva o ÍNDICE (0,1,2...). Se você reordenar o
    // enum um dia, os dados antigos "trocam de significado". O mais seguro é @Enumerated(EnumType.STRING),
    // que grava o texto ("PENDING"). No TypeORM: @Column({ type: 'enum', enum: OrderStatus }).
    @Enumerated
    @Column(nullable = false)
    private OrderStatus status;

    /*
     * @OneToMany -> um Order tem VÁRIOS OrderItem (relação 1:N).
     *   cascade = ALL      -> ao salvar/deletar o Order, aplica o mesmo aos itens (salva junto).
     *   orphanRemoval=true -> se um item sai da lista, ele é DELETADO do banco.
     * @JoinColumn(name="order_id") -> a tabela order_items ganha uma FK "order_id".
     *
     *   // TypeORM / NestJS equivalente:
     *   @OneToMany(() => OrderItem, item => item.order, { cascade: true })
     *   items: OrderItem[];
     */
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "order_id")
    private List<OrderItem> items;

    @Column(nullable = false)
    private BigDecimal totalPrice;

    @CreationTimestamp
    private LocalDateTime createdAt;

    // REGRA DE NEGÓCIO na própria entidade (modelo rico): quem sabe se um pedido pode
    // mudar de status é o próprio pedido — não o controller nem o use case.
    public void cancel() {
        if(this.status == OrderStatus.DELIVERED) {
            // IllegalStateException é capturada pelo GlobalExceptionHandler e vira HTTP 422.
            throw new IllegalStateException("Pedido entregue não pode ser cancelado");
        }

        this.status = OrderStatus.CANCELED;
    }

    public boolean isCancellable() {
        return this.status == OrderStatus.PENDING
                || this.status == OrderStatus.CONFIRMED;
    }
}
