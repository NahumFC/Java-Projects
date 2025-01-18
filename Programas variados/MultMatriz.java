import java.util.Scanner;

public class MultMatriz {
    public static void main(String[] args) {
        Scanner read = new Scanner(System.in);

        System.out.println("Introduce el número de filas de la primera matriz: ");
        int filas1 = read.nextInt();
        System.out.println("Introduce el número de columnas de la primera matriz: ");
        int columnas1 = read.nextInt();

        System.out.println("Introduce el número de filas de la segunda matriz: ");
        int filas2 = read.nextInt();
        System.out.println("Introduce el número de columnas de la segunda matriz: ");
        int columnas2 = read.nextInt();

        // Verificar que el número de columnas de la primera matriz sea igual al número de filas de la segunda
        if (columnas1 != filas2) {
            System.out.println("La multiplicación no es posible. El número de columnas de la primera matriz debe ser igual al número de filas de la segunda matriz.");
        } else {
            int[][] matriz1 = new int[filas1][columnas1];
            int[][] matriz2 = new int[filas2][columnas2];
            int[][] resultado = new int[filas1][columnas2]; 

            System.out.println("Introduce los valores de la primera matriz:");
            for (int i = 0; i < filas1; i++) {
                for (int j = 0; j < columnas1; j++) {
                    matriz1[i][j] = read.nextInt();
                }
            }

            System.out.println("Introduce los valores de la segunda matriz:");
            for (int i = 0; i < filas2; i++) {
                for (int j = 0; j < columnas2; j++) {
                    matriz2[i][j] = read.nextInt();
                }
            }

            // Multiplicar las matrices
            for (int i = 0; i < filas1; i++) {
                for (int j = 0; j < columnas2; j++) {
                    for (int k = 0; k < columnas1; k++) {
                        resultado[i][j] += matriz1[i][k] * matriz2[k][j];
                    }
                }
            }

            System.out.println("El resultado de la multiplicación es:");
            for (int i = 0; i < filas1; i++) {
                for (int j = 0; j < columnas2; j++) {
                    System.out.print(resultado[i][j] + " ");
                }
                System.out.println(); // Salto de línea para la siguiente fila
            }
        }

        read.close(); 
    }
}