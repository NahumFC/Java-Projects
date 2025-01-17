import java.util.Scanner;

public class Frac {
    public static void main(String[] args){
        Scanner read = new Scanner(System.in);
        int a, b, c, d, num, den;
        float res;
        System.out.println("Escribe el numerador y denominador del primer numero separados por un espacio: ");
        a = read.nextInt();
        b = read.nextInt();
        System.out.println("Escribe el numerador y denominador del segundo numero separados por un espacio:");
        c = read.nextInt();
        d = read.nextInt();
        num = a * c;
        den = b * d;
        float fnum = (float) num;
        float fden = (float) den;
        res = fnum/fden;

        System.out.println("Resultado: " + num + "/" + den + " = " + res);
    }
    
}