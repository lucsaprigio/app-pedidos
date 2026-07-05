package com.aprigio.pedidos.application.product.usecases;

import com.aprigio.pedidos.application.product.dto.ProdutoResponse;
import com.aprigio.pedidos.domain.exception.ProductNotFoundException;
import com.aprigio.pedidos.domain.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// @Component -> vira bean gerenciado pelo Spring. @RequiredArgsConstructor -> injeção
// por construtor do "repository" (mesmo padrão explicado em CreateProductUseCase).
@Component
@RequiredArgsConstructor
public class FindByProductUseCase {
    private final ProdutoRepository repository;

    public ProdutoResponse execute(Long id) {
        // findById devolve um Optional<Product> (pode ou não existir).
        //  .map(...)       -> se existir, converte pra DTO;
        //  .orElseThrow(..)-> se estiver vazio, lança a exceção de domínio (vira 404 depois).
        // Usar Optional assim evita o famoso NullPointerException.
        return repository.findById(id)
                .map(ProdutoResponse::from)
                .orElseThrow(() -> new ProductNotFoundException(id));
    }
}
