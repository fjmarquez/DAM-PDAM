package iesnervion.fjmarquez.pdam.Adaptadores;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

import iesnervion.fjmarquez.pdam.Entidades.Rutina;
import iesnervion.fjmarquez.pdam.R;

/**
 * Clase usada como adaptador para un AutoCompleteTextView, la cual mediante su constructor recibira un ArrayList de Rutinas.
 * A diferencia de AdaptadorListaRutinas este adaptador es especifico para usar en un AutoCompleteTextView.
 */
public class AdaptadorListaRutinasPerfil extends ArrayAdapter<Rutina> {

    private ArrayList<Rutina> listaRutinas;

    public AdaptadorListaRutinasPerfil(@NonNull Context context, @NonNull ArrayList<Rutina> rutinas) {
        super(context, 0, rutinas);
        listaRutinas = new ArrayList<>(rutinas);
    }

    @NonNull
    @Override
    public Filter getFilter() {
        return rutinasFiltros;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.item_rutina, parent, false
            );
        }

        TextView nombreRutina = convertView.findViewById(R.id.tvNombreRutina);

        Rutina rutina = getItem(position);

        if(rutina != null){
            //Nombre de la rutina
            nombreRutina.setText(rutina.getNombre());
        }

        return convertView;
    }

    private Filter rutinasFiltros = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            FilterResults results = new FilterResults();
            ArrayList<Rutina> suggestions = new ArrayList<>();

            if (constraint == null || constraint.length() == 0){
                suggestions.addAll(listaRutinas);
            } else if (constraint.length() > 2) {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for (Rutina r : listaRutinas){
                    if (r.getNombre().toLowerCase().contains(filterPattern)){
                        suggestions.add(r);
                    }
                }
            }

            results.values = suggestions;
            results.count = suggestions.size();

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            clear();
            addAll((ArrayList) results.values);
            notifyDataSetChanged();

        }

        @Override
        public CharSequence convertResultToString(Object resultValue) {
            return ((Rutina) resultValue).getNombre();
        }
    };
}