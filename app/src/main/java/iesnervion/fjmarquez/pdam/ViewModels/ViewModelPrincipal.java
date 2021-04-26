package iesnervion.fjmarquez.pdam.ViewModels;

import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;

import iesnervion.fjmarquez.pdam.Entidades.Usuario;
import iesnervion.fjmarquez.pdam.Repositorios.RepositorioLoginFirebase;
import iesnervion.fjmarquez.pdam.Utiles.TipoFragmento;


public class ViewModelPrincipal extends androidx.lifecycle.ViewModel{

    /* ATRIBUTOS */
    private RepositorioLoginFirebase mRepositorioLogin;
    private MutableLiveData<TipoFragmento> mTipoFragmento = new MutableLiveData<TipoFragmento>();
    private Usuario mUsuario;

    /* SETTERS */
    public void setmTipoFragmento(TipoFragmento mTipoFragmento) {

        this.mTipoFragmento.setValue(mTipoFragmento);

    }

    public void setmUsuario(Usuario mUsuario) {
        this.mUsuario = mUsuario;
    }

    /* GETTERS */
    public MutableLiveData<TipoFragmento> getmTipoFragmento() {

        return mTipoFragmento;

    }

    public Usuario getmUsuario() {
        return mUsuario;
    }

    /* CONSTRUCTOR */
    public ViewModelPrincipal() {
        mRepositorioLogin = new RepositorioLoginFirebase();
    }

    /* FUNCIONES */

    /**
     * Mediante la clase RepositorioLoginFirebase obtiene el usuario que actualmente esta logeado en la app (puede obtener
     * un usuario o no).
     * @return Devuelve un FirebaseUser con toda la informacion del usuario logeado o null.
     */
    public FirebaseUser usuarioActual(){

        return mRepositorioLogin.usuarioActual();

    }

    /**
     * Mediante la clase RespositorioLoginFirebase se registra a traves de Firebase Authentication usando un email
     * y una contraseña.
     * @param Email Email introducido por el usuario en el formulario de login.
     * @param Contraseña Contraseña introducida por el usuario en el formulario de login.
     * @return Devuelve una tarea, la cual controlaremos en el Fragment correspondiente.
     */
    public Task<AuthResult> registrarNuevoUsuario(String Email, String Contraseña){

        return mRepositorioLogin.registrarNuevoUsuario(Email, Contraseña);

    }

    /**
     * Mediante la clase RepositorioLoginFirebase se logea a traves de Firebase Authentication
     * usando un email y una contraseña.
     * @param Email Email introducido por el usuario en el formulario del login
     * @param Contraseña Contraseña introducida por el usuario en el formulario del login.
     * @return Devuelve una tarea, la cual controlaremos en el Fragment correspondiente.
     */
    public Task<AuthResult> iniciarSesionConUsuarioYContraseña(String Email, String Contraseña){

        return mRepositorioLogin.iniciarSesionConUsuarioYContraseña(Email, Contraseña);

    }

    /**
     * Mediante la clase RepositorioLoginFirebase se autentica en Google.
     * @param idToken Token necesario para autenticarse en Google.
     * @param context Contexto desde donde lanzaremos en Intent necesario para autenticarse con Google.
     * @return Devuelve un intent, el cual sera lanzado y controlado en el Fragment correspondiente.
     */
    public Intent autenticacionGoogle(String idToken, Context context){

        return mRepositorioLogin.autenticacionGoogle(idToken, context);

    }

    /**
     * Mediante la clase RepositorioLoginFirebase se registra/logea usando Firebase Authentication
     * en la app usando una cuenta de Google.
     * @param idToken Token de la cuenta de Google mediante la que el usuario se ha autenticado en Google.
     * @return Devuelve una tarea, la cual controlaremos en el Frament correspondiente.
     */
    public Task<AuthResult> accederMedianteGoogle(String idToken){

        return mRepositorioLogin.accederMedianteGoogle(idToken);

    }

    /**
     * Mediante la clase RepositorioLoginFirebase envia un correo a traves de Firebase Authentication
     * al mail indicado para reestablecer su contraseña.
     * @param Email Email introducido por el usuario en el formulario de login.
     * @return Devuelve una tarea, la cual controlaremos en el Fragment correspondiente.
     */
    public Task<Void> mandarMailRecuperarContraseña(String Email){
        return mRepositorioLogin.mandarMailRecuperarContraseña(Email);
    }

}
