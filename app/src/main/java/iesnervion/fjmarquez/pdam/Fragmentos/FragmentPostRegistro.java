package iesnervion.fjmarquez.pdam.Fragmentos;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;

import iesnervion.fjmarquez.pdam.Entidades.Usuario;
import iesnervion.fjmarquez.pdam.R;
import iesnervion.fjmarquez.pdam.Utiles.TipoFragmento;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelUsuario;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelEjercicios;

/**
 * Fragment destinado a recopilar informacion sobre el usuario despues de que este se haya
 * registrado.
 */
public class FragmentPostRegistro extends Fragment implements View.OnClickListener{

    /* ATRIBUTOS */
    private TextInputLayout mETNombre;
    private TextInputLayout mETApellidos;
    private TextInputLayout mETEdad;
    private TextInputLayout mETPeso;
    private TextInputLayout mETAltura;
    private Button mBtnEnviarDatosUsuario;

    private View mFragmentView;

    private ViewModelUsuario mViewModelUsuario;
    private ViewModelEjercicios mViewModelRutina;

    private String mNombre;
    private String mApellidos;
    private int mEdad;
    private Double mPeso;
    private Integer mAltura;

    public FragmentPostRegistro() {

    }

    public static FragmentPostRegistro newInstance() {
        FragmentPostRegistro fragment = new FragmentPostRegistro();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModelUsuario = new ViewModelProvider(getActivity()).get(ViewModelUsuario.class);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFragmentView = inflater.inflate(R.layout.fragment_post_registro, container, false);

        mETNombre = mFragmentView.findViewById(R.id.ETNombre);
        mETApellidos = mFragmentView.findViewById(R.id.ETApellidos);
        mETEdad = mFragmentView.findViewById(R.id.ETEdad);
        mETAltura = mFragmentView.findViewById(R.id.ETAltura);
        mETPeso = mFragmentView.findViewById(R.id.ETPeso);
        mBtnEnviarDatosUsuario = mFragmentView.findViewById(R.id.BTNEnviarDatosUsuario);
        mBtnEnviarDatosUsuario.setOnClickListener(this);

        return mFragmentView;

    }


    /**
     * Eventos onClick de los elementos del Fragment.
     * @param v Vista que contiene los elementos del Fragment.
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.BTNEnviarDatosUsuario:
                if(comprobarCampos()) {
                    recopilarYAlmacenarDatosUsuario();
                }
                break;
        }
    }

    /**
     * Obtiene toda la informacion del formulario, despues la persiste en Firestore.
     */
    public void recopilarYAlmacenarDatosUsuario(){

        mNombre = mETNombre.getEditText().getText().toString();
        mApellidos = mETApellidos.getEditText().getText().toString();
        mEdad = Integer.parseInt(mETEdad.getEditText().getText().toString());
        mAltura = Integer.parseInt(mETAltura.getEditText().getText().toString());
        mPeso = Double.parseDouble(mETPeso.getEditText().getText().toString());

        Usuario usuario = new Usuario(mViewModelUsuario.usuarioActual(), mNombre, mApellidos, mAltura, mPeso, mEdad);
        mViewModelUsuario.setmUsuario(usuario);

        mViewModelUsuario.añadirOActualizarUsuarioFirestore(usuario).addOnCompleteListener(new OnCompleteListener() {
            @Override
            public void onComplete(@NonNull Task task) {
                if (task.isSuccessful()){
                    mViewModelUsuario.setmTipoFragmento(TipoFragmento.DIAS_RUTINA);
                }
            }
        });

    }

    /**
     * Comprueba que los EditText presentes en el formulario han sido rellenados.
     * @return Devuelve un valor Boolean, si todos los campos estan rellenos devolvera true, en caso contrario false.
     */
    public boolean comprobarCampos(){

        boolean respuesta = true;

        limpiarErrores();

        if(mETNombre.getEditText().getText().toString().length() <= 0){
            respuesta = false;
            mETNombre.setError(getText(R.string.nombre_vacio));
        }
        if(mETApellidos.getEditText().getText().toString().length() <= 0){
            respuesta = false;
            mETApellidos.setError(getText(R.string.apellidos_vacios));
        }
        if(mETAltura.getEditText().getText().toString().length() <= 0){
            respuesta = false;
            mETAltura.setError(getText(R.string.altura_vacio));
        }
        if (mETEdad.getEditText().getText().toString().length() <= 0){
            respuesta = false;
            mETEdad.setError(getText(R.string.edad_vacio));
        }
        if(mETPeso.getEditText().getText().toString().length() <= 0){
            respuesta = false;
            mETPeso.setError(getText(R.string.peso_vacio));
        }

        return respuesta;

    }

    /**
     * Limpia/Vacia los campos disponibles para mostrar los errores de los EditText usados en el formulario.
     */
    public void limpiarErrores(){
        mETNombre.setError(null);
        mETApellidos.setError(null);
        mETPeso.setError(null);
        mETEdad.setError(null);
        mETAltura.setError(null);
    }



}