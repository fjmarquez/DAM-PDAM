package iesnervion.fjmarquez.pdam.Entidades;

import com.google.type.DateTime;

import java.util.ArrayList;
import java.util.Calendar;

import iesnervion.fjmarquez.pdam.Utiles.DiaSemana;

/**
 * Esta clase nos ayudara a saber que ejercicios corresponden a un dia en concreto.
 * Un dia tendra uno o varios ejercicios.
 */
public class Dia {

    /* ATRIBUTOS */
    private DiaSemana Dia;
    private ArrayList<Ejercicio> Ejercicios;
    private Calendar Inicio;
    private Calendar Finalizacion;
    private Boolean Finalizado;

    /* CONSTRUCTORES */
    public Dia(DiaSemana dia, ArrayList<Ejercicio> ejercicios) {
        Dia = dia;
        Ejercicios = ejercicios;
        Inicio = null;
        Finalizacion = null;
        Finalizado = false;
    }

    public Dia(){
        Ejercicios = new ArrayList<>();
    }

    /* GETTERS */
    public DiaSemana getDia() {
        return Dia;
    }

    public ArrayList<Ejercicio> getEjercicios() {
        return Ejercicios;
    }

    public Calendar getInicio() {
        return Inicio;
    }

    public Calendar getFinalizacion() {
        return Finalizacion;
    }

    public Boolean getFinalizado() {
        return Finalizado;
    }

    /* SETTERS */
    public void setDia(DiaSemana dia) {
        Dia = dia;
    }

    public void setEjercicios(ArrayList<Ejercicio> ejercicios) {
        Ejercicios = ejercicios;
    }

    public void setInicio(Calendar inicio) {
        Inicio = inicio;
    }

    public void setFinalizacion(Calendar finalizacion) {
        Finalizacion = finalizacion;
        //Finalizado = true;
    }

    public void setFinalizado(Boolean finalizado) {
        Finalizado = finalizado;
    }

}
