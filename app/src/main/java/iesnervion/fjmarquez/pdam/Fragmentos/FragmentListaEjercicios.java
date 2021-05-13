package iesnervion.fjmarquez.pdam.Fragmentos;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import iesnervion.fjmarquez.pdam.Adaptadores.AdaptadorEjercicios;
import iesnervion.fjmarquez.pdam.Adaptadores.AdaptadorSeriesDialogo;
import iesnervion.fjmarquez.pdam.Entidades.Ejercicio;
import iesnervion.fjmarquez.pdam.Entidades.Serie;
import iesnervion.fjmarquez.pdam.R;
import iesnervion.fjmarquez.pdam.Utiles.Utiles;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelRutina;

public class FragmentListaEjercicios extends Fragment {

    private View mFragmentView;
    private View mDialogSeriesView;
    private ViewModelRutina mViewModelRutina;

    private RecyclerView mRVEjercicios;
    private AdaptadorEjercicios mRVEjerciciosAdaptador;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<Serie> mSeriesDialog;
    private AlertDialog mAlertDialogSeries;
    private RecyclerView mRVSeries;
    private AdaptadorSeriesDialogo mRVSeriesDialogoAdaptador;

    public FragmentListaEjercicios() {

    }

    public static FragmentListaEjercicios newInstance() {

        FragmentListaEjercicios fragment = new FragmentListaEjercicios();
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mViewModelRutina = new ViewModelProvider(getActivity()).get(ViewModelRutina.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFragmentView = inflater.inflate(R.layout.fragment_lista_ejercicios, container, false);
        mDialogSeriesView = View.inflate(getContext(), R.layout.dialog_series_layout, null);

        mRVSeries = mDialogSeriesView.findViewById(R.id.rvSeries);

        mRVEjercicios = mFragmentView.findViewById(R.id.rvEjercicios);
        mRVEjercicios.setHasFixedSize(true);
        rellenarRecyclerViewEjercicios();

        return mFragmentView;

    }

    public void rellenarRecyclerViewEjercicios(){

        mViewModelRutina.obtenerEjerciciosFirestore().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    ArrayList<Ejercicio> mlistadoEjerciciosFirestore = new ArrayList<>();
                    for (QueryDocumentSnapshot document: task.getResult()
                    ) {
                        //Map<String, Object> mDocumentMap = document.getData();
                        Ejercicio mEjercicio = document.toObject(Ejercicio.class);
                        mEjercicio.setUid(document.getId());
                        mlistadoEjerciciosFirestore.add(mEjercicio);
                    }
                    mViewModelRutina.setListadoEjercicios(mlistadoEjerciciosFirestore);
                    mLayoutManager = new LinearLayoutManager(getActivity());
                    mRVEjerciciosAdaptador = new AdaptadorEjercicios(mViewModelRutina.getListadoEjercicios());
                    mRVEjercicios.setLayoutManager(mLayoutManager);
                    mRVEjercicios.setAdapter(mRVEjerciciosAdaptador);

                    mRVEjerciciosAdaptador.setOnItemClickListener(new AdaptadorEjercicios.OnItemClickListener() {
                        @Override
                        public void añadirListener(int position) {
                            Toast.makeText(getActivity().getApplicationContext(), ""+mViewModelRutina.getListadoEjercicios().get(position).getNombre(), Toast.LENGTH_SHORT).show();
                            /*mViewModelRutina.getDiasRutina().getValue()
                                    .get(mViewModelRutina.getDiaSemanaSeleccionado())
                                    .getEjercicios().add(mViewModelRutina.getListadoEjercicios()
                                    .get(position));*/
                            if (!mViewModelRutina.getDiasRutina().getValue()
                                    .get(mViewModelRutina.getDiaSemanaSeleccionado())
                                    .getEjercicios().contains(mViewModelRutina.getListadoEjercicios().get(position))){
                                mostrarDialogoSeries(mViewModelRutina.getListadoEjercicios().get(position));
                            }else {
                                Snackbar.make(getView(), "Ya has añadido este ejercicio", Snackbar.LENGTH_LONG).show();
                            }

                        }
                    });

                }

            }
        });

    }

    public void rellenarRecyclerViewSeries(){

            mSeriesDialog = Utiles.seriesPorDefecto();
            mLayoutManager = new LinearLayoutManager(getActivity());
            mRVSeriesDialogoAdaptador = new AdaptadorSeriesDialogo(mSeriesDialog);
            mRVSeries.setLayoutManager(mLayoutManager);
            mRVSeries.setAdapter(mRVSeriesDialogoAdaptador);

    }

    public void mostrarDialogoSeries(Ejercicio ejercicioSeries) {

        if (mAlertDialogSeries == null){
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
            builder.setTitle(getText(R.string.titulo_dialogo_series))
                    .setView(mDialogSeriesView)
                    .setCancelable(false)
                    .setPositiveButton(getText(R.string.boton_añadir), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setNeutralButton(getText(R.string.boton_positivo_dialog), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setNegativeButton(getText(R.string.boton_quitar), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

            mAlertDialogSeries = builder.create();
            mAlertDialogSeries.show();

            rellenarRecyclerViewSeries();

            //Asigno de nuevo la funcion de los botones del dialogo porque asi evito que este se cierre
            // al pulsar los botones y las series no esten bien especificadas
            mAlertDialogSeries.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewModelRutina.getDiasRutina().getValue().get(mViewModelRutina.getDiaSemanaSeleccionado()).getEjercicios().add(ejercicioSeries);
                    mAlertDialogSeries.dismiss();
                }
            });
            mAlertDialogSeries.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSeriesDialog.size() < 6) {
                        mSeriesDialog.add(new Serie());
                        mRVSeriesDialogoAdaptador.notifyDataSetChanged();
                    }
                }
            });
            mAlertDialogSeries.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mSeriesDialog.size() != 1){
                        mSeriesDialog.remove(mSeriesDialog.size()-1);
                        mRVSeriesDialogoAdaptador.notifyDataSetChanged();
                    }
                }
            });

        }else {
            mAlertDialogSeries.show();
        }

        mAlertDialogSeries.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

    }

}