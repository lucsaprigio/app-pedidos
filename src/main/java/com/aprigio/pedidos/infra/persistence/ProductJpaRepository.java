package com.aprigio.pedidos.infra.persistence;

import com.aprigio.pedidos.domain.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;

/*
 * Aqui acontece a "mágica" do Spring Data JPA. Você só declara a INTERFACE estendendo
 * JpaRepository<Entidade, TipoDoId> e NÃO escreve nenhuma implementação.
 *
 * Em tempo de execução, o Spring GERA sozinho uma classe concreta com os métodos prontos:
 * save(), findById(), findAll(), existsById(), deleteById(), count()... tudo já com o
 * SQL por baixo. Por isso este arquivo fica "vazio" — o corpo {} não precisa de nada.
 *
 * Repare que isto é uma interface de INFRAESTRUTURA (acoplada ao Spring/JPA), separada da
 * interface ProdutoRepository do domínio (que é pura). Quem faz a ponte entre as duas é a
 * classe ProductRepositoryImpl.
 */
public interface ProductJpaRepository extends JpaRepository<Product, Long> {}
