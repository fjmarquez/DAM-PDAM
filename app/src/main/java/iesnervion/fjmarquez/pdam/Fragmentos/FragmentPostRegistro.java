package iesnervion.fjmarquez.pdam.Fragmentos;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;

import iesnervion.fjmarquez.pdam.Entidades.Usuario;
import iesnervion.fjmarquez.pdam.R;
import iesnervion.fjmarquez.pdam.Utiles.TipoFragmento;
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
    private CheckBox mCBLunes;
    private CheckBox mCBMartes;
    private CheckBox mCBMiercoles;
    private CheckBox mCBJueves;
    private CheckBox mCBViernes;
    private CheckBox mCBSabado;
    private CheckBox mCBDomingo;

    private View mDialogDiasView;
    private View mFragmentView;

    private ViewModelPrincipal mViewModel;

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

        mViewModel = new ViewModelProvider(getActivity()).get(ViewModelPrincipal.class);

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

        mDialogDiasView = View.inflate(getContext(), R.layout.dialog_dias_layout, null);

        mCBLunes = mDialogDiasView.findViewById(R.id.cbLunes);
        mCBMartes = mDialogDiasView.findViewById(R.id.cbMartes);
        mCBMiercoles = mDialogDiasView.findViewById(R.id.cbMiercoles);
        mCBJueves = mDialogDiasView.findViewById(R.id.cbJueves);
        mCBViernes = mDialogDiasView.findViewById(R.id.cbViernes);
        mCBSabado = mDialogDiasView.findViewById(R.id.cbSabado);
        mCBDomingo = mDialogDiasView.findViewById(R.id.cbDomigo);

        return mFragmentView;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.BTNEnviarDatosUsuario:
                if(comprobarCampos()) {

                    mostrarDialogDias();
                }
                break;
        }
    }

    public void mostrarDialogDias(){


        MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
        builder.setTitle(getText(R.string.titulo_dialog_dias_semana))
                .setView(mDialogDiasView)
                .setCancelable(false)
                .setPositiveButton(getText(R.string.boton_positivo_dialog), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();

        alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                if(recopilarCheckBoxesDias()){
                    recopilarDatosUsuario();
                    alertDialog.cancel();
                    mViewModel.setmTipoFragmento(TipoFragmento.DIAS_RUTINA);
                }
                else {
                    Snackbar.make(getView(), R.string.error_dias, Snackbar.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void recopilarDatosUsuario(){

        mNombre = mETNombre.getEditText().getText().toString();
        mApellidos = mETApellidos.getEditText().getText().toString();
        mEdad = Integer.parseInt(mETEdad.getEditText().getText().toString());
        mAltura = Integer.parseInt(mETAltura.getEditText().getText().toString());
        mPeso = Double.parseDouble(mETPeso.getEditText().getText().toString());

        mViewModel.setmUsuario(new Usuario(mViewModel.usuarioActual(), mNombre, mApellidos, mAltura, mPeso, mEdad));

    }

    public boolean recopilarCheckBoxesDias(){

        boolean respuesta = false;

        if(mCBLunes.isChecked()){
            respuesta = true;
            Toast.makeText(getContext(), "Lunes", Toast.LENGTH_SHORT).show();
        }
        if(mCBMartes.isChecked()){
            respuesta = true;
            Toast.makeText(getContext(), "Martes", Toast.LENGTH_SHORT).show();
        }
        if(mCBMiercoles.isChecked()){
            respuesta = true;
            Toast.makeText(getContext(), "Miercoles", Toast.LENGTH_SHORT).show();
        }
        if(mCBJueves.isChecked()){
            respuesta = true;
            Toast.makeText(getContext(), "Jueves", Toast.LENGTH_SHORT).show();
        }
        if(mCBViernes.isChecked()){
            respuesta = true;
            Toast.makeText(getContext(), "Viernes", Toast.LENGTH_SHORT).show();
        }
        if(mCBSabado.isChecked()){
            respuesta = true;
            Toast.makeText(getContext(), "Sabado", Toast.LENGTH_SHORT).show();
        }
        if(mCBDomingo.isChecked()){
            respuesta = true;
            Toast.makeText(getContext(), "Domingo", Toast.LENGTH_SHORT).show();
        }

        return respuesta;

    }

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

    public void limpiarErrores(){
        mETNombre.setError(null);
        mETApellidos.setError(null);
        mETPeso.setError(null);
        mETEdad.setError(null);
        mETAltura.setError(null);
    }



}