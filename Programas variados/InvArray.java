import java.util.Scanner;

public class InvArray {
    public static void main(String[] args) {
        Scanner read = new Scanner(System.in);
        int intArray[] = new int[10];

        System.out.println("Escribe los números del array: ");
        for(int i = 9; i >= 0; i--){
            intArray[i] = read.nextInt();  
        }

        System.out.println("Números en orden inverso:");
        for(int j = 0; j < intArray.length; j++){
            System.out.println(intArray[j]);
        }
        
        read.close(); 
    }
}