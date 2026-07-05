package com.aprigio.pedidos.application.product.usecases;

import com.aprigio.pedidos.application.product.dto.ProdutoResponse;
import com.aprigio.pedidos.domain.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

// @Component -> bean gerenciado; @RequiredArgsConstructor -> injeção do repository via construtor.
@Component
@RequiredArgsConstructor
public class FindAllProductsUseCase {

    private final ProdutoRepository repository;

    public List<ProdutoResponse> execute() {
        // Stream API: pega a lista de entidades e transforma numa lista de DTOs.
        //  .stream()               -> abre um "fluxo" sobre a lista;
        //  .map(ProdutoResponse::from) -> converte cada Product em ProdutoResponse;
        //  .toList()               -> junta tudo numa nova List imutável.
        return repository.findAll()
                .stream()
                .map(ProdutoResponse::from)
                .toList();
    }
}
