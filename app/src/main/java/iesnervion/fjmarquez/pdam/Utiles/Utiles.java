package iesnervion.fjmarquez.pdam.Utiles;

import java.util.ArrayList;
import iesnervion.fjmarquez.pdam.Adaptadores.AdaptadorSeriesDialogo;
import iesnervion.fjmarquez.pdam.Entidades.Serie;
import iesnervion.fjmarquez.pdam.R;


/**
 * Clase que contiene funciones estaticas que se usan desde varias clases diferentes a modo de utilidades.
 */
public class Utiles {

    /**
     * Comprueba que todas las series de la lista de series tienen asignado como repeticiones un numero mayor que 0. Devuelve un booleano en funcion de si cumple la condicion o no.
     *
     * @return Boolean que tendra valor true si se cumple la condicion y false en caso contrario
     */
    public static boolean comprobarSeriesDialog(){

        boolean respuesta = true;

        for (Serie serie:
                AdaptadorSeriesDialogo.listaSeries){

            if (serie.getRepeticiones() == 0){

                respuesta = false;

            }

        }

        return respuesta;

    }

    /**
     * Devuelve un ArrayList de series por valores por defecto
     * @return ArrayList de series inicializadas.
     */
    public static ArrayList<Serie> seriesPorDefecto(){

        ArrayList<Serie> seriesDefecto = new ArrayList<>();

        for(int i = 0; i < 4; i++){
            seriesDefecto.add(new Serie());
        }

        return seriesDefecto;

    }

    /**
     * Capitaliza textos.
     * @param cadena String que recibe y procede a capitalizar.
     * @return String capitalizado.
     */
    public static String capitalizar(String cadena){

        String capitalizado = cadena.substring(0, 1).toUpperCase() + cadena.substring(1).toLowerCase();
        return capitalizado;

    }

    /**
     * Devuelve un color en funcion de un valor Enum recibido
     * @param dificultad Enum que indica la dificultad de un ejercicio.
     * @return Int que identifica un color, el cual corresponde al Enum recibido.
     */
    public static int colorDificultad (DificultadEjercicio dificultad){

        int color = R.color.negro;

        //Si dificultad es null en el ejercicio quiere decir que es un ejercicio personalizado
        if (dificultad != null){
            switch (dificultad){
                case PRINCIPIANTE:
                    color = R.color.principiante;
                    break;
                case INTERMEDIO:
                    color = R.color.intermedio;
                    break;
                case EXPERTO:
                    color = R.color.experto;
                    break;
            }
        }else {
            color = R.color.custom;
        }


        return color;

    }

}
