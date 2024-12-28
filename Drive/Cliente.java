import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Cliente {

    private static final int PUERTO = 1800;
    private static final int WINDOW_SIZE = 1024; // Tamaño de la ventana en bytes
    private static final int TIMEOUT = 2000; // Tiempo de espera en milisegundos
    private static InetAddress servidorIP;

    public static void main(String[] args) {
        try (DatagramSocket socket = new DatagramSocket()) {
            socket.setSoTimeout(TIMEOUT);
            servidorIP = InetAddress.getByName("127.0.0.1");
            Scanner scanner = new Scanner(System.in);

            int seqNum = 0;
            while (true) {
                System.out.println("Introduce un comando (crearCarpeta [nombre] / borrarCarpeta [nombre] / cambiarCarpeta [nombre] / regresarCBase / crearArchivo [nombre] / borrarArchivo [nombre] / salir): ");
                String command = scanner.nextLine();
                if (command.equalsIgnoreCase("salir")) {
                    break;
                }

                enviarComando(socket, seqNum, command);
                recibirACK(socket, seqNum);
                seqNum++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void enviarComando(DatagramSocket socket, int seqNum, String command) throws IOException {
        String message = seqNum + ":" + command;
        byte[] data = message.getBytes();
        DatagramPacket packet = new DatagramPacket(data, data.length, servidorIP, PUERTO);
        socket.send(packet);
        System.out.println("Comando enviado: " + command + " (secuencia " + seqNum + ")");
    }

    private static void recibirACK(DatagramSocket socket, int seqNumEsperado) {
        byte[] buffer = new byte[WINDOW_SIZE];
        DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

        while (true) {
            try {
                socket.receive(packet);
                String ackMessage = new String(packet.getData(), 0, packet.getLength());
                String[] parts = ackMessage.split(":", 3);
                int ackNum = Integer.parseInt(parts[1]);
                //ACK sirve para confirmar que el paquete fue recibido correctamente
                if (ackNum == seqNumEsperado) {
                    System.out.println("ACK recibido para secuencia " + ackNum + ": " + parts[2]);
                    break;
                } else {
                    System.out.println("ACK fuera de orden, esperado " + seqNumEsperado + ", recibido " + ackNum);
                }
            } catch (SocketTimeoutException e) {
                System.out.println("Tiempo de espera agotado, retransmitiendo...");
                break;
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
    }
}