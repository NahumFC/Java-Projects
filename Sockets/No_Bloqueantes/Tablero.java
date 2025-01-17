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

    public Tablero(int filas, int columnas, int numeroMinas) {
        this.filas = filas;
        this.columnas = columnas;
        this.numeroMinas = numeroMinas;
        this.tablero = new int[filas][columnas];
        this.descubierto = new boolean[filas][columnas];
        this.marcado = new boolean[filas][columnas];
        this.casillasPorDescubrir = (filas * columnas) - numeroMinas;
        generarTablero();
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
                    tableroVisible[i][j] = tablero[i][j];
                } else if (marcado[i][j]) {
                    tableroVisible[i][j] = -3;
                } else {
                    tableroVisible[i][j] = -2;
                }
            }
        }
        return tableroVisible;
    }

    public String obtenerTableroComoString() {
        StringBuilder sb = new StringBuilder();
        sb.append("   ");
        for (int i = 0; i < columnas; i++) {
            sb.append((char) ('A' + i)).append(" ");
        }
        sb.append("\n");

        for (int i = 0; i < filas; i++) {
            sb.append(String.format("%02d ", i + 1));
            for (int j = 0; j < columnas; j++) {
                if (descubierto[i][j]) {
                    if (tablero[i][j] == -1) {
                        sb.append("*").append(" ");
                    } else {
                        sb.append(tablero[i][j]).append(" ");
                    }
                } else if (marcado[i][j]) {
                    sb.append("?").append(" ");
                } else {
                    sb.append(".").append(" ");
                }
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public boolean destaparCasilla(int fila, int columna) {
        if (tablero[fila][columna] == -1) {
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
            for (int f = fila - 1; f <= fila + 1; f++) {
                for (int c = columna - 1; c <= columna + 1; c++) {
                    destaparRecursivo(f, c);
                }
            }
        }
    }

    public void marcarCasilla(int fila, int columna) {
        if (!descubierto[fila][columna] && !marcado[fila][columna]) {
            marcado[fila][columna] = true;
        }
    }

    public boolean verificarVictoria() {
        return casillasPorDescubrir == 0;
    }

    public void revelarTodasLasMinas() {
        for (int i = 0; i < filas; i++) {
            for (int j = 0; j < columnas; j++) {
                if (tablero[i][j] == -1) {
                    descubierto[i][j] = true;
                }
            }
        }
    }
}

