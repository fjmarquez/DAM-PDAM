package iesnervion.fjmarquez.pdam.ViewModels;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import iesnervion.fjmarquez.pdam.Entidades.Dia;
import iesnervion.fjmarquez.pdam.Entidades.Ejercicio;
import iesnervion.fjmarquez.pdam.Entidades.Rutina;
import iesnervion.fjmarquez.pdam.Repositorios.RepositorioFirebaseRutinas;
import iesnervion.fjmarquez.pdam.Repositorios.RepositorioFirestoreEjercicios;

public class ViewModelCreacionRutina extends androidx.lifecycle.ViewModel{

    /* ATRIBUTOS */
    private MutableLiveData<ArrayList<Dia>> DiasRutina;
    private int DiaSemanaSeleccionado;
    private Ejercicio EjercicioSeleccionado;
    private ArrayList<Ejercicio> ListadoEjerciciosMaster;
    private ArrayList<Ejercicio> ListadoEjercicios;
    private RepositorioFirestoreEjercicios mRepositorioFirestoreEjercicios;
    private RepositorioFirebaseRutinas mRepositorioFirestoreRutinas;

    /* CONSTRUTORES */
    public ViewModelCreacionRutina() {

        //ArrayList<Dia> Dias = new ArrayList<>();
        DiasRutina = new MutableLiveData<>();
        DiasRutina.postValue(new ArrayList<>());
        DiaSemanaSeleccionado = -1;
        EjercicioSeleccionado = null;
        mRepositorioFirestoreEjercicios = new RepositorioFirestoreEjercicios();
        mRepositorioFirestoreRutinas = new RepositorioFirebaseRutinas();

    }

    /* SETTERS */

    public void setDiasRutina(ArrayList<Dia> diasRutina) {
        DiasRutina.setValue(diasRutina);
    }

    public void setDiaSemanaSeleccionado(int diaSemanaSeleccionado) {
        DiaSemanaSeleccionado = diaSemanaSeleccionado;
    }

    public void setListadoEjerciciosMaster(ArrayList<Ejercicio> listadoEjercicios) {
        ListadoEjerciciosMaster = listadoEjercicios;
        //ListadoEjercicios = listadoEjercicios;
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

    public ArrayList<Ejercicio> getListadoEjerciciosMaster() {
        return ListadoEjerciciosMaster;
    }

    public ArrayList<Ejercicio> getListadoEjercicios() {
        return ListadoEjercicios;
    }

    /* FUNCIONES */

    public Task<QuerySnapshot> obtenerEjerciciosFirestore(){

        return mRepositorioFirestoreEjercicios.obtenerEjerciciosFirestore();

    }

    public Ejercicio getEjercicioSeleccionado() {
        return EjercicioSeleccionado;
    }

    public Task guardarNuevaRutinaFirestore(Rutina rutina){

        return mRepositorioFirestoreRutinas.añadirRutinaFirestore(rutina);

    }

}
