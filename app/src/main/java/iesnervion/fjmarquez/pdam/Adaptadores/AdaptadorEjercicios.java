package iesnervion.fjmarquez.pdam.Adaptadores;

import android.net.Uri;
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
import iesnervion.fjmarquez.pdam.Utiles.DificultadEjercicio;
import iesnervion.fjmarquez.pdam.Utiles.Utiles;

/**
 * Clase usada como adaptador para un RecyclerView, la cual mediante su constructor recibira un ArrayList de Ejercicios.
 */
public class AdaptadorEjercicios extends RecyclerView.Adapter<AdaptadorEjercicios.RVEjerciciosViewHolder> {

    private ArrayList<Ejercicio> listaEjercicios;
    public AdaptadorEjercicios.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void añadirListener(int position);
    }

    public void setOnItemClickListener(AdaptadorEjercicios.OnItemClickListener listener){
        mListener = listener;
    }

    public AdaptadorEjercicios(ArrayList<Ejercicio> listaEjercicios) {
        this.listaEjercicios = listaEjercicios;
    }

    @NonNull
    @Override
    public RVEjerciciosViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ejercicio, parent, false);
        RVEjerciciosViewHolder vh = new RVEjerciciosViewHolder(v, mListener);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorEjercicios.RVEjerciciosViewHolder holder, int position) {

        Ejercicio ejercicioActual = this.listaEjercicios.get(position);


        Glide.with(holder.mIVGIFEjercicio.getContext()).asBitmap()
                .load(ejercicioActual.getGif())
                //.load("https://drive.google.com/uc?id=1NOZQQFBOrW920Rll8wTzk0lc11GeRZ_P")
                //https://drive.google.com/uc?id=11liUozsdtLleaqYgYP9bjhRnBkN2GHB8
                                .into(holder.mIVGIFEjercicio);

        holder.mTVNombreEjercicio.setText(ejercicioActual.getNombre());
        holder.mTVDificulatadEjercicio.setText(Utiles.capitalizar(ejercicioActual.getGrupoMuscular().name()));
        holder.mTVDificulatadEjercicio.setTextColor(holder.mTVDificulatadEjercicio.getContext().getResources().getColor(Utiles.colorDificultad(ejercicioActual.getDificultad())));


    }

    @Override
    public int getItemCount() {
        return this.listaEjercicios.size();
    }

    public static class RVEjerciciosViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIVGIFEjercicio;
        private TextView mTVNombreEjercicio;
        private TextView mTVDificulatadEjercicio;
        private ImageButton mIBAñadirEjercicio;


        public RVEjerciciosViewHolder(@NonNull View itemView, final AdaptadorEjercicios.OnItemClickListener listener) {
            super(itemView);

            mIVGIFEjercicio = itemView.findViewById(R.id.ivGifEjercicio);
            mTVNombreEjercicio = itemView.findViewById(R.id.tvNombreEjercicio);
            mTVDificulatadEjercicio = itemView.findViewById(R.id.tvDificultadEjercicio);
            mIBAñadirEjercicio = itemView.findViewById(R.id.ibAñadirEjercicio);
            mIBAñadirEjercicio.setOnClickListener(new View.OnClickListener() {
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

        public TextView getmTVNombreEjercicio() {
            return mTVNombreEjercicio;
        }

        public TextView getmTVDificulatadEjercicio() {
            return mTVDificulatadEjercicio;
        }

        public void setmTVNombreEjercicio(TextView mTVNombreEjercicio) {
            this.mTVNombreEjercicio = mTVNombreEjercicio;
        }

        public void setmTVDificulatadEjercicio(TextView mTVDificulatadEjercicio) {
            this.mTVDificulatadEjercicio = mTVDificulatadEjercicio;
        }
    }

}
