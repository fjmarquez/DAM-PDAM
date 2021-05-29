package iesnervion.fjmarquez.pdam.ViewModels;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;

import iesnervion.fjmarquez.pdam.Entidades.Rutina;
import iesnervion.fjmarquez.pdam.Repositorios.RepositorioFirebaseRutinas;
import iesnervion.fjmarquez.pdam.Utiles.DiaSemana;

public class ViewModelRutina extends androidx.lifecycle.ViewModel{

    /* ATRIBUTOS */
    private RepositorioFirebaseRutinas mRepositorioFirestoreRutinas;
    private MutableLiveData<Rutina> rutinaActual;
    private DiaSemana mDiaSemanaSeleccionado;

    /* CONSTRUCTOR */
    public ViewModelRutina() {

        rutinaActual = new MutableLiveData<>();
        rutinaActual.postValue(new Rutina());
        mRepositorioFirestoreRutinas = new RepositorioFirebaseRutinas();

    }

    /* GETTERS */
    public MutableLiveData<Rutina> getRutinaActual() {
        return rutinaActual;
    }

    public DiaSemana getmDiaSemanaSeleccionado() {
        return mDiaSemanaSeleccionado;
    }

    /* SETTERS */
    public void setRutinaActual(Rutina rutinaActual) {
        this.rutinaActual.setValue(rutinaActual);
    }

    public void setmDiaSemanaSeleccionado(DiaSemana mDiaSemanaSeleccionado) {
        this.mDiaSemanaSeleccionado = mDiaSemanaSeleccionado;
    }

    /* FUNCIONES */
    public Task<DocumentSnapshot> obtenerRutinaActualUsuario(){

        return mRepositorioFirestoreRutinas.obtenerRutinaActualUsuario();

    }




}
