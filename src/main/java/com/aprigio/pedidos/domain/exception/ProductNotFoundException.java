package com.aprigio.pedidos.domain.exception;

/*
 * Exceção de domínio própria. Herda de RuntimeException (exceção "não checada"),
 * então não precisa de try/catch nem de "throws" na assinatura dos métodos.
 *
 * A ideia: quando o produto não existe, o use case simplesmente LANÇA esta exceção.
 * Quem transforma isso numa resposta HTTP 404 é o GlobalExceptionHandler — assim as
 * regras de negócio não precisam saber nada sobre HTTP/status code.
 */
public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(Long id) {
        super("Produto não encontrado: " + id);
    }
}
