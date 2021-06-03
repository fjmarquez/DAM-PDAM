package iesnervion.fjmarquez.pdam.Fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.ArrayList;

import iesnervion.fjmarquez.pdam.Adaptadores.AdaptadorEjerciciosDiaRutina;
import iesnervion.fjmarquez.pdam.Adaptadores.AdaptadorSeriesRealizarEjercicio;
import iesnervion.fjmarquez.pdam.Entidades.Serie;
import iesnervion.fjmarquez.pdam.R;
import iesnervion.fjmarquez.pdam.Utiles.TipoFragmento;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelRutina;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelUsuario;

public class FragmentRealizarEjercicio extends Fragment implements View.OnClickListener{

    /* ATRIBUTOS */
    private View mFragmentView;

    private ViewModelRutina mViewModelRutina;
    private ViewModelUsuario mViewModelUsuario;

    private RecyclerView mRVSeries;
    private LinearLayoutManager mLayoutManager;
    private AdaptadorSeriesRealizarEjercicio mAdaptadorSeriesRealizarEjercicio;

    private ImageButton mIBCancelarRealizarEjercicio;
    private ImageButton mIBConfirmarRealizarEjercicio;

    public FragmentRealizarEjercicio() {

    }

    public static FragmentRealizarEjercicio newInstance(String param1, String param2) {
        FragmentRealizarEjercicio fragment = new FragmentRealizarEjercicio();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mViewModelRutina = new ViewModelProvider(getActivity()).get(ViewModelRutina.class);
        mViewModelUsuario = new ViewModelProvider(getActivity()).get(ViewModelUsuario.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFragmentView = inflater.inflate(R.layout.fragment_realizar_ejercicio, container, false);

        mIBCancelarRealizarEjercicio = mFragmentView.findViewById(R.id.ibCancelarRealizarEjercicio);
        mIBCancelarRealizarEjercicio.setOnClickListener(this);
        mIBConfirmarRealizarEjercicio = mFragmentView.findViewById(R.id.ibConfirmarRealizarEjercicio);
        mIBConfirmarRealizarEjercicio.setOnClickListener(this);
        mRVSeries = mFragmentView.findViewById(R.id.rvSeriesRealizarEjercicio);
        mRVSeries.setHasFixedSize(true);
        configurarRecyclerViewSeriesRealizarEjercicio();

        return mFragmentView;

    }

    public void configurarRecyclerViewSeriesRealizarEjercicio(){

        mLayoutManager = new LinearLayoutManager(getContext());
        mRVSeries.setLayoutManager(mLayoutManager);
        ArrayList<Serie> series = mViewModelRutina.getmDiaSeleccionado().getEjercicios().get(mViewModelRutina.getmEjercicioRealizar()).getSeries();
        mAdaptadorSeriesRealizarEjercicio = new AdaptadorSeriesRealizarEjercicio(series);
        mRVSeries.setAdapter(mAdaptadorSeriesRealizarEjercicio);

        mAdaptadorSeriesRealizarEjercicio.setOnItemClickListener(new AdaptadorSeriesRealizarEjercicio.OnItemClickListener() {
            @Override
            public void confirmarSerieListener(int position) {
                Serie e = mViewModelRutina.getmDiaSeleccionado().getEjercicios().get(mViewModelRutina.getmEjercicioRealizar()).getSeries().get(position);
                Toast.makeText(getContext(), ""+e.getPeso()+"--"+e.getRepeticiones(), Toast.LENGTH_SHORT).show();
            }

            @Override
            public void editarSerieListener(int position) {

            }
        });

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.ibConfirmarRealizarEjercicio:
                //TODO actualizar antes rutina en firestore
                mViewModelUsuario.setmTipoFragmento(TipoFragmento.PANTALLA_INICIO);
                break;
            case R.id.ibCancelarRealizarEjercicio:
                mViewModelUsuario.setmTipoFragmento(TipoFragmento.PANTALLA_INICIO);
                break;
        }
    }
}