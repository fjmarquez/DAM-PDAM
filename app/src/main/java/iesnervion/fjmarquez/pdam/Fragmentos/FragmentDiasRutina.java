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

    public FragmentDiasRutina() {

    }

    public static FragmentDiasRutina newInstance() {

        FragmentDiasRutina fragment = new FragmentDiasRutina();
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
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
            case R.id.btnCambiarDias:
                mostrarDialogDias();
                break;
            case R.id.btnGuardarRutina:
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
                    if (comprobarNombreRutina()) {
                        if (mViewModelEjercicios.getDiasRutina().getValue().getUid() == null){
                            recopilarNombreRutina();
                        }
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

    public void recopilarNombreRutina(){

        mNombreRutina = mETNombreRutina.getEditText().getText().toString();

    }

    public boolean comprobarNombreRutina(){

        boolean respuesta = true;

        limpiarErroresDialogNombreRutina();

        if (mETNombreRutina.getEditText().getText().toString().length() == 0){

            mETNombreRutina.setError(getString(R.string.error_nombre_rutina_vacio));
            respuesta = false;

        }

        return respuesta;

    }

    public void limpiarErroresDialogNombreRutina(){

        mETNombreRutina.setError(null);

    }

    public void comprobarRutinasExistentes(){

        mViewModelRutina.obtenerListaRutinasUsuario().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                String uid = null;
                if (task.isSuccessful()){
                    if (task.getResult().getDocuments().size() == 0){
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
                    if (uid != null){
                        obtenerUsuarioRutinaActual(uid);
                    }else{
                        mViewModelUsuario.setmTipoFragmento(TipoFragmento.PANTALLA_INICIO);
                    }
                    Snackbar.make(getView(), R.string.guardado_exito, Snackbar.LENGTH_SHORT).show();
                }else {
                    //fallo/error al guardar
                    Snackbar.make(getView(), R.string.guardado_fallo, Snackbar.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void obtenerUsuarioRutinaActual(String uidRutina){

        if (mViewModelRutina.getRutinaActual().getValue().getUid() == null){
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

            }else{

                mViewModelUsuario.getmUsuario().setRutina(uidRutina);
                actualizarUsuarioConRutina();

            }
        }else {
            mViewModelUsuario.setmTipoFragmento(TipoFragmento.PANTALLA_INICIO);
        }


    }

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
     * Muestra un Dialog personalizado, el cual permite al usuario seleccionar los dias de entreno que desea.
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

                    if (recopilarCheckBoxesDias()) {
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
                mViewModelEjercicios.setDiaSemanaSeleccionado(position);
                mViewModelUsuario.setmTipoFragmento(TipoFragmento.EJERCICIOS);
            }

            @Override
            public void mostrarListener(int position) {

            }
        });

    }

    /**
     * Recopila los valores de los CheckBoxes del dialogo destinado a seleccionar los dias de entreno.
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