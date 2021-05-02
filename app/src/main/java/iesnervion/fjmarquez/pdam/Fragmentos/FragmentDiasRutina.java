package iesnervion.fjmarquez.pdam.Fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.snackbar.Snackbar;

import iesnervion.fjmarquez.pdam.Adaptadores.AdaptadorDias;
import iesnervion.fjmarquez.pdam.R;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelLogin;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelRutina;

public class FragmentDiasRutina extends Fragment {

    /* ATRIBUTOS */
    private View mFragmentView;
    private RecyclerView mRVDiasSeleccionados;
    private AdaptadorDias mAdaptadorDias;
    private RecyclerView.LayoutManager mLayoutManager;

    private ViewModelRutina mViewModel;

    public FragmentDiasRutina() {

    }

    public static FragmentDiasRutina newInstance() {

        FragmentDiasRutina fragment = new FragmentDiasRutina();
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mViewModel = new ViewModelProvider(getActivity()).get(ViewModelRutina.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFragmentView = inflater.inflate(R.layout.fragment_dias_rutina, container, false);

        mRVDiasSeleccionados = mFragmentView.findViewById(R.id.rvDias);
        mRVDiasSeleccionados.setHasFixedSize(true);
        mLayoutManager =  new LinearLayoutManager(getActivity());
        mAdaptadorDias = new AdaptadorDias(mViewModel.getDiasRutina().getValue());
        mRVDiasSeleccionados.setLayoutManager(mLayoutManager);
        mRVDiasSeleccionados.setAdapter(mAdaptadorDias);

        mAdaptadorDias.setOnItemClickListener(new AdaptadorDias.OnItemClickListener() {
            @Override
            public void añadirListener(int position) {
                Snackbar.make(getView(), "añadir " + mViewModel.getDiasRutina().getValue().get(position).getDia(), Snackbar.LENGTH_SHORT).show();
                // TODO: 02/05/2021 Navega hacia el fragment donde este la lista de ejercicios 
            }

            @Override
            public void mostrarListener(int position) {
                Snackbar.make(getView(), "mostrar " + mViewModel.getDiasRutina().getValue().get(position).getDia(), Snackbar.LENGTH_SHORT).show();
                // TODO: 02/05/2021 Hace visible la sublista con los ejercicios correspondientes a cada dia
            }
        });
        
        return mFragmentView;

    }
}