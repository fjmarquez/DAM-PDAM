package iesnervion.fjmarquez.pdam.Repositorios;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.GoogleAuthProvider;

public class RepositorioLoginFirebase {

    private FirebaseAuth mAuth;
    private GoogleSignInClient mGoogleSignInClient;

    public RepositorioLoginFirebase() {
        this.mAuth = FirebaseAuth.getInstance();
    }

    public Task<AuthResult> registrarNuevoUsuario(String Email, String Contraseña){

        return this.mAuth.createUserWithEmailAndPassword(Email, Contraseña);

    }

    public Intent autenticacionGoogle(String idToken, Context context){

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(idToken)
                .requestEmail()
                .build();

        mGoogleSignInClient = GoogleSignIn.getClient(context, gso);

        return mGoogleSignInClient.getSignInIntent();

    }

    public Task<AuthResult> accederMedianteGoogle(String idToken){

        AuthCredential credential = GoogleAuthProvider.getCredential(idToken, null);

        return this.mAuth.signInWithCredential(credential);

    }

}
