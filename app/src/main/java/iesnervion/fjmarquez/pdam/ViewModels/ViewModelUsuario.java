package iesnervion.fjmarquez.pdam.ViewModels;

import android.content.Context;
import android.content.Intent;

import androidx.lifecycle.MutableLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;

import iesnervion.fjmarquez.pdam.Entidades.Usuario;
import iesnervion.fjmarquez.pdam.Repositorios.RepositorioFirestoreUsuario;
import iesnervion.fjmarquez.pdam.Utiles.TipoFragmento;

/**
 * ViewModel destinado a mantener la informacion necesaria sobre el usuario y a persistir y obtener esta
 * mediante el Repositorio de usuarios.
 */
public class ViewModelUsuario extends androidx.lifecycle.ViewModel {

    /* ATRIBUTOS */

    private RepositorioFirestoreUsuario mRepositorioUsuarios;
    private MutableLiveData<TipoFragmento> mTipoFragmento = new MutableLiveData<TipoFragmento>();
    private TipoFragmento mUltimoFragmento;
    private Usuario mUsuario;

    /* CONSTRUCTOR */

    public ViewModelUsuario() {
        mRepositorioUsuarios = new RepositorioFirestoreUsuario();
        mUltimoFragmento = null;
    }

    /* SETTERS */

    public void setmTipoFragmento(TipoFragmento mTipoFragmento) {

        this.mTipoFragmento.setValue(mTipoFragmento);
        this.mUltimoFragmento = null;

    }

    public void setmUsuario(Usuario mUsuario) {
        this.mUsuario = mUsuario;
    }

    public void setmUltimoFragmento(TipoFragmento mUltimoFragmento) {
        this.mUltimoFragmento = mUltimoFragmento;
    }

    /* GETTERS */

    public MutableLiveData<TipoFragmento> getmTipoFragmento() {

        return mTipoFragmento;

    }

    public Usuario getmUsuario() {
        return mUsuario;
    }

    public TipoFragmento getmUltimoFragmento() {
        return mUltimoFragmento;
    }

    /* FUNCIONES */

    /**
     * Mediante la clase RepositorioLoginFirebase obtiene el usuario que actualmente esta logeado en la app (puede obtener
     * un usuario o no).
     *
     * @return Devuelve un FirebaseUser con toda la informacion del usuario logeado o null.
     */
    public FirebaseUser usuarioActual() {

        return mRepositorioUsuarios.usuarioActual();

    }

    /**
     * Comprueba si un usuario de Firebase tiene almacenado sus datos en Firestore, a traves del Repositorio de usuarios.
     *
     * @return Devuelve una tarea, la cual controlaremos en el Fragment correspondiente.
     */
    public Task<DocumentSnapshot> usuarioExisteFirestore(){

        return mRepositorioUsuarios.usuarioExisteFirestore();

    }

    /**
     * Añade o actualiza un usuario en Firestore, a traves del Repositorio de usuarios.
     *
     * @param usuario Objeto Usuario a añadir/actualizar.
     * @return Devuelve una tarea, la cual controlaremos en el Fragment correspondiente.
     */
    public Task añadirOActualizarUsuarioFirestore(Usuario usuario){

        return mRepositorioUsuarios.añadirOActualizarUsuarioFirestore(usuario);

    }

    /**
     * Obtiene el usuario actual en Firestore, a traves del Repositorio de usuarios.
     *
     * @return Devuelve una tarea, la cual controlaremos en el Fragment correspondiente.
     */
    public Task<DocumentSnapshot> obtenerUsuarioFirestore(){

        return mRepositorioUsuarios.obtenerUsuarioFirestore();

    }

    /**
     * Mediante la clase RespositorioLoginFirebase se registra a traves de Firebase Authentication usando un email
     * y una contraseña.
     *
     * @param Email      Email introducido por el usuario en el formulario de login.
     * @param Contraseña Contraseña introducida por el usuario en el formulario de login.
     * @return Devuelve una tarea, la cual controlaremos en el Fragment correspondiente.
     */
    public Task<AuthResult> registrarNuevoUsuario(String Email, String Contraseña) {

        return mRepositorioUsuarios.registrarNuevoUsuario(Email, Contraseña);

    }

    /**
     * Mediante la clase RepositorioLoginFirebase se logea a traves de Firebase Authentication
     * usando un email y una contraseña.
     *
     * @param Email      Email introducido por el usuario en el formulario del login
     * @param Contraseña Contraseña introducida por el usuario en el formulario del login.
     * @return Devuelve una tarea, la cual controlaremos en el Fragment correspondiente.
     */
    public Task<AuthResult> iniciarSesionConUsuarioYContraseña(String Email, String Contraseña) {

        return mRepositorioUsuarios.iniciarSesionConUsuarioYContraseña(Email, Contraseña);

    }

    /**
     * Mediante la clase RepositorioLoginFirebase se autentica en Google.
     *
     * @param idToken Token necesario para autenticarse en Google.
     * @param context Contexto desde donde lanzaremos en Intent necesario para autenticarse con Google.
     * @return Devuelve un intent, el cual sera lanzado y controlado en el Fragment correspondiente.
     */
    public Intent autenticacionGoogle(String idToken, Context context) {

        return mRepositorioUsuarios.autenticacionGoogle(idToken, context);

    }

    /**
     * Mediante la clase RepositorioLoginFirebase se registra/logea usando Firebase Authentication
     * en la app usando una cuenta de Google.
     *
     * @param idToken Token de la cuenta de Google mediante la que el usuario se ha autenticado en Google.
     * @return Devuelve una tarea, la cual controlaremos en el Frament correspondiente.
     */
    public Task<AuthResult> accederMedianteGoogle(String idToken) {

        return mRepositorioUsuarios.accederMedianteGoogle(idToken);

    }

    /**
     * Mediante la clase RepositorioLoginFirebase envia un correo a traves de Firebase Authentication
     * al mail indicado para reestablecer su contraseña.
     *
     * @param Email Email introducido por el usuario en el formulario de login.
     * @return Devuelve una tarea, la cual controlaremos en el Fragment correspondiente.
     */
    public Task<Void> mandarMailRecuperarContraseña(String Email) {
        return mRepositorioUsuarios.mandarMailRecuperarContraseña(Email);
    }

    public void cerrarSesionActual(){

        mRepositorioUsuarios.cerrarSesionActual();

    }

}
