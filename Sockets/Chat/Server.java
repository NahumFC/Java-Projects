import java.net.*;
import java.io.*;
import java.util.*;

public class Server {
    private static final int PUERTO = 12345;
    private static final String DIRECCION_MULTICAST = "230.0.0.1";
    private static Set<ManejadorCliente> manejadoresClientes = new HashSet<>();
    private static MulticastSocket socketMulticast;
    private static List<String> chatDeSala = new ArrayList<>();

    public static void main(String[] args) throws IOException {
        ServerSocket servidorSocket = new ServerSocket(PUERTO);
        socketMulticast = new MulticastSocket();
        InetAddress grupo = InetAddress.getByName(DIRECCION_MULTICAST);
        socketMulticast.joinGroup(grupo);

        System.out.println("Servidor de chat iniciado...");

        while (true) {
            Socket socket = servidorSocket.accept();
            ManejadorCliente manejadorCliente = new ManejadorCliente(socket);
            manejadoresClientes.add(manejadorCliente);
            new Thread(manejadorCliente).start();
        }
    }

    public static void enviarMensajeALaSala(String mensaje) throws IOException {
        chatDeSala.add(mensaje);
        byte[] buffer = mensaje.getBytes();
        socketMulticast.send(new DatagramPacket(buffer, buffer.length, InetAddress.getByName(DIRECCION_MULTICAST), PUERTO));
        System.out.println("Mensaje enviado a la sala: " + mensaje);
        for (ManejadorCliente cliente : manejadoresClientes) {
            cliente.enviarMensaje(mensaje);
        }
    }

    public static void enviarMensajePrivado(String nombreUsuario, String mensaje, String emisor) {
        for (ManejadorCliente cliente : manejadoresClientes) {
            if (cliente.obtenerNombreUsuario().equals(nombreUsuario)) {
                String mensajePrivado = "Mensaje privado de (" + emisor + "): " + mensaje;
                cliente.enviarMensaje(mensajePrivado);
                System.out.println("Mensaje privado enviado de " + emisor + " a " + nombreUsuario + ": " + mensaje);
                return;
            }
        }
        System.out.println("No se encontró al usuario: " + nombreUsuario);
    }

    public static void mostrarChatDeSala(ManejadorCliente cliente) {
        for (String mensaje : chatDeSala) {
            cliente.enviarMensaje(mensaje);
        }
    }

    static class ManejadorCliente implements Runnable {
        private Socket socket;
        private PrintWriter salida;
        private BufferedReader entrada;
        private String nombreUsuario;

        public ManejadorCliente(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            try {
                entrada = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                salida = new PrintWriter(socket.getOutputStream(), true);

                nombreUsuario = entrada.readLine();
                System.out.println(nombreUsuario + " se ha conectado al chat.");
                enviarMensajeALaSala(nombreUsuario + " se ha unido al chat.");

                mostrarChatDeSala(this);

                String mensaje;
                while ((mensaje = entrada.readLine()) != null) {
                    if (mensaje.equals("1")) {
                        listarUsuarios();
                    } else if (mensaje.startsWith("2")) {
                        String[] partes = mensaje.split(" ", 3);
                        if (partes.length == 3) {
                            enviarMensajePrivado(partes[1], partes[2], nombreUsuario);
                        }
                    } else if (mensaje.startsWith("3")) {
                        if (mensaje.length() > 2) {
                            enviarMensajeALaSala(nombreUsuario + ": " + mensaje.substring(2));
                        }
                    } else if (mensaje.startsWith("4")) {
                        String[] partes = mensaje.split(" ", 3);
                        if (partes.length == 3) {
                            recibirArchivo(partes[1], partes[2]);
                        }
                    } else {
                        salida.println("Comando no válido.");
                    }
                }

            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    manejadoresClientes.remove(this);
                    socket.close();
                    enviarMensajeALaSala(nombreUsuario + " se ha desconectado.");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void listarUsuarios() {
            StringBuilder listaUsuarios = new StringBuilder("Usuarios conectados: ");
            for (ManejadorCliente cliente : manejadoresClientes) {
                listaUsuarios.append(cliente.obtenerNombreUsuario()).append(", ");
            }
            if (listaUsuarios.length() > 0) {
                listaUsuarios.setLength(listaUsuarios.length() - 2);
            }
            salida.println(listaUsuarios.toString());
        }

        public String obtenerNombreUsuario() {
            return nombreUsuario;
        }

        public void enviarMensaje(String mensaje) {
            salida.println(mensaje);
        }

        private void recibirArchivo(String nombreDestino, String nombreArchivo) {
            try {
                for (ManejadorCliente cliente : manejadoresClientes) {
                    if (cliente.obtenerNombreUsuario().equals(nombreDestino)) {
                        File carpetaDestino = new File(nombreDestino);
                        if (!carpetaDestino.exists()) {
                            carpetaDestino.mkdir();
                        }

                        DataInputStream dis = new DataInputStream(socket.getInputStream());
                        File archivo = new File(carpetaDestino, nombreArchivo);
                        FileOutputStream fos = new FileOutputStream(archivo);
                        byte[] buffer = new byte[4096];
                        int bytesRead;

                        while ((bytesRead = dis.read(buffer)) > 0) {
                            fos.write(buffer, 0, bytesRead);
                        }

                        fos.close();
                        String mensajeArchivo = "Has recibido un archivo de (" + nombreUsuario + ")";
                        cliente.enviarMensaje(mensajeArchivo);
                        System.out.println("Archivo '" + nombreArchivo + "' enviado de " + nombreUsuario + " a " + nombreDestino);
                        return;
                    }
                }
                salida.println("Usuario destinatario no encontrado.");
            } catch (IOException e) {
                System.out.println("Error al recibir el archivo: " + e.getMessage());
            }
        }
    }
}