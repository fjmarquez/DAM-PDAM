package iesnervion.fjmarquez.pdam.Entidades;

import java.util.ArrayList;

import iesnervion.fjmarquez.pdam.Utiles.DificultadEjercicio;
import iesnervion.fjmarquez.pdam.Utiles.GrupoMuscular;

public class Ejercicio {

    /* ATRIBUTOS*/
    private String Uid;
    private String Nombre;
    private String Descripcion;
    private GrupoMuscular GrupoMuscular;
    private DificultadEjercicio Dificultad;
    private ArrayList<Serie> Series;
    private Boolean Material;

    /* CONSTRUCTORES */
    public Ejercicio(String uid, String nombre, String descripcion, GrupoMuscular grupoMuscular, DificultadEjercicio dificultad, ArrayList<Serie> series, Boolean material) {
        Uid = uid;
        Nombre = nombre;
        Descripcion = descripcion;
        GrupoMuscular = grupoMuscular;
        Dificultad = dificultad;
        Series = series;
        Material = material;
    }

    /* GETTERS */
    public String getUid() {
        return Uid;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getDescripcion() {
        return Descripcion;
    }

    public iesnervion.fjmarquez.pdam.Utiles.GrupoMuscular getGrupoMuscular() {
        return GrupoMuscular;
    }

    public DificultadEjercicio getDificultad() {
        return Dificultad;
    }

    public ArrayList<Serie> getSeries() {
        return Series;
    }

    public Boolean getMaterial() {
        return Material;
    }

    /* SETTERS */
    public void setUid(String uid) {
        Uid = uid;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public void setGrupoMuscular(iesnervion.fjmarquez.pdam.Utiles.GrupoMuscular grupoMuscular) {
        GrupoMuscular = grupoMuscular;
    }

    public void setDificultad(DificultadEjercicio dificultad) {
        Dificultad = dificultad;
    }

    public void setSeries(ArrayList<Serie> series) {
        Series = series;
    }

    public void setMaterial(Boolean material) {
        Material = material;
    }
}
