public class Palagrande {
    public static void main(String[] args) {
        String[] palabras = {"CDMX", "Madrid", "Managua", "Londres", "Cali"};

        String palabraMasLarga = palabras[0];

        for (int i = 1; i < palabras.length; i++) {
            if (palabras[i].length() > palabraMasLarga.length()) {
                palabraMasLarga = palabras[i];
            }
        }

        System.out.println("La palabra más larga es: " + palabraMasLarga);
    }
}