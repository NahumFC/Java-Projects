import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {

    public static void main(String[] args) {
        try {
            Scanner scanner = new Scanner(System.in);

            // Solicitamos la dirección del servidor
            System.out.print("Ingrese la dirección del servidor: ");
            String direccionServidor = scanner.nextLine();

            // Solicitamos el puerto del servidor
            System.out.print("Ingrese el puerto del servidor: ");
            int puertoServidor = Integer.parseInt(scanner.nextLine());

            // Conectar al servidor
            Socket socket = new Socket(direccionServidor, puertoServidor);
            DataInputStream entrada = new DataInputStream(socket.getInputStream());
            DataOutputStream salida = new DataOutputStream(socket.getOutputStream());
            ObjectInputStream objetoEntrada = new ObjectInputStream(socket.getInputStream());

            // Aquí el jugador nos dice que tan ficil quiere el juego
            System.out.print("Ingrese el nivel de dificultad (principiante, intermedio, experto): ");
            String dificultad = scanner.nextLine();
            salida.writeUTF(dificultad);
            salida.flush();

            // Recibimos el tamaño del tablero
            int filasTablero = entrada.readInt();
            int columnasTablero = entrada.readInt();
            System.out.println("Tamaño del tablero recibido: " + filasTablero + "x" + columnasTablero);

            if (filasTablero <= 0 || columnasTablero <= 0) {
                System.err.println("Error: el tamaño del tablero recibido es inválido: " + filasTablero + "x" + columnasTablero);
                socket.close();
                return;
            }

            // Enviar confirmación al servidor... lo puse porque tuve un pequeño error xd que no sabía en que momento sucedía y con el "OK" se que todo a salido bien hasta ahora 
            salida.writeUTF("OK");
            salida.flush();

            // Recibimos el tablero del servidor
            int[][] tablero = (int[][]) objetoEntrada.readObject();
            System.out.println("Tablero inicial recibido.");

            boolean juegoEnCurso = true;
            while (juegoEnCurso) {

                // Mostramos el tablero al jugador con coordenadas y marcas restantes
                mostrarTablero(tablero, filasTablero, columnasTablero);
                int marcasRestantes = entrada.readInt();
                System.out.println("Marcas restantes: " + marcasRestantes);

                // Solicitamos la acción del jugador
                System.out.print("Ingrese acción (destapar/marcar/desmarcar): ");
                String accion = scanner.nextLine();
                System.out.print("Ingrese fila (1-" + filasTablero + "): ");
                int fila = Integer.parseInt(scanner.nextLine()) - 1;
                System.out.print("Ingrese columna (A-" + (char) ('A' + columnasTablero - 1) + "): ");
                int columna = scanner.nextLine().toUpperCase().charAt(0) - 'A';

                // Enviamos la acción al servidor
                salida.writeUTF(accion);
                salida.writeInt(fila);
                salida.writeInt(columna);
                salida.flush();

                // Recibimos la respuesta del servidor
                String resultado = entrada.readUTF();
                System.out.println("Resultado recibido: " + resultado);

                // Aquí checamos el estado del juego y se lo mostramos al jugador, si ya ganó, ya perdió o simplemente continúa jugando
                if (resultado.equals("PERDISTE")) {
                    System.out.println("Perdiste, la mina explotó.");
                    tablero = (int[][]) objetoEntrada.readObject();
                    mostrarTablero(tablero, filasTablero, columnasTablero);
                    juegoEnCurso = false;
                    break;
                } else if (resultado.equals("GANASTE")) {
                    System.out.println("¡Felicidades, ganaste!");
                    tablero = (int[][]) objetoEntrada.readObject();
                    mostrarTablero(tablero, filasTablero, columnasTablero);
                    juegoEnCurso = false;
                    // Recibir el tiempo de la partida y el récord desde el servidor
                    long tiempoPartida = entrada.readLong();
                    //System.out.println("Tiempo de la partida: " + tiempoPartida + " ms");
                    break;

                } else if (resultado.equals("CONTINUAR")) {
                    tablero = (int[][]) objetoEntrada.readObject();
                }
            }
            // Cerramos la conexión al finalizar el juego
            entrada.close();
            salida.close();
            objetoEntrada.close();
            socket.close();
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    // Esta parte sirve para mostrar el tablero con sus simbolos (minas, casillas destapadas y libres, casillas con numeros), me pareció mas facil usar numeros y letras para identificar las filas y las columnas sin falla.s
    private static void mostrarTablero(int[][] tablero, int filas, int columnas) {
        // Mostramos las letras de las columnas
        System.out.print("   ");
        for (int i = 0; i < columnas; i++) {
            System.out.print((char) ('A' + i) + " ");
        }
        System.out.println();

        // Mostramos cada fila con sus coordenadas
        for (int i = 0; i < filas; i++) {
            System.out.print(String.format("%02d", i + 1) + " ");
            for (int j = 0; j < columnas; j++) {
                if (tablero[i][j] == -2) {
                    System.out.print("  ");
                } else if (tablero[i][j] == -3) {
                    System.out.print("? ");
                } else if (tablero[i][j] == -1) {
                    System.out.print("* ");
                } else if (tablero[i][j] == -4) {
                    System.out.print("\\ ");
                } else if (tablero[i][j] == 0) {
                    System.out.print("  ");
                } else {
                    System.out.print(tablero[i][j] + " ");
                }
            }
            System.out.println();
        }
    }
}