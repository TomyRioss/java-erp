package com.techlab.servicios;

import com.techlab.pedidos.Pedido;
import java.util.ArrayList;

public class PedidoService {

    private ArrayList<Pedido> pedidos = new ArrayList<>();

    public void agregar(Pedido p) {
        pedidos.add(p);
    }

    public ArrayList<Pedido> listar() {
        return pedidos;
    }

    public boolean estaVacio() {
        return pedidos.isEmpty();
    }
}
