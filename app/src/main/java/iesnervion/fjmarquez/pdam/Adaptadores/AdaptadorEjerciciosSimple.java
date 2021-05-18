package iesnervion.fjmarquez.pdam.Adaptadores;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import iesnervion.fjmarquez.pdam.Entidades.Ejercicio;
import iesnervion.fjmarquez.pdam.R;
import iesnervion.fjmarquez.pdam.Utiles.Utiles;

/**
 * Clase usada como adaptador para un RecyclerView, la cual mediante su constructor recibira un ArrayList de Ejercicios.
 * A diferencia de la clase AdaptadorEjercicios esta clase muestra una informacion mas simplificada en cada elemento
 * de la lista.
 */
public class AdaptadorEjerciciosSimple extends RecyclerView.Adapter<AdaptadorEjerciciosSimple.RVEjerciciosSimpleViewHolder> {

    private ArrayList<Ejercicio> listaEjercicios;

    public AdaptadorEjerciciosSimple(ArrayList<Ejercicio> listaEjercicios) {
        this.listaEjercicios = listaEjercicios;
    }

    @NonNull
    @Override
    public AdaptadorEjerciciosSimple.RVEjerciciosSimpleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ejercicio_simple, parent, false);
        AdaptadorEjerciciosSimple.RVEjerciciosSimpleViewHolder vh = new AdaptadorEjerciciosSimple.RVEjerciciosSimpleViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorEjerciciosSimple.RVEjerciciosSimpleViewHolder holder, int position) {

        Ejercicio ejercicioActual = this.listaEjercicios.get(position);

        holder.mTVNombreEjercicioSimple.setText(ejercicioActual.getNombre());
        holder.mTVDificulatadEjercicioSimple.setText(Utiles.capitalizar(ejercicioActual.getGrupoMuscular().name()));
        holder.mTVDificulatadEjercicioSimple.setTextColor(holder.mTVDificulatadEjercicioSimple.getContext().getResources().getColor(Utiles.colorDificultad(ejercicioActual.getDificultad())));

    }

    @Override
    public int getItemCount() {
        return this.listaEjercicios.size();
    }

    public static class RVEjerciciosSimpleViewHolder extends RecyclerView.ViewHolder {

        private TextView mTVNombreEjercicioSimple;
        private TextView mTVDificulatadEjercicioSimple;


        public RVEjerciciosSimpleViewHolder(@NonNull View itemView) {
            super(itemView);

            mTVNombreEjercicioSimple = itemView.findViewById(R.id.tvNombreEjercicioSimple);
            mTVDificulatadEjercicioSimple = itemView.findViewById(R.id.tvDificultadEjercicioSimple);

        }

        public TextView getmTVNombreEjercicioSimple() {
            return mTVNombreEjercicioSimple;
        }

        public TextView getmTVDificulatadEjercicioSimple() {
            return mTVDificulatadEjercicioSimple;
        }

        public void setmTVNombreEjercicioSimple(TextView mTVNombreEjercicioSimple) {
            this.mTVNombreEjercicioSimple = mTVNombreEjercicioSimple;
        }

        public void setmTVDificulatadEjercicioSimple(TextView mTVDificulatadEjercicioSimple) {
            this.mTVDificulatadEjercicioSimple = mTVDificulatadEjercicioSimple;
        }
    }
}
