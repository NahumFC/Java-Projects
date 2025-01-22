import java.util.Scanner;
public class Cuota {
    public static void main(String[] args) {
    
        int n_ejes;
        char opt = ' ';
        Scanner read = new Scanner(System.in);

        System.out.println("Escriba la opcion correspondiente: ");
        System.out.println("¿Es una motocicleta? (s/n)");
        opt = read.next().charAt(0);

        switch (opt) {
            case 's':
                System.out.println("La cuota es de $20");
                break;
            case 'n':
                System.out.println("Escriba el numero de ejes: ");
                n_ejes = read.nextInt();
                calcuota(n_ejes);
                break;
        
            default:
                break;
        }

    }

    public static void calcuota(int n_ejes){
        final int base = 6;
        switch (n_ejes) {
            case 2:
                System.out.println("Corresponde a un automovil");
                System.out.println("La cuota es de $40");
                break;

            case 3: 
                System.out.println("Corresponde a una camioneta");
                System.out.println("La cuota es de $60");
                break;

            case 4:
                System.out.println("Corresponde a un camion de carga de "+n_ejes);
                System.out.println("La cuota es de $250");
                break;

            case 5:
                System.out.println("Corresponde a un camion de carga de "+n_ejes);
                System.out.println("La cuota es de $250");
                break;

            case 6:
                System.out.println("Corresponde a un camion de carga de"+n_ejes);
                System.out.println("La cuota es de $250");
                break;

            default:
            int aumento = n_ejes - base;
            int f_cob = 250 + (aumento * 50);
            System.out.println("Corresponde a un camion de carga de "+n_ejes);
            System.out.println("La cuota es de $"+f_cob);
            break;
        }
    }
}