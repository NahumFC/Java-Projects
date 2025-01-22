import java.util.Scanner;

public class SumWhile {
    public static void main(String[] args) {
        int x, y;
        Scanner read = new Scanner(System.in);
        System.out.println("Escribe tu numero X y Y separados por un espacio. ");
        x = read.nextInt();
        y = read.nextInt();

        while (x <= 200) {
            System.out.println(x);
            x += y;
        }
    }
}