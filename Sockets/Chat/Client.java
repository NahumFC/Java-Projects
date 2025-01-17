import java.net.*;
import java.io.*;

public class Client {
    private static final String DIRECCION_SERVIDOR = "localhost";
    private static final int PUERTO = 12345;
    private static BufferedReader entradaUsuario;
    private static PrintWriter salida;

    public static void main(String[] args) throws IOException {
        Socket socketServidor = new Socket(DIRECCION_SERVIDOR, PUERTO);
        entradaUsuario = new BufferedReader(new InputStreamReader(System.in));
        salida = new PrintWriter(socketServidor.getOutputStream(), true);
        BufferedReader entradaServidor = new BufferedReader(new InputStreamReader(socketServidor.getInputStream()));

        new Thread(() -> {
            try {
                String mensaje;
                while ((mensaje = entradaServidor.readLine()) != null) {
                    System.out.println(mensaje);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }).start();

        String nombreUsuario = solicitarNombreUsuario();
        salida.println(nombreUsuario);

        while (true) {
            System.out.println("Seleccione una opción:");
            System.out.println("1. Ver usuarios conectados");
            System.out.println("2. Enviar mensaje privado");
            System.out.println("3. Enviar mensaje a la sala");
            System.out.println("4. Enviar archivo a un usuario");

            String opcion = entradaUsuario.readLine();
            salida.println(opcion);

            if (opcion.equals("2")) {
                System.out.print("Ingrese el nombre del usuario: ");
                String usuario = entradaUsuario.readLine();
                System.out.print("Ingrese el mensaje: ");
                String mensaje = entradaUsuario.readLine();
                salida.println("2 " + usuario + " " + mensaje);
            } else if (opcion.equals("3")) {
                System.out.print("Ingrese el mensaje para la sala: ");
                String mensaje = entradaUsuario.readLine();
                salida.println("3 " + mensaje);
            } else if (opcion.equals("4")) {
                System.out.print("Ingrese el nombre del usuario destinatario: ");
                String usuarioDestino = entradaUsuario.readLine();
                System.out.print("Ingrese el nombre del archivo: ");
                String nombreArchivo = entradaUsuario.readLine();
                enviarArchivo(usuarioDestino, nombreArchivo, socketServidor);
            }
        }
    }

    private static String solicitarNombreUsuario() throws IOException {
        System.out.print("Ingrese su nombre de usuario: ");
        return entradaUsuario.readLine();
    }

    private static void enviarArchivo(String usuarioDestino, String nombreArchivo, Socket socketServidor) {
        try {
            File archivo = new File(nombreArchivo);
            if (!archivo.exists()) {
                System.out.println("El archivo no existe en el directorio actual.");
                return;
            }

            salida.println("4 " + usuarioDestino + " " + archivo.getName());

            DataOutputStream dos = new DataOutputStream(socketServidor.getOutputStream());
            FileInputStream fis = new FileInputStream(archivo);
            byte[] buffer = new byte[4096];
            int bytesRead;

            while ((bytesRead = fis.read(buffer)) > 0) {
                dos.write(buffer, 0, bytesRead);
            }

            dos.flush();
            fis.close();
            System.out.println("Archivo enviado correctamente a " + usuarioDestino);
        } catch (IOException e) {
            System.out.println("Error al enviar el archivo: " + e.getMessage());
        }
    }
}
