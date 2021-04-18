package iesnervion.fjmarquez.pdam.Fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Pattern;

import iesnervion.fjmarquez.pdam.R;

public class FragmentLogin extends Fragment implements View.OnClickListener{

    //Atributos
    private Button mBtnInciarSesion;
    private Button mBtnRegistrar;
    private Button mBtnRegistrarGoogle;
    private Button mBtnRecordarContraseña;

    private TextInputLayout mETUsuario;
    private TextInputLayout mETContraseña;

    private String mUsuario;
    private String mContraseña;

    private Pattern mRegexMail = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private Pattern mRegexContraseña = Pattern.compile("((?=.*[a-z])(?=.*\\d)(?=.*[A-Z])(?=.*[@#._$,:;?¿¡)(*^<>%!]).{8,40})");

    public FragmentLogin() {

    }

    public static FragmentLogin newInstance() {

        FragmentLogin fragment = new FragmentLogin();
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        //Inicializo el ViewModel

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_login, container, false);

        //Identifico los componentes de la vista
        mBtnInciarSesion = v.findViewById(R.id.BTNIniciarSesion);
        mBtnInciarSesion.setOnClickListener(this);
        mBtnRegistrar = v.findViewById(R.id.BTNRegistrarse);
        mBtnRegistrar.setOnClickListener(this);
        mBtnRegistrarGoogle = v.findViewById(R.id.BTNRegistrarseGoogle);
        mBtnRegistrarGoogle.setOnClickListener(this);
        mBtnRecordarContraseña = v.findViewById(R.id.BTNRecordarContraseña);
        mBtnRecordarContraseña.setOnClickListener(this);

        mETUsuario = v.findViewById(R.id.ETUsuario);
        mETContraseña = v.findViewById(R.id.ETContraseña);

        return v;

    }

    @Override
    public void onClick(View v) {

            switch (v.getId()){
                case R.id.BTNIniciarSesion:
                    if(comprobarCampos(false, false)){
                        Toast.makeText(getContext(), "iniciar sesion", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.BTNRegistrarse:
                    if(comprobarCampos(false, true)){
                        Toast.makeText(getContext(), "registro correcto", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case R.id.BTNRegistrarseGoogle:
                    Toast.makeText(getContext(), "registrar con google", Toast.LENGTH_SHORT).show();
                    break;
                case R.id.BTNRecordarContraseña:
                    if (comprobarCampos(true, false)){
                        Toast.makeText(getContext(), "recordar contraseña", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }

    }

    public boolean regexCampos(boolean esRecordarContraseña, boolean esRegistro){

        boolean respuesta = false;

        if ((esMail() && esContraseñaSegura()) || (esMail() && esRecordarContraseña) ){
            respuesta = true;
        }else {

            if(!esMail()){

                if(mETUsuario.getError() == null) {
                    mETUsuario.setError(getString(R.string.no_email));
                }

            }

            if (!esRecordarContraseña && esRegistro && !esContraseñaSegura()){

                if(mETContraseña.getError() == null){
                    mETContraseña.setError(getString(R.string.contraseña_no_segura));
                }

            }

        }

        return respuesta;

    }

    public boolean comprobarCampos(boolean esRecordarContraseña, boolean esRegistro){

        boolean respuesta = false;

        mUsuario = mETUsuario.getEditText().getText().toString();
        mContraseña = mETContraseña.getEditText().getText().toString();

        limpiarErroresFormulario();

        if((!mUsuario.isEmpty() && !mContraseña.isEmpty()) || (!mUsuario.isEmpty() && esRecordarContraseña)){

            if(regexCampos(esRecordarContraseña, esRegistro)){
                respuesta = true;
            }

        }else{

            if(mUsuario.isEmpty()){
                mETUsuario.setError(getString(R.string.usuario_no_introducido));
            }else{
                regexCampos(esRecordarContraseña, esRegistro);
            }

            if(mContraseña.isEmpty() && !esRecordarContraseña){
                mETContraseña.setError(getString(R.string.contraseña_no_introducida));
            }else{
                regexCampos(esRecordarContraseña, esRegistro);
            }

        }

        return respuesta;

    }

    public boolean esMail(){

        return mRegexMail.matcher(mUsuario).matches();

    }

    public boolean esContraseñaSegura(){

        return mRegexContraseña.matcher(mContraseña).matches();

    }

    public void limpiarErroresFormulario(){
        mETUsuario.setError(null);
        mETContraseña.setError(null);
    }

}