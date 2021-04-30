package iesnervion.fjmarquez.pdam.Entidades;

public class Serie {

    /* ATRIBUTOS */
    private int Repeticiones;
    private double Peso;

    /* CONSTRUCTORES */
    public Serie(int repeticiones, double peso) {
        Repeticiones = repeticiones;
        Peso = peso;
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
