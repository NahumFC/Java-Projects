public class MenorIgual {
    public static void main(String[] args) {
        int[] intArray = {20, 13, 12, 11, 10, 9, 8, 7, 6, 4};
        boolean esDescendente = true;

        for(int i = 0; i < intArray.length - 1; i++) {
            if (intArray[i] < intArray[i + 1]) {
                esDescendente = false;
                break;  
            }
        }

        if (esDescendente) {
            System.out.println("Sí están en forma descendente.");
        } else {
            System.out.println("No están en forma descendente.");
        }
    }
}