package iesnervion.fjmarquez.pdam.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
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

    public AdaptadorEjerciciosSimpleHistorico(ArrayList<Ejercicio> listaEjercicios) {
        this.listaEjercicios = listaEjercicios;
    }

    @NonNull
    @Override
    public RVEjerciciosSimpleHistoricoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ejercicio_simple_historico, parent, false);

        RVEjerciciosSimpleHistoricoViewHolder vh = new RVEjerciciosSimpleHistoricoViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RVEjerciciosSimpleHistoricoViewHolder holder, int position) {

        Ejercicio ejercicioActual = this.listaEjercicios.get(position);

        //Nombre del ejercicio
        holder.getmTVNombreEjercicioSimple().setText(ejercicioActual.getNombre());
        //Numero de series
        holder.getmTVSeriesEjercicioSimple().setText(ejercicioActual.getSeries().size() + " series");

    }

    @Override
    public int getItemCount() {
        return this.listaEjercicios.size();
    }

    public static class RVEjerciciosSimpleHistoricoViewHolder extends RecyclerView.ViewHolder {

        private TextView mTVNombreEjercicioSimple;
        private TextView mTVSeriesEjercicioSimple;

        /* CONSTRUCTOR */

        public RVEjerciciosSimpleHistoricoViewHolder(@NonNull View itemView) {
            super(itemView);

            mTVNombreEjercicioSimple = itemView.findViewById(R.id.tvNombreEjercicioSimpleHistorico);
            mTVSeriesEjercicioSimple = itemView.findViewById(R.id.tvSeriesEjercicioSimpleHistorico);

        }

        /* GETTERS */

        public TextView getmTVNombreEjercicioSimple() {
            return mTVNombreEjercicioSimple;
        }

        public TextView getmTVSeriesEjercicioSimple() {
            return mTVSeriesEjercicioSimple;
        }

        /* SETTERS */

        public void setmTVNombreEjercicioSimple(TextView mTVNombreEjercicioSimple) {
            this.mTVNombreEjercicioSimple = mTVNombreEjercicioSimple;
        }

        public void setmTVSeriesEjercicioSimple(TextView mTVSeriesEjercicioSimple) {
            this.mTVSeriesEjercicioSimple = mTVSeriesEjercicioSimple;
        }
    }
}
