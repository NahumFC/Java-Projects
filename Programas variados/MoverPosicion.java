import java.util.Scanner;

public class MoverPosicion {
    public static void main(String[] args) {
        Scanner read = new Scanner(System.in);
        int[] numeros = new int[10];
        
        System.out.println("Introduce 10 números: ");
        for (int i = 0; i < numeros.length; i++) {
            numeros[i] = read.nextInt();
        }

        // Guardar el último número temporalmente
        int ultimo = numeros[numeros.length - 1];

        // Mover todos los elementos una posición hacia adelante
        for (int i = numeros.length - 1; i > 0; i--) {
            numeros[i] = numeros[i - 1];
        }

        // Colocar el último número en la primera posición
        numeros[0] = ultimo;

        System.out.println("El arreglo después de mover los elementos una posición adelante es: ");
        for (int i = 0; i < numeros.length; i++) {
            System.out.print(numeros[i] + " ");
        }

        read.close(); 
    }
}