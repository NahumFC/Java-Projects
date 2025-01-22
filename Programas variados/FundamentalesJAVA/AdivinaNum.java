import java.util.Scanner;


public class AdivinaNum {
    public static void main(String[] args) {
        int numero = (int) (Math.random() * 100 + 1);
        Scanner read = new Scanner(System.in);

        System.out.println("Adivina el numero que escogió la computadora, escribe el numero de 1 al 100");
        int opt = read.nextInt();

        while (opt != numero) {
            if (opt == 0) {
                break;
            }else{
                System.out.println("Error, intenta de nuevo o escribe 0 para salir. ");
                opt = read.nextInt();

            }
        }

        if (opt == 0){
            System.out.println("La respuesta era: " + numero);
        }else{
            System.out.println(numero + " Es la respuesta correcta, felicidades");
        }
        
    }
    
}