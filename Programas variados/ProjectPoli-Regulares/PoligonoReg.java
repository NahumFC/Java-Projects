/* 
 *  
 *  Flores Cornejo David Nahum
 *  
*/


public class PoligonoReg {
    private int numVertices;
    private double radio;

    // Constructor
    public PoligonoReg(int numVertices, double radio) {
        this.numVertices = numVertices;
        this.radio = radio;
    }

    // Método para calcular el área del polígono regular
    public double obtieneArea() {
        // Fórmula para el área de un polígono regular
        return (numVertices * Math.pow(radio, 2) * Math.sin(2 * Math.PI / numVertices)) / 2;
    }

    // Método para obtener el número de vértices
    public int getNumVertices() {
        return numVertices;
    }

    // Método para obtener el radio
    public double getRadio() {
        return radio;
    }
}
