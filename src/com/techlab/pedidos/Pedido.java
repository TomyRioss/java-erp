package com.techlab.pedidos;

import java.util.ArrayList;

public class Pedido {

    private static int contador = 1;

    private int id;
    private ArrayList<LineaPedido> lineas;

    public Pedido() {
        this.id = contador++;
        this.lineas = new ArrayList<>();
    }

    public void agregarLinea(LineaPedido linea) {
        lineas.add(linea);
    }

    public double calcularTotal() {
        double total = 0;
        for (LineaPedido l : lineas) {
            total += l.getSubtotal();
        }
        return total;
    }

    public int getId() { return id; }
    public ArrayList<LineaPedido> getLineas() { return lineas; }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("Pedido #%d\n", id));
        for (LineaPedido l : lineas) {
            sb.append(l.toString()).append("\n");
        }
        sb.append(String.format("  Total: $%.2f", calcularTotal()));
        return sb.toString();
    }
}
