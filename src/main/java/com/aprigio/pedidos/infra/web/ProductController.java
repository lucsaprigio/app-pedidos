package com.aprigio.pedidos.infra.web;

import com.aprigio.pedidos.application.dto.ProdutoRequest;
import com.aprigio.pedidos.application.dto.ProdutoResponse;
import com.aprigio.pedidos.application.usecases.*;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/*
 * @RestController -> é @Controller + @ResponseBody juntos. Marca a classe como bean E
 *                    diz que o RETORNO de cada método vira direto o CORPO da resposta HTTP
 *                    (o Spring serializa o objeto pra JSON automaticamente). Sem o @ResponseBody
 *                    embutido, o Spring tentaria interpretar o retorno como nome de uma página.
 *
 * @RequestMapping("/api/v1/products") -> prefixo de URL para TODOS os endpoints desta classe.
 *
 * @RequiredArgsConstructor -> injeta os 5 use cases via construtor (mesma DI de antes).
 *                             O controller só ORQUESTRA: recebe o request e chama o use case.
 */
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {

    // Beans injetados pelo Spring. O controller não contém regra de negócio — ele delega.
    private final CreateProductUseCase createProduct;
    private final FindByProductUseCase findProductById;
    private final FindAllProductsUseCase findAllProducts;
    private final UpdateProductUseCase updateProduct;
    private final DeleteProductUseCase deleteProduct;

    // @GetMapping sem path -> responde GET em /api/v1/products (o prefixo da classe).
    // ResponseEntity<T> -> permite controlar o status HTTP + corpo. .ok() = 200.
    @GetMapping
    public ResponseEntity<List<ProdutoResponse>> findAll(){
        return ResponseEntity.ok(findAllProducts.execute());
    }

    // @GetMapping("/{id}") -> GET em /api/v1/products/5, por exemplo.
    // @PathVariable -> pega o "{id}" da URL e joga no parâmetro Long id.
    @GetMapping("/{id}")
    public ResponseEntity<ProdutoResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(findProductById.execute(id));
    }

    // @PostMapping -> POST em /api/v1/products.
    // @RequestBody -> converte o JSON do corpo da requisição num ProdutoRequest.
    // @Valid       -> DISPARA as validações (@NotBlank, @Min...) do ProdutoRequest ANTES
    //                 de entrar no método. Se falhar, nem executa o corpo: vira 400.
    // .status(CREATED) -> retorna 201 (padrão REST para "recurso criado").
    @PostMapping
    public ResponseEntity<ProdutoResponse> create(
            @Valid @RequestBody ProdutoRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(createProduct.execute(request));
    }

    // @PutMapping -> PUT em /api/v1/products/{id}. Junta @PathVariable (id da URL)
    // com @Valid @RequestBody (corpo validado). Retorna 200 com o produto atualizado.
    @PutMapping("/{id}")
    public ResponseEntity<ProdutoResponse> update(
            @PathVariable Long id,
            @Valid @RequestBody ProdutoRequest request) {
        return ResponseEntity.ok(updateProduct.execute(id, request));
    }

    // @DeleteMapping -> DELETE em /api/v1/products/{id}.
    // .noContent() -> 204 (sucesso, sem corpo de resposta) — padrão REST para delete.
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        deleteProduct.execute(id);
        return ResponseEntity.noContent().build();
    }
}
