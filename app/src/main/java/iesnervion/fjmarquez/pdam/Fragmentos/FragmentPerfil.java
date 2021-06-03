package iesnervion.fjmarquez.pdam.Fragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;

import iesnervion.fjmarquez.pdam.Entidades.Ejercicio;
import iesnervion.fjmarquez.pdam.Entidades.Usuario;
import iesnervion.fjmarquez.pdam.R;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelRutina;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelUsuario;


public class FragmentPerfil extends Fragment {

    /* ATRIBUTOS */
    private View mFragmentView;

    private ViewModelUsuario mViewModelUsuario;
    private ViewModelRutina mViewModelRutina;

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

    public FragmentPerfil() {

    }


    public static FragmentPerfil newInstance(String param1, String param2) {
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

        accionesMenu();
        obtenerDatosUsuario();

        return mFragmentView;
    }

    public void obtenerDatosUsuario(){

        if(mViewModelUsuario.getmUsuario() == null){

            mViewModelUsuario.obtenerUsuarioFirestore().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(Task<DocumentSnapshot> task) {
                    if (task.isSuccessful() && task.getResult().exists()){

                        Usuario usuario = task.getResult().toObject(Usuario.class);
                        mViewModelUsuario.setmUsuario(usuario);
                        relleñarDatosUsuario();

                    }

                }
            });

        }

    }

    public void relleñarDatosUsuario(){

        String nombre = mViewModelUsuario.getmUsuario().getNombre();
        String apellidos = mViewModelUsuario.getmUsuario().getApellidos();
        String nombreCompleto = nombre + " " + apellidos;
        String email = mViewModelUsuario.getmUsuario().getEmail();
        Double peso = mViewModelUsuario.getmUsuario().getPeso();
        Integer altura = mViewModelUsuario.getmUsuario().getAltura();
        Integer edad = mViewModelUsuario.getmUsuario().getEdad();
        Double imc = peso / Math.pow((altura/100), 2);

        mTVNombre.setText(nombreCompleto);
        mTVNombre.setText(nombre);
        mTVEmail.setText(email);
        mTVIMC.setText(getText(R.string.imc_perfil) + " " + imc.toString());

        mETNombre.getEditText().setText(nombre);
        mETApellidos.getEditText().setText(apellidos);
        mETPeso.getEditText().setText(peso.toString());
        mETAltura.getEditText().setText(altura.toString());
        mETEdad.getEditText().setText(edad.toString());

    }

    public void accionesMenu(){

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){

                    case R.id.guardarPerfilMenu:
                        break;

                    case R.id.cambiarMailPerfilMenu:
                        break;

                    case R.id.cerrarSesionPerfilMenu:
                        break;

                }

                return false;

            }
        });

    }

}