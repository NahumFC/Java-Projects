import java.util.Scanner;

public class Mayor_Menor {
    public static void main(String[] args) {
        int a, b, c;
        Scanner read = new Scanner(System.in);

        System.out.println("Escribe 3 numeros separados por espacios: ");
        a = read.nextInt();
        b = read.nextInt();
        c = read.nextInt();

        if (a >= b && a >= c) {
            if (b >= c) {
                System.out.println("Ordenados: " + a + " " + b + " " + c);
            } else {
                System.out.println("Ordenados: " + a + " " + c + " " + b);
            }
        } else if (b >= a && b >= c) {
            if (a >= c) {
                System.out.println("Ordenados: " + b + " " + a + " " + c);
            } else {
                System.out.println("Ordenados: " + b + " " + c + " " + a);
            }
        } else {
            if (a >= b) {
                System.out.println("Ordenados: " + c + " " + a + " " + b);
            } else {
                System.out.println("Ordenados: " + c + " " + b + " " + a);
            }
        }
    }
}