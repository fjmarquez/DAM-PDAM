package iesnervion.fjmarquez.pdam.Entidades;

import java.util.ArrayList;

public class Rutina {

    /* ATRIBUTOS */
    private String Uid;
    private ArrayList<Dia> Dias;

    /* CONSTRUCTORES */
    public Rutina(String uid, ArrayList<Dia> dias) {
        Uid = uid;
        Dias = dias;
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
