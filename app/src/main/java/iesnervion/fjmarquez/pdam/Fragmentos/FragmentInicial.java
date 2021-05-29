package iesnervion.fjmarquez.pdam.Fragmentos;

import android.graphics.Color;
import android.os.Bundle;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;
import iesnervion.fjmarquez.pdam.Adaptadores.AdaptadorEjerciciosDiaRutina;
import iesnervion.fjmarquez.pdam.Entidades.Dia;
import iesnervion.fjmarquez.pdam.Entidades.Ejercicio;
import iesnervion.fjmarquez.pdam.Entidades.Rutina;
import iesnervion.fjmarquez.pdam.R;
import iesnervion.fjmarquez.pdam.Utiles.DiaSemana;
import iesnervion.fjmarquez.pdam.Utiles.Utiles;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelRutina;

/**
 * Fragment principal de la aplicacion.
 */
public class FragmentInicial extends Fragment {

    /* ATRIBUTOS */
    private View mFragmentView;

    private ViewModelRutina mViewModelRutina;
    private AdaptadorEjerciciosDiaRutina mAdaptadorEjerciciosDiaRutina;
    private RecyclerView.LayoutManager mLayoutManager;

    private Calendar startDate;
    private Calendar endDate;
    private boolean esHoy;

    private TextView mTVDiaActual;
    private ConstraintLayout mCLDescanso;
    private ImageView mIVDescanso;
    private HorizontalCalendar mCalendarioHorizontal;
    private RecyclerView mRVEjerciciosDiaRutina;

    public FragmentInicial() {

    }

    public static FragmentInicial newInstance() {
        FragmentInicial fragment = new FragmentInicial();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModelRutina = new ViewModelProvider(getActivity()).get(ViewModelRutina.class);

        startDate = Calendar.getInstance();
        startDate.add(Calendar.WEEK_OF_MONTH, -1);
        endDate = Calendar.getInstance();
        endDate.add(Calendar.WEEK_OF_MONTH, 1);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_inicial, container, false);

        mTVDiaActual = mFragmentView.findViewById(R.id.tvDiaSemana);
        mCLDescanso = mFragmentView.findViewById(R.id.clDescanso);
        mIVDescanso = mFragmentView.findViewById(R.id.ivDescanso);
        mRVEjerciciosDiaRutina = mFragmentView.findViewById(R.id.rvEjerciciosDia);
        mRVEjerciciosDiaRutina.setHasFixedSize(true);

        configurarCalendarioHorizontal();
        actualizarDiaActual(startDate);
        fechaEsHoy(Calendar.getInstance());
        obtenerEjerciciosDia();

        return mFragmentView;
    }

    public void configurarCalendarioHorizontal(){

        mCalendarioHorizontal = new HorizontalCalendar.Builder(mFragmentView, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .configure()
                .formatMiddleText("dd")
                .formatBottomText("EEE")
                .showBottomText(false)
                .sizeMiddleText(26)
                .textColor(Color.LTGRAY, Color.WHITE)
                .end()
                .build();

        mCalendarioHorizontal.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {

                actualizarDiaActual(date);
                fechaEsHoy(date);
                obtenerEjerciciosDia();
                configurarRecyclerViewEjerciciosDia();

            }

        });

    }

    public boolean fechaEsHoy(Calendar fecha){

        boolean respuesta = false;
        Date hoy = Calendar.getInstance().getTime();
        String sHoy = DateFormat.format("dd-MM-yyyy", hoy).toString();
        Date fechaRecibida = fecha.getTime();
        String sFechaRecibida = DateFormat.format("dd-MM-yyyy", fechaRecibida).toString();

        if(sHoy.equals(sFechaRecibida)){
            Toast.makeText(getContext(), "Hoy", Toast.LENGTH_SHORT).show();
            respuesta = true;
        }

        return respuesta;

    }

    public void actualizarDiaActual(Calendar fecha){

        mTVDiaActual.setText(Utiles.capitalizar(DateFormat.format("EEEE", fecha).toString()));

        int diaSemana = fecha.get(Calendar.DAY_OF_WEEK);

        switch (diaSemana){
            case 1:
                mViewModelRutina.setmDiaSemanaSeleccionado(DiaSemana.DOMINGO);
                break;
            case 2:
                mViewModelRutina.setmDiaSemanaSeleccionado(DiaSemana.LUNES);
                break;
            case 3:
                mViewModelRutina.setmDiaSemanaSeleccionado(DiaSemana.MARTES);
                break;
            case 4:
                mViewModelRutina.setmDiaSemanaSeleccionado(DiaSemana.MIERCOLES);
                break;
            case 5:
                mViewModelRutina.setmDiaSemanaSeleccionado(DiaSemana.JUEVES);
                break;
            case 6:
                mViewModelRutina.setmDiaSemanaSeleccionado(DiaSemana.VIERNES);
                break;
            case 7:
                mViewModelRutina.setmDiaSemanaSeleccionado(DiaSemana.SABADO);
                break;

        }

    }

    public void configurarRecyclerViewEjerciciosDia(){

        mLayoutManager = new LinearLayoutManager(getContext());
        mRVEjerciciosDiaRutina.setLayoutManager(mLayoutManager);
        mAdaptadorEjerciciosDiaRutina = new AdaptadorEjerciciosDiaRutina(obtenerListadoEjerciciosDiaActual());
        mRVEjerciciosDiaRutina.setAdapter(mAdaptadorEjerciciosDiaRutina);

        mAdaptadorEjerciciosDiaRutina.setOnItemClickListener(new AdaptadorEjerciciosDiaRutina.OnItemClickListener() {
            @Override
            public void realizarEjercicioListener(int position) {
                Toast.makeText(getContext(), ""+mAdaptadorEjerciciosDiaRutina.listaEjercicios.get(position).getNombre(), Toast.LENGTH_SHORT).show();
            }
        });

        mAdaptadorEjerciciosDiaRutina.notifyDataSetChanged();

    }

    public ArrayList<Ejercicio> obtenerListadoEjerciciosDiaActual(){

        ArrayList<Ejercicio> ejerciciosDia = new ArrayList<>();

        for (Dia dia
                :mViewModelRutina.getRutinaActual().getValue().getDias()){

            if (mViewModelRutina.getmDiaSemanaSeleccionado() == dia.getDia()){
                ejerciciosDia = new ArrayList<>(dia.getEjercicios());
            }

        }

        if (ejerciciosDia.size() == 0) {
            Animation animacionRotar = AnimationUtils.loadAnimation(getContext(), R.anim.rotar_imageview_360);
            mIVDescanso.startAnimation(animacionRotar);
            mRVEjerciciosDiaRutina.setVisibility(View.INVISIBLE);
            mCLDescanso.setVisibility(View.VISIBLE);
        }else{
            mRVEjerciciosDiaRutina.setVisibility(View.VISIBLE);
            mCLDescanso.setVisibility(View.GONE);
        }

        return ejerciciosDia;

    }

    public void obtenerEjerciciosDia(){

        mViewModelRutina.obtenerRutinaActualUsuario().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(Task<DocumentSnapshot> task) {

                if (task.isSuccessful()){

                    DocumentSnapshot documento = task.getResult();

                    if (documento.exists()){
                        mViewModelRutina.setRutinaActual(documento.toObject(Rutina.class));
                        configurarRecyclerViewEjerciciosDia();
                    }

                }else {

                    Snackbar.make(getView(), R.string.no_se_ha_encontrado_rutina, Snackbar.LENGTH_LONG).show();

                }

            }
        });

    }

}