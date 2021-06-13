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

import iesnervion.fjmarquez.pdam.Entidades.Dia;
import iesnervion.fjmarquez.pdam.Entidades.Ejercicio;
import iesnervion.fjmarquez.pdam.R;
import iesnervion.fjmarquez.pdam.Utiles.Utiles;

/**
 * Clase usada como adaptador para un RecyclerView, la cual mediante su constructor recibira un ArrayList de Ejercicios.
 * A diferencia del AdaptadorEjercicios muestra un boton diferente destinado a comenzar la realizacion del ejercicio.
 */
public class AdaptadorEjerciciosDiaRutina extends RecyclerView.Adapter<AdaptadorEjerciciosDiaRutina.RVEjerciciosDiaRutinaViewHolder> {

    public static Dia diaActualRutina;
    public AdaptadorEjerciciosDiaRutina.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void realizarEjercicioListener(int position);
        void clickListener(int position);
    }

    public void setOnItemClickListener(AdaptadorEjerciciosDiaRutina.OnItemClickListener listener){
        mListener = listener;
    }

    public AdaptadorEjerciciosDiaRutina(Dia diaActualRutina) {
        this.diaActualRutina = diaActualRutina;
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

        Ejercicio ejercicioActual = this.diaActualRutina.getEjercicios().get(position);

        //Gif del ejercicio
        if (ejercicioActual.getDificultad() != null){
            Glide.with(holder.getmIVGIFEjercicio().getContext()).asBitmap()
                    .load(ejercicioActual.getGif())
                    .into(holder.getmIVGIFEjercicio());
        }else {
            holder.getmIVGIFEjercicio().setImageResource(R.mipmap.custom_ejercicio);
        }

        if (diaActualRutina.getFecha() != null && !diaActualRutina.getFinalizado()){
            holder.getmIBRealizarEjercicio().setVisibility(View.VISIBLE);
        }else {
            holder.getmIBRealizarEjercicio().setVisibility(View.INVISIBLE);
        }


        //Nombre del ejercicio
        holder.getmTVNombreEjercicio().setText(ejercicioActual.getNombre());
        //Dificultad del ejercicio capitalizada
        holder.getmTVDificulatadEjercicio().setText(Utiles.capitalizar(ejercicioActual.getGrupoMuscular().name()));
        holder.getmTVDificulatadEjercicio().setTextColor(holder.getmTVDificulatadEjercicio().getContext().getResources().getColor(Utiles.colorDificultad(ejercicioActual.getDificultad())));




    }

    @Override
    public int getItemCount() {
        return this.diaActualRutina.getEjercicios().size();
    }

    public static class RVEjerciciosDiaRutinaViewHolder extends RecyclerView.ViewHolder {

        private ImageView mIVGIFEjercicio;
        private TextView mTVNombreEjercicio;
        private TextView mTVDificulatadEjercicio;
        private ImageButton mIBRealizarEjercicio;

        /* CONSTRUCTOR */

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

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){
                        int postition = getAdapterPosition();
                        if (postition != RecyclerView.NO_POSITION){
                            listener.clickListener(postition);
                        }
                    }
                }
            });


        }

        /* GETTERS */

        public TextView getmTVNombreEjercicio() {
            return mTVNombreEjercicio;
        }

        public TextView getmTVDificulatadEjercicio() {
            return mTVDificulatadEjercicio;
        }

        public ImageView getmIVGIFEjercicio() {
            return mIVGIFEjercicio;
        }

        public ImageButton getmIBRealizarEjercicio() {
            return mIBRealizarEjercicio;
        }

        /* SETTERS */

        public void setmTVNombreEjercicio(TextView mTVNombreEjercicio) {
            this.mTVNombreEjercicio = mTVNombreEjercicio;
        }

        public void setmTVDificulatadEjercicio(TextView mTVDificulatadEjercicio) {
            this.mTVDificulatadEjercicio = mTVDificulatadEjercicio;
        }

        public void setmIVGIFEjercicio(ImageView mIVGIFEjercicio) {
            this.mIVGIFEjercicio = mIVGIFEjercicio;
        }

        public void setmIBRealizarEjercicio(ImageButton mIBRealizarEjercicio) {
            this.mIBRealizarEjercicio = mIBRealizarEjercicio;
        }
    }

}
