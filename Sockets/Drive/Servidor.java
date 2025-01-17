import java.io.*;
import java.net.*;
import java.util.HashMap;
import java.util.Map;

public class Servidor {

    private static final int PUERTO = 1800;
    private static final int WINDOW_SIZE = 1024; // Tamaño de la ventana en bytes
    private static final int TIMEOUT = 2000; // Tiempo de espera en milisegundos
    private static final String BASE_DIRECTORY = "Drive"; // Directorio base para almacenar archivos y carpetas, cree esa carpeta con la intención de que no tuvieramos un caos en la carpeta en la que se almacenan los programas y ps así se parece mas a como funciona Drive
    private static File currentDirectory = new File(BASE_DIRECTORY); // Directorio actual (inicia en Drive)

    public static void main(String[] args) {
        // Crear la carpeta base "Drive" si no existe
        if (!currentDirectory.exists()) {
            currentDirectory.mkdir();
            System.out.println("Directorio base 'Drive' creado.");
        }

        try (DatagramSocket socket = new DatagramSocket(PUERTO)) {
            socket.setSoTimeout(TIMEOUT);
            System.out.println("Servidor iniciado en el puerto " + PUERTO);

            byte[] buffer = new byte[WINDOW_SIZE];
            Map<Integer, byte[]> ventanaRecibida = new HashMap<>();
            int expectedSeqNum = 0;

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);

                try {
                    socket.receive(packet);
                    String receivedData = new String(packet.getData(), 0, packet.getLength());
                    String[] parts = receivedData.split(":", 2);
                    int seqNum = Integer.parseInt(parts[0]);
                    String command = parts[1].trim();

                    if (seqNum == expectedSeqNum) {
                        System.out.println("Paquete recibido (secuencia " + seqNum + "): " + command);
                        expectedSeqNum++;

                        // Procesar comando
                        String response = procesarComando(command);
                        enviarACK(socket, packet.getAddress(), packet.getPort(), seqNum, response);
                    } else {
                        System.out.println("Secuencia fuera de orden, esperando " + expectedSeqNum + ", recibido " + seqNum);
                        enviarACK(socket, packet.getAddress(), packet.getPort(), expectedSeqNum - 1, "Reenvío esperado");
                    }

                } catch (SocketTimeoutException e) {
                    System.out.println("Tiempo de espera agotado, esperando paquetes...");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String procesarComando(String command) {
        String[] tokens = command.split(" ");
        String action = tokens[0];
        String path = tokens.length > 1 ? tokens[1] : null;

        switch (action) {
            case "crearCarpeta":
                return crearCarpeta(path);
            case "borrarCarpeta":
                return borrarCarpeta(path);
            case "crearArchivo":
                return crearArchivo(path);
            case "borrarArchivo":
                return borrarArchivo(path);
            case "cambiarCarpeta":
                return cambiarCarpeta(path);
            case "regresarCBase":
                return regresarCBase();
            default:
                return "Comando desconocido";
        }
    }
    //Función para crear una carpeta
    private static String crearCarpeta(String folderName) {
        File folder = new File(currentDirectory, folderName);
        if (!folder.exists()) {
            if (folder.mkdir()) {
                return "Carpeta creada: " + folder.getAbsolutePath();
            } else {
                return "Error al crear la carpeta";
            }
        } else {
            return "La carpeta ya existe";
        }
    }
    //Funcion para borrar una carpeta
    private static String borrarCarpeta(String folderName) {
        File folder = new File(currentDirectory, folderName);
        if (folder.exists()) {
            if (folder.delete()) {
                return "Carpeta eliminada: " + folder.getAbsolutePath();
            } else {
                return "Error al eliminar la carpeta";
            }
        } else {
            return "La carpeta no existe";
        }
    }
    //Función para crear un archivo
    private static String crearArchivo(String fileName) {
        try {
            File file = new File(currentDirectory, fileName);
            if (file.createNewFile()) {
                return "Archivo creado: " + file.getAbsolutePath();
            } else {
                return "El archivo ya existe";
            }
        } catch (IOException e) {
            return "Error al crear el archivo: " + e.getMessage();
        }
    }
    //Función para borrar un archivo
    private static String borrarArchivo(String fileName) {
        File file = new File(currentDirectory, fileName);
        if (file.exists()) {
            if (file.delete()) {
                return "Archivo eliminado: " + file.getAbsolutePath();
            } else {
                return "Error al eliminar el archivo";
            }
        } else {
            return "El archivo no existe";
        }
    }

    // Función para cambiar de carpeta
    private static String cambiarCarpeta(String folderName) {
        File newDirectory = new File(currentDirectory, folderName);
        if (newDirectory.exists() && newDirectory.isDirectory()) {
            currentDirectory = newDirectory;
            return "Cambió a la carpeta: " + newDirectory.getAbsolutePath();
        } else {
            return "La carpeta no existe: " + folderName;
        }
    }

    // Función para regresar a la carpeta base
    private static String regresarCBase() {
        currentDirectory = new File(BASE_DIRECTORY);
        return "Regresó a la carpeta base: " + currentDirectory.getAbsolutePath();
    }

    private static void enviarACK(DatagramSocket socket, InetAddress address, int port, int seqNum, String message) throws IOException {
        String ackMessage = "ACK:" + seqNum + ":" + message;
        byte[] ackData = ackMessage.getBytes();
        DatagramPacket ackPacket = new DatagramPacket(ackData, ackData.length, address, port);
        socket.send(ackPacket);
        System.out.println("ACK enviado para secuencia " + seqNum);
    }
}
