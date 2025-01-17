import java.util.Scanner;

public class Uni_Sex_Edad {
    public static void main(String[] args) {
        int edad = 0;
        char sex = ' ';
        Scanner read = new Scanner(System.in);

        
        boolean datosValidos = false;

        
        while (!datosValidos) {
            System.out.println("Escribe tu edad y tu sexo (H o M) separados por un espacio: ");
            edad = read.nextInt();
            String sexoInput = read.next();
            sex = sexoInput.charAt(0);

            try {
                validar(edad, sex); 
                datosValidos = true; 
            } catch (IllegalArgumentException e) {
                
                System.out.println("Error: " + e.getMessage());
                System.out.println("Por favor, intenta de nuevo.");
            }
        }

        asig(edad, sex);
    }

    // Método de validación
    public static void validar(int edad, char sex) {
        if (edad < 18 || (sex != 'H' && sex != 'M')) {
            throw new IllegalArgumentException("La edad debe ser mayor o igual a 18 y el sexo debe ser H o M");
        }
        System.out.println("Edad válida: " + edad + ", Sexo válido: " + sex);
    }

    //Método de Asignación de Edificio
    public static void asig(int edad, char sex){

        if(sex == 'H'){
            if(edad == 18){
                System.out.println("El edificio que corresponde es el A ");
            }
            if(edad > 22){
                System.out.println("El edificio que corresponde es el E1");
            }
            else{
                System.out.println("El edificio que corresponde es el C");
            }
        }
        else{
            if(edad == 18){
                System.out.println("El edificio que corresponde es el B ");
            }
            if(edad > 22){
                System.out.println("El edificio que corresponde es el E2");
            }
            else{
                System.out.println("El edificio que corresponde es el D");
            }
        }
    }
}