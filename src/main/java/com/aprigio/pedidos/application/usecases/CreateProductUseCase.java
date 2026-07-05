package com.aprigio.pedidos.application.usecases;

import com.aprigio.pedidos.application.dto.ProdutoRequest;
import com.aprigio.pedidos.application.dto.ProdutoResponse;
import com.aprigio.pedidos.domain.entity.Product;
import com.aprigio.pedidos.domain.repository.ProdutoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/*
 * ==== O QUE É @Component E COMO O SPRING GERENCIA ISSO (sua dúvida) ====
 *
 * @Component marca a classe como um "bean": um objeto cujo CICLO DE VIDA é controlado
 * pelo Spring, e não por você. Você nunca dá "new CreateProductUseCase(...)" à mão.
 *
 * Como funciona por baixo:
 *  1. Ao subir a app, o @ComponentScan (que veio no @SpringBootApplication) varre os
 *     pacotes e encontra esta classe por causa do @Component.
 *  2. O Spring então CRIA uma única instância dela (por padrão, é "singleton" — um só
 *     objeto compartilhado por toda a aplicação) e guarda no "Application Context",
 *     que é o container de injeção de dependência (IoC container).
 *  3. Quando outra classe (o ProductController) precisar de um CreateProductUseCase,
 *     o Spring ENTREGA essa mesma instância automaticamente. Isso é "injeção de dependência".
 *
 * @Service, @Repository e @RestController são "especializações" de @Component: fazem a
 * mesma coisa (viram beans), mas com nome semântico. Um use case ficaria igualmente bem
 * com @Service; aqui optaram por @Component genérico.
 *
 * ==== @RequiredArgsConstructor (Lombok) + INJEÇÃO POR CONSTRUTOR ====
 *
 * Ele gera um construtor com todos os campos "final". Como esta classe tem
 * "private final ProdutoRepository repository", o Lombok cria:
 *
 *     public CreateProductUseCase(ProdutoRepository repository) { this.repository = repository; }
 *
 * O Spring vê esse construtor e, na hora de criar o bean, INJETA automaticamente o bean
 * de ProdutoRepository que ele já tem no container. Isso é "injeção por construtor" —
 * a forma recomendada (deixa os campos final/imutáveis e facilita testes).
 */
@Component
@RequiredArgsConstructor
public class CreateProductUseCase {

    // "final" = obrigatório e imutável. O Spring injeta via construtor (gerado pelo Lombok).
    private final ProdutoRepository repository;

    // Um use case tem UMA responsabilidade e um método público de entrada (execute).
    public ProdutoResponse execute(ProdutoRequest request) {
        // Monta a entidade a partir do DTO usando o builder gerado pelo Lombok (@Builder).
        Product product = Product.builder()
                .nome(request.nome())
                .preco(request.preco())
                .estoque(request.estoque())
                .build();

        // Salva no banco (retorna o Product já com id gerado) e converte de volta pra DTO de saída.
        return ProdutoResponse.from(repository.save(product));
    }
}
