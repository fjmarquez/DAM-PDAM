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
    private ArrayList<Dia> Dias;

    /* CONSTRUCTORES */
    public Rutina(String uid, ArrayList<Dia> dias) {
        Uid = uid;
        Dias = dias;
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

    /* SETTERS */
    public void setUid(String uid) {
        Uid = uid;
    }

    public void setDias(ArrayList<Dia> dias) {
        Dias = dias;
    }

}
