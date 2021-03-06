package iesnervion.fjmarquez.pdam.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.snackbar.Snackbar;

import org.w3c.dom.Text;

import java.util.ArrayList;

import iesnervion.fjmarquez.pdam.Entidades.Dia;
import iesnervion.fjmarquez.pdam.R;
import iesnervion.fjmarquez.pdam.Utiles.Utiles;

/**
 * Clase usada como adaptador para un RecyclerView, la cual mediante su constructor recibira un ArrayList de Dias.
 */
public class AdaptadorDias extends RecyclerView.Adapter<AdaptadorDias.RVDiasViewHolder>{

    public static ArrayList<Dia> listaDias;
    public OnItemClickListener mListener;

    public interface OnItemClickListener{
        void añadirListener(int position);
        void mostrarListener(int position);
        void quitarListener(int position);
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

        v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_dia, parent, false);

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
        //Nombre del dia capitalizado
        holder.getTvDia().setText(Utiles.capitalizar(diaActual.getDia().name()));
        //Numero de ejercicios añadidos al dia
        holder.getTvNumeroEjercicios().setText(diaActual.getEjercicios().size() + " ejercicios añadidos");

        AdaptadorEjerciciosSimple adaptadorEjerciciosSimple = new AdaptadorEjerciciosSimple(diaActual.getEjercicios());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(holder.getRvEjerciciosSimple().getContext());
        holder.getRvEjerciciosSimple().setLayoutManager(linearLayoutManager);
        holder.getRvEjerciciosSimple().setAdapter(adaptadorEjerciciosSimple);

        adaptadorEjerciciosSimple.setOnItemClickListener(new AdaptadorEjerciciosSimple.OnItemClickListener() {
            @Override
            public void quitarListener(int position) {
                diaActual.getEjercicios().remove(position);
                adaptadorEjerciciosSimple.notifyDataSetChanged();
                notifyDataSetChanged();
                if (diaActual.getEjercicios().size() == 0){
                    holder.getRvEjerciciosSimple().setVisibility(View.GONE);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return this.listaDias.size();
    }

    public static class RVDiasViewHolder extends RecyclerView.ViewHolder {

        private TextView tvDia;
        private TextView tvNumeroEjercicios;
        private Button btnAñadir;
        private Button btnMostrar;
        private ImageButton btnQuitar;
        private RecyclerView rvEjerciciosSimple;

        /* CONSTRUCTOR */

        public RVDiasViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            tvDia = itemView.findViewById(R.id.tvDia);
            tvNumeroEjercicios = itemView.findViewById(R.id.tvNumeroEjercicios);
            btnMostrar = itemView.findViewById(R.id.btnVerEjerciciosDia);
            rvEjerciciosSimple = itemView.findViewById(R.id.rvEjerciciosSimple);
            btnMostrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listaDias.get(getAdapterPosition()).getEjercicios().size() != 0){
                        if(rvEjerciciosSimple.getVisibility() == View.GONE){
                            rvEjerciciosSimple.setVisibility(View.VISIBLE);
                        }else{
                            rvEjerciciosSimple.setVisibility(View.GONE);
                        }

                        if(listener != null){
                            int postition = getAdapterPosition();
                            if (postition != RecyclerView.NO_POSITION){
                                listener.mostrarListener(postition);
                            }
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
            btnQuitar = itemView.findViewById(R.id.btnQuitarDia);
            btnQuitar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int postition = getAdapterPosition();
                        if (postition != RecyclerView.NO_POSITION){
                            listener.quitarListener(postition);
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

        public RecyclerView getRvEjerciciosSimple() {
            return rvEjerciciosSimple;
        }

        public ImageButton getBtnQuitar() {
            return btnQuitar;
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

        public void setRvEjerciciosSimple(RecyclerView rvEjerciciosSimple) {
            this.rvEjerciciosSimple = rvEjerciciosSimple;
        }

        public void setBtnQuitar(ImageButton btnQuitar) {
            this.btnQuitar = btnQuitar;
        }
    }

}