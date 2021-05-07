package iesnervion.fjmarquez.pdam.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import iesnervion.fjmarquez.pdam.Entidades.Dia;
import iesnervion.fjmarquez.pdam.R;
import iesnervion.fjmarquez.pdam.Utiles.Utiles;

public class AdaptadorDias extends RecyclerView.Adapter<AdaptadorDias.RVDiasViewHolder>{

    private ArrayList<Dia> listaDias;
    public OnItemClickListener mListener;

    public interface OnItemClickListener{
        void añadirListener(int position);
        void mostrarListener(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public AdaptadorDias(ArrayList<Dia> listaDias) {
        this.listaDias = listaDias;
    }

    @NonNull
    @Override
    public AdaptadorDias.RVDiasViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v;
        if (getItemViewType(viewType) == 0){
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dia, parent, false);
        }else {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dia_2, parent, false);
        }

        RVDiasViewHolder vh = new RVDiasViewHolder(v, mListener);
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
    public void onBindViewHolder(@NonNull AdaptadorDias.RVDiasViewHolder holder, int position) {

        Dia diaActual = this.listaDias.get(position);
        holder.tvDia.setText(Utiles.capitalizar(diaActual.getDia().name()));
        holder.tvNumeroEjercicios.setText(diaActual.getEjercicios().size() + " ejercicios añadidos");

    }

    @Override
    public int getItemCount() {
        return this.listaDias.size();
    }

    public static class RVDiasViewHolder extends RecyclerView.ViewHolder {

        /* ATRIBUTOS */
        private TextView tvDia;
        private TextView tvNumeroEjercicios;
        private Button btnAñadir;
        private Button btnMostrar;

        /* CONSTRUCTOR */
        public RVDiasViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            tvDia = itemView.findViewById(R.id.tvDia);
            tvNumeroEjercicios = itemView.findViewById(R.id.tvNumeroEjercicios);
            btnMostrar = itemView.findViewById(R.id.btnVerEjerciciosDia);
            btnMostrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int postition = getAdapterPosition();
                        if (postition != RecyclerView.NO_POSITION){
                            listener.mostrarListener(postition);
                        }
                    }
                }
            });
            btnAñadir = itemView.findViewById(R.id.btnAñadirEjercicioDia);
            btnAñadir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int postition = getAdapterPosition();
                        if (postition != RecyclerView.NO_POSITION){
                            listener.añadirListener(postition);
                        }
                    }
                }
            });


        }

        /* GETTERS */
        public TextView getTvDia() {
            return tvDia;
        }

        public TextView getTvNumeroEjercicios() {
            return tvNumeroEjercicios;
        }

        public Button getBtnAñadir() {
            return btnAñadir;
        }

        public Button getBtnMostrar() {
            return btnMostrar;
        }

        /* SETTERS */
        public void setTvDia(TextView tvDia) {
            this.tvDia = tvDia;
        }

        public void setTvNumeroEjercicios(TextView tvNumeroEjercicios) {
            this.tvNumeroEjercicios = tvNumeroEjercicios;
        }

        public void setBtnAñadir(Button btnAñadir) {
            this.btnAñadir = btnAñadir;
        }

        public void setBtnMostrar(Button btnMostrar) {
            this.btnMostrar = btnMostrar;
        }
    }

}