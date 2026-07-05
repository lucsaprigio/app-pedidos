package com.aprigio.pedidos.domain.exception;

public class OrderNotFoundException extends RuntimeException {
    public OrderNotFoundException(Long id) {
        super("Pedido não encontrado: " + id);
    }
}
