package iesnervion.fjmarquez.pdam.Fragmentos;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputLayout;
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
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelEjercicios;
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
    private View mDialogEjercicioCustomView;

    private ConstraintLayout mCLExistenCoincidencias;
    private ImageView mIVNoExiste;

    private ViewModelEjercicios mViewModelRutina;
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
    private boolean dialogFiltrosCreado;

    private AlertDialog mAlertDialogEjercicioCustom;
    private TextInputLayout mETNombreEjercicioCustom;
    private TextInputLayout mETDescripcionEjercicioCustom;
    private TextInputLayout mACTVGrupoMuscularEjercicioCustom;
    private String mNombreEjercicioCustom;
    private String mDescripcionEjercicioCustom;
    private GrupoMuscular mGrupoMuscularEjercicioCustom;

    private ArrayList<Serie> mSeriesDialog;
    private AlertDialog mAlertDialogSeries;
    private RecyclerView mRVSeries;
    private AdaptadorSeriesDialogo mRVSeriesDialogoAdaptador;
    private boolean dialogSeriesCreado;

    /* CONSTRUCTOR */

    public FragmentListaEjercicios() {

    }

    public static FragmentListaEjercicios newInstance() {

        FragmentListaEjercicios fragment = new FragmentListaEjercicios();
        return fragment;

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        mViewModelRutina = new ViewModelProvider(getActivity()).get(ViewModelEjercicios.class);
        mViewModelUsuario = new ViewModelProvider(getActivity()).get(ViewModelUsuario.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFragmentView = inflater.inflate(R.layout.fragment_lista_ejercicios, container, false);
        mDialogSeriesView = View.inflate(getContext(), R.layout.dialog_series_layout, null);
        mDialogFiltrosView = View.inflate(getContext(), R.layout.dialog_filtros_ejercicios_layout, null);
        mDialogEjercicioCustomView = View.inflate(getContext(), R.layout.dialog_ejercicio_custom_layout, null);

        mCLExistenCoincidencias = mFragmentView.findViewById(R.id.clNoExistenCoincidencias);
        mIVNoExiste = mFragmentView.findViewById(R.id.ivNoExiste);

        mCGFiltroGrupoMuscular = mDialogFiltrosView.findViewById(R.id.cgFiltroGrupoMuscular);
        mCGFiltroDificultad = mDialogFiltrosView.findViewById(R.id.cgFiltroDificultad);
        mCGFiltroMaterial = mDialogFiltrosView.findViewById(R.id.cgFiltroMaterial);
        dialogFiltrosCreado = false;

        mETNombreEjercicioCustom = mDialogEjercicioCustomView.findViewById(R.id.etNombreEjercicioCustom);
        mETDescripcionEjercicioCustom = mDialogEjercicioCustomView.findViewById(R.id.etDescripcionEjercicioCustom);
        mACTVGrupoMuscularEjercicioCustom = mDialogEjercicioCustomView.findViewById(R.id.acGrupoMuscularEjercicioCustom);

        mToolbar = mFragmentView.findViewById(R.id.mtEjercicios);
        mETBuscadorEjercicio = mFragmentView.findViewById(R.id.etBuscarEjercicio);
        accionesMenu();

        mRVSeries = mDialogSeriesView.findViewById(R.id.rvSeries);
        dialogSeriesCreado = false;

        mRVEjercicios = mFragmentView.findViewById(R.id.rvEjercicios);
        mRVEjercicios.setHasFixedSize(true);
        rellenarRecyclerViewEjercicios();

        return mFragmentView;

    }


    /**
     * Funcion que crea/muestra (dependiendo de si existe o no) un Dialog custom que permite al usuario
     * agreagar a su rutina un ejercicio custom definido por el mismo.
     */
    public void mostrarDialogEjercicioCustom(){

        if(mAlertDialogEjercicioCustom == null) {

            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
            builder.setTitle(getText(R.string.ejercicio_custom_dialog_titulo))
                    .setView(mDialogEjercicioCustomView)
                    .setCancelable(false)
                    .setPositiveButton(getText(R.string.boton_crear_ejercicio_custom), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).setNegativeButton(getText(R.string.boton_cerrar_ejercicio_custom), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            //Configuro un ArrayAdapter necesario para mostrar las posibles opcion de Grupos Musculares
            //en un AutoCompleteTextview
            ArrayAdapter adaptadorACTVGruposMusculares = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, obtenerArrayGruposMusculares());
            //Parseo un EditText (Material) a AutoCompleteTextView y le seteo un ArrayAdapter
            ((AutoCompleteTextView) mACTVGrupoMuscularEjercicioCustom.getEditText()).setAdapter(adaptadorACTVGruposMusculares);

            mAlertDialogEjercicioCustom = builder.create();
            mAlertDialogEjercicioCustom.show();

            //Asigno de nuevo la funcion del boton principal del dialogo porque asi evito que este se cierre
            // al pulsar el boton y no tener elegido ningun checkbox
            mAlertDialogEjercicioCustom.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (comprobarCamposEjercicioCustom()) {
                        recopilarDatosEjercicioCustom();
                        mostrarDialogoSeries();
                        mAlertDialogEjercicioCustom.dismiss();
                    }
                }
            });

            mAlertDialogEjercicioCustom.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAlertDialogEjercicioCustom.dismiss();
                }
            });
        }else {

            mAlertDialogEjercicioCustom.show();

        }

    }

    /**
     * Funcion que setea a null, es decir limpia, los mensajes de error asignados a los diferentes EditText
     * de los que esta formado el Dialog EjercicioCustom.
     */
    public void limpiarErroresCamposEjercicioCustom(){

        mETNombreEjercicioCustom.setError(null);
        mETDescripcionEjercicioCustom.setError(null);
        mACTVGrupoMuscularEjercicioCustom.setError(null);

    }

    /**
     * Funcion que comprueba si los campos se han rellenado de forma correcta y devuelve un valor booleano que sera true
     * en caso positivo y false en caso negativo.
     *
     * @return Booleano que indica si los campos del Dialgo EjercicioCustom se han rellenado de manera correcta.
     */
    public boolean comprobarCamposEjercicioCustom(){

        boolean respuesta = true;

        limpiarErroresCamposEjercicioCustom();

        //Si el nombre del ejercicio tiene una longitud menor a 5 se mostrara un mensaje de error indicandolo
        if (mETNombreEjercicioCustom.getEditText().getText().toString().length() < 5){

            respuesta = false;

            //Si el nombre del ejercicio esta vacio se mostrara un mensaje de error indicandolo
            if(mETNombreEjercicioCustom.getEditText().getText().toString().length() == 0){
                mETNombreEjercicioCustom.setError(getString(R.string.nombre_ejercicio_custom_vacio));
            }else {
                mETNombreEjercicioCustom.setError(getString(R.string.nombre_ejercicio_custom_corto));
            }

        }

        //Si la descripcion del ejercicio esta vacia se mostrara un mensaje de error indicandolo
        if (mETDescripcionEjercicioCustom.getEditText().getText().toString().length() == 0){

            respuesta = false;
            mETDescripcionEjercicioCustom.setError(getString(R.string.descripcion_ejercicio_custom_vacia));

        }

        //Si no se ha seleccionado ningun Grupo Muscular se mostrara un mensaje de error indicandolo
        if (mACTVGrupoMuscularEjercicioCustom.getEditText().getText().toString().length() == 0){

            respuesta = false;
            mACTVGrupoMuscularEjercicioCustom.setError(getString(R.string.grupo_muscular_no_seleccionado_ejercicio_custom));

        }

        return respuesta;

    }

    /**
     * Recopila la informacion del Dialog EjercicioCustom y crea un objeto Ejercicio, el cual se almacena en
     * el ViewModelCreacionRutina para mas adelante indicar las series deseadas.
     */
    public void recopilarDatosEjercicioCustom(){

        mNombreEjercicioCustom = mETNombreEjercicioCustom.getEditText().getText().toString();
        mDescripcionEjercicioCustom = mETDescripcionEjercicioCustom.getEditText().getText().toString();
        String grupoMuscular = mACTVGrupoMuscularEjercicioCustom.getEditText().getText().toString();
        mGrupoMuscularEjercicioCustom = GrupoMuscular.valueOf(grupoMuscular.toUpperCase());

        Ejercicio ejercicioCustom = new Ejercicio();
        ejercicioCustom.setNombre(mNombreEjercicioCustom);
        ejercicioCustom.setDescripcion(mDescripcionEjercicioCustom);
        ejercicioCustom.setGrupoMuscular(mGrupoMuscularEjercicioCustom);
        ejercicioCustom.setDificultad(null);//Si la dificultad se setea a null quiero indica que es un ejercicio personalizado
        mViewModelRutina.setEjercicioSeleccionado(ejercicioCustom);

    }

    /**
     * Funcion que devuelve un ArrayList de String con todos los posibles valores del Enum Grupos Musculares.
     *
     * @return ArrayList de String con los valores del Enum Grupos Musculares
     */
    public ArrayList<String> obtenerArrayGruposMusculares(){

        ArrayList<String> respuesta = new ArrayList<>();

        //Itero los valores del enum
        for (GrupoMuscular grupo:
             GrupoMuscular.values()) {
            respuesta.add(Utiles.capitalizar(grupo.name()));
        }

        return respuesta;

    }

    /**
     * Rellena los diferentes ChipGroups con Chips de manera dinamica
     */
    public void rellenarChipGroups(){

            mViewModelRutina.incrementarmChipsCreados();

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

        //Eventos para el EditText destinado a la busqueda por nombre en la lista de ejercicios
        mETBuscadorEjercicio.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                //Cuando el texto introducido en el EditText se haya modificado y su longuitud sea 0,
                //se reseteara el RecyclerView de ejercicios limpiando asi el filtrado.
                if (s.length() == 0 && mViewModelRutina.getListadoEjerciciosMaster().size() > mViewModelRutina.getListadoEjercicios().size()){
                    //resetearRecyclerViewEjercicios();
                }
            }
        });

        //Evento onClick para el icono de navagacion contenido en el Toolbar
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModelUsuario.setmTipoFragmento(TipoFragmento.DIAS_RUTINA);
            }
        });

        //Eventos onClick para los iconos contenidos en el Toolbar
        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {

            @Override
            public boolean onMenuItemClick(MenuItem item) {

                switch (item.getItemId()){

                    case R.id.buscarEjercicio:
                        String busqueda = mETBuscadorEjercicio.getText().toString();
                        if(!busqueda.isEmpty()){
                            ArrayList<Ejercicio> ejercicios = new ArrayList<>(mViewModelRutina.getListadoEjerciciosMaster());
                            for ( Ejercicio ejercicio:
                                    ejercicios) {
                                if (!ejercicio.getNombre().toLowerCase().contains(busqueda.toLowerCase())){
                                    mViewModelRutina.getListadoEjercicios().remove(ejercicio);
                                }
                            }
                            mRVEjerciciosAdaptador.notifyDataSetChanged();
                        }else {
                            mETBuscadorEjercicio.requestFocus();
                        }
                        break;

                    case R.id.filtrarEjercicio:
                        mostrarDialogoFiltros();
                        break;

                    case R.id.añadirEjercicioCustom:
                        mostrarDialogEjercicioCustom();
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
        if (mAlertDialogFiltros == null|| !dialogFiltrosCreado) {

            dialogFiltrosCreado = true;
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
            builder.setTitle(getText(R.string.titulo_dialogo_filtros))
                    .setView(mDialogFiltrosView)
                    .setCancelable(false)
                    .setPositiveButton(getText(R.string.boton_filtrar), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setNeutralButton(getText(R.string.boton_limpiar_filtros), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .setNegativeButton(getText(R.string.boton_cerrar), new DialogInterface.OnClickListener() {
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
                    resetearRecyclerViewEjercicios();
                    mAlertDialogFiltros.dismiss();
                }
            });
            mAlertDialogFiltros.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Limpio el ArrayList de Ejercicios y aplico los filtros seleccionados por el usuario
                    mViewModelRutina.getListadoEjercicios().clear();
                    filtrarPorGrupoMuscular();
                    filtrarPorDificultad();
                    filtrarPorMaterial();
                    configuracionVisualRecyclerViewEjercicios();
                    mRVEjerciciosAdaptador.notifyDataSetChanged();
                    mAlertDialogFiltros.dismiss();

                }
            });
            mAlertDialogFiltros.getButton(AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAlertDialogFiltros.dismiss();
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
        int tamañoInicioFiltro = mViewModelRutina.getListadoEjercicios().size();
        for(int i = 0; i < selectedChips.size(); i++){

            int posicionChip = selectedChips.get(i)-1-mViewModelRutina.obtenerIdsChipCreados();

            if(/*mViewModelRutina.getListadoEjercicios().size() == 0 || selectedChips.size() > 1*/ tamañoInicioFiltro == 0){
                for (Ejercicio ejercicio:
                        mViewModelRutina.getListadoEjerciciosMaster()) {
                    if(ejercicio.getGrupoMuscular() == (GrupoMuscular.values()[posicionChip])){
                        mViewModelRutina.getListadoEjercicios().add(ejercicio);
                    }
                }

            }else{

                ArrayList<Ejercicio> copiaEjerciciosFiltrados = new ArrayList<>(mViewModelRutina.getListadoEjercicios());
                for (Ejercicio ejercicio:
                        copiaEjerciciosFiltrados) {
                    if(ejercicio.getGrupoMuscular() != (GrupoMuscular.values()[posicionChip])){
                        mViewModelRutina.getListadoEjercicios().remove(ejercicio);
                    }
                }

            }

        }
    }

    /**
     * Filtra los ejercicios segun su dificultad.
     */
    public void filtrarPorDificultad(){
        List<Integer> selectedChips = mCGFiltroDificultad.getCheckedChipIds();
        int tamañoInicioFiltro = mViewModelRutina.getListadoEjercicios().size();
        for(int i = 0; i < selectedChips.size(); i++){

            int posicionChip = selectedChips.get(i)-1-GrupoMuscular.values().length-mViewModelRutina.obtenerIdsChipCreados();

            if(/*mViewModelRutina.getListadoEjercicios().size() == 0*/ tamañoInicioFiltro == 0) {

                for (Ejercicio ejercicio :
                        mViewModelRutina.getListadoEjerciciosMaster()) {
                    if (ejercicio.getDificultad() == (DificultadEjercicio.values()[posicionChip])) {
                        mViewModelRutina.getListadoEjercicios().add(ejercicio);
                    }
                }
            }else {

                ArrayList<Ejercicio> copiaEjerciciosFiltrados = new ArrayList<>(mViewModelRutina.getListadoEjercicios());
                for (Ejercicio ejercicio :
                        copiaEjerciciosFiltrados) {
                    //int x = selectedChips.get(i)-6;
                    //String s = DificultadEjercicio.values()[selectedChips.get(i)-6].name();
                    if (ejercicio.getDificultad() != (DificultadEjercicio.values()[posicionChip])) {
                        mViewModelRutina.getListadoEjercicios().remove(ejercicio);
                    }
                }
            }
        }
    }

    /**
     * Filtra los ejercicios segun el material necesario para su realizacion.
     */
    public void filtrarPorMaterial(){
        List<Integer> selectedChips = mCGFiltroMaterial.getCheckedChipIds();
        int tamañoInicioFiltro = mViewModelRutina.getListadoEjercicios().size();
        for(int i = 0; i < selectedChips.size(); i++){

            int posicionChip = selectedChips.get(i)-1-GrupoMuscular.values().length-DificultadEjercicio.values().length-mViewModelRutina.obtenerIdsChipCreados();

            if (tamañoInicioFiltro == 0){

                for (Ejercicio ejercicio:
                        mViewModelRutina.getListadoEjerciciosMaster()) {
                    if(Materiales.values()[posicionChip] == Materiales.BANDAS_ELASTICAS && ejercicio.getBandasElasticas()){
                        mViewModelRutina.getListadoEjercicios().add(ejercicio);
                    }
                    if(Materiales.values()[posicionChip] == Materiales.GIMNASIO && ejercicio.getMaterial()){
                        mViewModelRutina.getListadoEjercicios().add(ejercicio);
                    }
                    if(Materiales.values()[posicionChip] == Materiales.SIN_MATERIAL && !ejercicio.getMaterial() && !ejercicio.getBandasElasticas()){
                        mViewModelRutina.getListadoEjercicios().add(ejercicio);
                    }
                }

            }else {

                for (Ejercicio ejercicio:
                        mViewModelRutina.getListadoEjerciciosMaster()) {
                    if(Materiales.values()[selectedChips.get(i)-DificultadEjercicio.values().length-GrupoMuscular.values().length-1] != Materiales.BANDAS_ELASTICAS && ejercicio.getBandasElasticas()){
                        mViewModelRutina.getListadoEjercicios().remove(ejercicio);
                    }
                    if(Materiales.values()[selectedChips.get(i)-DificultadEjercicio.values().length-GrupoMuscular.values().length-1] != Materiales.GIMNASIO && ejercicio.getMaterial()){
                        mViewModelRutina.getListadoEjercicios().remove(ejercicio);
                    }
                    if(Materiales.values()[selectedChips.get(i)-DificultadEjercicio.values().length-GrupoMuscular.values().length-1] != Materiales.SIN_MATERIAL && !ejercicio.getMaterial()){
                        mViewModelRutina.getListadoEjercicios().remove(ejercicio);
                    }
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
                        mViewModelRutina.setListadoEjercicios(new ArrayList<>());
                        //Itero los resultados obtenidos en la consulta, los parseo y los añado a la lista
                        for (QueryDocumentSnapshot document: task.getResult()
                        ) {
                            //Parseo el resultado obtenido a un objeto Ejercicio
                            Ejercicio mEjercicio = document.toObject(Ejercicio.class);
                            mEjercicio.setUid(document.getId());
                            mViewModelRutina.getListadoEjercicios().add(mEjercicio);
                        }
                        mViewModelRutina.setListadoEjerciciosMaster(new ArrayList<>(mViewModelRutina.getListadoEjercicios()));
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
        mRVEjerciciosAdaptador = new AdaptadorEjercicios(mViewModelRutina.getListadoEjercicios());
        mRVEjercicios.setLayoutManager(mLayoutManager);
        mRVEjercicios.setAdapter(mRVEjerciciosAdaptador);

        //Listener para eventos de Click de cada elemento de la lista
        mRVEjerciciosAdaptador.setOnItemClickListener(new AdaptadorEjercicios.OnItemClickListener() {
            //Evento onClick destinado a añadir un ejercicio al dia correspondiente de la rutina, aunque antes el usuario debe especificar las series.
            @Override
            public void añadirListener(int position) {
                //Si el ejercicio no ha sido añadido ya a ese dia
                /*if (!mViewModelRutina.getDiasRutina().getValue()
                        .getDias()
                        .get(mViewModelRutina.getDiaSemanaSeleccionado())
                        .getEjercicios().contains(mViewModelRutina.getListadoEjercicios().get(position)))*/
                mViewModelRutina.setEjercicioSeleccionado(mViewModelRutina.getListadoEjercicios().get(position));
                if (!comprobarEjercicioDia()){
                    mostrarDialogoSeries();
                }else {
                    Snackbar.make(getView(), "Ya has añadido este ejercicio", Snackbar.LENGTH_LONG).show();
                }
            }

            //Evento onClick que nos llevara a una vista detallada del ejericicio sobre el que hemos pulsado.
            @Override
            public void clickListener(int position) {
                mViewModelRutina.setEjercicioSeleccionado(mViewModelRutina.getListadoEjercicios().get(position));
                mViewModelUsuario.setmTipoFragmento(TipoFragmento.DETALLE_EJERCICIO);
            }
        });

    }

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

    public void configuracionVisualRecyclerViewEjercicios(){

        if (mViewModelRutina.getListadoEjercicios().size() == 0){

            mRVEjercicios.setVisibility(View.INVISIBLE);
            mCLExistenCoincidencias.setVisibility(View.VISIBLE);
            Animation animacionRotar = AnimationUtils.loadAnimation(getContext(), R.anim.rotar_imageview_360);
            mIVNoExiste.startAnimation(animacionRotar);

        }else {

            mCLExistenCoincidencias.setVisibility(View.GONE);
            mRVEjercicios.setVisibility(View.VISIBLE);

        }


    }

    /**
     * Devuelve el ArrayList de ejercicios a su estado inicial.
     */
    public void resetearRecyclerViewEjercicios(){

        //Necesito hacer esta iteracion porque una asignacion haria perder a mViewModelRutina.getListadoEjercicios() su actual referencia
        mViewModelRutina.getListadoEjercicios().clear();
        for (Ejercicio ejercicio:
                mViewModelRutina.getListadoEjerciciosMaster()) {
            if(!mViewModelRutina.getListadoEjercicios().contains(ejercicio)){
                mViewModelRutina.getListadoEjercicios().add(ejercicio);
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

                    if (comprobarSeriesDialog()){
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

    public boolean comprobarSeriesDialog(){

        boolean respuesta = true;

        for (Serie serie:
                AdaptadorSeriesDialogo.listaSeries){

            if (serie.getRepeticiones() == 0){

                respuesta = false;

            }

        }

        return respuesta;

    }

}