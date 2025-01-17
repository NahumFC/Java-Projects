import java.util.Scanner;
public class CFE {
    public static void main(String[] args) {
        char opt = ' ';
        double consumo;
        Scanner read = new Scanner(System.in);

        System.out.println("Cobro de consumo de electricidad.");
        System.out.println("Selecciona la opcion que corresponda (H o N)");
        System.out.println("Contrato de Hogar");
        System.out.println("Contrato de Negocio");
        opt = read.next().charAt(0);

        switch (opt) {
            case 'H':
                System.out.println("Introduce el consumo en kW");
                consumo = read.nextDouble();
                calHog(consumo);
                break;

            case 'N':
                System.out.println("Introduce el consumo en kW");
                consumo = read.nextDouble();
                calNeg(consumo);
                break;
        
            default:
                break;
        }
    }

    public static void calHog(double consumo){
        double preliminar;

        if((0 <= consumo) && (consumo <= 250)){
            System.out.println("Se tiene que pagar: $" + (consumo * 0.65));
        }

        if((251 <= consumo) && (consumo <= 500)){
            preliminar = consumo - 250;
            System.out.println("Se tiene que pagar: $" + ((250 * 0.65) + (preliminar * 0.85)));
        }

        if((501 <= consumo) && (consumo <= 1200)){
            preliminar = consumo - 500;
            System.out.println("Se tiene que pagar: $" + ((250 * 0.65) + (250 * 0.85) + (preliminar * 1.50)));

        }

        if((1201 <= consumo) && (consumo <= 2100)){
            preliminar = consumo - 1200;
            System.out.println("Se tiene que pagar: $" + ((250 * 0.65) + (250 * 0.85) + (250 * 1.50) + (preliminar * 2.50)));
        }

        if(consumo >= 2101){
            preliminar = consumo - 2100;
            System.out.println("Se tiene que pagar: $" + ((250 * 0.65) + (250 * 0.85) + (250 * 1.50) + (250 * 2.50) + (preliminar * 3.00)));

        }

    }

    public static void calNeg(double consumo){
        System.out.println("Se tiene que pagar: $" + consumo * 5);
    }
}