package iesnervion.fjmarquez.pdam.Fragmentos;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import iesnervion.fjmarquez.pdam.R;

public class FragmentListaEjercicios extends Fragment {

    private View mFragmentView;

    public FragmentListaEjercicios() {

    }

    public static FragmentListaEjercicios newInstance() {

        FragmentListaEjercicios fragment = new FragmentListaEjercicios();
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFragmentView = inflater.inflate(R.layout.fragment_lista_ejercicios, container, false);

        return mFragmentView;

    }
}