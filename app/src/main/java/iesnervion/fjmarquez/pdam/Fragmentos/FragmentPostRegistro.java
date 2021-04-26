package iesnervion.fjmarquez.pdam.Fragmentos;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import iesnervion.fjmarquez.pdam.Entidades.Usuario;
import iesnervion.fjmarquez.pdam.R;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelPrincipal;

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

    private ViewModelPrincipal mViewModel;

    private String mNombre;
    private String mApellidos;
    private int mEdad;
    private Double mPeso;
    private Double mAltura;

    public FragmentPostRegistro() {

    }

    public static FragmentPostRegistro newInstance() {
        FragmentPostRegistro fragment = new FragmentPostRegistro();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(getActivity()).get(ViewModelPrincipal.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_post_registro, container, false);

        mETNombre = v.findViewById(R.id.ETNombre);
        mETApellidos = v.findViewById(R.id.ETApellidos);
        mETEdad = v.findViewById(R.id.ETEdad);
        mETAltura = v.findViewById(R.id.ETAltura);
        mETPeso = v.findViewById(R.id.ETPeso);
        mBtnEnviarDatosUsuario = v.findViewById(R.id.BTNEnviarDatosUsuario);
        mBtnEnviarDatosUsuario.setOnClickListener(this);

        return v;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.BTNEnviarDatosUsuario:
                recopilarDatosUsuario();
                break;
        }
    }

    public void recopilarDatosUsuario(){

        mNombre = mETNombre.getEditText().getText().toString();
        mApellidos = mETApellidos.getEditText().getText().toString();
        mEdad = Integer.parseInt(mETEdad.getEditText().getText().toString());
        mAltura = Double.parseDouble(mETAltura.getEditText().getText().toString());
        mPeso = Double.parseDouble(mETPeso.getEditText().getText().toString());

        mViewModel.setmUsuario(new Usuario(mViewModel.usuarioActual(), mNombre, mApellidos, mAltura, mPeso, mEdad));

    }
}