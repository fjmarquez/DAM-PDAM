package iesnervion.fjmarquez.pdam.Adaptadores;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import iesnervion.fjmarquez.pdam.Entidades.Serie;
import iesnervion.fjmarquez.pdam.R;

public class AdaptadorSeriesRealizarEjercicio extends RecyclerView.Adapter<AdaptadorSeriesRealizarEjercicio.RVSeriesRealizarEjercicioViewHolder> {

    public static ArrayList<Serie> listaSeries;

    public AdaptadorSeriesRealizarEjercicio.OnItemClickListener mListener;

    public interface OnItemClickListener{
        void confirmarSerieListener(int position);
        void editarSerieListener(int position);
    }

    public void setOnItemClickListener(AdaptadorSeriesRealizarEjercicio.OnItemClickListener listener){
        mListener = listener;
    }

    public AdaptadorSeriesRealizarEjercicio(ArrayList<Serie> listaSeries) {
        this.listaSeries = listaSeries;
    }

    @NonNull
    @Override
    public AdaptadorSeriesRealizarEjercicio.RVSeriesRealizarEjercicioViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_series_realizar_ejercicio, parent, false);
        AdaptadorSeriesRealizarEjercicio.RVSeriesRealizarEjercicioViewHolder vh = new AdaptadorSeriesRealizarEjercicio.RVSeriesRealizarEjercicioViewHolder(v, mListener);

        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull AdaptadorSeriesRealizarEjercicio.RVSeriesRealizarEjercicioViewHolder holder, int position) {

        Serie serieActual = this.listaSeries.get(position);

        holder.mTVNombreSerieRealizarEjercicio.setText("Serie " + (position + 1));
        holder.mETRepeticionesSerieRealizarEjercicio.getEditText().setText(""+serieActual.getRepeticiones());
        holder.mTVRepeticionesSerieRealizarEjercicio.setText(""+serieActual.getRepeticiones()+ " Reps.");
        holder.mETKilosSerieRealizarEjercicio.getEditText().setText(""+serieActual.getPeso());
        holder.mTVKilosSerieRealizarEjercicio.setText(""+serieActual.getPeso()+" Kgs.");

    }

    @Override
    public int getItemCount() {
        return this.listaSeries.size();
    }

    public static class RVSeriesRealizarEjercicioViewHolder extends RecyclerView.ViewHolder {

        private TextView mTVNombreSerieRealizarEjercicio;
        private TextInputLayout mETRepeticionesSerieRealizarEjercicio;
        private TextView mTVRepeticionesSerieRealizarEjercicio;
        private TextInputLayout mETKilosSerieRealizarEjercicio;
        private TextView mTVKilosSerieRealizarEjercicio;
        private ImageButton mIBConfirmarSerieRealizarEjercicio;
        private ImageButton mIBEditarSerieRealizarEjercicio;


        public RVSeriesRealizarEjercicioViewHolder(@NonNull View itemView, final AdaptadorSeriesRealizarEjercicio.OnItemClickListener listener) {
            super(itemView);

            mTVNombreSerieRealizarEjercicio = itemView.findViewById(R.id.tvSerieNombreRealizarEjercicio);
            mETRepeticionesSerieRealizarEjercicio = itemView.findViewById(R.id.etRepsRealizarEjercicio);
            mTVRepeticionesSerieRealizarEjercicio = itemView.findViewById(R.id.tvRepsRealizarEjercicio);
            mETKilosSerieRealizarEjercicio = itemView.findViewById(R.id.etKgsRealizarEjercicio);
            mTVKilosSerieRealizarEjercicio = itemView.findViewById(R.id.tvKgsRealizarEjercicio);
            mIBConfirmarSerieRealizarEjercicio = itemView.findViewById(R.id.ibConfirmarSerieRealizarEjercicio);
            mIBEditarSerieRealizarEjercicio = itemView.findViewById(R.id.ibEditarSerieRealizarEjercicio);

            mIBConfirmarSerieRealizarEjercicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mETRepeticionesSerieRealizarEjercicio.setVisibility(View.INVISIBLE);
                    mTVRepeticionesSerieRealizarEjercicio.setVisibility(View.VISIBLE);
                    mETKilosSerieRealizarEjercicio.setVisibility(View.INVISIBLE);
                    mTVKilosSerieRealizarEjercicio.setVisibility(View.VISIBLE);
                    mIBConfirmarSerieRealizarEjercicio.setVisibility(View.GONE);
                    mIBEditarSerieRealizarEjercicio.setVisibility(View.VISIBLE);
                    if(listener != null){
                        int postition = getAdapterPosition();
                        if (postition != RecyclerView.NO_POSITION){
                            listener.confirmarSerieListener(postition);
                        }
                    }
                }
            });

            mIBEditarSerieRealizarEjercicio.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mETRepeticionesSerieRealizarEjercicio.setVisibility(View.VISIBLE);
                    mTVRepeticionesSerieRealizarEjercicio.setVisibility(View.GONE);
                    mETKilosSerieRealizarEjercicio.setVisibility(View.VISIBLE);
                    mTVKilosSerieRealizarEjercicio.setVisibility(View.GONE);
                    mIBConfirmarSerieRealizarEjercicio.setVisibility(View.VISIBLE);
                    mIBEditarSerieRealizarEjercicio.setVisibility(View.GONE);
                    if(listener != null){
                        int postition = getAdapterPosition();
                        if (postition != RecyclerView.NO_POSITION){
                            listener.editarSerieListener(postition);
                        }
                    }
                }
            });

            mETKilosSerieRealizarEjercicio.getEditText().addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //Compruebo que el EditText no esta vacio, si lo esta asigno a esa serie 0 kilos
                    if (!s.toString().isEmpty()){
                        //Compruebo si el numero introducido en el EditText es > o < que 0
                        if (Double.parseDouble(s.toString()) <= 0){
                            mETKilosSerieRealizarEjercicio.setError("Debe ser mayor que 0");
                        }else{
                            mETKilosSerieRealizarEjercicio.setError(null);
                        }
                        listaSeries.get(getAdapterPosition()).setPeso(Double.parseDouble(s.toString()));
                    }else {
                        mETKilosSerieRealizarEjercicio.setError("No puede estar vacio");
                        listaSeries.get(getAdapterPosition()).setPeso(0.0);
                    }
                    mTVKilosSerieRealizarEjercicio.setText("" + listaSeries.get(getAdapterPosition()).getPeso() + " Kgs.");
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

            mETRepeticionesSerieRealizarEjercicio.getEditText().addTextChangedListener(new TextWatcher() {

                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    //Compruebo que el EditText no esta vacio, si lo esta asigno a esa serie 0 repeticiones
                    if (!s.toString().isEmpty()){
                        //Compruebo si el numero introducido en el EditText es > o < que 0
                        if (Integer.parseInt(s.toString()) <= 0){
                            mETRepeticionesSerieRealizarEjercicio.setError("Debe ser mayor que 0");
                        }else{
                            mETRepeticionesSerieRealizarEjercicio.setError(null);
                        }
                        listaSeries.get(getAdapterPosition()).setRepeticiones(Integer.parseInt(s.toString()));
                    }else {
                        mETRepeticionesSerieRealizarEjercicio.setError("No puede estar vacio");
                        listaSeries.get(getAdapterPosition()).setRepeticiones(0);
                    }
                    mTVRepeticionesSerieRealizarEjercicio.setText(""+listaSeries.get(getAdapterPosition()).getRepeticiones() + " Reps.");
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });

        }


    }

}