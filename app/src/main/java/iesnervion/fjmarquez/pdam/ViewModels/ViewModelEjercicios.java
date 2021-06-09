package iesnervion.fjmarquez.pdam.ViewModels;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import iesnervion.fjmarquez.pdam.Entidades.Dia;
import iesnervion.fjmarquez.pdam.Entidades.Ejercicio;
import iesnervion.fjmarquez.pdam.Entidades.Rutina;
import iesnervion.fjmarquez.pdam.Repositorios.RepositorioFirestoreRutinas;
import iesnervion.fjmarquez.pdam.Repositorios.RepositorioFirestoreEjercicios;
import iesnervion.fjmarquez.pdam.Utiles.DificultadEjercicio;
import iesnervion.fjmarquez.pdam.Utiles.GrupoMuscular;
import iesnervion.fjmarquez.pdam.Utiles.Materiales;

public class ViewModelEjercicios extends androidx.lifecycle.ViewModel{

    /* ATRIBUTOS */
    private MutableLiveData<Rutina> DiasRutina;
    private int DiaSemanaSeleccionado;
    private Ejercicio EjercicioSeleccionado;
    private ArrayList<Ejercicio> ListadoEjerciciosMaster;
    private ArrayList<Ejercicio> ListadoEjercicios;
    private RepositorioFirestoreEjercicios mRepositorioFirestoreEjercicios;
    private RepositorioFirestoreRutinas mRepositorioFirestoreRutinas;
    private int mChipsCreados;
    private final int SUM_ENUMS_FILTRO = GrupoMuscular.values().length + DificultadEjercicio.values().length + Materiales.values().length;

    /* CONSTRUTORES */
    public ViewModelEjercicios() {

        //ArrayList<Dia> Dias = new ArrayList<>();
        DiasRutina = new MutableLiveData<>();
        DiasRutina.postValue(new Rutina());
        DiaSemanaSeleccionado = -1;
        EjercicioSeleccionado = null;
        mRepositorioFirestoreEjercicios = new RepositorioFirestoreEjercicios();
        mRepositorioFirestoreRutinas = new RepositorioFirestoreRutinas();
        mChipsCreados = -1;

    }

    /* SETTERS */

    public void setDiasRutina(Rutina diasRutina) {
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

    public void incrementarmChipsCreados() {
        this.mChipsCreados = this.mChipsCreados+1;
    }

    /* GETTERS*/
    public MutableLiveData<Rutina> getDiasRutina() {
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

    public int getmChipsCreados() {
        return mChipsCreados;
    }

    /* FUNCIONES */

    public int obtenerIdsChipCreados(){
        return SUM_ENUMS_FILTRO * mChipsCreados;
    }

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
