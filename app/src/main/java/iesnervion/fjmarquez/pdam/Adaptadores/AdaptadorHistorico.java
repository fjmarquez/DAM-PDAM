package iesnervion.fjmarquez.pdam.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import iesnervion.fjmarquez.pdam.Entidades.Dia;
import iesnervion.fjmarquez.pdam.R;
import iesnervion.fjmarquez.pdam.Utiles.Utiles;

/**
 * Clase usada como adaptador para un RecyclerView, la cual mediante su constructor recibira un ArrayList de Dias.
 * A diferencia del AdaptadorDias, este muestra la fecha en la que se llevo a cabo el dia y el numero de ejercicios que este tiene.
 */
public class AdaptadorHistorico extends RecyclerView.Adapter<AdaptadorHistorico.RVHistoricoViewHolder>{

    private ArrayList<Dia> listaDias;
    public AdaptadorHistorico.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void mostrarListener(int position);
    }

    public void setOnItemClickListener(AdaptadorHistorico.OnItemClickListener listener){
        mListener = listener;
    }

    public AdaptadorHistorico(ArrayList<Dia> listaDias) {
        this.listaDias = listaDias;
    }

    @NonNull
    @Override
    public RVHistoricoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_historico, parent, false);

        RVHistoricoViewHolder vh = new RVHistoricoViewHolder(v, mListener);

        return vh;
    }

    @Override
    public int getItemViewType(int position) {

        if (position % 2 == 0) {
            return 0;
        }
        else {
            return 1;
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RVHistoricoViewHolder holder, int position) {

        Dia historicoActual = this.listaDias.get(position);

        //Fecha historico
        holder.getTvDiaHistorico().setText(historicoActual.getFecha());
        //Numero de ejercicios
        holder.getTvNumeroEjerciciosHistorico().setText(historicoActual.getEjercicios().size() + " ejercicios realizados");

        AdaptadorEjerciciosSimpleHistorico adaptadorEjerciciosSimpleHistorico = new AdaptadorEjerciciosSimpleHistorico(historicoActual.getEjercicios());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(holder.getRvEjerciciosSimpleHistorico().getContext());
        holder.getRvEjerciciosSimpleHistorico().setLayoutManager(linearLayoutManager);
        holder.getRvEjerciciosSimpleHistorico().setAdapter(adaptadorEjerciciosSimpleHistorico);

    }



    @Override
    public int getItemCount() {
        return this.listaDias.size();
    }

    public static class RVHistoricoViewHolder extends RecyclerView.ViewHolder {

        /* ATRIBUTOS */

        private TextView tvDiaHistorico;
        private TextView tvNumeroEjerciciosHistorico;
        private Button btnMostrarEjerciciosHistorico;
        private RecyclerView rvEjerciciosSimpleHistorico;

        /* CONSTRUCTOR */

        public RVHistoricoViewHolder(@NonNull View itemView, final AdaptadorHistorico.OnItemClickListener listener) {
            super(itemView);

            tvDiaHistorico = itemView.findViewById(R.id.tvDiaHistorico);
            tvNumeroEjerciciosHistorico = itemView.findViewById(R.id.tvNumeroEjerciciosHistorico);
            btnMostrarEjerciciosHistorico = itemView.findViewById(R.id.btnVerEjerciciosHistorico);
            rvEjerciciosSimpleHistorico = itemView.findViewById(R.id.rvEjerciciosSimpleHistorico);
            btnMostrarEjerciciosHistorico.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(rvEjerciciosSimpleHistorico.getVisibility() == View.GONE){
                        rvEjerciciosSimpleHistorico.setVisibility(View.VISIBLE);
                    }else{
                        rvEjerciciosSimpleHistorico.setVisibility(View.GONE);
                    }

                    if(listener != null){
                        int postition = getAdapterPosition();
                        if (postition != RecyclerView.NO_POSITION){
                            listener.mostrarListener(postition);
                        }
                    }
                }
            });

        }

        /* GETTERS */

        public TextView getTvDiaHistorico() {
            return tvDiaHistorico;
        }

        public TextView getTvNumeroEjerciciosHistorico() {
            return tvNumeroEjerciciosHistorico;
        }

        public Button getBtnMostrarEjerciciosHistorico() {
            return btnMostrarEjerciciosHistorico;
        }

        public RecyclerView getRvEjerciciosSimpleHistorico() {
            return rvEjerciciosSimpleHistorico;
        }

        /* SETTERS */

        public void setTvDiaHistorico(TextView tvDiaHistorico) {
            this.tvDiaHistorico = tvDiaHistorico;
        }

        public void setTvNumeroEjerciciosHistorico(TextView tvNumeroEjerciciosHistorico) {
            this.tvNumeroEjerciciosHistorico = tvNumeroEjerciciosHistorico;
        }

        public void setBtnMostrarEjerciciosHistorico(Button btnMostrarEjerciciosHistorico) {
            this.btnMostrarEjerciciosHistorico = btnMostrarEjerciciosHistorico;
        }

        public void setRvEjerciciosSimpleHistorico(RecyclerView rvEjerciciosSimpleHistorico) {
            this.rvEjerciciosSimpleHistorico = rvEjerciciosSimpleHistorico;
        }
    }

}