package iesnervion.fjmarquez.pdam.ViewModels;

import android.content.Context;
import android.content.Intent;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.GoogleAuthProvider;

import iesnervion.fjmarquez.pdam.Repositorios.RepositorioLoginFirebase;

public class ViewModelPrincipal extends androidx.lifecycle.ViewModel{

    private RepositorioLoginFirebase mRepositorioLogin;

    public ViewModelPrincipal() {
        mRepositorioLogin = new RepositorioLoginFirebase();
    }

    public Task<AuthResult> registrarNuevoUsuario(String Email, String Contraseña){

        return mRepositorioLogin.registrarNuevoUsuario(Email, Contraseña);

    }

    public Intent autenticacionGoogle(String idToken, Context context){

        return mRepositorioLogin.autenticacionGoogle(idToken, context);

    }

    public Task<AuthResult> accederMedianteGoogle(String idToken){

        return mRepositorioLogin.accederMedianteGoogle(idToken);

    }

}
