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
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;

import iesnervion.fjmarquez.pdam.Entidades.Ejercicio;
import iesnervion.fjmarquez.pdam.R;
import iesnervion.fjmarquez.pdam.Utiles.GrupoMuscular;
import iesnervion.fjmarquez.pdam.Utiles.Utiles;

/**
 * Clase usada como adaptador para un RecyclerView, la cual mediante su constructor recibira un ArrayList de Ejercicios.
 */
public class AdaptadorEjercicios extends RecyclerView.Adapter<AdaptadorEjercicios.RVEjerciciosViewHolder> {

    private ArrayList<Ejercicio> listaEjercicios;
    public AdaptadorEjercicios.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void añadirListener(int position);
        void clickListener(int position);
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

        //Gif del ejercicio
        Glide.with(holder.getmIVGIFEjercicio().getContext()).asBitmap()
                .load(ejercicioActual.getGif())
                .apply(new RequestOptions().override(200, 200))
                                .into(holder.getmIVGIFEjercicio());

        //Nombre del ejercicio
        holder.getmTVNombreEjercicio().setText(ejercicioActual.getNombre());
        //Grupo muscular del ejercicio capitalizado
        holder.getmTVDificulatadEjercicio().setText(Utiles.capitalizar(ejercicioActual.getGrupoMuscular().name()));
        holder.getmTVDificulatadEjercicio().setTextColor(holder.getmTVDificulatadEjercicio().getContext().getResources().getColor(Utiles.colorDificultad(ejercicioActual.getDificultad())));


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

        /* CONSTRUCTOR */

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

        public ImageButton getmIBAñadirEjercicio() {
            return mIBAñadirEjercicio;
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

        public void setmIBAñadirEjercicio(ImageButton mIBAñadirEjercicio) {
            this.mIBAñadirEjercicio = mIBAñadirEjercicio;
        }

    }

}
