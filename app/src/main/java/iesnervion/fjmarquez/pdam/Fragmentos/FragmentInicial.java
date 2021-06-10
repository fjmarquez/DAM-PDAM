package iesnervion.fjmarquez.pdam.Fragmentos;

import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Calendar;
import java.util.Date;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import iesnervion.fjmarquez.pdam.Adaptadores.AdaptadorEjerciciosDiaRutina;
import iesnervion.fjmarquez.pdam.Entidades.Dia;
import iesnervion.fjmarquez.pdam.Entidades.Rutina;
import iesnervion.fjmarquez.pdam.Entidades.Usuario;
import iesnervion.fjmarquez.pdam.R;
import iesnervion.fjmarquez.pdam.Utiles.DiaSemana;
import iesnervion.fjmarquez.pdam.Utiles.TipoFragmento;
import iesnervion.fjmarquez.pdam.Utiles.Utiles;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelRutina;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelUsuario;

/**
 * Fragment principal de la aplicacion. En el se podran visualizar los ejercicios de cada dia,
 * asi como iniciar y finalizar el dia.
 */
public class FragmentInicial extends Fragment implements View.OnClickListener{

    /* ATRIBUTOS */

    private View mFragmentView;

    private ViewModelRutina mViewModelRutina;
    private AdaptadorEjerciciosDiaRutina mAdaptadorEjerciciosDiaRutina;
    private RecyclerView.LayoutManager mLayoutManager;

    private ViewModelUsuario mViewModelUsuario;

    private Calendar startDate;
    private Calendar endDate;

    private MaterialToolbar mToolbar;
    private TextView mTVDiaActual;
    private ConstraintLayout mCLDescanso;
    private ImageView mIVDescanso;
    private HorizontalCalendar mCalendarioHorizontal;
    private RecyclerView mRVEjerciciosDiaRutina;
    private MaterialToolbar mMTInicio;
    private FloatingActionButton mBtnComenzar;
    private FloatingActionButton mBtnFinalizar;

    private AlertDialog mAlertDialogTerminarRutina;
    private boolean dialogFinalizarDiaCreado;

    /* CONSTRUCTOR */

    public FragmentInicial() {

    }

    public static FragmentInicial newInstance() {
        FragmentInicial fragment = new FragmentInicial();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Instancio los ViewModels necesarios

        mViewModelRutina = new ViewModelProvider(getActivity()).get(ViewModelRutina.class);
        mViewModelUsuario = new ViewModelProvider(getActivity()).get(ViewModelUsuario.class);

        //Fecha hace un mes
        startDate = Calendar.getInstance();
        startDate.add(Calendar.WEEK_OF_MONTH, -1);

        //Fecha dentro de un mes
        endDate = Calendar.getInstance();
        endDate.add(Calendar.WEEK_OF_MONTH, 1);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFragmentView = inflater.inflate(R.layout.fragment_inicial, container, false);

        mCLDescanso = mFragmentView.findViewById(R.id.clDescanso);

        mToolbar = mFragmentView.findViewById(R.id.mtInicio);
        mTVDiaActual = mFragmentView.findViewById(R.id.tvDiaSemana);
        mIVDescanso = mFragmentView.findViewById(R.id.ivDescanso);
        mRVEjerciciosDiaRutina = mFragmentView.findViewById(R.id.rvEjerciciosDia);
        mRVEjerciciosDiaRutina.setHasFixedSize(true);
        mMTInicio = mFragmentView.findViewById(R.id.mtInicio);
        mBtnComenzar = mFragmentView.findViewById(R.id.btnComenzar);
        mBtnComenzar.setOnClickListener(this);
        mBtnFinalizar = mFragmentView.findViewById(R.id.btnFinalizar);
        mBtnFinalizar.setOnClickListener(this);
        dialogFinalizarDiaCreado = false;

        accionesMenu();
        configurarCalendarioHorizontal();
        actualizarDiaActual(startDate);
        fechaEsHoy(Calendar.getInstance());
        comprobarSiExisteHistorico();

        return mFragmentView;
    }

    /**
     * Comprueba si existe un historico con la fecha de hoy en Firestore.
     */
    public void comprobarSiExisteHistorico(){

        mViewModelRutina.obtenerHistoricosUsuarioHoy().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    for (DocumentSnapshot  document: task.getResult()
                    ) {
                        //Parseo del dia
                        Dia mDia = document.toObject(Dia.class);
                        //Lo establezco como dia seleccionado
                        mViewModelRutina.setmDiaSeleccionado(mDia);
                        //Configuro y relleno el recyclerView
                        configurarRecyclerViewEjerciciosDia();
                    }
                }
                //Si no existe ningun historico hoy, cargo el usuario para despues cargar el dia de la rutina correspondiente
                if (task.getResult().isEmpty()){
                    obtenerDatosUsuario();
                }
            }
        });



    }

    /**
     * Configura las acciones del menu del Fragment Inicial
     */
    public void accionesMenu(){

        //Configura las acciones a realizar cuando se pulsa sobre un MenuItem
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                //Comprueba si el historico del dia esta iniciado o no, si esta iniciado el menu no respondera
                if (mBtnComenzar.getVisibility() == View.VISIBLE ||
                    (mBtnComenzar.getVisibility() == View.GONE &&
                    mBtnFinalizar.getVisibility() == View.GONE)){

                    switch (item.getItemId()){
                        case R.id.historicoMenu:
                            mViewModelUsuario.setmTipoFragmento(TipoFragmento.HISTORICO);
                            break;
                        case R.id.perfilMenu:
                            mViewModelUsuario.setmTipoFragmento(TipoFragmento.PERFIL);
                            break;
                    }

                }

                return false;
            }
        });

    }

    /**
     * Instancia y configura el componente HorizontalCalendar que se muestra en el Fragment Inicial, asi como
     * controla el evento onDateSelected que se dispara cada vez que usuario selecciona un fecha
     */
    public void configurarCalendarioHorizontal(){

        mCalendarioHorizontal = new HorizontalCalendar.Builder(mFragmentView, R.id.calendarView)
                .range(startDate, endDate)//rango de fechas
                .datesNumberOnScreen(5)//dias mostrados en pantalla simultaneamente
                .configure()
                .formatMiddleText("dd")//Formato dia en numeros
                .formatBottomText("EEE")//Formato dia en letras
                .showBottomText(false)
                .sizeMiddleText(26)
                .textColor(Color.LTGRAY, Color.WHITE)
                .end()
                .build();

        //Evento del calendarioHorizontal, el cual se lanza cada vez que se selecciona un dia en el calendario
        mCalendarioHorizontal.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
                //Solamente respondera si el dia no esta iniciado
                if (mViewModelRutina.getmDiaSeleccionado().getFecha() == null ||
                    mViewModelRutina.getmDiaSeleccionado().getFinalizado()){

                    mViewModelRutina.setmFechaSeleccionada(date);
                    actualizarDiaActual(date);

                    //Si el dia se corresponde con el de hoy comprueba si existe un historico para mostrar
                    if(fechaEsHoy(date)){
                        comprobarSiExisteHistorico();
                    //Si el dia no se corresponde con el de hoy obtiene los datos de usuario y mas tarde obtiene su rutina
                    }else {
                        obtenerDatosUsuario();
                    }

                //Si el dia esta iniciado redirige al dia actual
                }else {
                    mCalendarioHorizontal.goToday(false);
                }

            }

        });

    }

    /**
     * Funcion que recibe un objeto Calendar y comprueba si ese objeto se corresponde con la fecha de hoy,
     * Devolviendo false en caso negativo y true en caso positivo.
     *
     * @param fecha Objeto Calendar recibido, se comprobara si su fecha se corresponde con la del dia actual.
     * @return Devulve un valor booleano en funcion de si el parametro recibido coincide con la fecha actual.
     */
    public boolean fechaEsHoy(Calendar fecha){

        boolean respuesta = false;
        Date hoy = Calendar.getInstance().getTime();
        String sHoy = DateFormat.format("dd-MM-yyyy", hoy).toString();
        Date fechaRecibida = fecha.getTime();
        String sFechaRecibida = DateFormat.format("dd-MM-yyyy", fechaRecibida).toString();

        if(sHoy.equals(sFechaRecibida)) {

            respuesta = true;

        }

        return respuesta;

    }

    /**
     * Funcion que actualiza un TextView con una cadena de texto dependiendo del dia de la semana
     * en el objeto calendar recibido por parametros.
     *
     * @param fecha Objeto Calendar, del cual extraeremos el dia de la semana correspondiente a la fecha.
     */
    public void actualizarDiaActual(Calendar fecha){

        mTVDiaActual.setText(Utiles.capitalizar(DateFormat.format("EEEE", fecha).toString()));

        int diaSemana = fecha.get(Calendar.DAY_OF_WEEK);

        switch (diaSemana){

            case 1:
                mViewModelRutina.setmDiaSemanaSeleccionado(DiaSemana.DOMINGO);
                break;

            case 2:
                mViewModelRutina.setmDiaSemanaSeleccionado(DiaSemana.LUNES);
                break;

            case 3:
                mViewModelRutina.setmDiaSemanaSeleccionado(DiaSemana.MARTES);
                break;

            case 4:
                mViewModelRutina.setmDiaSemanaSeleccionado(DiaSemana.MIERCOLES);
                break;

            case 5:
                mViewModelRutina.setmDiaSemanaSeleccionado(DiaSemana.JUEVES);
                break;

            case 6:
                mViewModelRutina.setmDiaSemanaSeleccionado(DiaSemana.VIERNES);
                break;

            case 7:
                mViewModelRutina.setmDiaSemanaSeleccionado(DiaSemana.SABADO);
                break;

        }

    }

    /**
     * Instancia y configura lo necesario para mostrar un RecyclerView con la informacion correspondiente a los
     * ejercicios del dia seleccionado.
     */
    public void configurarRecyclerViewEjerciciosDia(){

        mLayoutManager = new LinearLayoutManager(getContext());
        mRVEjerciciosDiaRutina.setLayoutManager(mLayoutManager);
        mAdaptadorEjerciciosDiaRutina = new AdaptadorEjerciciosDiaRutina(mViewModelRutina.getmDiaSeleccionado().getEjercicios());
        mRVEjerciciosDiaRutina.setAdapter(mAdaptadorEjerciciosDiaRutina);

        mAdaptadorEjerciciosDiaRutina.setOnItemClickListener(new AdaptadorEjerciciosDiaRutina.OnItemClickListener() {
            @Override
            public void realizarEjercicioListener(int position) {

                //Si el dia ha sido iniciado y no ha finalizado se podran realizar los ejercicios
                if (mViewModelRutina.getmDiaSeleccionado().getFecha() != null &&
                        !mViewModelRutina.getmDiaSeleccionado().getFinalizado()){

                    mViewModelRutina.setmEjercicioRealizar(position);
                    mViewModelUsuario.setmTipoFragmento(TipoFragmento.REALIZAR_EJERCICIO);

                }else {

                    Snackbar.make(getView(), R.string.no_ha_iniciado_dia, Snackbar.LENGTH_LONG).show();

                }
            }
        });

        configuracionVisualRecyclerViewEjerciciosDia();

        mAdaptadorEjerciciosDiaRutina.notifyDataSetChanged();

    }

    /**
     * Funcion que configura/modifica la apariencia visual del RecyclerView y algunos otros componentes de la vista
     * directamente relacionados.
     */
    public void configuracionVisualRecyclerViewEjerciciosDia(){

        //Si el dia seleccionado no tiene ejercicios establecidos, se mostrar un mensaje acompañado de
        //una animacion, la cual indicara que es un dia de descanso
        if (mViewModelRutina.getmDiaSeleccionado().getEjercicios().size() == 0) {

            Animation animacionRotar = AnimationUtils.loadAnimation(getContext(), R.anim.rotar_imageview_360);
            mIVDescanso.startAnimation(animacionRotar);
            mRVEjerciciosDiaRutina.setVisibility(View.INVISIBLE);
            mCLDescanso.setVisibility(View.VISIBLE);
            mBtnComenzar.setVisibility(View.GONE);

        }else{
            //Si existen ejercicios y ademas el dia seleccionado se corresponde con el actual, se mostrara
            //un boton para iniciar el dia
            if (fechaEsHoy(mViewModelRutina.getmFechaSeleccionada())){

                //si el dia esta por iniciar, mostrara el boton para iniciarlo
                if (mViewModelRutina.getmDiaSeleccionado().getFecha() == null && !mViewModelRutina.getmDiaSeleccionado().getFinalizado()){
                    mBtnComenzar.setVisibility(View.VISIBLE);
                //si el dia esta iniciado
                }else {
                    //Si el dia esta iniciado pero no finalizado
                    if (mViewModelRutina.getmDiaSeleccionado().getFecha() != null && !mViewModelRutina.getmDiaSeleccionado().getFinalizado()){
                        mBtnComenzar.setVisibility(View.GONE);
                        mBtnFinalizar.setVisibility(View.VISIBLE);
                    //Si el dia esta finalizado
                    }else {
                        mBtnComenzar.setVisibility(View.GONE);
                        mBtnFinalizar.setVisibility(View.GONE);
                    }
                }

            //Si el dia seleccionado no se corresponde con el de hoy
            }else {

                mBtnComenzar.setVisibility(View.GONE);

            }

            //Si existen ejercicios se mostrara la lista de ejercicios y se ocultara el mensaje de descanso
            mRVEjerciciosDiaRutina.setVisibility(View.VISIBLE);
            mCLDescanso.setVisibility(View.GONE);

        }

    }

    /**
     * Funcion que itera los dias que forman la rutina para encontrar el dia en la rutina que se corresponde con
     * el dia de la semana seleccionado.
     */
    public void obtenerDiaSeleccionado(){

        //"Reseteo" de la variable DiaSeleccionado
        mViewModelRutina.setmDiaSeleccionado(new Dia());

        //Itero los dias de la rutina
        for (Dia dia
                :mViewModelRutina.getRutinaActual().getValue().getDias()){
            //Compruebo que dia de la rutina equivale al dia que estamos iterando de la rutina
            if (mViewModelRutina.getmDiaSemanaSeleccionado() == dia.getDia()){

                mViewModelRutina.setmDiaSeleccionado(dia);

            }

        }

    }

    /**
     * Mediante ViewModelUsuario obtiene un objeto Usuario con los datos del usuario almacenados en Firestore.
     */
    public void obtenerDatosUsuario(){

            mViewModelUsuario.obtenerUsuarioFirestore().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(Task<DocumentSnapshot> task) {
                    if (task.isSuccessful() && task.getResult().exists()){
                        //Parseo el resultado
                        Usuario usuario = task.getResult().toObject(Usuario.class);
                        //Almaceno el resultado en ViewModelUsuario
                        mViewModelUsuario.setmUsuario(usuario);
                        //Obtengo la rutina del usuario
                        obtenerRutina();

                    }

                }
            });

    }

    /**
     * Funcion que obtiene a traves de ViewModelRutina la rutina correspondiente al usuario.
     */
    public void obtenerRutina(){

            mViewModelRutina.obtenerRutinaActualUsuario(mViewModelUsuario.getmUsuario().getRutina()).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(Task<DocumentSnapshot> task) {

                    if (task.isSuccessful()){

                        DocumentSnapshot documento = task.getResult();

                        if (documento.exists()){

                            //Parseo la respuesta de Firestore a un objeto de tipo Rutina y la almaceno en ViewModelRutina
                            mViewModelRutina.setRutinaActual(documento.toObject(Rutina.class));
                            mViewModelRutina.getRutinaActual().getValue().setUid(documento.getId());//seteo el uid del documento al objeto Rutina
                            obtenerDiaSeleccionado();//Obtengo el dia seleccionado de la rutina
                            configurarRecyclerViewEjerciciosDia();//Configuro y relleno el Recyclerview

                        }

                    }else {

                        Snackbar.make(getView(), R.string.no_se_ha_encontrado_rutina, Snackbar.LENGTH_LONG).show();

                    }

                }
            });

    }

    /**
     * Funcion que lleva a cabo las acciones necesarias para inciar el dia actual
     */
    public void iniciarDia(){

        //Guardo la fecha en la que inicia el dia, oculto el boton de iniciar dia y muestro el boton de finalizar dia
        String inicio = DateFormat.format("dd-MM-yyyy", Calendar.getInstance()).toString();
        mViewModelRutina.getmDiaSeleccionado().setFecha(inicio);
        mViewModelRutina.getmDiaSeleccionado().setRutina(mViewModelRutina.getRutinaActual().getValue().getUid());
        //Creo el historico en Firestore
        mViewModelRutina.crearHistoricoDia().addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(Task task) {
                //Cambio la visibilidad de los botones
                mBtnComenzar.setVisibility(View.GONE);
                mBtnFinalizar.setVisibility(View.VISIBLE);
                Snackbar.make(getView(), R.string.dia_iniciado, Snackbar.LENGTH_LONG).show();
            }
        });

    }

    /**
     * Funcion que lleva a cabo las acciones necesarias para finalizar el dia actual
     */
    public void finalizarDia(){

        mViewModelRutina.getmDiaSeleccionado().setFinalizado(true);
        //Actualizo el historico en Firestore
        mViewModelRutina.actualizarRutinaActualUsuario().addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(Task task) {
                if (task.isSuccessful()){
                    //Oculto el boton finalizar
                    mBtnFinalizar.setVisibility(View.GONE);
                }else {
                    Snackbar.make(getView(), R.string.dia_finalizado, Snackbar.LENGTH_LONG).show();
                }
            }
        });

    }

    /**
     * Funcion que se encargara de crear/mostrar (dependiendo de si ya existe) un Dialog custom,
     * que informara al usuario sobre si esta seguro de finalizar el dia y dara dos opciones una positiva y
     * otra negativa.
     */
    public void mostrarDialogoFinalizarDia(){

        if (mAlertDialogTerminarRutina == null || !dialogFinalizarDiaCreado) {

            dialogFinalizarDiaCreado = true;
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
            builder.setTitle(getText(R.string.titulo_dialogo_finalizar_dia))
                    .setMessage(getText(R.string.mensaje_dialogo_finalizar_dia))
                    .setCancelable(true)
                    .setPositiveButton(getText(R.string.boton_aceptar_nombre_rutina), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).setNegativeButton(getText(R.string.boton_cancelar_nombre_rutina), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            mAlertDialogTerminarRutina = builder.create();
            mAlertDialogTerminarRutina.show();

            //Asigno de nuevo la funcion del boton principal del dialogo porque asi evito que este se cierre
            // al pulsar el boton y no tener elegido ningun checkbox
            mAlertDialogTerminarRutina.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finalizarDia();
                    mAlertDialogTerminarRutina.dismiss();
                }
            });

            mAlertDialogTerminarRutina.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAlertDialogTerminarRutina.dismiss();
                }
            });
        }else {

            mAlertDialogTerminarRutina.show();

        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.btnComenzar:
                iniciarDia();
                break;

            case R.id.btnFinalizar:
                mostrarDialogoFinalizarDia();
                break;

        }

    }

}