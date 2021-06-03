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

import java.util.ArrayList;

import iesnervion.fjmarquez.pdam.Adaptadores.AdaptadorDias;
import iesnervion.fjmarquez.pdam.Entidades.Dia;
import iesnervion.fjmarquez.pdam.Entidades.Rutina;
import iesnervion.fjmarquez.pdam.R;
import iesnervion.fjmarquez.pdam.Utiles.DiaSemana;
import iesnervion.fjmarquez.pdam.Utiles.TipoFragmento;
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

    private ViewModelEjercicios mViewModelRutina;
    private ViewModelUsuario mViewModelLogin;

    public FragmentDiasRutina() {

    }

    public static FragmentDiasRutina newInstance() {

        FragmentDiasRutina fragment = new FragmentDiasRutina();
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mViewModelRutina = new ViewModelProvider(getActivity()).get(ViewModelEjercicios.class);
        mViewModelLogin = new ViewModelProvider(getActivity()).get(ViewModelUsuario.class);

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

        if (mViewModelRutina.getDiasRutina().getValue() == null || mViewModelRutina.getDiasRutina().getValue().size() == 0) {
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
                    mostrarDialogNombreRutina();
                    //guardarRutina();
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
                        recopilarNombreRutina();
                        guardarRutina();
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

    /**
     * Almacena en Firestore la rutina creada por el usuario.
     */
    public void guardarRutina(){
        mViewModelRutina.guardarNuevaRutinaFirestore(new Rutina(null, mNombreRutina, mViewModelRutina.getDiasRutina().getValue())).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(Task task) {
                if(task.isSuccessful()){
                    //Snackbar.make(getView(), R.string.guardado_exito, Snackbar.LENGTH_SHORT).show();
                    mViewModelLogin.setmTipoFragmento(TipoFragmento.PANTALLA_INICIO);
                }else {
                    //fallo/error al guardar
                    //Snackbar.make(getView(), R.string.guardado_fallo, Snackbar.LENGTH_SHORT).show();
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
             mViewModelRutina.getDiasRutina().getValue()) {

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
                    mViewModelRutina.getDiasRutina().setValue(new ArrayList<>());

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
        mAdaptadorDias = new AdaptadorDias(mViewModelRutina.getDiasRutina().getValue());
        mRVDiasSeleccionados.setLayoutManager(mLayoutManager);
        mRVDiasSeleccionados.setAdapter(mAdaptadorDias);

        //Listeners disponibles en el Adaptador, uno para controlar los clicks destinados a añadir ejercicios
        //y otro destinado a mostrar otro RecyclerView con los ejercicios añadidos a un dia.
        mAdaptadorDias.setOnItemClickListener(new AdaptadorDias.OnItemClickListener() {
            @Override
            public void añadirListener(int position) {
                mViewModelRutina.setDiaSemanaSeleccionado(position);
                mViewModelLogin.setmTipoFragmento(TipoFragmento.EJERCICIOS);
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

        if(mCBLunes.isChecked()){
            mViewModelRutina.getDiasRutina().getValue().add(new Dia(DiaSemana.LUNES, new ArrayList<>()));
        }
        if(mCBMartes.isChecked()){
            mViewModelRutina.getDiasRutina().getValue().add(new Dia(DiaSemana.MARTES, new ArrayList<>()));
        }
        if(mCBMiercoles.isChecked()){
            mViewModelRutina.getDiasRutina().getValue().add(new Dia(DiaSemana.MIERCOLES, new ArrayList<>()));
        }
        if(mCBJueves.isChecked()){
            mViewModelRutina.getDiasRutina().getValue().add(new Dia(DiaSemana.JUEVES, new ArrayList<>()));
        }
        if(mCBViernes.isChecked()){
            mViewModelRutina.getDiasRutina().getValue().add(new Dia(DiaSemana.VIERNES, new ArrayList<>()));
        }
        if(mCBSabado.isChecked()){
            mViewModelRutina.getDiasRutina().getValue().add(new Dia(DiaSemana.SABADO, new ArrayList<>()));
        }
        if(mCBDomingo.isChecked()){
            mViewModelRutina.getDiasRutina().getValue().add(new Dia(DiaSemana.DOMINGO, new ArrayList<>()));
        }

        if(mViewModelRutina.getDiasRutina().getValue().size() > 0){
            respuesta = true;
        }

        return respuesta;

    }

}