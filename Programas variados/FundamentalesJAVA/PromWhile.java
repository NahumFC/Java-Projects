import java.util.Scanner;

public class PromWhile {
    public static void main(String[] args) {
        double n = 1;
        double suma = 0;
        int i=0;
        Scanner read = new Scanner(System.in);

        System.out.println("Escribe numeros para sacar el promedio, escribe 0 si quieres parar.");
        while (n != 0) {
            n = read.nextDouble();
            suma += n;
            i++;
        }

        System.out.println("El promedio de tus " + (i-1) + " numeros, es: " + (suma/(i-1)));
    }
}