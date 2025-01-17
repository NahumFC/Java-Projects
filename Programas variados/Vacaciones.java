import java.util.Scanner;

public class Vacaciones {
    public static void main(String[] args) {
        int year;
        Scanner read = new Scanner(System.in);

        System.out.println("Escribe el numero de años que has trabajado en la empresa: ");
        year = read.nextInt();
            //Todo el proceso que sigue se puede optimizar por medio de ciclos for, sin embargo parte de los requerimientos es que se resuelva por medio de estructuras de decisión (if o case).
        if(year>=1 && year<=5){
            System.out.println("Te corresponden 5 dias de vacaciones");
        }
        if(year>=6 && year<=10){
            System.out.println("Te corresponden 10 dias de vacaciones");
        }
        if (year>=11 && year<=19) {
            switch (year) {
                case 11:
                    System.out.println("Te corresponden 11 dias de vacaciones");
                    break;

                case 12:
                    System.out.println("Te corresponden 12 dias de vacaciones");
                    break;

                case 13: 
                    System.out.println("Te corresponden 13 dias de vacaciones");
                    break;
                
                case 14:
                    System.out.println("Te corresponden 14 dias de vacaciones");
                    break;

                case 15:
                    System.out.println("Te corresponden 15 dias de vacaciones");
                    break;
                
                case 16:
                    System.out.println("Te corresponden 16 dias de vacaciones");
                    break;

                case 17:
                    System.out.println("Te corresponden 17 dias de vacaciones");
                    break;

                case 18:
                    System.out.println("Te corresponden 18 dias de vacaciones");
                    break;

                case 19:
                    System.out.println("Te corresponden 19 dias de vacaciones");
                    break;

                default:
                    break;
            }
        }
        if (year>=20) {
            switch (year) {
                case 20:
                    System.out.println("Te corresponden 21 dias de vacaciones");
                    break;
                
                case 21:
                    System.out.println("Te corresponden 23 dias de vacaciones");
                    break;

                case 22:
                    System.out.println("Te corresponden 25 dias de vacaciones");
                    break;

                case 23:
                    System.out.println("Te corresponden 27 dias de vacaciones");
                    break;
                
                case 24:
                    System.out.println("Te corresponden 29 dias de vacaciones");
                    break;

                case 25:
                    System.out.println("Te corresponden 31 dias de vacaciones");
                    break;

                case 26:
                    System.out.println("Te corresponden 33 dias de vacaciones");
                    break;

                case 27:
                    System.out.println("Te corresponden 35 dias de vacaciones");
                    break;

                case 28:
                    System.out.println("Te corresponden 36 dias de vacaciones");
                    break;

                case 29:
                    System.out.println("Te corresponden 38 dias de vacaciones");
                    break;

                case 30:
                    System.out.println("Te corresponden 40 dias de vacaciones");
                    break;

                case 31:
                    System.out.println("Te corresponden 42 dias de vacaciones");
                    break;

                case 32:
                    System.out.println("Te corresponden 44 dias de vacaciones");
                    break;

                default:
                    System.out.println("Te corresponden 45 dias de vacaciones");
                    break;
            }
        }
    }
}