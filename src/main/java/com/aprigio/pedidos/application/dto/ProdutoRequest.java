package com.aprigio.pedidos.application.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

/*
 * DTO (Data Transfer Object) de ENTRADA: representa o JSON que o cliente envia no
 * corpo do POST/PUT. Usamos um "record" (Java 14+): uma classe imutável que já vem
 * com construtor, getters (nome(), preco()...), equals/hashCode e toString prontos.
 *
 * Usar um DTO em vez da entidade Product na borda protege o domínio: o cliente não
 * consegue mandar campos que não deveria (ex.: id, createdAt).
 *
 * ANNOTATIONS DE VALIDAÇÃO (Bean Validation / jakarta.validation). Elas só são
 * DISPARADAS quando o parâmetro é anotado com @Valid no controller:
 *
 *  @NotBlank   -> String não pode ser null, vazia, nem só espaços.
 *  @NotNull    -> o campo não pode ser null (mas "" passaria — por isso String usa @NotBlank).
 *  @DecimalMin -> valor numérico mínimo (aqui, preço >= 0.01).
 *  @Min        -> valor inteiro mínimo (aqui, estoque >= 0).
 *
 * Se qualquer regra falhar, o Spring lança MethodArgumentNotValidException — que o
 * GlobalExceptionHandler captura e transforma em HTTP 400 com as mensagens abaixo.
 */
public record ProdutoRequest(

    @NotBlank(message = "O nome do produto é obrigatório")
    String nome,

    @NotNull(message = "Preço é obrigatório")
    @DecimalMin(value = "0.01", message = "O preço do produto deve ser maior que zero")
    BigDecimal preco,

    @NotNull(message = "Estoque é obrigatório")
    @Min(value = 0, message = "O estoque do produto não pode ser negativo")
    Integer estoque
) {}
