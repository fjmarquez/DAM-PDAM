package iesnervion.fjmarquez.pdam.Fragmentos;

import android.os.Bundle;

import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.google.common.collect.Lists;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import iesnervion.fjmarquez.pdam.Adaptadores.AdaptadorHistorico;
import iesnervion.fjmarquez.pdam.Entidades.Dia;
import iesnervion.fjmarquez.pdam.R;
import iesnervion.fjmarquez.pdam.Utiles.TipoFragmento;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelRutina;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelUsuario;

/**
 * En este Fragment se mostrara un listado con los historicos disponibles, tendra opcion de ver los detalles de un historico
 * y de filtrar por fecha, asi como limpiar el filtro.
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

    /* CONSTRUCTOR */

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

    /**
     * Establece las funcionalidades del menu correspondiente al Fragment.
     */
    public void accionesMenu(){

        //Configuro la funcionalidad del icono de navegacion
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModelUsuario.setmTipoFragmento(TipoFragmento.PANTALLA_INICIO);
            }
        });

        //Configuro la funcionalidad de los MenuItems correspondientes
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

    /**
     * Muestra un DatePicker, el cual permite seleccionar un dia o un rango de dias, para luego filtrar el listado de historicos.
     */
    public void mostrarDatePickerRange(){

        mMaterialDatePicker = MaterialDatePicker.Builder.dateRangePicker().setTitleText(getText(R.string.mensaje_date_picker_range)).build();

        //Evento que se dispara al pulsar el boton "Guardar"
        mMaterialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                //Obtengo las fechas correspondiente al rango de fechas seleccionado en milisegundos
                long millisPrimeraFecha = Long.valueOf(((androidx.core.util.Pair)selection).first.toString());
                long millisSegundaFecha = Long.valueOf(((androidx.core.util.Pair)selection).second.toString());

                //Calculo la diferencia de dias, restando ambas fechas obtenidas y dividiendo entre los milisegundos correspondientes a un dia
                int diferenciaDias = Integer.parseInt(String.valueOf((millisSegundaFecha-millisPrimeraFecha)/DAYms));

                //almacenare aqui las fechas que entren en el rango
                ArrayList<String> dias = new ArrayList<>();

                //Itero dependiendo de los dias de diferencia y obtengo una fecha por cada dia de diferencia
                for (int i = 0; i <= diferenciaDias; i++){
                    Calendar diaIteracion = Calendar.getInstance();
                    diaIteracion.setTimeInMillis(millisPrimeraFecha + (DAYms * i));
                    SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
                    String fecha = format1.format(diaIteracion.getTime());
                    dias.add(fecha);
                }

                //reinicio la lista de historico
                mViewModelRutina.setmListaHistorico(new ArrayList<>());
                int tamañoMax = 10;
                for (int i = 0; i < dias.size(); i += tamañoMax) {
                    int f = Math.min(i + tamañoMax, dias.size());

                    List<String> subListaFechas = dias.subList(i, f);

                    //llamar a firebase aqui
                    obtenerHistoricoRangoFechas(subListaFechas);

                }
            }
        });

        //Muestro el calendario
        mMaterialDatePicker.show(getFragmentManager(), null);

    }

    public void obtenerHistoricoRangoFechas(List<String> dias){

        mViewModelRutina.obtenerHistoricosRangoFechas(dias).addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()){

                    for (DocumentSnapshot document: task.getResult()
                    ) {
                        //parseo los resultados
                        Dia mDia = document.toObject(Dia.class);
                        mViewModelRutina.getmListaHistorico().add(mDia);
                    }

                }
                configurarRecyclerViewHistorico();
            }
        });

    }

    /**
     * Configura y rellena el RecyclerView encargado de listar historicos.
     */
    public void configurarRecyclerViewHistorico(){

        mLayoutManager = new LinearLayoutManager(getContext());
        mRVHistoricoAdaptador = new AdaptadorHistorico(mViewModelRutina.getmListaHistorico());
        mRVHistorico.setLayoutManager(mLayoutManager);
        mRVHistorico.setAdapter(mRVHistoricoAdaptador);
        mRVHistoricoAdaptador.setOnItemClickListener(new AdaptadorHistorico.OnItemClickListener() {
            @Override
            public void mostrarListener(int position) {

            }
        });

        configuracionVisualRecyclerViewHistorico();

    }

    /**
     * Modifica la apariencia visual del RecyclerView encargado de listar historicos.
     */
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

    /**
     * Obtiene un listado de historicos correspondiente al usuario actual de Firestore
     */
    public void obtenerListaHistorico(){

        mViewModelRutina.obtenerListaHistoricoUsuarioActual().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    //reinicio la lista de historicos
                    mViewModelRutina.setmListaHistorico(new ArrayList<>());

                    for (DocumentSnapshot document: task.getResult()
                    ) {
                        //parseo los resultador obtenidos
                        Dia mDia = document.toObject(Dia.class);
                        mViewModelRutina.getmListaHistorico().add(mDia);
                    }

                    configurarRecyclerViewHistorico();

                }else {

                    Snackbar.make(getView(), getText(R.string.mensaje_error_lista_historicos), Snackbar.LENGTH_SHORT).show();

                }

            }

        });

    }

}