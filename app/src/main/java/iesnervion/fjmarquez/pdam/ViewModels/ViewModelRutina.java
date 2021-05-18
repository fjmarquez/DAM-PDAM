package iesnervion.fjmarquez.pdam.ViewModels;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import iesnervion.fjmarquez.pdam.Entidades.Dia;
import iesnervion.fjmarquez.pdam.Entidades.Ejercicio;
import iesnervion.fjmarquez.pdam.Entidades.Rutina;
import iesnervion.fjmarquez.pdam.Repositorios.RepositorioFirestoreEjercicios;
import iesnervion.fjmarquez.pdam.Utiles.DiaSemana;

public class ViewModelRutina extends androidx.lifecycle.ViewModel{

    /* ATRIBUTOS */
    private MutableLiveData<ArrayList<Dia>> DiasRutina;
    private int DiaSemanaSeleccionado;
    private Ejercicio EjercicioSeleccionado;
    private ArrayList<Ejercicio> ListadoEjercicios;
    private RepositorioFirestoreEjercicios mRepositorioFirestoreEjercicios;

    /* CONSTRUTORES */
    public ViewModelRutina() {

        //ArrayList<Dia> Dias = new ArrayList<>();
        DiasRutina = new MutableLiveData<>();
        DiasRutina.postValue(new ArrayList<>());
        DiaSemanaSeleccionado = -1;
        EjercicioSeleccionado = null;
        mRepositorioFirestoreEjercicios = new RepositorioFirestoreEjercicios();

    }

    /* SETTERS */

    public void setDiasRutina(ArrayList<Dia> diasRutina) {
        DiasRutina.setValue(diasRutina);
    }

    public void setDiaSemanaSeleccionado(int diaSemanaSeleccionado) {
        DiaSemanaSeleccionado = diaSemanaSeleccionado;
    }

    public void setListadoEjercicios(ArrayList<Ejercicio> listadoEjercicios) {
        ListadoEjercicios = listadoEjercicios;
    }

    public void setEjercicioSeleccionado(Ejercicio ejercicioSeleccionado) {
        EjercicioSeleccionado = ejercicioSeleccionado;
    }

    /* GETTERS*/
    public MutableLiveData<ArrayList<Dia>> getDiasRutina() {
        return DiasRutina;
    }

    public int getDiaSemanaSeleccionado() {
        return DiaSemanaSeleccionado;
    }

    public ArrayList<Ejercicio> getListadoEjercicios() {
        return ListadoEjercicios;
    }

    public Task<QuerySnapshot> obtenerEjerciciosFirestore(){

        return mRepositorioFirestoreEjercicios.obtenerEjerciciosFirestore();

    }

    public Ejercicio getEjercicioSeleccionado() {
        return EjercicioSeleccionado;
    }

}
