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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.material.chip.Chip;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import iesnervion.fjmarquez.pdam.Adaptadores.AdaptadorSeriesDialogo;
import iesnervion.fjmarquez.pdam.Entidades.Ejercicio;
import iesnervion.fjmarquez.pdam.Entidades.Serie;
import iesnervion.fjmarquez.pdam.R;
import iesnervion.fjmarquez.pdam.Utiles.DificultadEjercicio;
import iesnervion.fjmarquez.pdam.Utiles.TipoFragmento;
import iesnervion.fjmarquez.pdam.Utiles.Utiles;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelEjercicios;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelUsuario;

/**
 * En este fragment se podra visualizar una vista detallada de un ejercicio, ademas desde este fragment tambien
 * se podra añadir dicho ejercicio a la rutina.
 */
public class FragmentDetalleEjercicio extends Fragment implements View.OnClickListener{

    /* ATRIBUTOS */

    private ViewModelEjercicios mViewModelRutina;
    private ViewModelUsuario mViewModelUsuario;

    private View mFragmentView;
    private View mDialogSeriesView;

    private ArrayList<Serie> mSeriesDialog;
    private AlertDialog mAlertDialogSeries;
    private RecyclerView mRVSeries;
    private RecyclerView.LayoutManager mLayoutManager;
    private AdaptadorSeriesDialogo mRVSeriesDialogoAdaptador;
    private boolean dialogSeriesCreado;

    private ImageView mIVGifEjercicioDetalles;
    private TextView mTVNombreEjercicioDetalles;
    private TextView mTVDescripcionEjercicioDetalles;
    private Chip mCHDificultadEjercicioDetalles;
    private Chip mCHMaterialEjercicioDetalles;
    private Chip mCHGrupoMuscularEjercicioDetalles;
    private Button mBtnAñadir;
    private ImageButton mIBRetroceder;

    public FragmentDetalleEjercicio() {

    }

    public static FragmentDetalleEjercicio newInstance() {
        FragmentDetalleEjercicio fragment = new FragmentDetalleEjercicio();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Instancio los ViewModelNecesarios
        mViewModelRutina = new ViewModelProvider(getActivity()).get(ViewModelEjercicios.class);
        mViewModelUsuario = new ViewModelProvider(getActivity()).get(ViewModelUsuario.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_detalle_ejercicio, container, false);
        mDialogSeriesView = View.inflate(getContext(), R.layout.dialog_series_layout, null);

        mIVGifEjercicioDetalles = mFragmentView.findViewById(R.id.ivGifEjercicioDetalles);
        mTVNombreEjercicioDetalles = mFragmentView.findViewById(R.id.tvNombreEjercicioDetalles);
        mTVDescripcionEjercicioDetalles = mFragmentView.findViewById(R.id.tvDescripcionEjercicioDetalles);
        mCHDificultadEjercicioDetalles = mFragmentView.findViewById(R.id.chDificultadDetalles);
        mCHMaterialEjercicioDetalles = mFragmentView.findViewById(R.id.chMaterialesDetalles);
        mCHGrupoMuscularEjercicioDetalles = mFragmentView.findViewById(R.id.chGrupoMuscularDetalles);
        mBtnAñadir = mFragmentView.findViewById(R.id.btnAñadirEjercicioDetalle);
        mBtnAñadir.setOnClickListener(this);
        mIBRetroceder = mFragmentView.findViewById(R.id.ibRetrocederListaEjercicios);
        mIBRetroceder.setOnClickListener(this);

        mRVSeries = mDialogSeriesView.findViewById(R.id.rvSeries);
        mRVSeries.setHasFixedSize(true);
        dialogSeriesCreado = false;

        configuracionVisual();
        rellenarInformacionEjercicio();

        return mFragmentView;
    }

    public void configuracionVisual(){

        if (!mViewModelRutina.isPermitirAñadirEjercicio()){
            mBtnAñadir.setVisibility(View.INVISIBLE);
        }

    }

    /**
     * Rellena la vista en funcion de los datos del ejercicio correspondiente.
     */
    public void rellenarInformacionEjercicio(){

        Ejercicio e = mViewModelRutina.getEjercicioSeleccionado();

        if (e.getDificultad() != null){
            Glide.with(getContext()).asGif()
                    .load(e.getGif())
                    .into(mIVGifEjercicioDetalles);
        }else {
            mIVGifEjercicioDetalles.setImageResource(R.mipmap.custom_ejercicio);
        }


        mTVNombreEjercicioDetalles.setText(e.getNombre());
        mTVDescripcionEjercicioDetalles.setText(e.getDescripcion());
        mCHGrupoMuscularEjercicioDetalles.setText(Utiles.capitalizar(e.getGrupoMuscular().name()));
        mCHDificultadEjercicioDetalles.setText(Utiles.capitalizar(e.getDificultad().name()));

        //Establece el color del Chip encargado de mostrar la dificultad del ejercicio.
        //El color dependera de la dificultad o de si es un ejercicio custom.
        if(e.getDificultad() == DificultadEjercicio.PRINCIPIANTE){
            mCHDificultadEjercicioDetalles.setChipBackgroundColorResource(R.color.principiante_transparente);
        }else if (e.getDificultad() == DificultadEjercicio.INTERMEDIO){
            mCHDificultadEjercicioDetalles.setChipBackgroundColorResource(R.color.color_principal_transparente);
        }else if (e.getDificultad() == DificultadEjercicio.EXPERTO){
            mCHDificultadEjercicioDetalles.setChipBackgroundColorResource(R.color.experto_transparente);
        }else {
            mCHDificultadEjercicioDetalles.setChipBackgroundColorResource(R.color.custom_transparente);
        }

        //Establecera el texto del Chip destinado a mostrar el material necesario para la realizacion del ejercicio.
        if (e.getMaterial()) {
            mCHMaterialEjercicioDetalles.setText(R.string.material_necesario);
        }else if (e.getBandasElasticas()){
            mCHMaterialEjercicioDetalles.setText(R.string.bandas_elasticas_necesarias);
        }else {
            mCHMaterialEjercicioDetalles.setText(R.string.material_no_necesario);
        }

    }

    /**
     * Rellena un RecyclerView con una lista de Series (4 series por defecto con 10 repeticiones cada una,
     * aunque se pueden añadir hasta 6 o eliminar hasta que solo quede 1, tambien se pueden modificar las repeticiones
     * correspondientes a cada serie).
     */
    public void rellenarRecyclerViewSeries(){

        mSeriesDialog = Utiles.seriesPorDefecto();
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRVSeriesDialogoAdaptador = new AdaptadorSeriesDialogo(mSeriesDialog);
        mRVSeries.setLayoutManager(mLayoutManager);
        mRVSeries.setAdapter(mRVSeriesDialogoAdaptador);

    }

    /**
     * Crea y muestra un Dialog personalizado que muestra una lista de Series editables.
     */
    public void mostrarDialogoSeries() {

        //Si ya ha sido creado evitamos crearlo otra vez y solamente lo mostramos usando .show()
        if (mAlertDialogSeries == null|| !dialogSeriesCreado) {

            dialogSeriesCreado = true;
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
            builder.setTitle(getText(R.string.titulo_dialogo_series))
                    .setView(mDialogSeriesView)
                    .setCancelable(true)
                    .setPositiveButton(getText(R.string.boton_añadir), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setNeutralButton(getText(R.string.boton_añadir_ejercicio), new DialogInterface.OnClickListener() {
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

                    if (Utiles.comprobarSeriesDialog()){
                        //asignamos las series especificadas al ejercicios seleccionado
                        mViewModelRutina.getEjercicioSeleccionado().setSeries(AdaptadorSeriesDialogo.listaSeries);
                        //si los ejercicios son menos de 10 en el dia seleccionado se añadira el nuevo ejercicio al dia
                        if (mViewModelRutina.getDiasRutina().getValue().getDias().get(mViewModelRutina.getDiaSemanaSeleccionado()).getEjercicios().size() < 10){
                            //añadirmos el ejercicio seleccionado con sus correspondientes series al dia correspondiente de la rutina
                            mViewModelRutina.getDiasRutina().getValue().getDias().get(mViewModelRutina.getDiaSemanaSeleccionado()).getEjercicios().add(mViewModelRutina.getEjercicioSeleccionado());
                        }else{
                            Snackbar.make(getView(), R.string.max_ejercicios_superado, Snackbar.LENGTH_LONG).show();
                        }
                        mAlertDialogSeries.dismiss();
                    }

                }
            });
            mAlertDialogSeries.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //si las series son menos de 6 se añadira otra nueva serie
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
            rellenarRecyclerViewSeries();
            mAlertDialogSeries.show();
        }

        //Esta linea permite desplegar el teclado al poner el foco sobre un EditText dentro del Dialog
        mAlertDialogSeries.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

    }

    /**
     * Comprueba si un ejercicio ha sido añadido ya a un dia, devolvera un booleano.
     * @return Boolean que en caso de ser negativo indicara que el ejercicio no ha sido añadido a ese dia antes.
     */
    public boolean comprobarEjercicioDia(){

        boolean respuesta = false;

        if (mViewModelRutina.getDiasRutina().getValue().getDias().get(mViewModelRutina.getDiaSemanaSeleccionado()).getEjercicios().size() > 0)
        {
            for (Ejercicio e:
                    mViewModelRutina.getDiasRutina().getValue().getDias().get(mViewModelRutina.getDiaSemanaSeleccionado()).getEjercicios()) {

                String uid1 = mViewModelRutina.getEjercicioSeleccionado().getUid();
                String uid2 = e.getUid();

                if (mViewModelRutina.getEjercicioSeleccionado().getUid().equals(e.getUid()) ){

                    respuesta = true;

                }

            }
        }

        return respuesta;

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btnAñadirEjercicioDetalle:
                if (!comprobarEjercicioDia()){
                    mostrarDialogoSeries();
                }else {
                    Snackbar.make(getView(), getText(R.string.ejercicio_ya_añadido), Snackbar.LENGTH_LONG).show();
                }
                break;
            case R.id.ibRetrocederListaEjercicios:
                //mViewModelUsuario.setmTipoFragmento(TipoFragmento.EJERCICIOS);
                getFragmentManager().popBackStack();
                break;
        }
    }
}