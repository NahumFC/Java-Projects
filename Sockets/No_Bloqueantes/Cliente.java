import java.util.Iterator;
import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {
    public static void main(String[] args) {
        String host = "127.0.0.1";
        int puerto = 9999;

        try {
            SocketChannel cliente = SocketChannel.open();
            cliente.configureBlocking(false);

            Selector selector = Selector.open();
            cliente.register(selector, SelectionKey.OP_CONNECT);
            cliente.connect(new InetSocketAddress(host, puerto));

            Scanner scanner = new Scanner(System.in);
            boolean juegoIniciado = false;

            while (true) {
                selector.select();
                Iterator<SelectionKey> iterador = selector.selectedKeys().iterator();

                while (iterador.hasNext()) {
                    SelectionKey key = iterador.next();
                    iterador.remove();

                    if (key.isConnectable()) {
                        if (cliente.finishConnect()) {
                            System.out.println("Conexión establecida con el servidor.");
                            cliente.register(selector, SelectionKey.OP_WRITE);
                        }
                    } else if (key.isWritable()) {
                        if (!juegoIniciado) {
                            System.out.print("Elige dificultad (principiante/intermedio/experto): ");
                            String dificultad = scanner.nextLine();
                            cliente.write(ByteBuffer.wrap(dificultad.getBytes()));
                            cliente.register(selector, SelectionKey.OP_READ);
                        } else {
                            System.out.print("Acción (DESTAPAR o MARCAR): ");
                            String accion = scanner.nextLine();
                            System.out.print("Fila (número): ");
                            String fila = scanner.nextLine();
                            System.out.print("Columna (letra): ");
                            String columna = scanner.nextLine().toUpperCase();

                            String comando = accion + "," + fila + "," + columna;
                            cliente.write(ByteBuffer.wrap(comando.getBytes()));
                            cliente.register(selector, SelectionKey.OP_READ);
                        }
                    } else if (key.isReadable()) {
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        cliente.read(buffer);
                        buffer.flip();
                        String respuesta = new String(buffer.array(), 0, buffer.limit());

                        String[] partes = respuesta.split(",", 2);
                        String estado = partes[0];

                        switch (estado) {
                            case "OK":
                                juegoIniciado = true;
                                String[] dimensiones = partes[1].split(",");
                                System.out.println("Tablero de " + dimensiones[0] + "x" + dimensiones[1]);
                                break;
                            case "CONTINUAR":
                            case "GANASTE":
                            case "PERDISTE":
                                System.out.println(partes[1]);
                                if (estado.equals("GANASTE") || estado.equals("PERDISTE")) {
                                    System.out.println("Fin del juego.");
                                    return;
                                }
                                break;
                            default:
                                System.out.println("Error: " + partes[1]);
                        }

                        cliente.register(selector, SelectionKey.OP_WRITE);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
