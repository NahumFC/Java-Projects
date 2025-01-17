import java.util.*;;

public class Circ_Tri {
    public static void main(String[] args) {
        double rad, area, semiP, her, a, b, c;
        Scanner read = new Scanner(System.in);

        System.out.println("Escribe los tres lados del triángulo separados por un espacio:");
        a = read.nextFloat();
        b = read.nextFloat();
        c = read.nextFloat();

        semiP = (a + b + c)/2; //Semiperímetro del triángulo
        her = semiP * (semiP - a) * (semiP - b) * (semiP - c);
        area = Math.sqrt(her); //Formula de Herón
        rad = (area)/(semiP); //Formula del radio

        System.out.println("El radio de la circunferencia inscrita en el triangulo proporcionado es: " + rad);

    }

}