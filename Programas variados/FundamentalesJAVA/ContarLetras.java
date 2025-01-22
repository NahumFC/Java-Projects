import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class ContarLetras {
    public static void main(String[] args) {
        String archivoEntrada = "archivo_texto.txt";

        // Crear un HashMap para almacenar el conteo de cada letra
        Map<Character, Integer> conteoLetras = new HashMap<>();

        try (BufferedReader lector = new BufferedReader(new FileReader(archivoEntrada))) {
            int caracter;

            // Leer el archivo carácter por carácter
            while ((caracter = lector.read()) != -1) {
                char letra = Character.toLowerCase((char) caracter);  // Convertir a minúscula

                // Solo contar si es una letra
                if (Character.isLetter(letra)) {
                    conteoLetras.put(letra, conteoLetras.getOrDefault(letra, 0) + 1);
                }
            }

            // Mostrar el resultado
            System.out.println("Cantidad de veces que aparece cada letra en el archivo:");
            for (Map.Entry<Character, Integer> entrada : conteoLetras.entrySet()) {
                System.out.println(entrada.getKey() + ": " + entrada.getValue());
            }

        } catch (IOException e) {
            System.out.println("Ocurrió un error al leer el archivo: " + e.getMessage());
        }
    }
}
