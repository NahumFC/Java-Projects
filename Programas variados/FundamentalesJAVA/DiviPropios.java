public class DiviPropios {
    public static void main(String[] args) {
        
        for(int i = 1; i <= 10000; i++){
            if(nPerfecto(i)){
                System.out.println(i);
            }
        }

    }

    public static boolean nPerfecto(int n){
        int suma = 0;
        for(int j = 1; j < n ; j++){
            if(n % j == 0){
                suma += j;
            }
        } return suma == n;
    }
}