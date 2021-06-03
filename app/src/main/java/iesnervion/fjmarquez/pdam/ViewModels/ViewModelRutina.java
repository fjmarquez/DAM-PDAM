package iesnervion.fjmarquez.pdam.ViewModels;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Calendar;

import iesnervion.fjmarquez.pdam.Entidades.Dia;
import iesnervion.fjmarquez.pdam.Entidades.Ejercicio;
import iesnervion.fjmarquez.pdam.Entidades.Rutina;
import iesnervion.fjmarquez.pdam.Repositorios.RepositorioFirebaseRutinas;
import iesnervion.fjmarquez.pdam.Utiles.DiaSemana;

public class ViewModelRutina extends androidx.lifecycle.ViewModel{

    /* ATRIBUTOS */
    private RepositorioFirebaseRutinas mRepositorioFirestoreRutinas;
    private MutableLiveData<Rutina> rutinaActual;
    private DiaSemana mDiaSemanaSeleccionado;
    private Dia mDiaSeleccionado;
    private int mEjercicioRealizar;
    private Calendar mFechaSeleccionada;

    /* CONSTRUCTOR */
    public ViewModelRutina() {

        mFechaSeleccionada = Calendar.getInstance();
        rutinaActual = new MutableLiveData<>();
        rutinaActual.postValue(new Rutina());
        mRepositorioFirestoreRutinas = new RepositorioFirebaseRutinas();
        mDiaSeleccionado = new Dia();
        mEjercicioRealizar = -1;

    }

    /* GETTERS */
    public MutableLiveData<Rutina> getRutinaActual() {
        return rutinaActual;
    }

    public DiaSemana getmDiaSemanaSeleccionado() {
        return mDiaSemanaSeleccionado;
    }

    public Dia getmDiaSeleccionado() {
        return mDiaSeleccionado;
    }

    public Calendar getmFechaSeleccionada() {
        return mFechaSeleccionada;
    }

    public int getmEjercicioRealizar() {
        return mEjercicioRealizar;
    }

    /* SETTERS */
    public void setRutinaActual(Rutina rutinaActual) {
        this.rutinaActual.setValue(rutinaActual);
    }

    public void setmDiaSemanaSeleccionado(DiaSemana mDiaSemanaSeleccionado) {
        this.mDiaSemanaSeleccionado = mDiaSemanaSeleccionado;
    }

    public void setmDiaSeleccionado(Dia mDiaSeleccionado) {
        this.mDiaSeleccionado = mDiaSeleccionado;
    }


    public void setmFechaSeleccionada(Calendar mFechaSeleccionada) {
        this.mFechaSeleccionada = mFechaSeleccionada;
    }

    public void setmEjercicioRealizar(int mEjercicioRealizar) {
        this.mEjercicioRealizar = mEjercicioRealizar;
    }

    /* FUNCIONES */
    public Task<DocumentSnapshot> obtenerRutinaActualUsuario(){

        return mRepositorioFirestoreRutinas.obtenerRutinaActualUsuario();

    }




}
