package iesnervion.fjmarquez.pdam.Entidades;

import android.graphics.Bitmap;
import android.text.BoringLayout;

import java.util.ArrayList;
import java.util.List;

import iesnervion.fjmarquez.pdam.Utiles.DificultadEjercicio;
import iesnervion.fjmarquez.pdam.Utiles.GrupoMuscular;

/**
 * Clase destinada a organizar y facilitar la informacion necesaria de cada ejercicio.
 * Despues de la clase Serie sera la segunda "unidad de medida". Un ejericicio tendra una o varias series.
 */
public class Ejercicio {

    /* ATRIBUTOS*/

    private String Uid;
    private String Nombre;
    private String Descripcion;
    private GrupoMuscular GrupoMuscular;
    private DificultadEjercicio Dificultad;
    private ArrayList<Serie> Series;
    private Boolean Material;
    private Boolean BandasElasticas;
    private String Gif;

    /* CONSTRUCTORES */

    public Ejercicio() {
    }

    public Ejercicio(String uid, String nombre, String descripcion, iesnervion.fjmarquez.pdam.Utiles.GrupoMuscular grupoMuscular, DificultadEjercicio dificultad, ArrayList<Serie> series, Boolean material, Boolean bandasElasticas, String gif) {
        Uid = uid;
        Nombre = nombre;
        Descripcion = descripcion;
        GrupoMuscular = grupoMuscular;
        Dificultad = dificultad;
        Series = series;
        Material = material;
        BandasElasticas = bandasElasticas;
        Gif = gif;
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

    public Boolean getBandasElasticas() {
        return BandasElasticas;
    }

    public String getGif() {
        return Gif;
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

    public void setBandasElasticas(Boolean bandasElasticas) {
        BandasElasticas = bandasElasticas;
    }

    public void setGif(String gif) {
        Gif = gif;
    }

}
