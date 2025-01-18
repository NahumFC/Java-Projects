import java.util.Scanner;

public class SumMatriz {
    public static void main(String[] args) {
        Scanner read = new Scanner(System.in);
        int[][] matriz = new int[5][5];
        int suma = 0;

        System.out.println("Introduce los valores de una matriz 5x5: ");
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 5; j++) {
                matriz[i][j] = read.nextInt();
                suma += matriz[i][j];  
            }
        }

        System.out.println("La suma total de los elementos de la matriz es: " + suma);

        read.close();  
    }
}