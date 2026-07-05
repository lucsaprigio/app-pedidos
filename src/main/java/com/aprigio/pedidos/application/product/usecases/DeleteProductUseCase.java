package com.aprigio.pedidos.application.product.usecases;

import com.aprigio.pedidos.domain.exception.ProductNotFoundException;
import com.aprigio.pedidos.domain.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

// @Component -> bean gerenciado; @RequiredArgsConstructor -> injeção do repository via construtor.
@Component
@RequiredArgsConstructor
public class DeleteProductUseCase {

    private final ProdutoRepository repository;

    public void execute(Long id) {
        // Confere se existe ANTES de deletar: assim conseguimos devolver um 404 claro
        // em vez de simplesmente não fazer nada quando o id não existe.
        if (!repository.existsById(id)) {
            throw new ProductNotFoundException(id);
        }

        repository.deleteById(id);
    }
}
