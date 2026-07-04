package com.aprigio.pedidos.application.dto;

import java.math.BigDecimal;

import com.aprigio.pedidos.domain.entity.Product;

public record ProdutoResponse(
    Long id,
    String nome,
    BigDecimal preco,
    Integer estoque
) {
    public static ProdutoResponse from(Product product) {
        return new ProdutoResponse(
            product.getId(),
            product.getNome(),
            product.getPreco(),
            product.getEstoque()
        );
    }
}
