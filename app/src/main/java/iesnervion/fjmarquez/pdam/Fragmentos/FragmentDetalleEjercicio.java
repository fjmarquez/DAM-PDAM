package iesnervion.fjmarquez.pdam.Fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import iesnervion.fjmarquez.pdam.R;

/**
 * En este fragment se podra visualizar una vista detallada de un ejercicio, ademas desde este fragment tambien
 * se podra añadir dicho ejercicio a la rutina.
 */
public class FragmentDetalleEjercicio extends Fragment {

    /* ATRIBUTOS */
    private View mFragmentView;

    public FragmentDetalleEjercicio() {

    }

    public static FragmentDetalleEjercicio newInstance(String param1, String param2) {
        FragmentDetalleEjercicio fragment = new FragmentDetalleEjercicio();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_detalle_ejercicio, container, false);
        return mFragmentView;
    }
}