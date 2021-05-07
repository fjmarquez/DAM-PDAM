package iesnervion.fjmarquez.pdam.Entidades;

import java.util.ArrayList;

import iesnervion.fjmarquez.pdam.Utiles.DiaSemana;

/**
 * Esta clase nos ayudara a saber que ejercicios corresponden a un dia en concreto.
 * Un dia tendra uno o varios ejercicios.
 */
public class Dia {

    /* ATRIBUTOS */
    private DiaSemana Dia;
    private ArrayList<Ejercicio> Ejercicios;

    /* CONSTRUCTORES */
    public Dia(DiaSemana dia, ArrayList<Ejercicio> ejercicios) {
        Dia = dia;
        Ejercicios = ejercicios;
    }

    public Dia(){

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
