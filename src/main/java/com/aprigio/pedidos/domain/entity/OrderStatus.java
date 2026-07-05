package com.aprigio.pedidos.domain.entity;

// enum = conjunto fixo de valores possíveis para o status do pedido. É type-safe: o compilador
// só aceita um destes 4 valores (impossível setar "status" com uma string qualquer, como no TS
// seria fácil errar). No TypeScript o equivalente é: enum OrderStatus { PENDING, CONFIRMED, ... }
// ou um union type: type OrderStatus = 'PENDING' | 'CONFIRMED' | 'CANCELED' | 'DELIVERED'.
public enum OrderStatus {
    PENDING,
    CONFIRMED,
    CANCELED,
    DELIVERED
}
