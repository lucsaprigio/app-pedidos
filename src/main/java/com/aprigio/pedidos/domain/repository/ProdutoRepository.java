package com.aprigio.pedidos.domain.repository;

import java.util.List;
import java.util.Optional;

import com.aprigio.pedidos.domain.entity.Product;

/*
 * Esta é uma INTERFACE do domínio — um "contrato" que diz O QUE o sistema precisa
 * fazer com produtos (salvar, buscar, deletar), mas NÃO diz COMO nem qual tecnologia.
 *
 * É o conceito de "Inversão de Dependência" (o D do SOLID): o domínio e os use cases
 * dependem desta interface, e não do Spring Data / JPA. Quem implementa de verdade é a
 * classe ProductRepositoryImpl na camada infra. Assim, se um dia trocar Postgres por
 * Mongo, você só cria outra implementação — o domínio nem fica sabendo.
 *
 * Repare que aqui NÃO tem nenhuma annotation: o domínio é "puro", sem dependência de framework.
 */
public interface ProdutoRepository {
    Product save (Product product);
    Optional<Product> findById(Long id);
    List<Product> findAll();
    boolean existsById(Long id);
    void deleteById(Long id);
}
