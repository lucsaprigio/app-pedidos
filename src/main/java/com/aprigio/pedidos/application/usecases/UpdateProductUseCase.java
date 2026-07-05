package com.aprigio.pedidos.application.usecases;

import com.aprigio.pedidos.application.dto.ProdutoRequest;
import com.aprigio.pedidos.application.dto.ProdutoResponse;
import com.aprigio.pedidos.domain.entity.Product;
import com.aprigio.pedidos.domain.exception.ProductNotFoundException;
import com.aprigio.pedidos.domain.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// @Component -> bean gerenciado; @RequiredArgsConstructor -> injeção do repository via construtor.
@Component
@RequiredArgsConstructor
public class UpdateProductUseCase {
    private final ProdutoRepository repository;

    public ProdutoResponse execute(Long id, ProdutoRequest request) {
        // 1) Busca o produto existente; se não achar, lança 404.
        Product product = repository.findById(id)
                         .orElseThrow(() -> new ProductNotFoundException(id));

        // 2) Altera o estado pelo método de negócio da própria entidade (não por setters soltos).
        product.update(request.nome(), request.preco(), request.estoque());

        // 3) Persiste. Como o id já existe, o JPA faz UPDATE (e não INSERT).
        return ProdutoResponse.from(repository.save(product));
    }
}
