import com.techlab.excepciones.StockInsuficienteException;
import com.techlab.pedidos.LineaPedido;
import com.techlab.pedidos.Pedido;
import com.techlab.productos.Bebida;
import com.techlab.productos.Comida;
import com.techlab.productos.Producto;
import com.techlab.servicios.PedidoService;
import com.techlab.servicios.ProductoService;

import java.util.Scanner;

public class Main {

    static Scanner sc = new Scanner(System.in);
    static ProductoService productoService = new ProductoService();
    static PedidoService pedidoService = new PedidoService();

    public static void main(String[] args) {
        int opcion = 0;

        while (opcion != 7) {
            mostrarMenu();

            try {
                opcion = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Ingrese un número válido.");
                continue;
            }

            switch (opcion) {
                case 1: agregarProducto(); break;
                case 2: listarProductos(); break;
                case 3: buscarActualizarProducto(); break;
                case 4: eliminarProducto(); break;
                case 5: crearPedido(); break;
                case 6: listarPedidos(); break;
                case 7: System.out.println("Hasta luego."); break;
                default: System.out.println("Opción no válida.");
            }
        }
    }

    static void mostrarMenu() {
        System.out.println("\n=== SISTEMA DE GESTIÓN - TECHLAB ===");
        System.out.println("1) Agregar producto");
        System.out.println("2) Listar productos");
        System.out.println("3) Buscar/Actualizar producto");
        System.out.println("4) Eliminar producto");
        System.out.println("5) Crear un pedido");
        System.out.println("6) Listar pedidos");
        System.out.println("7) Salir");
        System.out.print("Elija una opción: ");
    }

    static void agregarProducto() {
        System.out.println("\n-- Agregar producto --");
        System.out.println("Tipo: 1) Genérico  2) Bebida  3) Comida");
        System.out.print("Tipo: ");

        int tipo = 1;
        try {
            tipo = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("Tipo no válido, se usará Genérico.");
        }

        System.out.print("Nombre: ");
        String nombre = sc.nextLine().trim();

        double precio = 0;
        try {
            System.out.print("Precio: ");
            precio = Double.parseDouble(sc.nextLine().trim());
            if (precio < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("Precio inválido.");
            return;
        }

        int stock = 0;
        try {
            System.out.print("Stock: ");
            stock = Integer.parseInt(sc.nextLine().trim());
            if (stock < 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("Stock inválido.");
            return;
        }

        Producto p;

        if (tipo == 2) {
            double volumen = 0;
            try {
                System.out.print("Volumen en litros: ");
                volumen = Double.parseDouble(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("Volumen inválido.");
                return;
            }
            p = new Bebida(nombre, precio, stock, volumen);
        } else if (tipo == 3) {
            System.out.print("Fecha de vencimiento (ej: 12/2025): ");
            String fecha = sc.nextLine().trim();
            p = new Comida(nombre, precio, stock, fecha);
        } else {
            p = new Producto(nombre, precio, stock);
        }

        productoService.agregar(p);
        System.out.println("Producto agregado: " + p);
    }

    static void listarProductos() {
        System.out.println("\n-- Productos registrados --");
        if (productoService.estaVacio()) {
            System.out.println("No hay productos cargados.");
            return;
        }
        for (Producto p : productoService.listar()) {
            System.out.println(p);
        }
    }

    static void buscarActualizarProducto() {
        System.out.println("\n-- Buscar producto --");
        System.out.print("Ingrese ID o nombre: ");
        String entrada = sc.nextLine().trim();

        Producto p = null;
        try {
            int id = Integer.parseInt(entrada);
            p = productoService.buscarPorId(id);
        } catch (NumberFormatException e) {
            p = productoService.buscarPorNombre(entrada);
        }

        if (p == null) {
            System.out.println("Producto no encontrado.");
            return;
        }

        System.out.println("Encontrado: " + p);
        System.out.print("¿Desea actualizar? (s/n): ");
        String resp = sc.nextLine().trim();

        if (!resp.equalsIgnoreCase("s")) return;

        System.out.print("Nuevo precio (Enter para mantener $" + p.getPrecio() + "): ");
        String entradaPrecio = sc.nextLine().trim();
        if (!entradaPrecio.isEmpty()) {
            try {
                double nuevoPrecio = Double.parseDouble(entradaPrecio);
                if (nuevoPrecio < 0) throw new NumberFormatException();
                p.setPrecio(nuevoPrecio);
            } catch (NumberFormatException e) {
                System.out.println("Precio inválido, no se modificó.");
            }
        }

        System.out.print("Nuevo stock (Enter para mantener " + p.getStock() + "): ");
        String entradaStock = sc.nextLine().trim();
        if (!entradaStock.isEmpty()) {
            try {
                int nuevoStock = Integer.parseInt(entradaStock);
                if (nuevoStock < 0) throw new NumberFormatException();
                p.setStock(nuevoStock);
            } catch (NumberFormatException e) {
                System.out.println("Stock inválido, no se modificó.");
            }
        }

        System.out.println("Actualizado: " + p);
    }

    static void eliminarProducto() {
        System.out.println("\n-- Eliminar producto --");
        if (productoService.estaVacio()) {
            System.out.println("No hay productos cargados.");
            return;
        }

        listarProductos();
        System.out.print("ID del producto a eliminar: ");

        int id;
        try {
            id = Integer.parseInt(sc.nextLine().trim());
        } catch (NumberFormatException e) {
            System.out.println("ID inválido.");
            return;
        }

        Producto p = productoService.buscarPorId(id);
        if (p == null) {
            System.out.println("No existe un producto con ese ID.");
            return;
        }

        System.out.print("¿Confirma eliminar '" + p.getNombre() + "'? (s/n): ");
        String conf = sc.nextLine().trim();
        if (conf.equalsIgnoreCase("s")) {
            productoService.eliminar(id);
            System.out.println("Producto eliminado.");
        } else {
            System.out.println("Operación cancelada.");
        }
    }

    static void crearPedido() {
        System.out.println("\n-- Crear pedido --");
        if (productoService.estaVacio()) {
            System.out.println("No hay productos disponibles.");
            return;
        }

        listarProductos();

        int cantidad = 0;
        try {
            System.out.print("¿Cuántos productos distintos va a agregar al pedido? ");
            cantidad = Integer.parseInt(sc.nextLine().trim());
            if (cantidad <= 0) throw new NumberFormatException();
        } catch (NumberFormatException e) {
            System.out.println("Cantidad inválida.");
            return;
        }

        Pedido pedido = new Pedido();

        for (int i = 0; i < cantidad; i++) {
            int idProducto;
            try {
                System.out.print("ID del producto #" + (i + 1) + ": ");
                idProducto = Integer.parseInt(sc.nextLine().trim());
            } catch (NumberFormatException e) {
                System.out.println("ID inválido, se omite esta línea.");
                continue;
            }

            Producto p = productoService.buscarPorId(idProducto);
            if (p == null) {
                System.out.println("No existe ese producto, se omite.");
                continue;
            }

            int cant;
            try {
                System.out.print("Cantidad de '" + p.getNombre() + "': ");
                cant = Integer.parseInt(sc.nextLine().trim());
                if (cant <= 0) throw new NumberFormatException();
            } catch (NumberFormatException e) {
                System.out.println("Cantidad inválida, se omite.");
                continue;
            }

            try {
                if (cant > p.getStock()) {
                    throw new StockInsuficienteException(p.getNombre(), p.getStock(), cant);
                }
                pedido.agregarLinea(new LineaPedido(p, cant));
            } catch (StockInsuficienteException e) {
                System.out.println("Error: " + e.getMessage());
            }
        }

        if (pedido.getLineas().isEmpty()) {
            System.out.println("El pedido quedó vacío, no se guardó.");
            return;
        }

        System.out.println("\nResumen del pedido:");
        System.out.println(pedido);
        System.out.print("¿Confirmar pedido? (s/n): ");
        String conf = sc.nextLine().trim();

        if (conf.equalsIgnoreCase("s")) {
            for (LineaPedido l : pedido.getLineas()) {
                l.getProducto().setStock(l.getProducto().getStock() - l.getCantidad());
            }
            pedidoService.agregar(pedido);
            System.out.println("Pedido confirmado. Total: $" + String.format("%.2f", pedido.calcularTotal()));
        } else {
            System.out.println("Pedido cancelado.");
        }
    }

    static void listarPedidos() {
        System.out.println("\n-- Pedidos realizados --");
        if (pedidoService.estaVacio()) {
            System.out.println("No hay pedidos registrados.");
            return;
        }
        for (Pedido p : pedidoService.listar()) {
            System.out.println(p);
            System.out.println("--------------------");
        }
    }
}
