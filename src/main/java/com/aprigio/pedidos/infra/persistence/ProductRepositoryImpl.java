package com.aprigio.pedidos.infra.persistence;

import com.aprigio.pedidos.domain.entity.Product;
import com.aprigio.pedidos.domain.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/*
 * Esta classe é o "ADAPTADOR" que liga o mundo do domínio ao mundo do Spring Data.
 * Ela IMPLEMENTA a interface pura do domínio (ProdutoRepository) e, por dentro, delega
 * as chamadas para o ProductJpaRepository (a interface mágica do Spring Data).
 *
 * @Repository -> é uma especialização de @Component (então também vira um bean). Além
 *                de marcar o bean, ela habilita a "tradução de exceções": erros de acesso
 *                a dados de baixo nível viram exceções padronizadas do Spring.
 *
 * @RequiredArgsConstructor -> injeta o ProductJpaRepository via construtor.
 *
 * É graças a esta classe que os use cases dependem só da interface do domínio: quando eles
 * pedem um "ProdutoRepository", o Spring entrega esta implementação aqui.
 */
@Repository
@RequiredArgsConstructor
public class ProductRepositoryImpl implements ProdutoRepository {

    // O bean gerado automaticamente pelo Spring Data (ver ProductJpaRepository).
    private final ProductJpaRepository jpa;

    @Override
    public Product save(Product product) {
        return jpa.save(product);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return jpa.findById(id);
    }

    @Override
    public List<Product> findAll() {
        return jpa.findAll();
    }

    @Override
    public boolean existsById(Long id) {
        return jpa.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        jpa.deleteById(id);
    }
}
