package iesnervion.fjmarquez.pdam.Fragmentos;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;

import java.util.regex.Pattern;

import iesnervion.fjmarquez.pdam.R;
import iesnervion.fjmarquez.pdam.Utiles.TipoFragmento;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelUsuario;

/**
 * Fragment destinado a contener lo necesario para el logeo/registro de la app.
 */
public class FragmentLogin extends Fragment implements View.OnClickListener{

    /* ATRIBUTOS */
    private Button mBtnInciarSesion;
    private Button mBtnRegistrar;
    private Button mBtnRegistrarGoogle;
    private Button mBtnRecordarContraseña;

    private TextInputLayout mETUsuario;
    private TextInputLayout mETContraseña;

    private String mUsuario;
    private String mContraseña;
    private final int CODE_GOOGLE_SIGN = 1;

    private Pattern mRegexMail = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private Pattern mRegexContraseña = Pattern.compile("((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#._$,:;?¿¡)(*^<>%!]).{8,40})");

    private ViewModelUsuario mViewModel;

    public FragmentLogin() {

    }

    public static FragmentLogin newInstance() {

        FragmentLogin fragment = new FragmentLogin();
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //Inicializo el ViewModel
        mViewModel = new ViewModelProvider(getActivity()).get(ViewModelUsuario.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_login, container, false);

        //Identifico los componentes de la vista
        mBtnInciarSesion = v.findViewById(R.id.BTNIniciarSesion);
        mBtnInciarSesion.setOnClickListener(this);
        mBtnRegistrar = v.findViewById(R.id.BTNRegistrarse);
        mBtnRegistrar.setOnClickListener(this);
        mBtnRegistrarGoogle = v.findViewById(R.id.BTNRegistrarseGoogle);
        mBtnRegistrarGoogle.setOnClickListener(this);
        mBtnRecordarContraseña = v.findViewById(R.id.BTNRecordarContraseña);
        mBtnRecordarContraseña.setOnClickListener(this);

        mETUsuario = v.findViewById(R.id.ETUsuario);
        mETContraseña = v.findViewById(R.id.ETContraseña);

        return v;

    }

    /**
     * Eventos onClick de los elementos del Fragment.
     * @param v Vista que contiene los elementos del Fragment.
     */
    @Override
    public void onClick(View v) {

            switch (v.getId()){

                case R.id.BTNIniciarSesion:
                    if(comprobarCampos(false, false)){
                        mViewModel.iniciarSesionConUsuarioYContraseña(mUsuario, mContraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    mViewModel.setmTipoFragmento(TipoFragmento.PANTALLA_INICIO);
                                    //mViewModel.setmTipoFragmento(TipoFragmento.POST_REGISTRO);
                                }else{
                                    mostrarDialogError(false, true, false);
                                }
                            }
                        });
                    }
                    break;

                case R.id.BTNRegistrarse:
                    if(comprobarCampos(false, true)){
                        mViewModel.registrarNuevoUsuario(mUsuario, mContraseña).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if(task.isSuccessful()){
                                    mViewModel.setmTipoFragmento(TipoFragmento.POST_REGISTRO);
                                }else{
                                    mostrarDialogError(true, false, false);
                                }
                            }
                        });
                    }
                    break;

                case R.id.BTNRegistrarseGoogle:
                    Intent i = mViewModel.autenticacionGoogle(getString(R.string.default_web_client_id), getContext());
                    startActivityForResult(i, CODE_GOOGLE_SIGN);
                    break;

                case R.id.BTNRecordarContraseña:
                    if (comprobarCampos(true, false)){
                        mViewModel.mandarMailRecuperarContraseña(mUsuario).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if(task.isSuccessful()){
                                    Snackbar.make(getView(), R.string.correo_recuperacion_enviado, Snackbar.LENGTH_SHORT).show();
                                }else{
                                    mostrarDialogError(false, false, true);
                                }
                            }
                        });
                    }
                    break;
            }

    }

    /**
     * Obtiene el resultado de un intent lanzado.
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == CODE_GOOGLE_SIGN){

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount acount = task.getResult(ApiException.class);
                mViewModel.accederMedianteGoogle(acount.getIdToken()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            mViewModel.setmTipoFragmento(TipoFragmento.POST_REGISTRO);
                            Snackbar.make(getView(), R.string.inicio_con_google_correcto, Snackbar.LENGTH_SHORT).show();
                        }else {
                            mostrarDialogError(false, true, false);
                            Snackbar.make(getView(), R.string.inicio_con_google_fallido, Snackbar.LENGTH_SHORT).show();
                        }
                    }
                });
            } catch (ApiException e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * Comprueba que los diferentes campos del formulario cumplen las expresiones regulares dependiendo de la
     * accion que se vaya a realizar.
     * @param esRecordarContraseña Booleano que indica que la accion a realizar en enviar un correo para
     *                             reestablecer la contraseña del usuario.
     * @param esRegistro Booleano que indica que la accion a realizar es el registro de un nuevo usuario.
     * @return
     */
    public boolean regexCampos(boolean esRecordarContraseña, boolean esRegistro){

        boolean respuesta = false;

        if ((esMail() && esContraseñaSegura()) || (esMail() && esRecordarContraseña) ){
            respuesta = true;
        }else {

            if(!esMail()){

                if(mETUsuario.getError() == null) {
                    mETUsuario.setError(getString(R.string.no_email));
                }

            }

            if (!esRecordarContraseña && esRegistro && !esContraseñaSegura()){

                if(mETContraseña.getError() == null){
                    mETContraseña.setError(getString(R.string.contraseña_no_segura));
                }

            }

        }

        return respuesta;

    }

    /**
     * Comprueba los diferentes campos del formulario de login segun la accion que se quiera llevar a cabo y
     * muestra los errores correspondientes.
     * @param esRecordarContraseña Booleano que indica que la accion a realizar en enviar un correo para
     *                             reestablecer la contraseña del usuario.
     * @param esRegistro Booleano que indica que la accion a realizar es un nuevo registro.
     * @return Devuelve true en el caso de que no se encuentren errores en los datos introducidos o false para el
     * caso contrario.
     */
    public boolean comprobarCampos(boolean esRecordarContraseña, boolean esRegistro){

        boolean respuesta = false;

        mUsuario = mETUsuario.getEditText().getText().toString();
        mContraseña = mETContraseña.getEditText().getText().toString();

        limpiarErroresFormulario();

        if((!mUsuario.isEmpty() && !mContraseña.isEmpty()) || (!mUsuario.isEmpty() && esRecordarContraseña)){

            if(regexCampos(esRecordarContraseña, esRegistro)){
                respuesta = true;
            }

        }else{

            if(mUsuario.isEmpty()){
                mETUsuario.setError(getString(R.string.usuario_no_introducido));
            }else{
                regexCampos(esRecordarContraseña, esRegistro);
            }

            if(mContraseña.isEmpty() && !esRecordarContraseña){
                mETContraseña.setError(getString(R.string.contraseña_no_introducida));
            }else{
                regexCampos(esRecordarContraseña, esRegistro);
            }

        }

        return respuesta;

    }

    /**
     * Comprueba mendiante una Expresion Regular que el email introducido por el usuario es valido.
     * @return
     */
    public boolean esMail(){

        return mRegexMail.matcher(mUsuario).matches();

    }

    /**
     * Comprueba mediante una Expresion Regular que la contraseña introducida por el usuario es segura.
     * @return
     */
    public boolean esContraseñaSegura(){

        return mRegexContraseña.matcher(mContraseña).matches();

    }

    /**
     * Limpia los EditText que contienen los errores.
     */
    public void limpiarErroresFormulario(){

        mETUsuario.setError(null);
        mETContraseña.setError(null);

    }

    /**
     * Muestra un dialogo con un mensaje de error si durante el registro/logeo se produce algun error.
     * @param esRegistro Booleano que indica si el error que vamos a mostrar esta relacionado con el registro de usuarios.
     * @param esLogin Booleano que inidica si el error que vamos a mostrar esta relacionado con el logeo.
     * @param esContraseñaOlvidada Booleano que indica si el error que vamos a mostrar esta relacionado
     *                             con el reestablecimiento de contraseñas
     */
    public void mostrarDialogError(boolean esRegistro, boolean esLogin, boolean esContraseñaOlvidada){

        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        if (esRegistro){
            builder.setTitle(R.string.fallo_registro);
            builder.setMessage(R.string.no_se_pudo_registrar);
            builder.setPositiveButton(R.string.volver_login, null);
        }else if (esLogin){
            builder.setTitle(R.string.fallo_inicio_sesion);
            builder.setMessage(R.string.no_se_pudo_iniciar_sesion);
            builder.setPositiveButton(R.string.volver_login, null);
        }else if (esContraseñaOlvidada){
            builder.setTitle(R.string.fallo_contraseña_olvidada);
            builder.setMessage(R.string.no_se_pudo_recuperar_contraseña);
            builder.setPositiveButton(R.string.volver_login, null);
        }

        AlertDialog dialog = builder.create();
        dialog.show();

    }



}