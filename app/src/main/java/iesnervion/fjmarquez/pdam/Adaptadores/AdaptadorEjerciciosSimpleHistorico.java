package iesnervion.fjmarquez.pdam.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import iesnervion.fjmarquez.pdam.Entidades.Ejercicio;
import iesnervion.fjmarquez.pdam.R;

/**
 * Clase usada como adaptador para un RecyclerView, la cual mediante su constructor recibira un ArrayList de Ejercicios.
 * A diferencia del AdaptadorEjercicioSimple muestra el numero de series establecidas para cada ejercicio
 */
public class AdaptadorEjerciciosSimpleHistorico extends RecyclerView.Adapter<AdaptadorEjerciciosSimpleHistorico.RVEjerciciosSimpleHistoricoViewHolder> {

    private ArrayList<Ejercicio> listaEjercicios;
    public AdaptadorEjerciciosSimpleHistorico.OnItemClickListener mListener;

    public AdaptadorEjerciciosSimpleHistorico(ArrayList<Ejercicio> listaEjercicios) {
        this.listaEjercicios = listaEjercicios;
    }

    public interface OnItemClickListener{
        void quitarListener(int position);
    }

    public void setOnItemClickListener(AdaptadorEjerciciosSimpleHistorico.OnItemClickListener listener){
        mListener = listener;
    }

    @NonNull
    @Override
    public RVEjerciciosSimpleHistoricoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ejercicio_simple_historico, parent, false);

        RVEjerciciosSimpleHistoricoViewHolder vh = new RVEjerciciosSimpleHistoricoViewHolder(v, mListener);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RVEjerciciosSimpleHistoricoViewHolder holder, int position) {

        Ejercicio ejercicioActual = this.listaEjercicios.get(position);

        //Nombre del ejercicio
        holder.getmTVNombreEjercicioSimple().setText(ejercicioActual.getNombre());
        //Numero de series
        holder.getmTVSeriesEjercicioSimple().setText(ejercicioActual.getSeries().size() + " series");

        AdaptadorSerieEjercicioHistorico adaptadorSerieEjercicioHistorico = new AdaptadorSerieEjercicioHistorico(ejercicioActual.getSeries());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(holder.getmRVSeriesEjercicio().getContext());
        holder.getmRVSeriesEjercicio().setLayoutManager(linearLayoutManager);
        holder.getmRVSeriesEjercicio().setAdapter(adaptadorSerieEjercicioHistorico);

    }

    @Override
    public int getItemCount() {
        return this.listaEjercicios.size();
    }

    public static class RVEjerciciosSimpleHistoricoViewHolder extends RecyclerView.ViewHolder {

        private TextView mTVNombreEjercicioSimple;
        private TextView mTVSeriesEjercicioSimple;
        private ImageButton mIBVerInfoEjercicio;
        private RecyclerView mRVSeriesEjercicio;

        /* CONSTRUCTOR */

        public RVEjerciciosSimpleHistoricoViewHolder(@NonNull View itemView, final AdaptadorEjerciciosSimpleHistorico.OnItemClickListener listener) {
            super(itemView);

            mTVNombreEjercicioSimple = itemView.findViewById(R.id.tvNombreEjercicioSimpleHistorico);
            mTVSeriesEjercicioSimple = itemView.findViewById(R.id.tvSeriesEjercicioSimpleHistorico);
            mRVSeriesEjercicio = itemView.findViewById(R.id.rvSeriesEjercicioHistorico);
            mIBVerInfoEjercicio = itemView.findViewById(R.id.ibVerInfoEjercicio);
            mIBVerInfoEjercicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mRVSeriesEjercicio.getVisibility() == View.GONE){
                        mRVSeriesEjercicio.setVisibility(View.VISIBLE);
                    }else{
                        mRVSeriesEjercicio.setVisibility(View.GONE);
                    }
                }
            });

        }

        /* GETTERS */

        public TextView getmTVNombreEjercicioSimple() {
            return mTVNombreEjercicioSimple;
        }

        public TextView getmTVSeriesEjercicioSimple() {
            return mTVSeriesEjercicioSimple;
        }

        public ImageButton getmIBVerInfoEjercicio() {
            return mIBVerInfoEjercicio;
        }

        public RecyclerView getmRVSeriesEjercicio() {
            return mRVSeriesEjercicio;
        }

        /* SETTERS */

        public void setmTVNombreEjercicioSimple(TextView mTVNombreEjercicioSimple) {
            this.mTVNombreEjercicioSimple = mTVNombreEjercicioSimple;
        }

        public void setmTVSeriesEjercicioSimple(TextView mTVSeriesEjercicioSimple) {
            this.mTVSeriesEjercicioSimple = mTVSeriesEjercicioSimple;
        }

        public void setmIBVerInfoEjercicio(ImageButton mIBVerInfoEjercicio) {
            this.mIBVerInfoEjercicio = mIBVerInfoEjercicio;
        }

        public void setmRVSeriesEjercicio(RecyclerView mRVSeriesEjercicio) {
            this.mRVSeriesEjercicio = mRVSeriesEjercicio;
        }
    }
}
