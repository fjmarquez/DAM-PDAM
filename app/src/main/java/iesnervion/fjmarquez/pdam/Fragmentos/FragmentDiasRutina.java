package iesnervion.fjmarquez.pdam.Fragmentos;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Toast;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import iesnervion.fjmarquez.pdam.Adaptadores.AdaptadorDias;
import iesnervion.fjmarquez.pdam.Entidades.Dia;
import iesnervion.fjmarquez.pdam.R;
import iesnervion.fjmarquez.pdam.Utiles.DiaSemana;
import iesnervion.fjmarquez.pdam.Utiles.TipoFragmento;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelLogin;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelRutina;

public class FragmentDiasRutina extends Fragment {

    /* ATRIBUTOS */
    private View mFragmentView;
    private RecyclerView mRVDiasSeleccionados;
    private AdaptadorDias mAdaptadorDias;
    private RecyclerView.LayoutManager mLayoutManager;

    private CheckBox mCBLunes;
    private CheckBox mCBMartes;
    private CheckBox mCBMiercoles;
    private CheckBox mCBJueves;
    private CheckBox mCBViernes;
    private CheckBox mCBSabado;
    private CheckBox mCBDomingo;

    private View mDialogDiasView;

    private ViewModelRutina mViewModelRutina;
    private ViewModelLogin mViewModelLogin;

    public FragmentDiasRutina() {

    }

    public static FragmentDiasRutina newInstance() {

        FragmentDiasRutina fragment = new FragmentDiasRutina();
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        mViewModelRutina = new ViewModelProvider(getActivity()).get(ViewModelRutina.class);
        mViewModelLogin = new ViewModelProvider(getActivity()).get(ViewModelLogin.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFragmentView = inflater.inflate(R.layout.fragment_dias_rutina, container, false);
        mDialogDiasView = View.inflate(getContext(), R.layout.dialog_dias_layout, null);

        mCBLunes = mDialogDiasView.findViewById(R.id.cbLunes);
        mCBMartes = mDialogDiasView.findViewById(R.id.cbMartes);
        mCBMiercoles = mDialogDiasView.findViewById(R.id.cbMiercoles);
        mCBJueves = mDialogDiasView.findViewById(R.id.cbJueves);
        mCBViernes = mDialogDiasView.findViewById(R.id.cbViernes);
        mCBSabado = mDialogDiasView.findViewById(R.id.cbSabado);
        mCBDomingo = mDialogDiasView.findViewById(R.id.cbDomigo);

        mRVDiasSeleccionados = mFragmentView.findViewById(R.id.rvDias);
        mRVDiasSeleccionados.setHasFixedSize(true);

        if (mViewModelRutina.getDiasRutina().getValue() == null || mViewModelRutina.getDiasRutina().getValue().size() == 0) {
            mostrarDialogDias();
        } else {
            rellenarRecyclerViewDias();
        }

        return mFragmentView;

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

            //Asigno de nuevo la funcion del boton principal del dialogo porque asi evito que este se cierre
            // al pulsar el boton y no tener elegido ningun checkbox
            alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewModelRutina.getDiasRutina().setValue(new ArrayList<>());
                    if (recopilarCheckBoxesDias()) {
                        rellenarRecyclerViewDias();
                        alertDialog.cancel();
                    } else {
                        Snackbar.make(getView(), R.string.error_dias, Snackbar.LENGTH_SHORT).show();
                    }

                }
            });

    }

    public void rellenarRecyclerViewDias(){

        mLayoutManager =  new LinearLayoutManager(getActivity());
        mAdaptadorDias = new AdaptadorDias(mViewModelRutina.getDiasRutina().getValue());
        mRVDiasSeleccionados.setLayoutManager(mLayoutManager);
        mRVDiasSeleccionados.setAdapter(mAdaptadorDias);

        mAdaptadorDias.setOnItemClickListener(new AdaptadorDias.OnItemClickListener() {
            @Override
            public void añadirListener(int position) {
                Snackbar.make(getView(), "añadir " + mViewModelRutina.getDiasRutina().getValue().get(position).getDia(), Snackbar.LENGTH_SHORT).show();
                mViewModelLogin.setmTipoFragmento(TipoFragmento.EJERCICIOS);
                // TODO: 02/05/2021 Navega hacia el fragment donde este la lista de ejercicios
            }

            @Override
            public void mostrarListener(int position) {
                Snackbar.make(getView(), "mostrar " + mViewModelRutina.getDiasRutina().getValue().get(position).getDia(), Snackbar.LENGTH_SHORT).show();
                // TODO: 02/05/2021 Hace visible la sublista con los ejercicios correspondientes a cada dia
            }
        });

    }

    public boolean recopilarCheckBoxesDias(){

        boolean respuesta = false;

        if(mCBLunes.isChecked()){
            mViewModelRutina.getDiasRutina().getValue().add(new Dia(DiaSemana.LUNES, new ArrayList<>()));
            Toast.makeText(getContext(), "Lunes", Toast.LENGTH_SHORT).show();
        }
        if(mCBMartes.isChecked()){
            mViewModelRutina.getDiasRutina().getValue().add(new Dia(DiaSemana.MARTES, new ArrayList<>()));
            Toast.makeText(getContext(), "Martes", Toast.LENGTH_SHORT).show();
        }
        if(mCBMiercoles.isChecked()){
            mViewModelRutina.getDiasRutina().getValue().add(new Dia(DiaSemana.MIERCOLES, new ArrayList<>()));
            Toast.makeText(getContext(), "Miercoles", Toast.LENGTH_SHORT).show();
        }
        if(mCBJueves.isChecked()){
            mViewModelRutina.getDiasRutina().getValue().add(new Dia(DiaSemana.JUEVES, new ArrayList<>()));
            Toast.makeText(getContext(), "Jueves", Toast.LENGTH_SHORT).show();
        }
        if(mCBViernes.isChecked()){
            mViewModelRutina.getDiasRutina().getValue().add(new Dia(DiaSemana.VIERNES, new ArrayList<>()));
            Toast.makeText(getContext(), "Viernes", Toast.LENGTH_SHORT).show();
        }
        if(mCBSabado.isChecked()){
            mViewModelRutina.getDiasRutina().getValue().add(new Dia(DiaSemana.SABADO, new ArrayList<>()));
            Toast.makeText(getContext(), "Sabado", Toast.LENGTH_SHORT).show();
        }
        if(mCBDomingo.isChecked()){
            mViewModelRutina.getDiasRutina().getValue().add(new Dia(DiaSemana.DOMINGO, new ArrayList<>()));
            Toast.makeText(getContext(), "Domingo", Toast.LENGTH_SHORT).show();
        }

        if(mViewModelRutina.getDiasRutina().getValue().size() > 0){
            respuesta = true;
        }

        return respuesta;

    }


}