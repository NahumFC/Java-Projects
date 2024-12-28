import java.io.*;
import java.net.*;
import java.util.Properties;

public class Servidor {
    private static final int FILAS_PRINC = 9;
    private static final int COLUMNAS_PRINC = 9;
    private static final int FILAS_INTER = 16;
    private static final int COLUMNAS_INTER = 16;
    private static final int FILAS_EXPERT = 16;
    private static final int COLUMNAS_EXPERT = 30;
    private static final String RECORDS_FILE = "records.txt";

    // Se leen los récords desde el archivo
    public static Properties leerRecords() throws IOException {
        Properties records = new Properties();
        File file = new File(RECORDS_FILE);
        if (!file.exists()) {
            file.createNewFile(); // Si no existe el archivo, lo creamos
        }
        FileInputStream fis = new FileInputStream(file);
        records.load(fis);
        fis.close();
        return records;
    }

    // Se guardan los récords en el archivo
    public static void guardarRecords(Properties records) throws IOException {
        FileOutputStream fos = new FileOutputStream(RECORDS_FILE);
        records.store(fos, "Récords de Buscaminas");
        fos.close();
    }

    // Para actualizar el récord si el tiempo es menor
    public static void actualizarRecord(String nivel, long tiempo) throws IOException {
        Properties records = leerRecords();
        String recordStr = records.getProperty(nivel, String.valueOf(Long.MAX_VALUE));
        long recordActual = Long.parseLong(recordStr);

        if (tiempo < recordActual) {
            records.setProperty(nivel, String.valueOf(tiempo));
            guardarRecords(records);
            System.out.println("Nuevo récord para el nivel " + nivel + ": " + tiempo + " ms");
        }
    }

    public static void main(String[] args) {
        try {
            BufferedReader lector = new BufferedReader(new InputStreamReader(System.in));
            System.out.print("Ingrese el puerto del servidor: ");
            int puerto = Integer.parseInt(lector.readLine());

            // Creamos el ServerSocket que escucha en el puerto especificado
            ServerSocket servidorSocket = new ServerSocket(puerto);
            System.out.println("Servidor escuchando en el puerto: " + puerto);

            // Aceptamos una conexión y la manejamos como una partida individual
            Socket clienteSocket = servidorSocket.accept();
            System.out.println("Jugador conectado.");

            DataInputStream entrada = new DataInputStream(clienteSocket.getInputStream());
            DataOutputStream salida = new DataOutputStream(clienteSocket.getOutputStream());
            ObjectOutputStream objetoSalida = new ObjectOutputStream(clienteSocket.getOutputStream());

            try {
                // Recibimos la dificultad que escogió el jugador y de esta forma ajustamos el tamaño del tablero dependiendo de su respuesta
                String dificultad = entrada.readUTF();
                System.out.println("Dificultad recibida: " + dificultad);

                int[] tamanioTablero = obtenerTamanioTablero(dificultad);
                int numeroMinas = obtenerNumeroMinas(dificultad);

                if (tamanioTablero[0] <= 0 || tamanioTablero[1] <= 0) {
                    System.err.println("Error: Tamaño del tablero inválido.");
                    clienteSocket.close();
                    return;
                }

                Tablero tablero = new Tablero(tamanioTablero[0], tamanioTablero[1], numeroMinas);
                tablero.generarTablero();

                // Enviamos el tamaño del tablero al cliente
                salida.writeInt(tamanioTablero[0]);
                salida.writeInt(tamanioTablero[1]);
                salida.flush();
                System.out.println("Tamaño del tablero enviado.");

                // Esperar confirmación del cliente
                String confirmacion = entrada.readUTF();
                if (!"OK".equals(confirmacion)) {
                    System.err.println("Error: No se recibió confirmación del cliente.");
                    clienteSocket.close();
                    return;
                }

                // Enviamos el tablero inicial al cliente
                objetoSalida.writeObject(tablero.obtenerTablero());
                objetoSalida.flush();
                salida.writeInt(tablero.obtenerMarcasRestantes());
                salida.flush();
                System.out.println("Tablero inicial enviado al cliente.");

                long tiempoInicio = System.currentTimeMillis();
                boolean juegoEnCurso = true;

                // Ciclo para manejar las acciones del cliente durante el juego
                while (juegoEnCurso) {
                    System.out.println("Esperando acción del cliente...");
                    String accion = entrada.readUTF();
                    int fila = entrada.readInt();
                    int columna = entrada.readInt();
                    System.out.println("Acción recibida: " + accion + ", Fila: " + fila + ", Columna: " + columna);

                    if (accion.equals("destapar")) {
                        if (!tablero.destaparCasilla(fila, columna)) {
                            salida.writeUTF("PERDISTE");
                            salida.flush();
                            // Revelamos todas las minas y enviamos el tablero completo
                            tablero.revelarTodasLasMinas();
                            objetoSalida.writeObject(tablero.obtenerTablero());
                            objetoSalida.flush();
                            salida.writeInt(tablero.obtenerMarcasRestantes());
                            salida.flush();
                            System.out.println("Cliente perdió. Tablero final enviado.");
                            juegoEnCurso = false;
                        } else if (tablero.verificarVictoria()) {
                            salida.writeUTF("GANASTE");
                            salida.flush();
                            objetoSalida.writeObject(tablero.obtenerTablero());
                            objetoSalida.flush();
                            salida.writeInt(tablero.obtenerMarcasRestantes());
                            salida.flush();
                            System.out.println("Cliente ganó. Tablero final enviado.");
                            juegoEnCurso = false;
                        } else {
                            salida.writeUTF("CONTINUAR");
                            salida.flush();
                            objetoSalida.writeObject(tablero.obtenerTablero());
                            salida.writeInt(tablero.obtenerMarcasRestantes());
                            salida.flush();
                            System.out.println("Acción procesada. Tablero actualizado enviado.");
                        }
                    } else if (accion.equals("marcar")) {
                        tablero.marcarCasilla(fila, columna);
                        if (tablero.verificarVictoria()) {
                            salida.writeUTF("GANASTE");
                            salida.flush();
                            objetoSalida.writeObject(tablero.obtenerTablero());
                            objetoSalida.flush();
                            salida.writeInt(tablero.obtenerMarcasRestantes());
                            salida.flush();
                            System.out.println("Cliente ganó al marcar todas las minas. Tablero final enviado.");
                            juegoEnCurso = false;

                            // Si gana se calcula el tiempo total
                            long tiempoFin = System.currentTimeMillis();
                            long duracionPartida = tiempoFin - tiempoInicio;

                            // Conversión de tiempo a minutos y segundos
                            long minutos = (duracionPartida / 1000) / 60;
                            long segundos = (duracionPartida / 1000) % 60;

                            System.out.println("Duración de la partida: " + minutos + " minutos y " + segundos + " segundos ");

                            // Aquí especificamos el nivel, se puede cambiar segun el nivel en el que esté jugando el cliente
                            String nivel = "principiante"; // Podemos cambiarlo a intermedio o experto

                            // Intentamos actualizar el récord
                            try {
                                Properties records = leerRecords();
                                String recordStr = records.getProperty(nivel, String.valueOf(Long.MAX_VALUE));
                                long recordActual = Long.parseLong(recordStr);

                                // Si el tiempo actual es menor, actualizamos el récord
                                if (duracionPartida < recordActual) {
                                    actualizarRecord(nivel, duracionPartida);
                                }

                                // Enviamos al cliente el tiempo total y el récord
                                salida.writeLong(duracionPartida); // Enviar tiempo de la partida
                                salida.writeLong(recordActual); // Enviar récord actual
                                salida.flush();

                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                            juegoEnCurso = false;


                        } else {
                            salida.writeUTF("CONTINUAR");
                            salida.flush();
                            objetoSalida.writeObject(tablero.obtenerTablero());
                            salida.writeInt(tablero.obtenerMarcasRestantes());
                            salida.flush();
                            System.out.println("Casilla marcada. Tablero actualizado enviado.");
                        }
                    } else if (accion.equals("desmarcar")) {
                        tablero.desmarcarCasilla(fila, columna);
                        salida.writeUTF("CONTINUAR");
                        salida.flush();
                        objetoSalida.writeObject(tablero.obtenerTablero());
                        salida.writeInt(tablero.obtenerMarcasRestantes());
                        salida.flush();
                        System.out.println("Casilla desmarcada. Tablero actualizado enviado.");
                    } else {
                        System.err.println("Acción desconocida recibida: " + accion);
                        salida.writeUTF("ERROR");
                        salida.flush();
                    }
                }

                long tiempoFin = System.currentTimeMillis();
                System.out.println("Duración de la partida: " + (tiempoFin - tiempoInicio) + " ms");

            } catch (IOException e) {
                System.err.println("Error durante la comunicación con el cliente: " + e.getMessage());
            } finally {
                try {
                    // Cerrar todos los recursos
                    entrada.close();
                    salida.close();
                    objetoSalida.close();
                    clienteSocket.close();
                    servidorSocket.close();  // Cerrar el servidor después de una partida
                } catch (IOException e) {
                    System.err.println("Error al cerrar recursos: " + e.getMessage());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static int[] obtenerTamanioTablero(String dificultad) {
        switch (dificultad.toLowerCase()) {
            case "principiante": return new int[]{FILAS_PRINC, COLUMNAS_PRINC};
            case "intermedio": return new int[]{FILAS_INTER, COLUMNAS_INTER};
            case "experto": return new int[]{FILAS_EXPERT, COLUMNAS_EXPERT};
            default: return new int[]{FILAS_PRINC, COLUMNAS_PRINC};
        }
    }

    private static int obtenerNumeroMinas(String dificultad) {
        switch (dificultad.toLowerCase()) {
            case "principiante": return 10;
            case "intermedio": return 40;
            case "experto": return 99;
            default: return 5;
        }
    }
}