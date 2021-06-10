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

    /**
     * Obtiene la rutina del usuario actual en Firestore, a traves del Repositorio de rutinas.
     *
     * @param uid String que contiene el uid del usuario actual.
     * @return Devuelve una tarea, la cual controlaremos en el Fragment correspondiente.
     */
    public Task<DocumentSnapshot> obtenerRutinaActualUsuario(String uid){

        return mRepositorioFirestoreRutinas.obtenerRutinaActualUsuario(uid);

    }

    /**
     * Comprueba si el usuario actual tiene al alguna rutina en Firestore, a traves del Repositorio de rutinas.
     *
     * @return Devuelve una tarea, la cual controlaremos en el Fragment correspondiente.
     */
    public Task<DocumentSnapshot> comprobarSiExisteRutinaUsuarioActual(){

        return mRepositorioFirestoreRutinas.comprobarSiExisteRutinaUsuarioActual();

    }

    /**
     * Obtiene una lista todas las rutinas correspondientes al usuario actual en Firestore, a traves del Repositorio de rutinas.
     *
     * @return Devuelve una tarea, la cual controlaremos en el Fragment correspondiente.
     */
    public Task<QuerySnapshot> obtenerListaRutinasUsuario(){

        return mRepositorioFirestoreRutinas.obtenerListaRutinasUsuario();

    }

    /**
     * Crea un nuevo historico en Firestore, a traves del Repositorio de historicos.
     *
     * @return Devuelve una tarea, la cual controlaremos en el Fragment correspondiente.
     */
    public Task crearHistoricoDia(){

        mDiaSeleccionado.setRutina(rutinaActual.getValue().getUid());
        mDiaSeleccionado.setUid(mDiaSeleccionado.getUsuario()+"_"+mDiaSeleccionado.getRutina()+"_"+mDiaSeleccionado.getFecha());

        return  mRepositorioFirestoreHistorico.añadirOActualizarDiaHistoricoFirebase(mDiaSeleccionado);

    }

    /**
     * Actualiza la rutina actual en Firestore, a traves del Repositorio de rutinas.
     *
     * @return Devuelve una tarea, la cual controlaremos en el Fragment correspondiente.
     */
    public Task actualizarRutinaActualUsuario(){

        return mRepositorioFirestoreHistorico.añadirOActualizarDiaHistoricoFirebase(mDiaSeleccionado);

    }

    /**
     * Obtiene, si existe, el historico correspondiente al dia de hoy en Firestore, a traves del Repositorio de historicos.
     *
     * @return Devuelve una tarea, la cual controlaremos en el Fragment correspondiente.
     */
    public Task<QuerySnapshot> obtenerHistoricosUsuarioHoy()
    {

        return mRepositorioFirestoreHistorico.obtenerHistoricosUsuarioHoy();

    }

    /**
     * Obtiene un historico o listado de historicos cuya fecha cumplan el rango en Firestore, a traves del Repositorio de rutinas.
     *
     * @return Devuelve una tarea, la cual controlaremos en el Fragment correspondiente.
     */
    public Task<QuerySnapshot> obtenerHistoricosRangoFechas(ArrayList<String> dias){

        return mRepositorioFirestoreHistorico.obtenerHistoricosRangoFechas(dias);

    }

    /**
     * Elimina una rutina en Firestore, a traves del Repositorio de rutinas.
     *
     * @return Devuelve una tarea, la cual controlaremos en el Fragment correspondiente.
     */
    public Task<Void> eliminarRutina(Rutina rutina){

        return mRepositorioFirestoreRutinas.eliminarRutinaFirestore(rutina);

    }

    /**
     * Obtiene un listado de historicos correspondientes al usuario actual en Firestore, a traves del Repositorio de historicos.
     *
     * @return Devuelve una tarea, la cual controlaremos en el Fragment correspondiente.
     */
    public Task<QuerySnapshot> obtenerListaHistoricoUsuarioActual(){

        return mRepositorioFirestoreHistorico.obtenerListaHistoricoUsuarioActual();

    }

}
