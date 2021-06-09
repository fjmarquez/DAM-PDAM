package iesnervion.fjmarquez.pdam.Fragmentos;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Pair;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import iesnervion.fjmarquez.pdam.Adaptadores.AdaptadorHistorico;
import iesnervion.fjmarquez.pdam.Entidades.Dia;
import iesnervion.fjmarquez.pdam.R;
import iesnervion.fjmarquez.pdam.Utiles.TipoFragmento;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelRutina;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelUsuario;

import static java.time.temporal.ChronoUnit.DAYS;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link FragmentListaHistorico#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FragmentListaHistorico extends Fragment {

    /* ATRIBUTOS */
    private View mFragmentView;

    private ViewModelUsuario mViewModelUsuario;
    private ViewModelRutina mViewModelRutina;

    private RecyclerView mRVHistorico;
    private LinearLayoutManager mLayoutManager;
    private AdaptadorHistorico mRVHistoricoAdaptador;
    private ConstraintLayout mCLNoExistenCoincidencias;
    private ImageView mIVNoExiste;

    private MaterialDatePicker mMaterialDatePicker;
    private final int DAYms = 86400000;

    private MaterialToolbar mToolbar;

    public FragmentListaHistorico() {

    }

    public static FragmentListaHistorico newInstance() {
        FragmentListaHistorico fragment = new FragmentListaHistorico();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModelUsuario = new ViewModelProvider(getActivity()).get(ViewModelUsuario.class);
        mViewModelRutina = new ViewModelProvider(getActivity()).get(ViewModelRutina.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFragmentView = inflater.inflate(R.layout.fragment_historico, container, false);

        mToolbar = mFragmentView.findViewById(R.id.mtHistorico);
        mCLNoExistenCoincidencias = mFragmentView.findViewById(R.id.clNoExistenCoincidenciasHistorico);
        mIVNoExiste = mFragmentView.findViewById(R.id.ivNoExisteHistorico);
        mRVHistorico = mFragmentView.findViewById(R.id.rvHistorico);
        mRVHistorico.setHasFixedSize(true);

        accionesMenu();
        obtenerListaHistorico();

        return mFragmentView;

    }

    public void accionesMenu(){

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModelUsuario.setmTipoFragmento(TipoFragmento.PANTALLA_INICIO);
            }
        });

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){
                    case R.id.filtrarHistorico:
                        mostrarDatePickerRange();
                        break;
                    case R.id.limpiarFiltroHistorico:
                        obtenerListaHistorico();
                        break;
                }

                return false;
            }
        });

    }

    public void mostrarDatePickerRange(){

        mMaterialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setTitleText("Selecciona el intervalo de fechas").build();

        //capturar fecha/rango seleccionado
        mMaterialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                long millisPrimeraFecha = Long.valueOf(((androidx.core.util.Pair)selection).first.toString());
                long millisSegundaFecha = Long.valueOf(((androidx.core.util.Pair)selection).second.toString());

                int diferenciaDias = Integer.parseInt(String.valueOf((millisSegundaFecha-millisPrimeraFecha)/DAYms));

                ArrayList<String> dias = new ArrayList<>();

                for (int i = 0; i <= diferenciaDias; i++){
                    Calendar diaIteracion = Calendar.getInstance();
                    diaIteracion.setTimeInMillis(millisPrimeraFecha + (DAYms * i));
                    SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
                    String fecha = format1.format(diaIteracion.getTime());
                    dias.add(fecha);
                }

                mViewModelRutina.obtenerHistoricosRangoFechas(dias).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(Task<QuerySnapshot> task) {
                        if (task.isSuccessful()){
                            mViewModelRutina.setmListaHistorico(new ArrayList<>());

                            for (DocumentSnapshot document: task.getResult()
                            ) {
                                Dia mDia = document.toObject(Dia.class);
                                mViewModelRutina.getmListaHistorico().add(mDia);
                            }
                        }
                        configurarRecyclerViewHistorico();
                    }
                });

            }
        });

        mMaterialDatePicker.show(getFragmentManager(), null);

    }

    public void configurarRecyclerViewHistorico(){

        //if (mLayoutManager == null && mRVHistoricoAdaptador == null){
            mLayoutManager = new LinearLayoutManager(getContext());
            mRVHistoricoAdaptador = new AdaptadorHistorico(mViewModelRutina.getmListaHistorico());
            mRVHistorico.setLayoutManager(mLayoutManager);
            mRVHistorico.setAdapter(mRVHistoricoAdaptador);
        //}else {
        //    mRVHistoricoAdaptador.notifyDataSetChanged();
        //}

        configuracionVisualRecyclerViewHistorico();

    }

    public void configuracionVisualRecyclerViewHistorico(){

        if (mViewModelRutina.getmListaHistorico().size() == 0){

            mRVHistorico.setVisibility(View.GONE);
            mCLNoExistenCoincidencias.setVisibility(View.VISIBLE);
            Animation animacionRotar = AnimationUtils.loadAnimation(getContext(), R.anim.rotar_imageview_360);
            mIVNoExiste.startAnimation(animacionRotar);

        }else {

            mRVHistorico.setVisibility(View.VISIBLE);
            mCLNoExistenCoincidencias.setVisibility(View.GONE);

        }

    }

    public void obtenerListaHistorico(){

        mViewModelRutina.obtenerListaHistoricoUsuarioActual().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    mViewModelRutina.setmListaHistorico(new ArrayList<>());

                    for (DocumentSnapshot document: task.getResult()
                    ) {
                        Dia mDia = document.toObject(Dia.class);
                        mViewModelRutina.getmListaHistorico().add(mDia);
                        configurarRecyclerViewHistorico();
                    }
                }else {

                    Snackbar.make(getView(), getText(R.string.mensaje_error_lista_historicos), Snackbar.LENGTH_SHORT).show();

                }

            }

        });

    }

}