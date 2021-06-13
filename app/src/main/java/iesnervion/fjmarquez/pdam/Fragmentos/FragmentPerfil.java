package iesnervion.fjmarquez.pdam.Fragmentos;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DecimalFormat;
import java.util.ArrayList;

import iesnervion.fjmarquez.pdam.Adaptadores.AdaptadorListaRutinasPerfil;
import iesnervion.fjmarquez.pdam.Entidades.Dia;
import iesnervion.fjmarquez.pdam.Entidades.Rutina;
import iesnervion.fjmarquez.pdam.Entidades.Usuario;
import iesnervion.fjmarquez.pdam.R;
import iesnervion.fjmarquez.pdam.Utiles.TipoFragmento;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelEjercicios;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelRutina;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelUsuario;

/**
 * En este Fragment se mostrar la informacion principal de usuario, tendra opcion de editar dicha informacion,
 * desde aqui tambien podra cerrar sesion y ver la lista de rutinas del usuario.
 */
public class FragmentPerfil extends Fragment {

    /* ATRIBUTOS */

    private View mFragmentView;

    private ViewModelUsuario mViewModelUsuario;
    private ViewModelRutina mViewModelRutina;
    private ViewModelEjercicios mViewModelEjercicios;

    private AlertDialog mAlertDialogCerrarSesion;
    private boolean dialogCerrarSesionCreado;

    private AlertDialog mAlertDialogGuardarCambiosPerfil;
    private boolean dialogGuardarCambiosPerfil;
    private Usuario mUsuarioModificado;

    private MaterialToolbar mToolbar;
    private TextView mTVNombre;
    private TextView mTVEmail;
    private TextView mTVIMC;
    private TextInputLayout mETNombre;
    private TextInputLayout mETApellidos;
    private TextInputLayout mETEdad;
    private TextInputLayout mETPeso;
    private TextInputLayout mETAltura;
    private TextInputLayout mACTVRutinas;
    private AdaptadorListaRutinasPerfil mACTVRutinasAdaptador;

    /* CONSTRUCTOR */

    public FragmentPerfil() {

    }

    public static FragmentPerfil newInstance() {
        FragmentPerfil fragment = new FragmentPerfil();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModelUsuario = new ViewModelProvider(getActivity()).get(ViewModelUsuario.class);
        mViewModelRutina = new ViewModelProvider(getActivity()).get(ViewModelRutina.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFragmentView = inflater.inflate(R.layout.fragment_perfil, container, false);

        mToolbar = mFragmentView.findViewById(R.id.mtPerfil);
        mTVNombre = mFragmentView.findViewById(R.id.tvNombrePerfil);
        mTVEmail = mFragmentView.findViewById(R.id.tvEmailPerfil);
        mTVIMC = mFragmentView.findViewById(R.id.tvIMCPerfil);
        mETNombre = mFragmentView.findViewById(R.id.ETNombrePerfil);
        mETApellidos = mFragmentView.findViewById(R.id.ETApellidosPerfil);
        mETAltura = mFragmentView.findViewById(R.id.ETAlturaPerfil);
        mETEdad = mFragmentView.findViewById(R.id.ETEdadPerfil);
        mETPeso = mFragmentView.findViewById(R.id.ETPesoPerfil);
        mACTVRutinas = mFragmentView.findViewById(R.id.actvRutinaPerfil);

        dialogCerrarSesionCreado = false;
        dialogGuardarCambiosPerfil = false;
        mUsuarioModificado = new Usuario();

        accionesMenu();
        obtenerDatosUsuario();

        return mFragmentView;

    }

    /**
     * Muestra o crea un dialogo de confirmacion para consultar al usuario si desea guardar los cambios relizados.
     */
    public void mostrarDialogoGuardarCambiosPerfil(){

        if (mAlertDialogGuardarCambiosPerfil == null || !dialogGuardarCambiosPerfil) {

            dialogGuardarCambiosPerfil = true;
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
            builder.setTitle(getText(R.string.titulo_dialogo_guardar_cambios))
                    .setMessage(getText(R.string.desea_guardar_cambios))
                    .setCancelable(true)
                    .setPositiveButton(getText(R.string.boton_aceptar_nombre_rutina), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setNegativeButton(getText(R.string.boton_cancelar_nombre_rutina), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    mAlertDialogGuardarCambiosPerfil.dismiss();
                }
            });

            mAlertDialogGuardarCambiosPerfil = builder.create();
            mAlertDialogGuardarCambiosPerfil.show();

            //Asigno de nuevo la funcion del boton principal del dialogo porque asi evito que este se cierre
            // al pulsar el boton
            mAlertDialogGuardarCambiosPerfil.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recopilarDatosUsuario();
                    actualizarUsuario();
                    mAlertDialogGuardarCambiosPerfil.dismiss();
                }
            });

            mAlertDialogGuardarCambiosPerfil.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAlertDialogGuardarCambiosPerfil.dismiss();
                }
            });
        }else {

            mAlertDialogGuardarCambiosPerfil.show();

        }

    }

    /**
     * Muestra o crea un dialog de confirmacion preguntando al usuario si desea cerrar sesion.
     */
    public void mostrarDialogoCerrarSesion(){

        if (mAlertDialogCerrarSesion == null || !dialogCerrarSesionCreado) {

            dialogCerrarSesionCreado = true;
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
            builder.setTitle(getText(R.string.titulo_dialogo_cerrar_sesion))
                    .setMessage(getText(R.string.desea_cerrar_sesion))
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

            mAlertDialogCerrarSesion = builder.create();
            mAlertDialogCerrarSesion.show();

            //Asigno de nuevo la funcion del boton principal del dialogo porque asi evito que este se cierre
            // al pulsar el boton
            mAlertDialogCerrarSesion.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewModelUsuario.setmTipoFragmento(TipoFragmento.LOGIN);
                    //Cierra sesion en Firebase
                    mViewModelUsuario.cerrarSesionActual();
                    mAlertDialogCerrarSesion.dismiss();
                }
            });

            mAlertDialogCerrarSesion.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAlertDialogCerrarSesion.dismiss();
                }
            });
        }else {

            mAlertDialogCerrarSesion.show();

        }

    }

    /**
     * Recopila la informacion contenida en el formulario del perfil.
     */
    public void recopilarDatosUsuario(){

        mUsuarioModificado.setUid(mViewModelUsuario.getmUsuario().getUid());
        mUsuarioModificado.setEmail(mViewModelUsuario.getmUsuario().getEmail());
        mUsuarioModificado.setNombre(mETNombre.getEditText().getText().toString());
        mUsuarioModificado.setApellidos(mETApellidos.getEditText().getText().toString());
        mUsuarioModificado.setAltura(Integer.parseInt(mETAltura.getEditText().getText().toString()));
        mUsuarioModificado.setPeso(Double.parseDouble(mETPeso.getEditText().getText().toString()));
        mUsuarioModificado.setEdad(Integer.parseInt(mETEdad.getEditText().getText().toString()));
        //La rutina de mUsuarioModificado se modifica en el listener del autocompletetextview, si esta no ha sido seteada
        //le establezco la misma rutina que tenia, obteniendola de mUsuario en ViewModelUsuario
        if(mUsuarioModificado.getRutina() == null){
            mUsuarioModificado.setRutina(mViewModelUsuario.getmUsuario().getRutina());
        }

    }

    /**
     * Actualiza el usuario en Firestore.
     */
    public void actualizarUsuario(){

        mViewModelUsuario.añadirOActualizarUsuarioFirestore(mUsuarioModificado).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(Task task) {
                if (task.isSuccessful()){
                    Snackbar.make(getView(), R.string.cambios_guardados_con_exito, Snackbar.LENGTH_SHORT).show();
                    //Obtiene de nuevo los datos del usuario para mostrarlos actualizados
                    obtenerDatosUsuario();
                }
            }
        });

    }

    /**
     * Obtiene la informacion del usuario actual contenida en Firestore.
     */
    public void obtenerDatosUsuario(){

            mViewModelUsuario.obtenerUsuarioFirestore().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(Task<DocumentSnapshot> task) {
                    if (task.isSuccessful() && task.getResult().exists()){
                        //Parseo el resultado
                        Usuario usuario = task.getResult().toObject(Usuario.class);
                        mViewModelUsuario.setmUsuario(usuario);
                        //Obtiene las rutinas correspondientes al usuario actual
                        obtenerRutinasUsuario();

                    }

                }
            });

    }

    /**
     * Obtiene un listado de todas las rutinas del usuario actual.
     */
    public void obtenerRutinasUsuario(){

        mViewModelRutina.obtenerListaRutinasUsuario().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    mViewModelRutina.setmListaRutinasUsuario(new ArrayList<>());
                    for (DocumentSnapshot  document: task.getResult()
                    ) {
                        Rutina mRutina = document.toObject(Rutina.class);
                        mRutina.setUid(document.getId());
                        mViewModelRutina.getmListaRutinasUsuario().add(mRutina);
                    }
                    relleñarDatosUsuario();
                }
            }
        });

    }

    /**
     * Rellena el formulario con los datos actuales del usuario.
     */
    public void relleñarDatosUsuario(){

        String nombre = mViewModelUsuario.getmUsuario().getNombre();
        String apellidos = mViewModelUsuario.getmUsuario().getApellidos();
        String nombreCompleto = nombre + " " + apellidos;
        String email = mViewModelUsuario.getmUsuario().getEmail();
        Double peso = mViewModelUsuario.getmUsuario().getPeso();
        Integer altura = mViewModelUsuario.getmUsuario().getAltura();
        Integer edad = mViewModelUsuario.getmUsuario().getEdad();
        Double alturaMts = altura.doubleValue()/100;
        Double imc = peso / Math.pow(alturaMts, 2);
        DecimalFormat decimalFormat = new DecimalFormat("#.00");

        mTVNombre.setText(nombreCompleto);
        mTVEmail.setText(email);
        mTVIMC.setText(getText(R.string.imc_perfil) + " " + decimalFormat.format(imc));

        mETNombre.getEditText().setText(nombre);
        mETApellidos.getEditText().setText(apellidos);
        mETPeso.getEditText().setText(peso.toString());
        mETAltura.getEditText().setText(altura.toString());
        mETEdad.getEditText().setText(edad.toString());

        rellenarACTVRutinas();

    }

    /**
     * Rellena un AutoCompleteTextView con los nombres de las rutinas pertenecientes al usuario actual.
     */
    public void rellenarACTVRutinas(){

        mACTVRutinasAdaptador = new AdaptadorListaRutinasPerfil(getContext(), mViewModelRutina.getmListaRutinasUsuario());
        ((AutoCompleteTextView) mACTVRutinas.getEditText()).setAdapter(mACTVRutinasAdaptador);
        ((AutoCompleteTextView) mACTVRutinas.getEditText()).setText(obtenerNombreRutinaActual(), false);
        ((AutoCompleteTextView) mACTVRutinas.getEditText()).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Rutina r = (Rutina) ((AutoCompleteTextView) mACTVRutinas.getEditText()).getAdapter().getItem(position);
                mUsuarioModificado.setRutina(r.getUid());
            }
        });

    }

    /**
     * Obtiene el nombre de la rutina que actualmente tiene asignada el usuario actual.
     *
     * @return String que contiene el nombre de la rutina que actualmente esta usando el usuario.
     */
    public String obtenerNombreRutinaActual(){

        String respuesta = "";

        for (Rutina rutina:
                mViewModelRutina.getmListaRutinasUsuario()){
            if (mViewModelUsuario.getmUsuario().getRutina().equals(rutina.getUid())){
                respuesta = rutina.getNombre();
            }
        }

        return respuesta;
    }

    /**
     * Configura las acciones del menu del Fragment Perfil
     */
    public void accionesMenu(){

        //Configura las acciones a realizar cuando se pulsa sobre un MenuItem
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModelUsuario.setmTipoFragmento(TipoFragmento.PANTALLA_INICIO);
            }
        });

        //Configuro la funcionalidad de los MenuItems correspondientes
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){

                    case R.id.guardarPerfilMenu:
                        mostrarDialogoGuardarCambiosPerfil();
                        break;

                    case R.id.cerrarSesionPerfilMenu:
                        mostrarDialogoCerrarSesion();
                        break;

                    case R.id.rutinasPerfilMenu:
                        mViewModelUsuario.setmTipoFragmento(TipoFragmento.LISTA_RUTINAS);
                        break;

                }

                return false;

            }
        });

    }

}