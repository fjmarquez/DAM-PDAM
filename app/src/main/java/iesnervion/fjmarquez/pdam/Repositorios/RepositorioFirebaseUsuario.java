package iesnervion.fjmarquez.pdam.Repositorios;

import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import iesnervion.fjmarquez.pdam.Entidades.Usuario;

public class RepositorioFirebaseUsuario {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;
    FirebaseFirestore mFirestoreDB;
    CollectionReference mUsuariosColRef;

    /**
     * Constuctor de la clase.
     */
    public RepositorioFirebaseUsuario() {
        this.mAuth = FirebaseAuth.getInstance();
        this.mFirestoreDB = FirebaseFirestore.getInstance();
        this.mUsuariosColRef = mFirestoreDB.collection("usuarios");
    }

    /**
     * Obtiene el usuario que actualmente esta autenticado mediante Firebase en nuestra aplicacion.
     * En caso de no existir ningun usuario autenticado en ese momento devolvera null.
     * @return  Devuelve un FirebaseUser con la informacion de usuario, puede ser null.
     */
    public FirebaseUser usuarioActual(){
        return mAuth.getCurrentUser();
    }

    /**
     * Comprueba si el usuario actual completo el formulario post-registro.
     * @return Devuelve una tarea, mediante la cual podra realizar una accion cuando esta sea completada (puede finalizar
     * correctamente o no).
     */
    public Task<DocumentSnapshot> usuarioExisteFirestore(){

        DocumentReference mUsuarioDocRef = mUsuariosColRef.document(usuarioActual().getUid());

        return mUsuarioDocRef.get();

    }

    /**
     * Añade al usuario a la base de datos de Firestore una vez este rellena el formulario post-registro.
     * @param usuario Objeto Usuario con la informacion del usuario registrado.
     * @return Devuelve una tarea, mediante la cual podra realizar una accion cuando esta sea completada (puede finalizar
     * correctamente o no).
     */
    public Task añadirUsuarioFirestore(Usuario usuario){

        DocumentReference mUsuarioDocRef = mUsuariosColRef.document(usuarioActual().getUid());

        return mUsuarioDocRef.set(usuario);

    }

    /**
     * Registra un nuevo usuario en Firebase Authentication con su email y su correspondiente contraseña.
     * @param Email Email introducido por el usuario en el formulario de login.
     * @param Contraseña    Contraseña introducida por el usuario en el formulario de login.
     * @return  Devuelve una tarea, mediante la cual podra realizar alguna accion cuando esta sea completada (puede finalizar
     * correctamente o no).
     */
    public Task<AuthResult> registrarNuevoUsuario(String Email, String Contraseña){

        return mAuth.createUserWithEmailAndPassword(Email, Contraseña);

    }

    /**
     * Inicia sesion con el usuario cuyo email y contraseña coincidan con las recibidas por parametros.
     * @param Email Email introducido por el usuario en el formulario de login
     * @param Contraseña Contraseña introducida por el usuario en el formulario de login
     * @return Devuelve una tarea, mediante la cual podremos realizar alguna accion cuando esta sea completada (puede finalizar
     * correctamente o no)
     */
    public Task<AuthResult> iniciarSesionConUsuarioYContraseña(String Email, String Contraseña){
        return mAuth.signInWithEmailAndPassword(Email, Contraseña);
    }

    /**
     * Se autentica en Google usando un token.
     * @param idToken Token mediante el que autenticarse en Google.
     * @param context Contexto desde donde lanzaremos en Intent necesario para autenticarse con Google.
     * @return Devuelve un intent que sera lanzado y cuya respuesta sera recopilada desde startForActivityResult.
     */
    public Intent autenticacionGoogle(String idToken, Context context){

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(idToken)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);

        return mGoogleSignInClient.getSignInIntent();

    }

    /**
     * Obtiene el token de una cuenta de Google autenticada previamente y se registra/logea con esos datos.
     * @param idToken Token de la cuenta de Google del usuario.
     * @return Devuelve una tarea, mediante la cual podremos realizar alguna accion cuando esta sea completada (puede finalizar
     * correctamente o no)
     */
    public Task<AuthResult> accederMedianteGoogle(String idToken){

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        return this.mAuth.signInWithCredential(credential);

    }

    /**
     * Mediante Firebase Authentication envia un correo para reestablecer la contraseña del usuario cuyo email coincida con el
     * recibido.
     * @param Email Email introducido por el usuario en el formulario de login.
     * @return Devuelve una tarea, mediante la cual podremos realizar alguna accion cuando esta sea completada (puede finalizar
     * correctamente o no)
     */
    public Task<Void> mandarMailRecuperarContraseña(String Email){
        return this.mAuth.sendPasswordResetEmail(Email);
    }

}
