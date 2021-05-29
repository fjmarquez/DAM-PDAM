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

public class AdaptadorEjerciciosDiaRutina extends RecyclerView.Adapter<AdaptadorEjerciciosDiaRutina.RVEjerciciosDiaRutinaViewHolder> {

    public static ArrayList<Ejercicio> listaEjercicios;
    public AdaptadorEjerciciosDiaRutina.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void realizarEjercicioListener(int position);
    }

    public void setOnItemClickListener(AdaptadorEjerciciosDiaRutina.OnItemClickListener listener){
        mListener = listener;
    }

    public AdaptadorEjerciciosDiaRutina(ArrayList<Ejercicio> listaEjercicios) {
        this.listaEjercicios = listaEjercicios;
    }

    @NonNull
    @Override
    public AdaptadorEjerciciosDiaRutina.RVEjerciciosDiaRutinaViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ejercicio_dia_rutina, parent, false);
        AdaptadorEjerciciosDiaRutina.RVEjerciciosDiaRutinaViewHolder vh = new AdaptadorEjerciciosDiaRutina.RVEjerciciosDiaRutinaViewHolder(v, mListener);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorEjerciciosDiaRutina.RVEjerciciosDiaRutinaViewHolder holder, int position) {

        holder.setIsRecyclable(true);

        Ejercicio ejercicioActual = this.listaEjercicios.get(position);


        Glide.with(holder.mIVGIFEjercicio.getContext()).asBitmap()
                .load(Utiles.urlDrive(ejercicioActual.getGif()))
                .into(holder.mIVGIFEjercicio);

        holder.mTVNombreEjercicio.setText(ejercicioActual.getNombre());
        holder.mTVDificulatadEjercicio.setText(Utiles.capitalizar(ejercicioActual.getGrupoMuscular().name()));
        holder.mTVDificulatadEjercicio.setTextColor(holder.mTVDificulatadEjercicio.getContext().getResources().getColor(Utiles.colorDificultad(ejercicioActual.getDificultad())));


    }

    @Override
    public int getItemCount() {
        return this.listaEjercicios.size();
    }

    public static class RVEjerciciosDiaRutinaViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIVGIFEjercicio;
        private TextView mTVNombreEjercicio;
        private TextView mTVDificulatadEjercicio;
        private ImageButton mIBRealizarEjercicio;


        public RVEjerciciosDiaRutinaViewHolder(@NonNull View itemView, final AdaptadorEjerciciosDiaRutina.OnItemClickListener listener) {
            super(itemView);

            mIVGIFEjercicio = itemView.findViewById(R.id.ivGifEjercicioDiaRutina);
            mTVNombreEjercicio = itemView.findViewById(R.id.tvNombreEjercicioDiaRutina);
            mTVDificulatadEjercicio = itemView.findViewById(R.id.tvDificultadEjercicioDiaRutina);
            mIBRealizarEjercicio = itemView.findViewById(R.id.ibRealizarEjercicioDiaRutina);
            mIBRealizarEjercicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int postition = getAdapterPosition();
                        if (postition != RecyclerView.NO_POSITION){
                            listener.realizarEjercicioListener(postition);
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
