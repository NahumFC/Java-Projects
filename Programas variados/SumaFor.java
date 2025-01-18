import java.util.Scanner;

public class SumaFor {
    public static void main(String[] args) {
        int numero;
        int suma = 0;
        Scanner read = new Scanner(System.in);

        System.out.println("Escribe un numero: ");
        numero = read.nextInt();

        for(int i = 0; i<=numero; i++){
            suma = suma + i;
        }
        System.out.println("La suma de numeros desde 1 hasta tu numero es: " + suma);
    }
    
}