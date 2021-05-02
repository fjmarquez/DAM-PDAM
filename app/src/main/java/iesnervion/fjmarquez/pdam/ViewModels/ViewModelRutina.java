package iesnervion.fjmarquez.pdam.ViewModels;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;

import iesnervion.fjmarquez.pdam.Entidades.Dia;
import iesnervion.fjmarquez.pdam.Entidades.Rutina;

public class ViewModelRutina extends androidx.lifecycle.ViewModel{

    /* ATRIBUTOS */
    private MutableLiveData<ArrayList<Dia>> DiasRutina;

    /* CONSTRUTORES */
    public ViewModelRutina() {

        //ArrayList<Dia> Dias = new ArrayList<>();
        DiasRutina = new MutableLiveData<>();
        DiasRutina.postValue(new ArrayList<>());

    }

    /* SETTERS */

    public void setDiasRutina(ArrayList<Dia> diasRutina) {
        DiasRutina.setValue(diasRutina);
    }

    /* GETTERS*/
    public MutableLiveData<ArrayList<Dia>> getDiasRutina() {
        return DiasRutina;
    }
}
