package iesnervion.fjmarquez.pdam.Entidades;

/**
 * Es la clase destinada a facilitar el manejo de datos en el historico los registros del dia en cada serie de un ejercicio.
 * Seria la "unidad de medida" mas basica del proyecto
 */
public class Serie {

    /* ATRIBUTOS */
    private int Repeticiones;
    private double Peso;
    private boolean Terminada;

    /* CONSTRUCTORES */
    public Serie(int repeticiones, double peso, boolean terminada) {
        Repeticiones = repeticiones;
        Peso = peso;
    }

    public Serie() {
        Repeticiones = 10;
        Peso = 0.0;
    }

    /* GETTERS */
    public int getRepeticiones() {
        return Repeticiones;
    }

    public double getPeso() {
        return Peso;
    }

    /* SETTERS */
    public void setPeso(double peso) {
        Peso = peso;
    }

    public void setRepeticiones(int repeticiones) {
        Repeticiones = repeticiones;
    }

}
