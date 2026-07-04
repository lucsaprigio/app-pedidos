package com.aprigio.pedidos.domain.repository;

import java.util.List;
import java.util.Optional;

import com.aprigio.pedidos.domain.entity.Product;

public interface ProdutoRepository {
    Product save (Product product);
    Optional<Product> findById(Long id);
    List<Product> findAll();
    void deleteById(Long id);
}
