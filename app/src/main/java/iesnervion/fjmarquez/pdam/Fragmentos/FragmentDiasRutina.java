package iesnervion.fjmarquez.pdam.Fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import iesnervion.fjmarquez.pdam.R;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelPrincipal;

public class FragmentDiasRutina extends Fragment {

    /* ATRIBUTOS */
    private View mFragmentView;

    private ViewModelPrincipal mViewModel;

    public FragmentDiasRutina() {

    }

    public static FragmentDiasRutina newInstance(String param1, String param2) {
        FragmentDiasRutina fragment = new FragmentDiasRutina();
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
        mFragmentView = inflater.inflate(R.layout.fragment_dias_rutina, container, false);
        return mFragmentView;
    }
}