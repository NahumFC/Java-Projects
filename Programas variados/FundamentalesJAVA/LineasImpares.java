import java.io.*;

public class LineasImpares {
    public static void main(String[] args) {
        // Especificar el archivo de entrada y el archivo de salida
        String archivoEntrada = "archivo_original.txt";
        String archivoSalida = "archivo_impares.txt";

        try (
            BufferedReader lector = new BufferedReader(new FileReader(archivoEntrada));
            BufferedWriter escritor = new BufferedWriter(new FileWriter(archivoSalida))
        ) {
            String linea;
            int numeroLinea = 1;

            // Leer línea por línea del archivo original
            while ((linea = lector.readLine()) != null) {
                // Escribir solo las líneas impares en el archivo de salida
                if (numeroLinea % 2 != 0) {
                    escritor.write(linea);
                    escritor.newLine();  // Agregar un salto de línea en el archivo de salida
                }
                numeroLinea++;  
            }

            System.out.println("Proceso completado. Las líneas impares se han guardado en " + archivoSalida);

        } catch (IOException e) {
            System.out.println("Ocurrió un error: " + e.getMessage());
        }
    }
}