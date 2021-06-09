package iesnervion.fjmarquez.pdam.Fragmentos;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import iesnervion.fjmarquez.pdam.Adaptadores.AdaptadorListaRutinas;
import iesnervion.fjmarquez.pdam.Entidades.Rutina;
import iesnervion.fjmarquez.pdam.R;
import iesnervion.fjmarquez.pdam.Utiles.TipoFragmento;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelEjercicios;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelRutina;
import iesnervion.fjmarquez.pdam.ViewModels.ViewModelUsuario;

public class FragmentListaRutinas extends Fragment {

    /* ATRIBUTOS */
    private View mFragmentView;

    private ViewModelRutina mViewModelRutina;
    private ViewModelUsuario mViewModelUsuario;
    private ViewModelEjercicios mViewModelEjercicios;

    private AlertDialog mAlertDialogEliminarRutina;
    private boolean dialogEliminarRutinaCreado;

    private MaterialToolbar mToolbar;

    private RecyclerView mRVRutinas;
    private LinearLayoutManager mLayoutManager;
    private AdaptadorListaRutinas mRVRutinasAdaptador;

    public FragmentListaRutinas() {

    }


    public static FragmentListaRutinas newInstance(String param1, String param2) {
        FragmentListaRutinas fragment = new FragmentListaRutinas();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModelRutina = new ViewModelProvider(getActivity()).get(ViewModelRutina.class);
        mViewModelUsuario = new ViewModelProvider(getActivity()).get(ViewModelUsuario.class);
        mViewModelEjercicios = new ViewModelProvider(getActivity()).get(ViewModelEjercicios.class);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        mFragmentView = inflater.inflate(R.layout.fragment_lista_rutinas, container, false);

        mToolbar = mFragmentView.findViewById(R.id.mtRutinas);

        dialogEliminarRutinaCreado = false;

        mRVRutinas = mFragmentView.findViewById(R.id.rvRutinas);
        accionesMenu();
        configurarRecyclerViewRutinas();

        return mFragmentView;
    }

    public void accionesMenu(){

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mViewModelUsuario.setmTipoFragmento(TipoFragmento.PERFIL);
            }
        });

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()){
                    case R.id.añadirRutina:
                        mViewModelEjercicios.setDiasRutina(new Rutina());
                        mViewModelEjercicios.getDiasRutina().getValue().setDias(new ArrayList<>());
                        mViewModelUsuario.setmTipoFragmento(TipoFragmento.DIAS_RUTINA);
                        break;
                }
                return false;
            }
        });

    }

    public void configurarRecyclerViewRutinas(){

        mRVRutinas.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRVRutinasAdaptador = new AdaptadorListaRutinas(mViewModelRutina.getmListaRutinasUsuario());
        mRVRutinas.setLayoutManager(mLayoutManager);
        mRVRutinas.setAdapter(mRVRutinasAdaptador);

        mRVRutinasAdaptador.setOnItemClickListener(new AdaptadorListaRutinas.OnItemClickListener() {
            @Override
            public void editarListener(int position) {
                mViewModelEjercicios.setDiasRutina(mRVRutinasAdaptador.listaRutinas.get(position));
                mViewModelUsuario.setmTipoFragmento(TipoFragmento.DIAS_RUTINA);
            }

            @Override
            public void eliminarListener(int position) {
                Rutina rutina = mRVRutinasAdaptador.listaRutinas.get(position);
                if (mViewModelRutina.getmListaRutinasUsuario().size() > 1){
                    if (!mViewModelUsuario.getmUsuario().getRutina().equals(rutina.getUid())){
                        mostrarDialogoEliminarRutina(rutina);
                    }else{
                        Snackbar.make(getView(), R.string.fallo_eliminar_rutina_actual, Snackbar.LENGTH_SHORT);
                    }
                }else {
                    Snackbar.make(getView(), R.string.fallo_eliminar_unica_rutina, Snackbar.LENGTH_SHORT);
                }

            }
        });

    }

    public void mostrarDialogoEliminarRutina(Rutina rutina){

        if (mAlertDialogEliminarRutina == null || !dialogEliminarRutinaCreado) {

            dialogEliminarRutinaCreado = true;
            MaterialAlertDialogBuilder builder = new MaterialAlertDialogBuilder(getContext());
            builder.setTitle(getText(R.string.titulo_dialogo_eliminar_rutina))
                    .setMessage(getText(R.string.mensaje_dialogo_eliminar_rutina))
                    .setCancelable(true)
                    .setPositiveButton(getText(R.string.boton_eliminar_rutina), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    }).setNegativeButton(getText(R.string.boton_cerrar), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            });

            mAlertDialogEliminarRutina = builder.create();
            mAlertDialogEliminarRutina.show();

            //Asigno de nuevo la funcion del boton principal del dialogo porque asi evito que este se cierre
            // al pulsar el boton y no tener elegido ningun checkbox
            mAlertDialogEliminarRutina.getButton(androidx.appcompat.app.AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    eliminarRutina(rutina);
                    mAlertDialogEliminarRutina.dismiss();
                }
            });

            mAlertDialogEliminarRutina.getButton(androidx.appcompat.app.AlertDialog.BUTTON_NEGATIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mAlertDialogEliminarRutina.dismiss();
                }
            });
        }else {

            mAlertDialogEliminarRutina.show();

        }

    }

    public void eliminarRutina(Rutina rutina){
        mViewModelRutina.eliminarRutina(rutina).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(Task<Void> task) {

                if (task.isSuccessful()){
                    mViewModelRutina.getmListaRutinasUsuario().remove(rutina);
                    mRVRutinasAdaptador.notifyDataSetChanged();
                    Snackbar.make(getView(), R.string.rutina_eliminada_correctamente, Snackbar.LENGTH_SHORT);
                }else {
                    Snackbar.make(getView(), R.string.fallo_eliminar_rutina, Snackbar.LENGTH_SHORT);
                }

            }
        });

    }

}