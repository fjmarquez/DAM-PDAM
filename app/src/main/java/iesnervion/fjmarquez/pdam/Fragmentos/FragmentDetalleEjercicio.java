package iesnervion.fjmarquez.pdam.Fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;

import iesnervion.fjmarquez.pdam.Entidades.Ejercicio;
import iesnervion.fjmarquez.pdam.R;
import iesnervion.fjmarquez.pdam.Utiles.DificultadEjercicio;
import iesnervion.fjmarquez.pdam.Utiles.TipoFragmento;
import iesnervion.fjmarquez.pdam.Utiles.Utiles;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelEjercicios;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelUsuario;

/**
 * En este fragment se podra visualizar una vista detallada de un ejercicio, ademas desde este fragment tambien
 * se podra añadir dicho ejercicio a la rutina.
 */
public class FragmentDetalleEjercicio extends Fragment implements View.OnClickListener{

    /* ATRIBUTOS */
    private ViewModelEjercicios mViewModelRutina;
    private ViewModelUsuario mViewModelUsuario;

    private View mFragmentView;

    private ImageView mIVGifEjercicioDetalles;
    private TextView mTVNombreEjercicioDetalles;
    private TextView mTVDescripcionEjercicioDetalles;
    private Chip mCHDificultadEjercicioDetalles;
    private Chip mCHMaterialEjercicioDetalles;
    private Chip mCHGrupoMuscularEjercicioDetalles;
    private Button mBtnAñadir;
    private ImageButton mIBRetroceder;

    public FragmentDetalleEjercicio() {

    }

    public static FragmentDetalleEjercicio newInstance(String param1, String param2) {
        FragmentDetalleEjercicio fragment = new FragmentDetalleEjercicio();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModelRutina = new ViewModelProvider(getActivity()).get(ViewModelEjercicios.class);
        mViewModelUsuario = new ViewModelProvider(getActivity()).get(ViewModelUsuario.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_detalle_ejercicio, container, false);

        mIVGifEjercicioDetalles = mFragmentView.findViewById(R.id.ivGifEjercicioDetalles);
        mTVNombreEjercicioDetalles = mFragmentView.findViewById(R.id.tvNombreEjercicioDetalles);
        mTVDescripcionEjercicioDetalles = mFragmentView.findViewById(R.id.tvDescripcionEjercicioDetalles);
        mCHDificultadEjercicioDetalles = mFragmentView.findViewById(R.id.chDificultadDetalles);
        mCHMaterialEjercicioDetalles = mFragmentView.findViewById(R.id.chMaterialesDetalles);
        mCHGrupoMuscularEjercicioDetalles = mFragmentView.findViewById(R.id.chGrupoMuscularDetalles);
        mBtnAñadir = mFragmentView.findViewById(R.id.btnAñadirEjercicioDetalle);
        mBtnAñadir.setOnClickListener(this);
        mIBRetroceder = mFragmentView.findViewById(R.id.ibRetrocederListaEjercicios);
        mIBRetroceder.setOnClickListener(this);

        rellenarInformacionEjercicio();

        return mFragmentView;
    }

    /**
     * Rellena la vista en funcion de los datos del ejercicio correspondiente.
     */
    public void rellenarInformacionEjercicio(){

        Ejercicio e = mViewModelRutina.getEjercicioSeleccionado();

        Glide.with(getContext()).asGif()
                .load(Utiles.urlDrive(e.getGif()))
                .into(mIVGifEjercicioDetalles);
        mTVNombreEjercicioDetalles.setText(e.getNombre());
        mTVDescripcionEjercicioDetalles.setText(e.getDescripcion());
        mCHGrupoMuscularEjercicioDetalles.setText(Utiles.capitalizar(e.getGrupoMuscular().name()));
        mCHDificultadEjercicioDetalles.setText(Utiles.capitalizar(e.getDificultad().name()));
        if(e.getDificultad() == DificultadEjercicio.PRINCIPIANTE){
            mCHDificultadEjercicioDetalles.setChipBackgroundColorResource(R.color.principiante_transparente);
        }else if (e.getDificultad() == DificultadEjercicio.INTERMEDIO){
            mCHDificultadEjercicioDetalles.setChipBackgroundColorResource(R.color.color_principal_transparente);
        }else {
            mCHDificultadEjercicioDetalles.setChipBackgroundColorResource(R.color.experto_transparente);
        }
        if (e.getMaterial()) {
            mCHMaterialEjercicioDetalles.setText(R.string.material_necesario);
        }else if (e.getBandasElasticas()){
            mCHMaterialEjercicioDetalles.setText(R.string.bandas_elasticas_necesarias);
        }else {
            mCHMaterialEjercicioDetalles.setText(R.string.material_no_necesario);
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAñadirEjercicioDetalle:
                Toast.makeText(getContext(), "click", Toast.LENGTH_SHORT).show();
                break;
            case R.id.ibRetrocederListaEjercicios:
                mViewModelUsuario.setmTipoFragmento(TipoFragmento.EJERCICIOS);
                break;
        }
    }
}