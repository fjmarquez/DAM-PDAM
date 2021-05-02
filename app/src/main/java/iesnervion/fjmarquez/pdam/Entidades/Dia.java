package iesnervion.fjmarquez.pdam.Entidades;

import java.util.ArrayList;

import iesnervion.fjmarquez.pdam.Utiles.DiaSemana;

public class Dia {

    /* ATRIBUTOS */
    private DiaSemana Dia;
    private ArrayList<Ejercicio> Ejercicios;

    /* CONSTRUCTORES */
    public Dia(DiaSemana dia, ArrayList<Ejercicio> ejercicios) {
        Dia = dia;
        Ejercicios = ejercicios;
    }

    /* GETTERS */
    public DiaSemana getDia() {
        return Dia;
    }

    public ArrayList<Ejercicio> getEjercicios() {
        return Ejercicios;
    }

    /* SETTERS */
    public void setDia(DiaSemana dia) {
        Dia = dia;
    }

    public void setEjercicios(ArrayList<Ejercicio> ejercicios) {
        Ejercicios = ejercicios;
    }

}
