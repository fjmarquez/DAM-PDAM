package iesnervion.fjmarquez.pdam.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import iesnervion.fjmarquez.pdam.Entidades.Serie;
import iesnervion.fjmarquez.pdam.R;

/**
 * Clase destinada a rellenar un RecyclerView con las series de un ejercicio perteneciente a un registro historico de entreanamiento.
 */
public class AdaptadorSerieEjercicioHistorico extends RecyclerView.Adapter<AdaptadorSerieEjercicioHistorico.RVSeriesEjercicioHistoricoViewHolder> {

    private ArrayList<Serie> listaSeries;

    public AdaptadorSerieEjercicioHistorico(ArrayList<Serie> listaSeries) {
        this.listaSeries = listaSeries;
    }

    @NonNull
    @Override
    public RVSeriesEjercicioHistoricoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_series_ejercicio_historico, parent, false);

        RVSeriesEjercicioHistoricoViewHolder vh = new RVSeriesEjercicioHistoricoViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull RVSeriesEjercicioHistoricoViewHolder holder, int position) {

        Serie serieActual = this.listaSeries.get(position);

        //Nombre de la serie (Serie + posicion de la serie en el ArrayList)
        holder.getmTVNombreSerieEjercicioHistorico().setText("Serie " + (position + 1));
        //Repeticiones de la serie
        holder.getmTVRepsSerieEjercicioHistorico().setText(""+serieActual.getRepeticiones()+ " Reps.");
        //Kgs de la serie
        holder.getmTVKgsSerieEjercicioHistorico().setText(""+serieActual.getPeso()+ " Kgs.");

    }

    @Override
    public int getItemCount() {
        return this.listaSeries.size();
    }

    public static class RVSeriesEjercicioHistoricoViewHolder extends RecyclerView.ViewHolder {

        private TextView mTVNombreSerieEjercicioHistorico;
        private TextView mTVRepsSerieEjercicioHistorico;
        private TextView mTVKgsSerieEjercicioHistorico;

        /* CONSTRUCTOR */

        public RVSeriesEjercicioHistoricoViewHolder(@NonNull View itemView) {
            super(itemView);

            mTVNombreSerieEjercicioHistorico = itemView.findViewById(R.id.tvNombreSerieEjercicioHistorico);
            mTVRepsSerieEjercicioHistorico = itemView.findViewById(R.id.tvRepsSerieEjercicioHistorico);
            mTVKgsSerieEjercicioHistorico = itemView.findViewById(R.id.tvKgsSerieEjercicioHistorico);

        }

        /* GETTERS */

        public TextView getmTVNombreSerieEjercicioHistorico() {
            return mTVNombreSerieEjercicioHistorico;
        }

        public TextView getmTVRepsSerieEjercicioHistorico() {
            return mTVRepsSerieEjercicioHistorico;
        }

        public TextView getmTVKgsSerieEjercicioHistorico() {
            return mTVKgsSerieEjercicioHistorico;
        }

        /* SETTERS */

        public void setmTVNombreSerieEjercicioHistorico(TextView mTVNombreSerieEjercicioHistorico) {
            this.mTVNombreSerieEjercicioHistorico = mTVNombreSerieEjercicioHistorico;
        }

        public void setmTVRepsSerieEjercicioHistorico(TextView mTVRepsSerieEjercicioHistorico) {
            this.mTVRepsSerieEjercicioHistorico = mTVRepsSerieEjercicioHistorico;
        }

        public void setmTVKgsSerieEjercicioHistorico(TextView mTVKgsSerieEjercicioHistorico) {
            this.mTVKgsSerieEjercicioHistorico = mTVKgsSerieEjercicioHistorico;
        }

    }
}
