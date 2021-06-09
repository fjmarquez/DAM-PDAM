package iesnervion.fjmarquez.pdam.ViewModels;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;

import iesnervion.fjmarquez.pdam.Entidades.Dia;
import iesnervion.fjmarquez.pdam.Entidades.Rutina;
import iesnervion.fjmarquez.pdam.Repositorios.RepositorioFirestoreRutinas;
import iesnervion.fjmarquez.pdam.Repositorios.RepositorioFirestoreHistorico;
import iesnervion.fjmarquez.pdam.Utiles.DiaSemana;

public class ViewModelRutina extends androidx.lifecycle.ViewModel{

    /* ATRIBUTOS */
    private RepositorioFirestoreRutinas mRepositorioFirestoreRutinas;
    private RepositorioFirestoreHistorico mRepositorioFirestoreHistorico;
    private ArrayList<Rutina> mListaRutinasUsuario;
    private ArrayList<Dia> mListaHistorico;
    private MutableLiveData<Rutina> rutinaActual;
    private DiaSemana mDiaSemanaSeleccionado;
    private Dia mDiaSeleccionado;
    private int mEjercicioRealizar;
    private Calendar mFechaSeleccionada;

    /* CONSTRUCTOR */
    public ViewModelRutina() {

        mFechaSeleccionada = Calendar.getInstance();
        mListaRutinasUsuario = new ArrayList<>();
        mListaHistorico = new ArrayList<>();
        rutinaActual = new MutableLiveData<>();
        rutinaActual.postValue(new Rutina());
        mRepositorioFirestoreRutinas = new RepositorioFirestoreRutinas();
        mRepositorioFirestoreHistorico = new RepositorioFirestoreHistorico();
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

    public ArrayList<Rutina> getmListaRutinasUsuario() {
        return mListaRutinasUsuario;
    }

    public ArrayList<Dia> getmListaHistorico() {
        return mListaHistorico;
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

    public void setmListaRutinasUsuario(ArrayList<Rutina> mListaRutinasUsuario) {
        this.mListaRutinasUsuario = mListaRutinasUsuario;
    }

    public void setmListaHistorico(ArrayList<Dia> mListaHistorico) {
        this.mListaHistorico = mListaHistorico;
    }

    /* FUNCIONES */
    public Task<DocumentSnapshot> obtenerRutinaActualUsuario(String uid){

        return mRepositorioFirestoreRutinas.obtenerRutinaActualUsuario(uid);

    }

    public Task<DocumentSnapshot> comprobarSiExisteRutinaUsuarioActual(){

        return mRepositorioFirestoreRutinas.comprobarSiExisteRutinaUsuarioActual();

    }

    public Task<QuerySnapshot> obtenerListaRutinasUsuario(){

        return mRepositorioFirestoreRutinas.obtenerListaRutinasUsuario();

    }

    public Task crearHistoricoDia(){

        mDiaSeleccionado.setRutina(rutinaActual.getValue().getUid());
        mDiaSeleccionado.setUid(mDiaSeleccionado.getUsuario()+"_"+mDiaSeleccionado.getRutina()+"_"+mDiaSeleccionado.getFecha());

        return  mRepositorioFirestoreHistorico.añadirOActualizarDiaHistoricoFirebase(mDiaSeleccionado);

    }

    public Task actualizarRutinaActualUsuario(){

        return mRepositorioFirestoreHistorico.añadirOActualizarDiaHistoricoFirebase(mDiaSeleccionado);

    }

    public Task<QuerySnapshot> obtenerHistoricosUsuarioHoy()
    {

        return mRepositorioFirestoreHistorico.obtenerHistoricosUsuarioHoy();

    }

    public Task<QuerySnapshot> obtenerHistoricosRangoFechas(ArrayList<String> dias){

        return mRepositorioFirestoreHistorico.obtenerHistoricosRangoFechas(dias);

    }

    public Task<Void> eliminarRutina(Rutina rutina){

        return mRepositorioFirestoreRutinas.eliminarRutinaFirestore(rutina);

    }

    public Task<QuerySnapshot> obtenerListaHistoricoUsuarioActual(){

        return mRepositorioFirestoreHistorico.obtenerListaHistoricoUsuarioActual();

    }


}
