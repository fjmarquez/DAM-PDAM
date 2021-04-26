package iesnervion.fjmarquez.pdam.Fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import iesnervion.fjmarquez.pdam.R;

/**
 * Fragment principal de la aplicacion.
 */
public class FragmentInicial extends Fragment {

    public FragmentInicial() {

    }

    public static FragmentInicial newInstance() {
        FragmentInicial fragment = new FragmentInicial();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_inicial, container, false);

        return v;
    }
}