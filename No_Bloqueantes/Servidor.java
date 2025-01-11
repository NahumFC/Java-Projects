import java.io.*;
import java.nio.*;
import java.nio.channels.*;
import java.net.*;
import java.util.*;

public class Servidor {
    private static final int FILAS_PRINC = 9;
    private static final int COLUMNAS_PRINC = 9;
    private static final int FILAS_INTER = 16;
    private static final int COLUMNAS_INTER = 16;
    private static final int FILAS_EXPERT = 16;
    private static final int COLUMNAS_EXPERT = 30;

    public static void main(String[] args) {
        int puerto = 9999;
        try {
            ServerSocketChannel servidor = ServerSocketChannel.open();
            servidor.configureBlocking(false);
            servidor.bind(new InetSocketAddress(puerto));

            Selector selector = Selector.open();
            servidor.register(selector, SelectionKey.OP_ACCEPT);

            Map<SocketChannel, Juego> juegos = new HashMap<>();
            System.out.println("Servidor iniciado en el puerto " + puerto);

            while (true) {
                selector.select();
                Iterator<SelectionKey> iterador = selector.selectedKeys().iterator();

                while (iterador.hasNext()) {
                    SelectionKey key = iterador.next();
                    iterador.remove();

                    if (key.isAcceptable()) {
                        SocketChannel cliente = servidor.accept();
                        cliente.configureBlocking(false);
                        cliente.register(selector, SelectionKey.OP_READ);
                        System.out.println("Cliente conectado desde: " + cliente.getRemoteAddress());
                    } else if (key.isReadable()) {
                        SocketChannel cliente = (SocketChannel) key.channel();
                        ByteBuffer buffer = ByteBuffer.allocate(1024);
                        int bytesLeidos = cliente.read(buffer);
                        if (bytesLeidos == -1) {
                            juegos.remove(cliente);
                            cliente.close();
                            System.out.println("Cliente desconectado.");
                            continue;
                        }

                        buffer.flip();
                        String[] comando = new String(buffer.array(), 0, buffer.limit()).split(",");

                        Juego juego = juegos.get(cliente);
                        String respuesta;

                        if (juego == null) {
                            String dificultad = comando[0].toLowerCase();
                            int filas, columnas, minas;
                            switch (dificultad) {
                                case "principiante":
                                    filas = FILAS_PRINC;
                                    columnas = COLUMNAS_PRINC;
                                    minas = 10;
                                    break;
                                case "intermedio":
                                    filas = FILAS_INTER;
                                    columnas = COLUMNAS_INTER;
                                    minas = 40;
                                    break;
                                case "experto":
                                    filas = FILAS_EXPERT;
                                    columnas = COLUMNAS_EXPERT;
                                    minas = 99;
                                    break;
                                default:
                                    respuesta = "ERROR,Dificultad no válida";
                                    cliente.write(ByteBuffer.wrap(respuesta.getBytes()));
                                    continue;
                            }

                            juego = new Juego(new Tablero(filas, columnas, minas));
                            juegos.put(cliente, juego);
                            respuesta = "OK," + filas + "," + columnas;
                        } else {
                            String accion = comando[0].toUpperCase();
                            int fila = Integer.parseInt(comando[1]);
                            int columna = comando[2].toUpperCase().charAt(0) - 'A';
                            
                            switch (accion) {
                                case "DESTAPAR":
                                    if (!juego.tablero.destaparCasilla(fila - 1, columna)) {
                                        juego.tablero.revelarTodasLasMinas();
                                        respuesta = "PERDISTE," + juego.tablero.obtenerTableroComoString();
                                        juegos.remove(cliente);
                                    } else if (juego.tablero.verificarVictoria()) {
                                        respuesta = "GANASTE," + juego.tablero.obtenerTableroComoString();
                                        juegos.remove(cliente);
                                    } else {
                                        respuesta = "CONTINUAR," + juego.tablero.obtenerTableroComoString();
                                    }
                                    break;
                                case "MARCAR":
                                    juego.tablero.marcarCasilla(fila - 1, columna);
                                    respuesta = "CONTINUAR," + juego.tablero.obtenerTableroComoString();
                                    break;
                                default:
                                    respuesta = "ERROR,Comando no válido";
                            }
                        }

                        cliente.write(ByteBuffer.wrap(respuesta.getBytes()));
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static class Juego {
        Tablero tablero;

        public Juego(Tablero tablero) {
            this.tablero = tablero;
        }
    }
}

