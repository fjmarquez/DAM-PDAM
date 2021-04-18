package iesnervion.fjmarquez.pdam;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentContainerView;

import android.os.Bundle;

import iesnervion.fjmarquez.pdam.Fragmentos.FragmentLogin;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelPrincipal;

public class MainActivity extends AppCompatActivity {

    //Atributos
    private FragmentContainerView mContenedorGeneral;

    private ViewModelPrincipal mViewModel;

    private FragmentLogin mFragmentLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_Pdam);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inicializar();

        fragmentInicial();
    }

    public void inicializar(){
        mContenedorGeneral = findViewById(R.id.contenedorGeneral);
        mFragmentLogin = new FragmentLogin();
    }

    // TODO: 17/04/2021 comprobar si hay alguna sesion iniciada, si la hay mandar directamente a la app, y si no la hay mandar al login
    public void fragmentInicial(){
        getSupportFragmentManager()
                .beginTransaction()
                .setReorderingAllowed(true)
                .replace(mContenedorGeneral.getId(), mFragmentLogin)
                .commit();
    }

}