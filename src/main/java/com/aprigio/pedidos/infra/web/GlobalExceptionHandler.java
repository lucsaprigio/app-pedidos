package com.aprigio.pedidos.infra.web;

import com.aprigio.pedidos.domain.exception.OrderNotFoundException;
import com.aprigio.pedidos.domain.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;
import java.util.stream.Collectors;

/*
 * @RestControllerAdvice -> um "interceptador global" de exceções para TODOS os controllers.
 * Em vez de encher cada método do controller de try/catch, você centraliza aqui o
 * tratamento de erros. É @ControllerAdvice + @ResponseBody (o retorno vira JSON).
 *
 * A ideia central: os use cases só LANÇAM exceções de negócio; esta classe é a única
 * que sabe transformar cada tipo de erro no status HTTP correto.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    // @ExceptionHandler(X.class) -> "sempre que QUALQUER controller lançar uma X, cai aqui".
    // Aqui: ProductNotFoundException -> vira uma resposta 404 com {"error": "mensagem"}.
    @ExceptionHandler(ProductNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleNotFound(
            ProductNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    // Captura o erro que o @Valid dispara quando o ProdutoRequest é inválido.
    // Transforma a lista de campos com erro num mapa {campo: mensagem} e devolve 400.
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> handleValidation(
            MethodArgumentNotValidException ex) {

        // getBindingResult().getFieldErrors() -> lista de todos os campos que falharam.
        // Collectors.toMap(chave, valor) -> monta o mapa: nome do campo -> mensagem de erro.
        Map<String, String> errors = ex.getBindingResult()
                .getFieldErrors()
                .stream()
                .collect(Collectors.toMap(
                        FieldError::getField,
                        FieldError::getDefaultMessage
                ));

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(Map.of("errors", errors));
    }

    @ExceptionHandler(OrderNotFoundException.class)
    public ResponseEntity<Map<String, String>> handleOrderNotFound(
            OrderNotFoundException ex) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(Map.of("error", ex.getMessage()));
    }

    @ExceptionHandler(IllegalStateException.class)
    public ResponseEntity<Map<String, String>> handleIllegalState(
            IllegalStateException ex) {
        return ResponseEntity
                .status(HttpStatus.UNPROCESSABLE_ENTITY)
                .body(Map.of("error", ex.getMessage()));
    }
}
