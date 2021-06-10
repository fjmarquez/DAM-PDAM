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
    private String Fecha;
    private Boolean Finalizado;
    private String Usuario;
    private String Rutina;
    private String Uid;

    /* CONSTRUCTORES */

    public Dia(DiaSemana dia, ArrayList<Ejercicio> ejercicios, String usuario, String rutina) {
        Dia = dia;
        Ejercicios = ejercicios;
        Fecha = null;
        Finalizado = false;
        Usuario = usuario;
        Rutina = rutina;
        Uid = null;
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

    public String getFecha() {
        return Fecha;
    }

    public Boolean getFinalizado() {
        return Finalizado;
    }

    public String getUsuario() {
        return Usuario;
    }

    public String getRutina() {
        return Rutina;
    }

    public String getUid() {
        return Uid;
    }

    /* SETTERS */

    public void setDia(DiaSemana dia) {
        Dia = dia;
    }

    public void setEjercicios(ArrayList<Ejercicio> ejercicios) {
        Ejercicios = ejercicios;
    }

    public void setFecha(String fecha) {
        Fecha = fecha;
    }

    public void setFinalizado(Boolean finalizado) {
        Finalizado = finalizado;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

    public void setRutina(String rutina) {
        Rutina = rutina;
    }

    public void setUid(String uid) {
        Uid = uid;
    }
}
