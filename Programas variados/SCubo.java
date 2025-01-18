import java.math.*;

public class SCubo {
    public static void main(String[] args) {
        
        for(int i = 1; i<=5000; i++){
            if(Armstrong(i)){
                System.out.println(i);
            }
        }

    }

    public static boolean Armstrong(int n){
        double suma;

        int uni = n % 10;
        int dece = (n / 10) % 10;
        int cent = (n / 100) % 10;
        int milla = (n / 1000) % 10;

        suma = (Math.pow(uni, 3) + (Math.pow(dece, 3)) + (Math.pow(cent, 3)) + (Math.pow(milla, 3)));

        return suma == n;

    }
}