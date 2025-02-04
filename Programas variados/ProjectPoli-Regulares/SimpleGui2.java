/* 
 *  
 *  Flores Cornejo David Nahum
 *  
*/

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class SimpleGui2 extends JFrame {
    private List<PoligonoReg> poligonos;  // Lista de polígonos
    private List<Point> posiciones;  // Posiciones aleatorias
    private List<Double> velocidades;  // Velocidades inversamente proporcionales al área
    private List<Integer> dxs, dys;    // Direcciones de movimiento en X y Y
    private List<Boolean> started;     // Lista para saber si el polígono ya empezó a moverse
    private int numPoligonos;          // Pues xd numero de poligonos
    private int currentPolygonIndex = 0;  // Índice del polígono que está iniciando su movimiento

    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]); // Aquí pues se lee el numero de poligonos en la linea de comandos 
        SimpleGui2 gui = new SimpleGui2(n);
        gui.setVisible(true);
    }

    public SimpleGui2(int n) {
        this.numPoligonos = n;
        poligonos = new ArrayList<>();
        posiciones = new ArrayList<>();
        velocidades = new ArrayList<>();
        dxs = new ArrayList<>();
        dys = new ArrayList<>();
        started = new ArrayList<>();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        setSize(800, 600);
        setLocationRelativeTo(null); 

        Panel p = new Panel();
        add(p);

        // Aquí pues se generan los polígonos con su posición la cual es aleatoria
        SwingUtilities.invokeLater(() -> {
            generarPoligonos(n);
            poligonos.sort((p1, p2) -> Double.compare(p2.obtieneArea(), p1.obtieneArea()));
            repaint();  // Sirve para forzar a la pantalla actualizar la pantalla justamente para que se muestre el movimiento
        });

        // Con esto se espera 3 segundos para inciar con el movimiento 
        new Timer(3000, e -> iniciarMovimientoEnOrden()).start();
    }

    // Método para generar polígonos, posiciones aleatorias y velocidades
    public void generarPoligonos(int n) {
        Random rand = new Random();

        Dimension screenSize = getContentPane().getSize();
        int width = screenSize.width;
        int height = screenSize.height;

        if (width == 0 || height == 0) {
            width = 800;  
            height = 600;
        }

        double maxArea = 0;

        for (int i = 0; i < n; i++) {
            int numVertices = rand.nextInt(6) + 5;  // Número aleatorio de vértices entre 5 y 10
            double radio = 50 + rand.nextDouble() * 100;  
            PoligonoReg poligono = new PoligonoReg(numVertices, radio);
            poligonos.add(poligono);

            // Con esto los polígonos van a aparecer aleatoriamente en la pantalla
            // Poner margenes es importante para que los polígonos se puedan mantener dentro de la pantalla 
            int x = rand.nextInt(width - 100) + 50;   
            int y = rand.nextInt(height - 100) + 50;
            posiciones.add(new Point(x, y));

            // Calcular el área del polígono
            double area = poligono.obtieneArea();

            // Usé el área del polígonos mas grande para tener una velocidad base
            if (area > maxArea) {
                maxArea = area;
            }

            // Se genran direcciones aleatorias iniciales
            dxs.add(rand.nextInt(3) - 1);  
            dys.add(rand.nextInt(3) - 1);  

            // La dirección no puede ser cero si no no va a a haber movimiento por eso el if
            if (dxs.get(i) == 0 && dys.get(i) == 0) {
                dxs.set(i, 1); // Si ambas direcciones son 0, va a forzar a que se muevan 
            }

            // Como el mas grande se mueve despues de 3 segundos pues deben iniar quietos
            started.add(false);
        }

        for (PoligonoReg poligono : poligonos) {
            double area = poligono.obtieneArea();
            // La velocidad base es la del polígono más grande, los más pequeños se mueven más rápido
            double velocidad = 100 * (maxArea / area);
            velocidades.add(velocidad);
        }
    }

    private class Panel extends JPanel {
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.blue);

            // Dibujar cada polígono en su posición actual
            for (int i = 0; i < poligonos.size(); i++) {
                PoligonoReg poligono = poligonos.get(i);
                Point posicion = posiciones.get(i);
                dibujarPoligono(g, poligono, posicion.x, posicion.y);
            }
        }

        // Método para dibujar un polígono regular
        private void dibujarPoligono(Graphics g, PoligonoReg poligono, int centerX, int centerY) {
            Polygon p = new Polygon();
            int n = poligono.getNumVertices();
            double r = poligono.getRadio();

            for (int i = 0; i < n; i++) {
                int x = (int) (centerX + r * Math.cos(2 * Math.PI * i / n));
                int y = (int) (centerY + r * Math.sin(2 * Math.PI * i / n));
                p.addPoint(x, y);
            }
            g.drawPolygon(p);
        }
    }

    // Método para que jale el movimiento en orden de mas grande al mas chico
    private void iniciarMovimientoEnOrden() {
        Timer iniciarPoligonos = new Timer(1000, e -> {
            if (currentPolygonIndex < poligonos.size()) {
                started.set(currentPolygonIndex, true); 
                currentPolygonIndex++;  // Pasar al siguiente polígono
            }
        });
        iniciarPoligonos.start();

        Timer movimientoContinuo = new Timer(16, e -> {  
            for (int i = 0; i < poligonos.size(); i++) {
                // Solo mover los polígonos que ya han comenzado a moverse, para sea todo fluido y no se trabe cuando inicie otro poligono
                if (started.get(i)) {
                    moverPoligono(i);
                }
            }

            repaint();  
        });
        movimientoContinuo.start();  
    }

    private void moverPoligono(int index) {
        Point posicion = posiciones.get(index);

        // Aquí se checa que la proporción tamaño velocidad pues cumpla con que el mas pequeño es el mas rápido
        double velocidad = velocidades.get(index);
        int dx = dxs.get(index);
        int dy = dys.get(index);

        // Actualizar la posición del polígono
        posicion.translate((int) (dx * velocidad / 100), (int) (dy * velocidad / 100));

        if (posicion.x < 0 || posicion.x > getWidth()) {
            dx = -dx;  // Invertir dirección en x, para que cuando toque un borde el poli rebote
            dxs.set(index, dx);  // Actualizar dirección
        }
        if (posicion.y < 0 || posicion.y > getHeight()) {
            dy = -dy;  // Invertir dirección en y para que cuando toque un brde el poli rebote
            dys.set(index, dy);  // Actualizar dirección
        }
    }
}
