package iesnervion.fjmarquez.pdam.Adaptadores;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import iesnervion.fjmarquez.pdam.Entidades.Ejercicio;
import iesnervion.fjmarquez.pdam.Entidades.Serie;
import iesnervion.fjmarquez.pdam.R;
import iesnervion.fjmarquez.pdam.Utiles.Utiles;

/**
 * Clase usada como adaptador para un RecyclerView, la cual mediante su constructor recibira un ArrayList de Series.
 * Dicho listado de series podra aumentar o disminuir de tamaño (Min. 1 - Max. 6) y permitira cambiar el numero de
 * Repeticiones de cada objeto Serie.
 */
public class AdaptadorSeriesDialogo extends RecyclerView.Adapter<AdaptadorSeriesDialogo.RVSeriesViewHolder> {

    public static ArrayList<Serie> listaSeries;

    public AdaptadorSeriesDialogo(ArrayList<Serie> listaSeries) {
        this.listaSeries = listaSeries;
    }

    @NonNull
    @Override
    public AdaptadorSeriesDialogo.RVSeriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_serie, parent, false);
        AdaptadorSeriesDialogo.RVSeriesViewHolder vh = new AdaptadorSeriesDialogo.RVSeriesViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorSeriesDialogo.RVSeriesViewHolder holder, int position) {

        Serie serieActual = this.listaSeries.get(position);

        holder.mTVNombreSerie.setText("Serie " + (position + 1));
        holder.mETRepeticionesSerie.getEditText().setText(""+serieActual.getRepeticiones());

    }

    @Override
    public int getItemCount() {
        return this.listaSeries.size();
    }

    public static class RVSeriesViewHolder extends RecyclerView.ViewHolder {

        private TextView mTVNombreSerie;
        private TextInputLayout mETRepeticionesSerie;


        public RVSeriesViewHolder(@NonNull View itemView) {
            super(itemView);

            mTVNombreSerie = itemView.findViewById(R.id.tvSerieNombre);
            mETRepeticionesSerie = itemView.findViewById(R.id.etReps);
            mETRepeticionesSerie.getEditText().addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //Compruebo que el EditText no esta vacio, si lo esta asigno a esa serie 0 repeticiones
                    if (!s.toString().isEmpty()){
                        //Compruebo si el numero introducido en el EditText es > o < que 0
                        if (Integer.parseInt(s.toString()) <= 0){
                            mETRepeticionesSerie.setError("Debe ser mayor que 0");
                        }else{
                            mETRepeticionesSerie.setError(null);
                        }
                        listaSeries.get(getAdapterPosition()).setRepeticiones(Integer.parseInt(s.toString()));
                    }else {
                        mETRepeticionesSerie.setError("No puede estar vacio");
                        listaSeries.get(getAdapterPosition()).setRepeticiones(0);
                    }
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }


    }

}