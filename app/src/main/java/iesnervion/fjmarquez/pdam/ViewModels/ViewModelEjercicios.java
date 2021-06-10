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
    }

    public void setListadoEjercicios(ArrayList<Ejercicio> listadoEjercicios) {
        ListadoEjercicios = listadoEjercicios;
    }

    public void setEjercicioSeleccionado(Ejercicio ejercicioSeleccionado) {
        EjercicioSeleccionado = ejercicioSeleccionado;
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

    public Ejercicio getEjercicioSeleccionado() {
        return EjercicioSeleccionado;
    }

    /* FUNCIONES */

    /**
     * Obtiene el ultimo id asignado a un chip que se creo dinamicamente.
     *
     * @return Devuelve un entero que indica el ultimo id repartido a un chip.
     */
    public int obtenerIdsChipCreados(){
        return SUM_ENUMS_FILTRO * mChipsCreados;
    }

    /**
     * Obtiene a traves del Repositorio de ejercicios una lista con todos los ejercicios almacenados en Firestore.
     *
     * @return Devuelve una tarea, la cual controlaremos en el Fragment correspondiente.
     */
    public Task<QuerySnapshot> obtenerEjerciciosFirestore(){

        return mRepositorioFirestoreEjercicios.obtenerEjerciciosFirestore();

    }

    /**
     * Guarda una rutina en Firestore, a traves del Repositorio de rutinas.
     *
     * @return Devuelve una tarea, la cual controlaremos en el Fragment correspondiente.
     */
    public Task guardarNuevaRutinaFirestore(Rutina rutina){

        return mRepositorioFirestoreRutinas.añadirRutinaFirestore(rutina);

    }

    /**
     * Incrementa en uno las veces que se generan dinamicamente los chips
     */
    public void incrementarmChipsCreados() {
        this.mChipsCreados = this.mChipsCreados+1;
    }

}
