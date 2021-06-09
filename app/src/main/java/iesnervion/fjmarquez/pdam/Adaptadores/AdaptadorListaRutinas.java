package iesnervion.fjmarquez.pdam.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import iesnervion.fjmarquez.pdam.Entidades.Rutina;
import iesnervion.fjmarquez.pdam.R;

public class AdaptadorListaRutinas extends RecyclerView.Adapter<AdaptadorListaRutinas.RVRutinaViewHolder>{

    public static ArrayList<Rutina> listaRutinas;
    public AdaptadorListaRutinas.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void editarListener(int position);
        void eliminarListener(int position);
    }

    public void setOnItemClickListener(AdaptadorListaRutinas.OnItemClickListener listener){
        mListener = listener;
    }

    public AdaptadorListaRutinas(ArrayList<Rutina> listaRutinas) {
        this.listaRutinas = listaRutinas;
    }

    @NonNull
    @Override
    public AdaptadorListaRutinas.RVRutinaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_lista_rutinas, parent, false);

        AdaptadorListaRutinas.RVRutinaViewHolder vh = new AdaptadorListaRutinas.RVRutinaViewHolder(v, mListener);
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
    public void onBindViewHolder(@NonNull AdaptadorListaRutinas.RVRutinaViewHolder holder, int position) {

        Rutina rutinaActual = this.listaRutinas.get(position);
        holder.getTvRutina().setText(rutinaActual.getNombre());
        holder.getTvNumeroDias().setText(rutinaActual.getDias().size() + " dias");

    }

    @Override
    public int getItemCount() {
        return this.listaRutinas.size();
    }

    public static class RVRutinaViewHolder extends RecyclerView.ViewHolder {

        private TextView tvRutina;
        private TextView tvNumeroDias;
        private Button btnEditar;
        private Button btnEliminar;

        /* CONSTRUCTOR */

        public RVRutinaViewHolder(@NonNull View itemView, final AdaptadorListaRutinas.OnItemClickListener listener) {
            super(itemView);

            tvRutina = itemView.findViewById(R.id.tvRutina);
            tvNumeroDias = itemView.findViewById(R.id.tvDiasRutina);
            btnEliminar = itemView.findViewById(R.id.btnEliminarRutina);
            btnEliminar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int postition = getAdapterPosition();
                        if (postition != RecyclerView.NO_POSITION){
                            listener.eliminarListener(postition);
                        }
                    }
                }
            });
            btnEditar = itemView.findViewById(R.id.btnEditarRutina);
            btnEditar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int postition = getAdapterPosition();
                        if (postition != RecyclerView.NO_POSITION){
                            listener.editarListener(postition);
                        }
                    }

                }
            });

        }

        /* GETTERS */

        public TextView getTvRutina() {
            return tvRutina;
        }

        public TextView getTvNumeroDias() {
            return tvNumeroDias;
        }

        public Button getBtnEditar() {
            return btnEditar;
        }

        public Button getBtnEliminar() {
            return btnEliminar;
        }

        /* SETTERS */

        public void setTvRutina(TextView tvRutina) {
            this.tvRutina = tvRutina;
        }

        public void setTvNumeroDias(TextView tvNumeroDias) {
            this.tvNumeroDias = tvNumeroDias;
        }

        public void setBtnEditar(Button btnEditar) {
            this.btnEditar = btnEditar;
        }

        public void setBtnEliminar(Button btnEliminar) {
            this.btnEliminar = btnEliminar;
        }
    }

}