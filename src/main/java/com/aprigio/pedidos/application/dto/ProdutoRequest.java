package com.aprigio.pedidos.application.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProdutoRequest(
    @NotBlank(message = "O nome do produto é obrigatório")
    String nome;

    @NotNull
    @DecimalMin(value = "0.01", message = "O preço do produto deve ser maior que zero")
    BigDecimal preco;


    @NotNull
    @Min(value = 0, message = "O estoque do produto não pode ser negativo")
    Integer estoque;
) {}
