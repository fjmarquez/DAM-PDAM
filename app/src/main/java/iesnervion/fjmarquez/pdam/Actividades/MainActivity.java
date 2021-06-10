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

import iesnervion.fjmarquez.pdam.Fragmentos.FragmentDetalleEjercicio;
import iesnervion.fjmarquez.pdam.Fragmentos.FragmentDiasRutina;
import iesnervion.fjmarquez.pdam.Fragmentos.FragmentListaHistorico;
import iesnervion.fjmarquez.pdam.Fragmentos.FragmentInicial;
import iesnervion.fjmarquez.pdam.Fragmentos.FragmentListaEjercicios;
import iesnervion.fjmarquez.pdam.Fragmentos.FragmentListaRutinas;
import iesnervion.fjmarquez.pdam.Fragmentos.FragmentLogin;
import iesnervion.fjmarquez.pdam.Fragmentos.FragmentPerfil;
import iesnervion.fjmarquez.pdam.Fragmentos.FragmentPostRegistro;
import iesnervion.fjmarquez.pdam.Fragmentos.FragmentRealizarEjercicio;
import iesnervion.fjmarquez.pdam.R;
import iesnervion.fjmarquez.pdam.Utiles.TipoFragmento;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelRutina;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelUsuario;

public class MainActivity extends AppCompatActivity {

    /* ATRIBUTOS */
    private FragmentContainerView mContenedorGeneral;
    private ViewModelUsuario mViewModelUsuario;
    private ViewModelRutina mViewModelRutina;
    //Observer que se encargara de la navegacion entre fragmentos.
    private Observer<TipoFragmento> mTipoFragmentoObserver = new Observer<TipoFragmento>() {
        @Override
        public void onChanged(TipoFragmento tipoFragmento) {

            mViewModelUsuario.setmUltimoFragmento(tipoFragmento);

            switch (tipoFragmento){
                case LOGIN:
                    cambiarFragment(mFragmentLogin, false);
                    break;
                case POST_REGISTRO:
                    cambiarFragment(mFragmentPostRegistro, false);
                    break;
                case PANTALLA_INICIO:
                    cambiarFragment(mFragmentoInicial, false);
                    break;
                case DIAS_RUTINA:
                    cambiarFragment(mFragmentoDiasRutina, false);
                    break;
                case EJERCICIOS:
                    cambiarFragment(mFragmentoListaEjercicios, true);
                    break;
                case DETALLE_EJERCICIO:
                    cambiarFragment(mFragmentoDetalleEjercicio, true);
                    break;
                case PERFIL:
                    cambiarFragment(mFragmentPerfil, true);
                    break;
                case HISTORICO:
                    cambiarFragment(mFragmentListaHistorico, true);
                    break;
                case REALIZAR_EJERCICIO:
                    cambiarFragment(mFragmentRealizarEjercicio, true);
                    break;
                case LISTA_RUTINAS:
                    cambiarFragment(mFragmentListaRutinas, true);
            }

        }
    };

    private boolean mFragmentInicial;
    private FragmentLogin mFragmentLogin;
    private FragmentPostRegistro mFragmentPostRegistro;
    private FragmentInicial mFragmentoInicial;
    private FragmentDiasRutina mFragmentoDiasRutina;
    private FragmentListaEjercicios mFragmentoListaEjercicios;
    private FragmentDetalleEjercicio mFragmentoDetalleEjercicio;
    private FragmentPerfil mFragmentPerfil;
    private FragmentListaHistorico mFragmentListaHistorico;
    private FragmentRealizarEjercicio mFragmentRealizarEjercicio;
    private FragmentListaRutinas mFragmentListaRutinas;

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

        mViewModelUsuario = new ViewModelProvider(this).get(ViewModelUsuario.class);
        mViewModelRutina = new ViewModelProvider(this).get(ViewModelRutina.class);
        //Observacion de la variable que se encargara de almacenar el fragment en el cual debe encontrarse la app
        mViewModelUsuario.getmTipoFragmento().observe(this, mTipoFragmentoObserver);
        mContenedorGeneral = findViewById(R.id.contenedorGeneral);

        mFragmentInicial = false;
        mFragmentLogin = new FragmentLogin();
        mFragmentPostRegistro = new FragmentPostRegistro();
        mFragmentoInicial = new FragmentInicial();
        mFragmentoDiasRutina = new FragmentDiasRutina();
        mFragmentoListaEjercicios = new FragmentListaEjercicios();
        mFragmentoDetalleEjercicio = new FragmentDetalleEjercicio();
        mFragmentPerfil = new FragmentPerfil();
        mFragmentListaHistorico = new FragmentListaHistorico();
        mFragmentRealizarEjercicio = new FragmentRealizarEjercicio();
        mFragmentListaRutinas = new FragmentListaRutinas();

    }

    /**
     * Establece el fragment que debe ser el primero en mostrarse y lo carga en el fragment container.
     */
    public void fragmentInicial(){
        //Comprueba que existe un usuario actualmente, si no existe redirige al login
        if (mViewModelUsuario.usuarioActual() != null) {

            //Compruba si existe un documento usuario en firestore con el uid del usuario actual
            mViewModelUsuario.usuarioExisteFirebase().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                    if (task.isSuccessful()){
                        DocumentSnapshot documento = task.getResult();
                        //si existe un usuario en firestore
                        if (documento.exists()){

                            mViewModelRutina.comprobarSiExisteRutinaUsuarioActual().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                @Override
                                public void onComplete(Task<DocumentSnapshot> task) {
                                    if (task.isSuccessful()){
                                        //si el usuario actual ya creo su primera rutina
                                        if (task.getResult().exists()){
                                            //se redirige al fragment inicial
                                            if (!mFragmentInicial){
                                                cambiarFragment(mFragmentoInicial, false);
                                                mFragmentInicial = true;
                                            }
                                        //si el usuario existe en firestore pero no creo su primera rutina
                                        }else{
                                            //se redirige al fragment Dias Rutinas para que el usuario defina su rutina
                                            if (!mFragmentInicial){
                                                cambiarFragment(mFragmentoDiasRutina, false);
                                                mFragmentInicial = true;
                                            }
                                        }
                                    }
                                }
                            });
                        //si no existe un usuario en firestore
                        } else {
                            //se redirige al fragment post-registro para recopilar la info necesaria y crear un documento usuario
                            if (!mFragmentInicial){
                                cambiarFragment(mFragmentPostRegistro, false);
                                mFragmentInicial = true;
                            }
                        }
                    }
                }
            });
        //si no existe usuario actual se redirige al login
        }else {
            cambiarFragment(mFragmentLogin, false);
        }

    }

    /**
     * Navega hacia otro fragment.
     * @param fragment Fragment hacia el que se desea navegar.
     */
    public void cambiarFragment(Fragment fragment, boolean puedeRetroceder){

        if(puedeRetroceder){
            getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(mContenedorGeneral.getId(), fragment)
                    .addToBackStack(null)
                    .commit();
        }else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .setReorderingAllowed(true)
                    .replace(mContenedorGeneral.getId(), fragment)
                    .commit();
        }

    }

}