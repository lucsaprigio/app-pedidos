package com.aprigio.pedidos.application.product.dto;

import java.math.BigDecimal;

import com.aprigio.pedidos.domain.entity.Product;

/*
 * DTO de SAÍDA: o que a API devolve pro cliente. Também é um record (imutável).
 *
 * Repare que ele NÃO expõe o campo createdAt — controlar exatamente quais dados saem
 * é justamente uma das vantagens de não devolver a entidade Product diretamente.
 */
public record ProdutoResponse(
    Long id,
    String nome,
    BigDecimal preco,
    Integer estoque
) {
    // Método de fábrica ("factory"): converte a entidade do domínio (Product) neste DTO.
    // Ser "static" permite chamar sem instanciar: ProdutoResponse.from(product).
    // E, por ser um método, dá pra usar como referência: .map(ProdutoResponse::from).
    public static ProdutoResponse from(Product product) {
        return new ProdutoResponse(
            product.getId(),
            product.getNome(),
            product.getPreco(),
            product.getEstoque()
        );
    }
}
