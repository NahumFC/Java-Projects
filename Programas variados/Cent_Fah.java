import java.util.Scanner;

public class Cent_Fah {
    public static void main(String[] args) {
        float cent, fah;
        Scanner read = new Scanner(System.in);
        
        System.out.println("Escribe los grados centigrados a convertir:");
        cent = read.nextFloat();

        fah = (cent * 1.8f) + 32;
        System.out.println(cent + "°C equivale a " + fah + "°F");
    }
}