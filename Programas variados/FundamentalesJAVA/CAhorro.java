public class CAhorro{
    public static void main(String[] args) {
        int invr = 10000;
        double rent;
        double suma = 0;

        for(int i = 1; i <= 20; i++){
            suma += invr;
            rent = suma * (0.05);
            suma += rent;
        }

        System.out.println("Ganancia de: " + suma);

    }
}