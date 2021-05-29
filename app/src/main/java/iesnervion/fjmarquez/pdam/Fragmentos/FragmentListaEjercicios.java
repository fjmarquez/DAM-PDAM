package iesnervion.fjmarquez.pdam.Fragmentos;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import iesnervion.fjmarquez.pdam.Adaptadores.AdaptadorEjercicios;
import iesnervion.fjmarquez.pdam.Adaptadores.AdaptadorSeriesDialogo;
import iesnervion.fjmarquez.pdam.Entidades.Ejercicio;
import iesnervion.fjmarquez.pdam.Entidades.Serie;
import iesnervion.fjmarquez.pdam.R;
import iesnervion.fjmarquez.pdam.Utiles.DificultadEjercicio;
import iesnervion.fjmarquez.pdam.Utiles.GrupoMuscular;
import iesnervion.fjmarquez.pdam.Utiles.Materiales;
import iesnervion.fjmarquez.pdam.Utiles.TipoFragmento;
import iesnervion.fjmarquez.pdam.Utiles.Utiles;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelCreacionRutina;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelUsuario;

/**
 * Fragment donde se mostrar un listado de los ejercicios disponibles, desde este fragment se podra acceder
 * a una vista detallada de cada ejercicio asi como añadir ese ejercicio a la rutina.
 */
public class FragmentListaEjercicios extends Fragment {

    /* ATRIBUTOS */
    private View mFragmentView;
    private View mDialogSeriesView;
    private View mDialogFiltrosView;

    private ViewModelCreacionRutina mViewModelRutina;
    private ViewModelUsuario mViewModelUsuario;

    private MaterialToolbar mToolbar;
    private EditText mETBuscadorEjercicio;
    private AlertDialog mAlertDialogFiltros;

    private ArrayList<Ejercicio> mEjercicios;
    private RecyclerView mRVEjercicios;
    private AdaptadorEjercicios mRVEjerciciosAdaptador;
    private RecyclerView.LayoutManager mLayoutManager;

    private ChipGroup mCGFiltroGrupoMuscular;
    private ChipGroup mCGFiltroDificultad;
    private ChipGroup mCGFiltroMaterial;

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

        mViewModelRutina = new ViewModelProvider(getActivity()).get(ViewModelCreacionRutina.class);
        mViewModelUsuario = new ViewModelProvider(getActivity()).get(ViewModelUsuario.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFragmentView = inflater.inflate(R.layout.fragment_lista_ejercicios, container, false);
        mDialogSeriesView = View.inflate(getContext(), R.layout.dialog_series_layout, null);
        mDialogFiltrosView = View.inflate(getContext(), R.layout.dialog_filtros_ejercicios_layout, null);

        mCGFiltroGrupoMuscular = mDialogFiltrosView.findViewById(R.id.cgFiltroGrupoMuscular);
        mCGFiltroDificultad = mDialogFiltrosView.findViewById(R.id.cgFiltroDificultad);
        mCGFiltroMaterial = mDialogFiltrosView.findViewById(R.id.cgFiltroMaterial);

        mToolbar = mFragmentView.findViewById(R.id.topAppBar);
        mETBuscadorEjercicio = mFragmentView.findViewById(R.id.etBuscarEjercicio);
        accionesMenu();

        mRVSeries = mDialogSeriesView.findViewById(R.id.rvSeries);

        mRVEjercicios = mFragmentView.findViewById(R.id.rvEjercicios);
        mRVEjercicios.setHasFixedSize(true);
        rellenarRecyclerViewEjercicios();

        return mFragmentView;

    }

    /**
     * Rellena los diferentes ChipGroups con Chips de manera dinamica
     */
    public void rellenarChipGroups(){

        //Creo los Chips para los grupos musculares
        for (GrupoMuscular grupo:
             GrupoMuscular.values()) {
            Chip chipGrupoMuscular = new Chip(getContext());
            chipGrupoMuscular.setText(Utiles.capitalizar(grupo.name()));
            chipGrupoMuscular.setCheckable(true);
            chipGrupoMuscular.setCheckedIconTintResource(R.color.color_principal);
            mCGFiltroGrupoMuscular.addView(chipGrupoMuscular);
        }

        //Creo los Chips para las diferentes dificultades
        for(DificultadEjercicio dificultad:
            DificultadEjercicio.values()) {
            Chip chipDificultad = new Chip(getContext());
            chipDificultad.setText(Utiles.capitalizar(dificultad.name()));
            chipDificultad.setCheckable(true);
            chipDificultad.setCheckedIconTintResource(R.color.color_principal);
            mCGFiltroDificultad.addView(chipDificultad);
        }

        //Creo los chip teniendo en cuenta los materiales necesarios en cada ejercicio
        for (Materiales material:
             Materiales.values()) {
            Chip chipMaterial = new Chip(getContext());
            chipMaterial.setText(Utiles.capitalizar(material.name().replace("_", " ")));
            chipMaterial.setCheckable(true);
            chipMaterial.setCheckedIconTintResource(R.color.color_principal);
            mCGFiltroMaterial.addView(chipMaterial);
        }

    }

    /**
     * Define las acciones que realizaran los elementos pertenecientes a la barra de menu superior que encontramos en
     * el listado de Ejercicios.
     */
    public void accionesMenu(){

        mETBuscadorEjercicio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (count == 0 && mViewModelRutina.getListadoEjerciciosMaster().size() > mEjercicios.size()){
                    resetearRecyclerViewEjercicios();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModelUsuario.setmTipoFragmento(TipoFragmento.DIAS_RUTINA);
            }
        });

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){
                    case R.id.buscarEjercicio:
                        String busqueda = mETBuscadorEjercicio.getText().toString();
                        if(!busqueda.isEmpty()){
                            Toast.makeText(getContext(), "buscar", Toast.LENGTH_SHORT).show();
                            ArrayList<Ejercicio> ejercicios = new ArrayList<>(mViewModelRutina.getListadoEjerciciosMaster());
                            for ( Ejercicio ejercicio:
                                    ejercicios) {
                                if (!ejercicio.getNombre().toLowerCase().contains(busqueda.toLowerCase())){
                                    mEjercicios.remove(ejercicio);
                                }
                            }
                            mRVEjerciciosAdaptador.notifyDataSetChanged();
                        }
                        break;
                    case R.id.filtrarEjercicio:
                        Toast.makeText(getContext(), "filtrar", Toast.LENGTH_SHORT).show();
                        mostrarDialogoFiltros();
                        break;
                }
                return false;
            }
        });

    }

    /**
     * Crea y muestra un Dialog personalizado que mostrar una serie de Chips para filtrar los ejercicios
     */
    public void mostrarDialogoFiltros(){

        //Si ya ha sido creado evitamos crearlo otra vez y solamente lo mostramos usando .show()
        if (mAlertDialogFiltros == null){
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
            builder.setTitle(getText(R.string.titulo_dialogo_filtros))
                    .setView(mDialogFiltrosView)
                    .setCancelable(false)
                    .setPositiveButton(getText(R.string.boton_filtrar), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setNeutralButton(getText(R.string.boton_cerrar), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setNegativeButton(getText(R.string.boton_limpiar_filtros), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    });

            rellenarChipGroups();

            mAlertDialogFiltros = builder.create();
            mAlertDialogFiltros.show();

            //Asigno de nuevo la funcion de los botones del dialogo porque asi evito que este se cierre
            // al pulsar los botones y las series no esten bien especificadas
            mAlertDialogFiltros.getButton(AlertDialog.BUTTON_NEUTRAL).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
            mAlertDialogFiltros.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mEjercicios.clear();
                    filtrarPorGrupoMuscular();
                    filtrarPorDificultad();
                    filtrarPorMaterial();
                    mRVEjerciciosAdaptador.notifyDataSetChanged();
                    mAlertDialogFiltros.dismiss();

                }
            });
            mAlertDialogFiltros.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });

        }else {
            mAlertDialogFiltros.show();
        }

        //Esta linea permite desplegar el teclado al poner el foco sobre un EditText dentro del Dialog
        mAlertDialogFiltros.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

    }

    /**
     * Filtra los ejercicios segun el grupo muscular que involucren.
     */
    public void filtrarPorGrupoMuscular(){
        List<Integer> selectedChips = mCGFiltroGrupoMuscular.getCheckedChipIds();
        for(int i = 0; i < selectedChips.size(); i++){
            if(mEjercicios.size() == 0 || selectedChips.size() > 1){

                for (Ejercicio ejercicio:
                        mViewModelRutina.getListadoEjerciciosMaster()) {
                    if(ejercicio.getGrupoMuscular() == (GrupoMuscular.values()[selectedChips.get(i)-1])){
                        mEjercicios.add(ejercicio);
                    }
                }
                int o = 0;

            }else{

                ArrayList<Ejercicio> copiaEjerciciosFiltrados = new ArrayList<>(mEjercicios);
                for (Ejercicio ejercicio:
                        copiaEjerciciosFiltrados) {
                    if(ejercicio.getGrupoMuscular() != (GrupoMuscular.values()[selectedChips.get(i)-1])){
                        mEjercicios.remove(ejercicio);
                    }
                }
                int o = 0;

            }

        }
    }

    /**
     * Filtra los ejercicios segun su dificultad.
     */
    public void filtrarPorDificultad(){
        List<Integer> selectedChips = mCGFiltroDificultad.getCheckedChipIds();
        for(int i = 0; i < selectedChips.size(); i++){
            if(mEjercicios.size() == 0 || selectedChips.size() > 1) {

                for (Ejercicio ejercicio :
                        mViewModelRutina.getListadoEjerciciosMaster()) {
                    if (ejercicio.getDificultad() == (DificultadEjercicio.values()[selectedChips.get(i)-GrupoMuscular.values().length-1])) {
                        mEjercicios.add(ejercicio);
                    }
                }
                int o = 0;
            }else {

                ArrayList<Ejercicio> copiaEjerciciosFiltrados = new ArrayList<>(mEjercicios);
                for (Ejercicio ejercicio :
                        copiaEjerciciosFiltrados) {
                    int x = selectedChips.get(i)-6;
                    //String s = DificultadEjercicio.values()[selectedChips.get(i)-6].name();
                    if (ejercicio.getDificultad() != (DificultadEjercicio.values()[selectedChips.get(i)-GrupoMuscular.values().length-1])) {
                        mEjercicios.remove(ejercicio);
                    }
                }
                int o = 0;
            }
        }
    }

    /**
     * Filtra los ejercicios segun el material necesario para su realizacion
     */
    public void filtrarPorMaterial(){
        List<Integer> selectedChips = mCGFiltroMaterial.getCheckedChipIds();
        for(int i = 0; i < selectedChips.size(); i++){
            //GrupoMuscular.values()[selectedChips.get(i)-1].name();
            for (Ejercicio ejercicio:
                    mViewModelRutina.getListadoEjerciciosMaster()) {
                if(Materiales.values()[selectedChips.get(i)-DificultadEjercicio.values().length-GrupoMuscular.values().length-1] == Materiales.BANDAS_ELASTICAS && ejercicio.getBandasElasticas()){
                    mEjercicios.add(ejercicio);
                }
                if(Materiales.values()[selectedChips.get(i)-DificultadEjercicio.values().length-GrupoMuscular.values().length-1] == Materiales.GIMNASIO && ejercicio.getMaterial()){
                    mEjercicios.add(ejercicio);
                }
                if(Materiales.values()[selectedChips.get(i)-DificultadEjercicio.values().length-GrupoMuscular.values().length-1] == Materiales.SIN_MATERIAL && !ejercicio.getMaterial()){
                    mEjercicios.add(ejercicio);
                }
            }
        }
    }

    /**
     * Rellena un RecyclerView con todos los ejercicios obtenidos desde Firestore.
     */
    public void rellenarRecyclerViewEjercicios(){

        if (mViewModelRutina.getListadoEjerciciosMaster() == null){

            mViewModelRutina.obtenerEjerciciosFirestore().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if(task.isSuccessful()){
                        //ArrayList<Ejercicio> mlistadoEjerciciosFirestore = new ArrayList<>();
                        mEjercicios = new ArrayList<>();
                        for (QueryDocumentSnapshot document: task.getResult()
                        ) {
                            //Map<String, Object> mDocumentMap = document.getData();
                            Ejercicio mEjercicio = document.toObject(Ejercicio.class);
                            mEjercicio.setUid(document.getId());
                            //mlistadoEjerciciosFirestore.add(mEjercicio);
                            mEjercicios.add(mEjercicio);
                        }
                        mViewModelRutina.setListadoEjerciciosMaster(new ArrayList<>(mEjercicios));
                        configurarAdaptadorEjercicios();
                    }
                }
            });

        }else {

            configurarAdaptadorEjercicios();

        }

    }

    /**
     * Realizar las acciones necesarias para comenzar a usar el Adaptador de Ejercicios, ademas establece las acciones que se realizaran
     * para los eventos Click.
     */
    public void configurarAdaptadorEjercicios(){

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRVEjerciciosAdaptador = new AdaptadorEjercicios(mEjercicios);
        mRVEjercicios.setLayoutManager(mLayoutManager);
        mRVEjercicios.setAdapter(mRVEjerciciosAdaptador);

        //Listener para eventos de Click de cada elemento de la lista
        mRVEjerciciosAdaptador.setOnItemClickListener(new AdaptadorEjercicios.OnItemClickListener() {
            @Override
            public void añadirListener(int position) {
                Toast.makeText(getActivity().getApplicationContext(), ""+mViewModelRutina.getListadoEjerciciosMaster().get(position).getNombre(), Toast.LENGTH_SHORT).show();
                if (!mViewModelRutina.getDiasRutina().getValue()
                        .get(mViewModelRutina.getDiaSemanaSeleccionado())
                        .getEjercicios().contains(mViewModelRutina.getListadoEjerciciosMaster().get(position))){
                    mViewModelRutina.setEjercicioSeleccionado(mViewModelRutina.getListadoEjerciciosMaster().get(position));
                    mostrarDialogoSeries();
                }else {
                    Snackbar.make(getView(), "Ya has añadido este ejercicio", Snackbar.LENGTH_LONG).show();
                }
            }

            @Override
            public void clickListener(int position) {
                Toast.makeText(getActivity().getApplicationContext(), ""+mViewModelRutina.getListadoEjerciciosMaster().get(position).getNombre(), Toast.LENGTH_SHORT).show();
                mViewModelRutina.setEjercicioSeleccionado(mViewModelRutina.getListadoEjerciciosMaster().get(position));
                mViewModelUsuario.setmTipoFragmento(TipoFragmento.DETALLE_EJERCICIO);
            }
        });

    }

    /**
     * Devuelve el ArrayList de ejercicios a su estado inicial.
     */
    public void resetearRecyclerViewEjercicios(){

        //Necesito hacer esta iteracion porque una asignacion haria perder a mEjercicios su actual referencia
        mEjercicios.clear();
        for (Ejercicio ejercicio:
                mViewModelRutina.getListadoEjerciciosMaster()) {
            if(!mEjercicios.contains(ejercicio)){
                mEjercicios.add(ejercicio);
            }
        }
        mRVEjerciciosAdaptador.notifyDataSetChanged();

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
                    //asignamos las series especificadas al ejercicios seleccionado
                    mViewModelRutina.getEjercicioSeleccionado().setSeries(AdaptadorSeriesDialogo.listaSeries);
                    //añadirmos el ejercicio seleccionado con sus correspondientes series al dia correspondiente de la rutina
                    mViewModelRutina.getDiasRutina().getValue().get(mViewModelRutina.getDiaSemanaSeleccionado()).getEjercicios().add(mViewModelRutina.getEjercicioSeleccionado());
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

        //Esta linea permite desplegar el teclado al poner el foco sobre un EditText dentro del Dialog
        mAlertDialogSeries.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE|WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);

    }

}