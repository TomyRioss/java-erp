package com.techlab.excepciones;

public class StockInsuficienteException extends Exception {

    public StockInsuficienteException(String nombreProducto, int disponible, int pedido) {
        super("Stock insuficiente para '" + nombreProducto + "'. Disponible: " + disponible + ", pedido: " + pedido);
    }
}
