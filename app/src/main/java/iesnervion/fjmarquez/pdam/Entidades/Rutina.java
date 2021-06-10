package iesnervion.fjmarquez.pdam.Entidades;

import java.util.ArrayList;

/**
 * Esta clase facilita el manejo de los datos relacionados con cada dia, ejericio o series, contiene informacion sobre todo
 * lo comentado anteriormente. Es la "unidad de medida" de mas alto nivel en el proyecto.
 * Una rutina tendra uno o varios dias.
 */
public class Rutina {

    /* ATRIBUTOS */

    private String Uid;
    private String Nombre;
    private ArrayList<Dia> Dias;
    private String Usuario;

    /* CONSTRUCTORES */

    public Rutina(String uid, String nombre, ArrayList<Dia> dias, String usuario) {
        Uid = uid;
        Nombre = nombre;
        Dias = dias;
        Usuario = usuario;
    }

    public Rutina(){

    }

    /* GETTERS */

    public String getUid() {
        return Uid;
    }

    public ArrayList<Dia> getDias() {
        return Dias;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getUsuario() {
        return Usuario;
    }

    /* SETTERS */

    public void setUid(String uid) {
        Uid = uid;
    }

    public void setDias(ArrayList<Dia> dias) {
        Dias = dias;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public void setUsuario(String usuario) {
        Usuario = usuario;
    }

}
