package com.aprigio.pedidos.domain.entity;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/*
 * ANNOTATIONS DE PERSISTÊNCIA (JPA/Hibernate) — vêm do pacote jakarta.persistence:
 *
 *  @Entity        -> diz ao JPA que esta classe representa uma TABELA do banco.
 *                    Cada instância de Product = uma linha da tabela.
 *  @Table(name=)  -> nome da tabela no banco. Sem isso, usaria o nome da classe ("product").
 *
 * ANNOTATIONS DO LOMBOK — geram código repetitivo em tempo de compilação (getters,
 * construtores, etc.), pra você não precisar escrever à mão:
 *
 *  @Getter          -> gera getId(), getNome(), getPreco()... para todos os campos.
 *  @Builder         -> gera o padrão builder: Product.builder().nome(...).preco(...).build().
 *  @NoArgsConstructor  -> gera um construtor vazio. O JPA/Hibernate PRECISA dele para
 *                         instanciar a entidade via reflection ao ler do banco.
 *  @AllArgsConstructor -> gera um construtor com todos os campos (usado pelo @Builder).
 */
@Entity
@Table(name = "products")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
    // @Id -> marca este campo como a CHAVE PRIMÁRIA da tabela.
    // @GeneratedValue(IDENTITY) -> quem gera o valor é o próprio banco (coluna auto-incremento).
    // O Hibernate deixa o INSERT sem id e lê o id gerado de volta.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    // Campos sem annotation viram colunas normais (o nome da coluna = nome do campo).
    private String nome;
    private BigDecimal preco;
    private Integer estoque;

    // @CreationTimestamp (Hibernate) -> preenche automaticamente com a data/hora
    // no momento em que o registro é inserido pela primeira vez. Você nunca seta na mão.
    @CreationTimestamp
    private LocalDateTime createdAt;

    // Regras de negócio moram DENTRO da entidade (modelo "rico", não anêmico).
    // Alterar o estado do objeto por métodos como este é melhor do que ter setters soltos.
    public void update(String nome, BigDecimal preco, Integer estoque) {
        this.nome = nome;
        this.preco = preco;
        this.estoque = estoque;
    }

    // Outra regra de negócio na própria entidade: responde uma pergunta sobre seu estado.
    public boolean temEstoque() {
        return this.estoque > 0;
    }
}