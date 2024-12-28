import java.io.Serializable;
import java.util.Random;

public class Tablero implements Serializable {
    private int[][] tablero;
    private boolean[][] descubierto;
    private boolean[][] marcado;
    private int filas;
    private int columnas;
    private int numeroMinas;
    private int casillasPorDescubrir;
    private int minasMarcadas;

    // Esto es un constructor, con esto podemos iniciar variables instanciandolas en nuestra clase, y le pase como parámetros el tamaño y el numero de minas las cuales se fijan en el servidor sin embargo el jugador lo determina escogiendo la dificultad... la dificultadad se la pedimos en el cliente y el servidor determina el tamaño
    public Tablero(int filas, int columnas, int numeroMinas) {
        this.filas = filas;
        this.columnas = columnas;
        this.numeroMinas = numeroMinas;
        this.tablero = new int[filas][columnas];
        this.descubierto = new boolean[filas][columnas];
        this.marcado = new boolean[filas][columnas];
        this.casillasPorDescubrir = (filas * columnas) - numeroMinas;
        this.minasMarcadas = 0;
    }

    public void generarTablero() {
        Random rand = new Random();

        for (int i = 0; i < numeroMinas; i++) {
            int fila, columna;
            do {
                fila = rand.nextInt(filas);
                columna = rand.nextInt(columnas);
            } while (tablero[fila][columna] == -1);

            tablero[fila][columna] = -1;

            // Con estos for anidados las casillas adyacentes se actualizan
            for (int f = fila - 1; f <= fila + 1; f++) {
                for (int c = columna - 1; c <= columna + 1; c++) {
                    if (f >= 0 && f < filas && c >= 0 && c < columnas && tablero[f][c] != -1) {
                        tablero[f][c]++;
                    }
                }
            }
        }
    }

    public int[][] obtenerTablero() {
        int[][] tableroVisible = new int[filas][columnas];
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (descubierto[i][j]) {
                    if (tablero[i][j] == 0) {
                        tableroVisible[i][j] = -4; // Casilla vacía aledaña descubierta
                    } else {
                        tableroVisible[i][j] = tablero[i][j];
                    }
                } else if (marcado[i][j]) {
                    tableroVisible[i][j] = -3; // Casilla marcada
                } else {
                    tableroVisible[i][j] = -2; // Casilla no descubierta
                }
            }
        }
        return tableroVisible;
    }

    public boolean destaparCasilla(int fila, int columna) {
        if (tablero[fila][columna] == -1) {
            // El jugador destapó una mina y por ende pues muere
            return false;
        }

        destaparRecursivo(fila, columna);
        return true;
    }

    private void destaparRecursivo(int fila, int columna) {
        if (fila < 0 || fila >= filas || columna < 0 || columna >= columnas || descubierto[fila][columna] || marcado[fila][columna]) {
            return;
        }

        descubierto[fila][columna] = true;
        casillasPorDescubrir--;

        if (tablero[fila][columna] == 0) {
            // Con estos for anidados, si el jugador destapa una casilla en la que no hay nada pues descubre tods las casillas que la rodean
            for (int f = fila - 1; f <= fila + 1; f++) {
                for (int c = columna - 1; c <= columna + 1; c++) {
                    if (f != fila || c != columna) {
                        destaparRecursivo(f, c);
                    }
                }
            }
        }
    }

    // Función para marcar las casillas
    public void marcarCasilla(int fila, int columna) {
        if (!descubierto[fila][columna] && !marcado[fila][columna] && minasMarcadas < numeroMinas) {
            marcado[fila][columna] = true;
            minasMarcadas++;
        } else if (marcado[fila][columna]) {
            marcado[fila][columna] = false;
            minasMarcadas--;
        }
    }
    //Función para desmarcar las casillas
    public void desmarcarCasilla(int fila, int columna) {
        if (fila >= 0 && fila < filas && columna >= 0 && columna < columnas) {
            marcado[fila][columna] = !marcado[fila][columna];
            if (marcado[fila][columna]) {
                minasMarcadas++;
            } else {
                minasMarcadas--;
            }
        }
    }

    public boolean verificarVictoria() {
        // l jugador gana si todas las minas han sido marcadas correctamente
        if (minasMarcadas == numeroMinas) {
            for (int i = 0; i < filas; i++) {
                for (int j = 0; j < columnas; j++) {
                    if (marcado[i][j] && tablero[i][j] != -1) {
                        return false; // Hay una mina marcada incorrectamente y por ende no gana, tiene que desmarcar y corregir 
                    }
                }
            }
            return true;
        }
        return casillasPorDescubrir == 0;
    }

    // Cuando el jugador muere o gana esta función llamamos para que se muestren todas ls minad 
    public void revelarTodasLasMinas() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (tablero[i][j] == -1) {
                    descubierto[i][j] = true; // Revelan todas las minas
                }
            }
        }
    }
    // Como la cantidad de minas es igual a las marcas pues cada que marcamos las minas tenemos que actualizar
    public int obtenerMarcasRestantes() {
        return numeroMinas - minasMarcadas;
    }
}
