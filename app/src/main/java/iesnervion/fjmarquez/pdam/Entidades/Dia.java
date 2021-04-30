package iesnervion.fjmarquez.pdam.Entidades;

import java.util.ArrayList;

import iesnervion.fjmarquez.pdam.Utiles.DiaSemana;

public class Dia {

    /* ATRIBUTOS */
    private DiaSemana Dia;
    private ArrayList<Ejercicio> Ejercicios;
    private boolean Descanso;

    /* CONSTRUCTORES */
    public Dia(DiaSemana dia, ArrayList<Ejercicio> ejercicios, boolean descanso) {
        Dia = dia;
        Ejercicios = ejercicios;
        Descanso = descanso;
    }

    /* GETTERS */
    public DiaSemana getDia() {
        return Dia;
    }

    public ArrayList<Ejercicio> getEjercicios() {
        return Ejercicios;
    }

    public boolean isDescanso() {
        return Descanso;
    }

    /* SETTERS */
    public void setDia(DiaSemana dia) {
        Dia = dia;
    }

    public void setEjercicios(ArrayList<Ejercicio> ejercicios) {
        Ejercicios = ejercicios;
    }

    public void setDescanso(boolean descanso) {
        Descanso = descanso;
    }

}
