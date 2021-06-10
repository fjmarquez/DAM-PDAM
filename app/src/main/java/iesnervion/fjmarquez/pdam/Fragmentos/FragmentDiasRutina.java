package iesnervion.fjmarquez.pdam.Fragmentos;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import iesnervion.fjmarquez.pdam.Adaptadores.AdaptadorDias;
import iesnervion.fjmarquez.pdam.Entidades.Dia;
import iesnervion.fjmarquez.pdam.Entidades.Rutina;
import iesnervion.fjmarquez.pdam.Entidades.Usuario;
import iesnervion.fjmarquez.pdam.R;
import iesnervion.fjmarquez.pdam.Utiles.DiaSemana;
import iesnervion.fjmarquez.pdam.Utiles.TipoFragmento;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelRutina;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelUsuario;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelEjercicios;

/**
 * Este fragmento se encargara de mostrar un listado de dias, los cuales ha especificado el usuario previamente,
 * desde este fragmento se podran añadir ejercicios a cada dia asi como visualizar los ejercicios ya añadidos a un dia.
 */
public class FragmentDiasRutina extends Fragment implements View.OnClickListener{

    /* ATRIBUTOS */

    private View mFragmentView;
    private RecyclerView mRVDiasSeleccionados;
    private AdaptadorDias mAdaptadorDias;
    private RecyclerView.LayoutManager mLayoutManager;

    private Button mBtnGuardarDias;
    private Button mBtnCambiarDias;

    private AlertDialog mAlertDialogDias;
    private CheckBox mCBLunes;
    private CheckBox mCBMartes;
    private CheckBox mCBMiercoles;
    private CheckBox mCBJueves;
    private CheckBox mCBViernes;
    private CheckBox mCBSabado;
    private CheckBox mCBDomingo;
    private boolean dialogDiasCreado;

    private AlertDialog mAlertDialogNombreRutina;
    private TextInputLayout mETNombreRutina;
    private String mNombreRutina;
    private boolean dialogNombreRutinaCreado;

    private View mDialogDiasView;
    private View mDialogNombreRutinaView;

    private ViewModelEjercicios mViewModelEjercicios;
    private ViewModelRutina mViewModelRutina;
    private ViewModelUsuario mViewModelUsuario;

    /*  CONSTRUCTOR */

    public FragmentDiasRutina() {

    }

    public static FragmentDiasRutina newInstance() {

        FragmentDiasRutina fragment = new FragmentDiasRutina();

        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Instancio los ViewModel necesarios
        mViewModelEjercicios = new ViewModelProvider(getActivity()).get(ViewModelEjercicios.class);
        mViewModelUsuario = new ViewModelProvider(getActivity()).get(ViewModelUsuario.class);
        mViewModelRutina = new ViewModelProvider(getActivity()).get(ViewModelRutina.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFragmentView = inflater.inflate(R.layout.fragment_dias_rutina, container, false);
        mDialogDiasView = View.inflate(getContext(), R.layout.dialog_dias_layout, null);
        mDialogNombreRutinaView = View.inflate(getContext(), R.layout.dialog_nombre_rutina_layout, null);

        mBtnGuardarDias = mFragmentView.findViewById(R.id.btnGuardarRutina);
        mBtnGuardarDias.setOnClickListener(this);
        mBtnCambiarDias = mFragmentView.findViewById(R.id.btnCambiarDias);
        mBtnCambiarDias.setOnClickListener(this);

        mCBLunes = mDialogDiasView.findViewById(R.id.cbLunes);
        mCBMartes = mDialogDiasView.findViewById(R.id.cbMartes);
        mCBMiercoles = mDialogDiasView.findViewById(R.id.cbMiercoles);
        mCBJueves = mDialogDiasView.findViewById(R.id.cbJueves);
        mCBViernes = mDialogDiasView.findViewById(R.id.cbViernes);
        mCBSabado = mDialogDiasView.findViewById(R.id.cbSabado);
        mCBDomingo = mDialogDiasView.findViewById(R.id.cbDomigo);
        dialogDiasCreado = false;
        dialogNombreRutinaCreado = false;

        mETNombreRutina = mDialogNombreRutinaView.findViewById(R.id.etNombreRutina);

        mRVDiasSeleccionados = mFragmentView.findViewById(R.id.rvDias);
        mRVDiasSeleccionados.setHasFixedSize(true);

        /*
        * Controlo si lo que vamos a hacer es crear una rutina (mostrarDialogdias) o editar una ya existente
        * (rellenarRecyclerViewDias)
        */

        if (mViewModelEjercicios.getDiasRutina().getValue() == null || mViewModelEjercicios.getDiasRutina().getValue().getDias().size() == 0) {
            mostrarDialogDias();
        } else {
            rellenarRecyclerViewDias();
        }

        return mFragmentView;

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            //Vuelve a mostrar el dialogo para la seleccion de los dias de la rutina
            case R.id.btnCambiarDias:
                mostrarDialogDias();
                break;
            /*
            * En caso de estar creando una rutina, se mostrar un dialog destinado a obtener el nombre que le deseas poner
            * a la rutina y despues guardar esta en firebase. En caso de estar editando una rutina, se guardara automaticamente
            * sin mostrar el dialogo para recopilar el nombre.
            */
            case R.id.btnGuardarRutina:
                //Se comprueba que todos los dias de la rutina tienen al menos un ejercicio
                if(comprobarRutinaAntesGuardar()){
                    if(mViewModelEjercicios.getDiasRutina().getValue().getUid() != null){
                        mNombreRutina = mViewModelEjercicios.getDiasRutina().getValue().getNombre();
                        guardarRutina(mViewModelEjercicios.getDiasRutina().getValue().getUid());
                    }else{
                        mostrarDialogNombreRutina();
                    }
                }else{
                    Snackbar.make(getView(), getText(R.string.rutina_incompleta), Snackbar.LENGTH_SHORT).show();
                }
                break;
        }

    }

    /**
     * Crea o muestra un dialogo personalizado, cuya funcion es obtener el nombre de la rutina.
     * Posee dos botones, el de aceptar procedera a guardar la rutina en firestore y el de cancelar cerrar el dialogo,
     * cancelando asi la operacion de guardado.
     */
    public void mostrarDialogNombreRutina(){

        if (mAlertDialogNombreRutina == null || !dialogNombreRutinaCreado) {

            dialogNombreRutinaCreado = true;
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
            builder.setTitle(getText(R.string.titulo_dialog_nombre_rutina))
                    .setView(mDialogNombreRutinaView)
                    .setCancelable(false)
                    .setPositiveButton(getText(R.string.boton_aceptar_nombre_rutina), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setNegativeButton(getText(R.string.boton_cancelar_nombre_rutina), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    });

            mAlertDialogNombreRutina = builder.create();
            mAlertDialogNombreRutina.show();

            //Asigno de nuevo la funcion del boton principal del dialogo porque asi evito que este se cierre
            // al pulsar el boton y no tener elegido ningun checkbox
            mAlertDialogNombreRutina.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Comprueba si la longitud del nombre introducido es mayor que 0
                    if (comprobarNombreRutina()) {
                        if (mViewModelEjercicios.getDiasRutina().getValue().getUid() == null){
                            //Almacena el nombre introducido en una variable a nivel de Fragment
                            recopilarNombreRutina();
                        }
                        //Compruebo si el usuario tiene mas rutinas en Firestore
                        comprobarRutinasExistentes();
                        mAlertDialogNombreRutina.dismiss();
                    }
                }
            });

            mAlertDialogNombreRutina.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAlertDialogNombreRutina.dismiss();
                }
            });
        }else {

            mAlertDialogNombreRutina.show();

        }

    }

    /**
     * Almacena el nombre de la rutina, el cual obtiene de un EditText, en una variable a nivel de Fragment.
     */
    public void recopilarNombreRutina(){

        mNombreRutina = mETNombreRutina.getEditText().getText().toString();

    }

    /**
     * Comprueba si el nombre introducido en el EditText destinado a recopilar el nombre de rutina tiene una longitud
     * de mas de 0. Devolvera una valor booleano, el cual sera positivo en caso de cumplir los requisitos y false en caso
     * de no cumplirlos.
     *
     * @return Boolean destinado a indicar si el nombre de la rutina introducido cumple con los requisitos establecidos.
     */
    public boolean comprobarNombreRutina(){

        boolean respuesta = true;

        limpiarErroresDialogNombreRutina();

        if (mETNombreRutina.getEditText().getText().toString().length() == 0){

            mETNombreRutina.setError(getString(R.string.error_nombre_rutina_vacio));
            respuesta = false;

        }

        return respuesta;

    }

    /**
     * Limpia los errores del TextInputLayout destinado a obtener el nombre de la rutina
     */
    public void limpiarErroresDialogNombreRutina(){

        mETNombreRutina.setError(null);

    }

    /**
     * Comprueba mediante el ViewModelRutina si el usuario actual dispone de mas rutinas en Firestore.
     */
    public void comprobarRutinasExistentes(){

        mViewModelRutina.obtenerListaRutinasUsuario().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                String uid = null; //uid sera null si el usuario tiene alguna otra rutina en Firestore
                if (task.isSuccessful()){
                    if (task.getResult().getDocuments().size() == 0){
                        //uid tendra como valor el uid del usuario actual si esta es la primera rutina del usuario en Firestore
                        uid = mViewModelUsuario.usuarioActual().getUid();
                    }

                    guardarRutina(uid);

                }

            }
        });

    }

    /**
     * Almacena en Firestore la rutina creada por el usuario.
     */
    public void guardarRutina(String uid){

        mViewModelEjercicios.guardarNuevaRutinaFirestore(new Rutina(uid, mNombreRutina, mViewModelEjercicios.getDiasRutina().getValue().getDias(), mViewModelUsuario.usuarioActual().getUid())).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(Task task) {
                if(task.isSuccessful()){
                    //Si uid es distinto de null quiere decir que es la primera rutina del usuario en firestore
                    if (uid != null){
                        obtenerUsuarioRutinaActual(uid);
                        Snackbar.make(getView(), R.string.guardado_exito, Snackbar.LENGTH_SHORT).show();
                    }else{
                        mViewModelUsuario.setmTipoFragmento(TipoFragmento.PANTALLA_INICIO);
                        Snackbar.make(getView(), R.string.editador_exito, Snackbar.LENGTH_SHORT).show();
                    }

                }else {
                    Snackbar.make(getView(), R.string.guardado_fallo, Snackbar.LENGTH_SHORT).show();
                }

            }
        });

    }

    /**
     * Mediante ViewModelUsuario obtiene el objeto Usuario correspondiente al usuario actual.
     *
     * @param uidRutina
     */
    public void obtenerUsuarioRutinaActual(String uidRutina){

        //Solo se llevara a cabo si el uid de la rutina es null, lo cual quiere decir que es la primera rutina del usuario
        if (mViewModelRutina.getRutinaActual().getValue().getUid() == null){
            //si aun no tengo los datos del usuario en ViewModelUsuario los obtengo antes de seguir
            if(mViewModelUsuario.getmUsuario() == null){

                mViewModelUsuario.obtenerUsuarioFirestore().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(Task<DocumentSnapshot> task) {
                        if (task.isSuccessful() && task.getResult().exists()){

                            Usuario usuario = task.getResult().toObject(Usuario.class);
                            usuario.setRutina(uidRutina); //id de la nueva rutina creada
                            mViewModelUsuario.setmUsuario(usuario);
                            actualizarUsuarioConRutina();

                        }

                    }
                });

            //Si el uid de la rutina es distinto de null actualizaremos el campo rutina del usuario
            }else{

                mViewModelUsuario.getmUsuario().setRutina(uidRutina);
                actualizarUsuarioConRutina();

            }
        }else {
            mViewModelUsuario.setmTipoFragmento(TipoFragmento.PANTALLA_INICIO);
        }


    }

    /**
     * Actualiza el usuario con la nueva rutina asignada en Firestore.
     */
    public void actualizarUsuarioConRutina(){

        mViewModelUsuario.añadirOActualizarUsuarioFirestore(mViewModelUsuario.getmUsuario()).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(Task task) {
                if (task.isSuccessful()){
                    Snackbar.make(getView(), R.string.rutina_asignada, Snackbar.LENGTH_SHORT).show();
                    mViewModelUsuario.setmTipoFragmento(TipoFragmento.PANTALLA_INICIO);
                }else{
                    Snackbar.make(getView(), R.string.error_rutina_asignada, Snackbar.LENGTH_SHORT).show();
                }
            }
        });

    }

    /**
     * Comprueba que todos los dias introducidos en la rutina por el usuario tienen ejercicios asignados.
     *
     * @return  Devuelve un booleano que en funcion de si un dia tiene ejercicios o no sera true o false.
     */
    public Boolean comprobarRutinaAntesGuardar(){

        Boolean respuesta = true;

        for (Dia dia:
             mViewModelEjercicios.getDiasRutina().getValue().getDias()) {

            if(dia.getEjercicios().size() == 0){
                respuesta = false;
            }

        }

        return respuesta;

    }

    /**
     * Crea o muestra un Dialog personalizado, el cual permite al usuario seleccionar los dias de entreno que desea.
     */
    public void mostrarDialogDias(){

        if(mAlertDialogDias == null || !dialogDiasCreado) {

            dialogDiasCreado = true;
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
            builder.setTitle(getText(R.string.titulo_dialog_dias_semana))
                    .setView(mDialogDiasView)
                    .setCancelable(false)
                    .setPositiveButton(getText(R.string.boton_positivo_dialog), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

            mAlertDialogDias = builder.create();
            mAlertDialogDias.show();

            //Asigno de nuevo la funcion del boton principal del dialogo porque asi evito que este se cierre
            // al pulsar el boton y no tener elegido ningun checkbox
            mAlertDialogDias.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewModelEjercicios.getDiasRutina().getValue().setDias(new ArrayList<>());
                    //Compruebo si el usuario a seleccionado al menos un dia
                    if (recopilarCheckBoxesDias()) {
                        //Si el usuario a seleccionado al menos un dia relleno la lista de dias
                        rellenarRecyclerViewDias();
                        mAdaptadorDias.notifyDataSetChanged();
                        mAlertDialogDias.cancel();
                    } else {
                        Snackbar.make(getView(), R.string.error_dias, Snackbar.LENGTH_SHORT).show();
                    }

                }
            });

        }else {

            mAlertDialogDias.show();

        }

    }

    /**
     * Rellena un RecyclerView con los dias especificados por el usuario mediante el usuario.
     * Permite añadir ejercicios a cada dia del listado, asi como visualizar los ejercicios añadidos a cada dia.
     */
    public void rellenarRecyclerViewDias(){

        mLayoutManager =  new LinearLayoutManager(getActivity());
        mAdaptadorDias = new AdaptadorDias(mViewModelEjercicios.getDiasRutina().getValue().getDias());
        mRVDiasSeleccionados.setLayoutManager(mLayoutManager);
        mRVDiasSeleccionados.setAdapter(mAdaptadorDias);

        //Listeners disponibles en el Adaptador, uno para controlar los clicks destinados a añadir ejercicios
        //y otro destinado a mostrar otro RecyclerView con los ejercicios añadidos a un dia.
        mAdaptadorDias.setOnItemClickListener(new AdaptadorDias.OnItemClickListener() {
            @Override
            public void añadirListener(int position) {
                //almaceno en ViewModelRutina el dia de la semana sobre el que vamos a añadir los ejercicios
                mViewModelEjercicios.setDiaSemanaSeleccionado(position);
                mViewModelUsuario.setmTipoFragmento(TipoFragmento.EJERCICIOS);
            }

            @Override
            public void mostrarListener(int position) {
                //el codigo correspondiente a esta accion se encuentra en AdaptadorDias
            }
        });

    }

    /**
     * Recopila los valores de los CheckBoxes del dialogo destinado a seleccionar los dias de entreno.
     *
     * @return Devuelve un valor Boolean, el cual sera false si no se selecciona ningun CheckBox o true
     * si se ha seleccionado al menos un CheckBox.
     */
    public boolean recopilarCheckBoxesDias(){

        boolean respuesta = false;
        String usuario = mViewModelUsuario.usuarioActual().getUid();

        if(mCBLunes.isChecked()){
            mViewModelEjercicios.getDiasRutina().getValue().getDias().add(new Dia(DiaSemana.LUNES, new ArrayList<>(), usuario, null));
        }
        if(mCBMartes.isChecked()){
            mViewModelEjercicios.getDiasRutina().getValue().getDias().add(new Dia(DiaSemana.MARTES, new ArrayList<>(), usuario, null));
        }
        if(mCBMiercoles.isChecked()){
            mViewModelEjercicios.getDiasRutina().getValue().getDias().add(new Dia(DiaSemana.MIERCOLES, new ArrayList<>(), usuario, null));
        }
        if(mCBJueves.isChecked()){
            mViewModelEjercicios.getDiasRutina().getValue().getDias().add(new Dia(DiaSemana.JUEVES, new ArrayList<>(), usuario, null));
        }
        if(mCBViernes.isChecked()){
            mViewModelEjercicios.getDiasRutina().getValue().getDias().add(new Dia(DiaSemana.VIERNES, new ArrayList<>(), usuario, null));
        }
        if(mCBSabado.isChecked()){
            mViewModelEjercicios.getDiasRutina().getValue().getDias().add(new Dia(DiaSemana.SABADO, new ArrayList<>(), usuario, null));
        }
        if(mCBDomingo.isChecked()){
            mViewModelEjercicios.getDiasRutina().getValue().getDias().add(new Dia(DiaSemana.DOMINGO, new ArrayList<>(), usuario, null));
        }

        if(mViewModelEjercicios.getDiasRutina().getValue().getDias().size() > 0){
            respuesta = true;
        }

        return respuesta;

    }

}