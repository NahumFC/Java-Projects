import java.util.Scanner;

public class Capicua {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Ingresa un número entre 0 y 9999: ");
        int numero = scanner.nextInt();

        if (esCapicua(numero)) {
            System.out.println(numero + " es un número capicúa.");
        } else {
            System.out.println(numero + " no es un número capicúa.");
        }

        scanner.close();
    }

    public static boolean esCapicua(int n) {
        if (n >= 0 && n <= 9) {
            return true; // Un solo dígito es capicúa
        } else if (n >= 10 && n <= 99) {
            return (n / 10) == (n % 10); // Compara decenas con unidades
        } else if (n >= 100 && n <= 999) {
            return (n / 100) == (n % 10); // Compara centenas con unidades
        } else if (n >= 1000 && n <= 9999) {
            return (n / 1000) == (n % 10) && ((n / 100) % 10) == ((n / 10) % 10); // Compara millares con unidades y centenas con decenas
        } else {
            return false; // Número fuera de rango
        }
    }
}