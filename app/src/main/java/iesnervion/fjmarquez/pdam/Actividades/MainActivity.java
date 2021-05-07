package iesnervion.fjmarquez.pdam.Actividades;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentContainerView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import iesnervion.fjmarquez.pdam.Fragmentos.FragmentDiasRutina;
import iesnervion.fjmarquez.pdam.Fragmentos.FragmentInicial;
import iesnervion.fjmarquez.pdam.Fragmentos.FragmentListaEjercicios;
import iesnervion.fjmarquez.pdam.Fragmentos.FragmentLogin;
import iesnervion.fjmarquez.pdam.Fragmentos.FragmentPostRegistro;
import iesnervion.fjmarquez.pdam.R;
import iesnervion.fjmarquez.pdam.Utiles.TipoFragmento;
import iesnervion.fjmarquez.pdam.Utiles.Utiles;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelLogin;

public class MainActivity extends AppCompatActivity {

    /* ATRIBUTOS */
    private FragmentContainerView mContenedorGeneral;
    private ViewModelLogin mViewModel;
    //Observer que se encargara de la navegacion entre fragmentos.
    private Observer<TipoFragmento> mTipoFragmentoObserver = new Observer<TipoFragmento>() {
        @Override
        public void onChanged(TipoFragmento tipoFragmento) {

            switch (tipoFragmento){
                case LOGIN:
                    break;
                case POST_REGISTRO:
                    cambiarFragment(mFragmentPostRegistro);
                    break;
                case PANTALLA_INICIO:
                    cambiarFragment(mFragmentoInicial);
                    break;
                case DIAS_RUTINA:
                    cambiarFragment(mFragmentoDiasRutina);
                    break;
                case EJERCICIOS:
                    cambiarFragment(mFragmentoListaEjercicios);
                    break;
            }

        }
    };

    private FragmentLogin mFragmentLogin;
    private FragmentPostRegistro mFragmentPostRegistro;
    private FragmentInicial mFragmentoInicial;
    private FragmentDiasRutina mFragmentoDiasRutina;
    private FragmentListaEjercicios mFragmentoListaEjercicios;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Pdam);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Utiles.pruebaFirebase();

        inicializar();

        fragmentInicial();
    }

    /**
     * Inicializa los componentes de la vista correspondiente a MainActivity
     */
    public void inicializar(){

        mViewModel = new ViewModelProvider(this).get(ViewModelLogin.class);
        //Observacion de la variable que se encargara de almacenar el fragment en el cual debe encontrarse la app
        mViewModel.getmTipoFragmento().observe(this, mTipoFragmentoObserver);
        mContenedorGeneral = findViewById(R.id.contenedorGeneral);
        mFragmentLogin = new FragmentLogin();
        mFragmentPostRegistro = new FragmentPostRegistro();
        mFragmentoInicial = new FragmentInicial();
        mFragmentoDiasRutina = new FragmentDiasRutina();
        mFragmentoListaEjercicios = new FragmentListaEjercicios();

    }

    // TODO: 17/04/2021 comprobar si hay alguna sesion iniciada, si la hay mandar directamente a la app, y si no la hay mandar al login

    /**
     * Establece el fragment que debe ser el primero en mostrarse y lo carga en el fragment container.
     */
    public void fragmentInicial(){

        if (mViewModel.usuarioActual() != null) {
            mViewModel.usuarioExisteFirebase().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        DocumentSnapshot documento = task.getResult();
                        if (documento.exists()){
                            cambiarFragment(mFragmentoInicial);
                        } else {
                            cambiarFragment(mFragmentPostRegistro);
                        }
                    }
                }
            });
        }else {
            cambiarFragment(mFragmentLogin);
        }

    }

    /**
     * Navega hacia otro fragment.
     * @param fragment Fragment hacia el que se desea navegar.
     */
    public void cambiarFragment(Fragment fragment){

        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(mContenedorGeneral.getId(), fragment)
                .addToBackStack(null)
                .commit();

    }

}