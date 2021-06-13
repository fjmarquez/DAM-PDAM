package iesnervion.fjmarquez.pdam.Utiles;


import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import iesnervion.fjmarquez.pdam.Adaptadores.AdaptadorSeriesDialogo;
import iesnervion.fjmarquez.pdam.Entidades.Ejercicio;
import iesnervion.fjmarquez.pdam.Entidades.Serie;
import iesnervion.fjmarquez.pdam.R;

import static android.content.ContentValues.TAG;

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

    public static ArrayList<Serie> seriesPorDefecto(){

        ArrayList<Serie> seriesDefecto = new ArrayList<>();

        for(int i = 0; i < 4; i++){
            seriesDefecto.add(new Serie());
        }

        return seriesDefecto;

    }

    public static String capitalizar(String cadena){

        String capitalizado = cadena.substring(0, 1).toUpperCase() + cadena.substring(1).toLowerCase();
        return capitalizado;

    }

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


    public static void pruebaFirebase(){
        ArrayList<Ejercicio> ejercicios = new ArrayList<>();

        Ejercicio e = new Ejercicio(
                null,
                "Curl Concentrado en Pronación con Barra",
                "Sentado, con los antebrazos apoyados sobre las rodillas o el banco y las palmas de las manos mirando hacia abajo, sujetar firmemente la barra con las manos a la anchura de los hombros y realizar el movimiento de subida y bajada de las muñecas, sin despegar los antebrazos del apoyo.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.PRINCIPIANTE,
                null,
                true,
                false,
                "1bhc6a84o8by4B92ewE6GJ_9tgovClZuu");

        Ejercicio e1 = new Ejercicio(
                null,
                "Curl Concentrado en Pronación con Mancuerna",
                "Sentado, con el antebrazo apoyado sobre la rodilla o el banco y la palma de la mano mirando hacia abajo, sujetar firmemente la mancuerna y realizar el movimiento de subida y bajada de la muñeca, sin despegar el antebrazo del apoyo.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.PRINCIPIANTE,
                null,
                true,
                false,
                "1dmrcJ71dBiZqW8fo1p18EEiLKvnhaQ6r");

        Ejercicio e2 = new Ejercicio(
                null,
                "Curl Concentrado en Supinación con Mancuerna",
                "Sentado en un banco o similar, abre las piernas e inclina el cuerpo un poco hacia delante. Coge una mancuerna con la palma hacia arriba (supinación) y apoya la parte de atrás del codo en el interior del muslo. Flexiona el codo hasta llevar la mancuerna a la altura del pecho y desciende nuevamente de forma controlada.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.PRINCIPIANTE,
                null,
                true,
                false,
                "1YEqHDERN4AVq1JchdPPPFsTXIexdx68n");

        Ejercicio e3 = new Ejercicio(
                null,
                "Fondos en banco",
                "Coloca las manos encima del banco, plataforma o silla a la anchura de los hombros. Los pies hacia adelante en el suelo y separados a la anchura de la cadera. Empieza el movimiento inspirando y flexionando los codos para descender la parte superior del cuerpo hacia el suelo, intentando llegar con los brazos a un ángulo recto de 90º. La espalda tiene que estar siempre cerca del banco. Mantén los codos lo más cerca posible del cuerpo y flexionados hacia atrás. De esta manera evitarás que se desplacen y trabajarás solo el tríceps. De lo contrario, también estarías involucrando los hombros en el ejercicio. Para regresar a la posición inicial exhala y mientras asciendes coloca los brazos rectos.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.PRINCIPIANTE,
                null,
                true,
                false,
                "1wDc2ThTZzu7gyrC6Cnya3-IA3SOJP-6n");

        Ejercicio e4 = new Ejercicio(
                null,
                "Curl Concentrado en Supinación en Polea Baja",
                "Agachado con las piernas abiertas, inclina el cuerpo hacia delante y coge la barra corta de la polea con las manos hacia arriba (supinación). Apoya la parte de atrás de los codos en el interior de los muslos. Flexiona los codos subiendo la barra hasta la cara. Desciende de forma controlada.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.INTERMEDIO,
                null,
                true,
                false,
                "1Nu-X8Qa11wjGI82jaauzx_XS1vbafLjs");

        Ejercicio e5 = new Ejercicio(
                null,
                "Curl en Banco Scott en Pronación con Polea",
                "Sentado en el banco con el pecho apoyado, coge la polea con agarre en pronación (palmas hacia abajo). Apoya bien la parte trasera superior de los brazos en el banco scott y flexiona los codos subiendo la barra. Desciende lentamente sin llegar a estirar del todo.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.INTERMEDIO,
                null,
                true,
                false,
                "1rXB1MVKNDbTPr4ym1hse0imWbT9p6srl");

        Ejercicio e6 = new Ejercicio(
                null,
                "Curl en Banco Scott en Supinación con Polea",
                "Sentado en el banco con el pecho apoyado, coge la polea con agarre en supinación (palmas hacia arriba). Apoya bien la parte trasera superior de los brazos en el banco scott y flexiona los codos subiendo la barra. Desciende lentamente sin llegar a estirar del todo.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.INTERMEDIO,
                null,
                true,
                false,
                "1763hL3dKoy3IbQiyQXwomoaO-TU4U6jx");

        Ejercicio e7 = new Ejercicio(
                null,
                "Curl en caída con Bandas",
                "De pie, engancha las bandas en algún lugar alto. Coge un extremo con cada mano, con los codos flexionados y las palmas hacia el cuerpo. Déjate caer suavemente estirando los brazos. Flexiona de nuevo tirando de los bíceps para volver arriba.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.INTERMEDIO,
                null,
                false,
                true,
                "1zqvPPqSi7xEKT2VQCyj6PD1fXhhC5abv");

        Ejercicio e8 = new Ejercicio(
                null,
                "Curl en Martillo Alterno con Mancuernas",
                "De pie con la espalda recta y las piernas ligeramente separadas, coge una mancuerna con cada mano y con la palma hacia el cuerpo (agarre neutro). Flexiona un codo llevando la mancuerna a la altura del pecho. Mientras desciende, sube la otra mancuerna.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.INTERMEDIO,
                null,
                true,
                false,
                "1oVMzFxA4SJuwJmpLG3bjtODdT8uVE7Pq");

        Ejercicio e9 = new Ejercicio(
                null,
                "Curl en Martillo con Barra",
                "De pie con las piernas ligeramente separadas y la espalda recta, coge la barra con agarre neutro o en martillo (barra con agarres especiales), por delante del cuerpo. Flexiona los codos hasta subir la barra hasta la altura del pecho y baja de forma controlada.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.INTERMEDIO,
                null,
                true,
                false,
                "1JoXB-vtG5KzNRQUTfA4fFphEZf9noOYo");

        Ejercicio e10 = new Ejercicio(
                null,
                "Curl en Martillo Cruzado con Mancuernas",
                "De pie con la espalda recta y las piernas ligeramente separadas, coge una mancuerna con cada mano y con la palma hacia el cuerpo (agarre neutro). Flexiona un codo llevando la mancuerna al pectoral opuesto. Mientras desciende, sube la otra mancuerna al pectoral contrario.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.INTERMEDIO,
                null,
                true,
                false,
                "1cF9GVa_oiWBmaIhllq7ExLSrixGS8gdr");

        Ejercicio e11 = new Ejercicio(
                null,
                "Curl en Martillo en Polea Baja",
                "De pie con las piernas ligeramente separadas y la espalda recta, coge la cuerda con las manos a la anchura de los hombros, flexiona los codos subiendo la cuerda hasta el pecho, con los codos pegados al cuerpo. Desciende de forma controlada.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.PRINCIPIANTE,
                null,
                true,
                false,
                "1UcrjSe0YGm7Tk0Zn_oHqnzRIeHk-c-E7");

        Ejercicio e12 = new Ejercicio(
                null,
                "Curl en Pronación con Barra Z",
                "De pie con la espalda recta y las piernas ligeramente separadas, coge la barra z con las manos a la anchura de los hombros, con las palmas hacia abajo (pronación). Sube la barra flexionando los codos y desciende de forma controlada.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.INTERMEDIO,
                null,
                true,
                false,
                "1KcJ45z2MzzVh6nYQqWOHOHb44A33u7UT");

        Ejercicio e13 = new Ejercicio(
                null,
                "Curl en Pronación en banco Scott con Barra",
                "Sentado en el banco Scott con el pecho apoyado, coge la barra con las manos a la anchura de los hombros, con las palmas hacia abajo (pronación). Sube la barra flexionando los codos y desciende de forma controlada, con la parte superior de los brazos siempre bien apoyada en el banco.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.PRINCIPIANTE,
                null,
                true,
                false,
                "1ZDj1rDGffSyN0OzfUgDVEutWf3v5QxmS");

        Ejercicio e14 = new Ejercicio(
                null,
                "Curl en Supinación Abierto Con Barra",
                "De pie con la espalda recta y las piernas ligeramente separadas, coge la barra con las manos a una anchura superior a la de los hombros, con las palmas hacia arriba (supinación). Sube la barra flexionando los codos y desciende de forma controlada.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.INTERMEDIO,
                null,
                true,
                false,
                "1uZylDJuJO2wH7SfnaDckwKM5PmdzcKDX");

        Ejercicio e15 = new Ejercicio(
                null,
                "Curl en Supinación Alterno con Bandas",
                "De pie con la espalda recta y las piernas ligeramente separadas, pisa la banda y coge un extremo con cada mano, con ellas a ambos lados del cuerpo en supinación (palmas hacia delante). Flexiona un codo hasta llegar con la mano aproximadamente a la altura de los hombros. Desciende de nuevo de forma controlada y procede con el otro brazo.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.PRINCIPIANTE,
                null,
                false,
                true,
                "1us0o_bcCp0HDnsZYRTdmRdtR0iQh7YCr");

        Ejercicio e16 = new Ejercicio(
                null,
                "Curl en Supinación Cerrado con Barra",
                "De pie con la espalda recta y las piernas ligeramente separadas, coge la barra con las manos a una anchura inferior a la de los hombros, con las palmas hacia arriba (supinación). Sube la barra flexionando los codos y desciende de forma controlada.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.INTERMEDIO,
                null,
                true,
                false,
                "1brk-xIX_RndV-bAVrNLnroU71EZ0Dbei");

        Ejercicio e17 = new Ejercicio(
                null,
                "Curl en Supinación con Barra",
                "De pie con la espalda recta y las piernas ligeramente separadas, coge la barra con las manos a la anchura de los hombros, con las palmas hacia arriba (supinación). Sube la barra flexionando los codos y desciende de forma controlada.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.PRINCIPIANTE,
                null,
                true,
                false,
                "1pidogbK2GSz77De5VjM6ItVsQP02F-OJ");

        Ejercicio e18 = new Ejercicio(
                null,
                "Extensión Concentrada en Polea Baja",
                "De pie y de espaldas a la polea, coge la barra de la polea con una mano en supinación (palma hacia arriba). Estira el brazo hacia arriba y con la otra mano agarra la parte trasera del codo para inmovilizar la parte superior del brazo. Flexiona el codo hacia atrás y vuelve a estirar de forma controlada.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.INTERMEDIO,
                null,
                true,
                false,
                "1dH32Mta7QFbCbPyWvWhyC0odaBYCUHDo");

        Ejercicio e19 = new Ejercicio(
                null,
                "Extensión en caída con Bandas",
                "De pie, engancha las bandas en algún lugar alto. De espaldas, coge un extremo con cada mano, con los codos flexionados y las palmas hacia el cuerpo. Déjate caer suavemente flexionando los brazos. Estira de nuevo tirando de los tríceps para volver arriba.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.INTERMEDIO,
                null,
                false,
                true,
                "1LcTf4F4KjiXV6nVuE4e8jNhuyhqWvimk");

        Ejercicio e20 = new Ejercicio(
                null,
                "Extensión Frontal Aislada en Polea Alta",
                "De pie con una pierna delante de otra y el cuerpo inclinado hacia delante de espaldas a la polea, coge la polea alta con la palma hacia delante y estira el brazo sobre la cabeza. Desciende la polea por detrás de la cabeza flexionando el codo de forma controlada.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.INTERMEDIO,
                null,
                true,
                false,
                "1gRCqEXPwW_PTMyLs8-GFei_5fRS3ZD4U");

        Ejercicio e21 = new Ejercicio(
                null,
                "Extensión hacia abajo con Bandas",
                "Coloca las bandas en un lugar alto sobre la cabeza. De pie con la espalda recta, coge un extremo con cada mano y estira los brazos hacia abajo. Aguanta y flexiona los codos de nuevo, llegando lo más arriba posible sin llegar a relajar el músculo ni descansar. Estira de nuevo con movimientos controlados.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.PRINCIPIANTE,
                null,
                false,
                true,
                "1sFz9Wa6BAp9f6MLxofmPEdQ4q0sjI3Ya");

        Ejercicio e22 = new Ejercicio(
                null,
                "Extensión Horizontal con Barra Z",
                "Tumbado hacia arriba en un banco, coge la barra z por el interior con las palmas hacia arriba, frente al pecho y estira los brazos. Flexiona los codos y desciende la barra hasta llegar detrás de la cabeza. Vuelve a la posición inicial con el movimiento controlado.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.PRINCIPIANTE,
                null,
                true,
                false,
                "1KPKsVwKFTmj3GlH8zrLx44Wbvak-B86y");

        Ejercicio e23 = new Ejercicio(
                null,
                "Extensión Vertical Aislada con Mancuerna",
                "Sentado con la espalda recta, coge la mancuerna con agarre neutro (palma hacia el interior) y estira el brazo sobre la cabeza. Desciende la mancuerna por detrás de la cabeza flexionando el codo 90º, de forma controlada. Estira volviendo a la posición inicial. Puedes utilizar la otra mano para ejercer de apoyo o tope, colocándola detrás del codo y ejerciendo presión para mantener el otro brazo completamente recto.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.INTERMEDIO,
                null,
                true,
                false,
                "13xxrY3VI2FfJZhnUbJI1ZRdpyIJL-377");

        Ejercicio e24 = new Ejercicio(
                null,
                "Extensión Vertical Aislada en Pronación en Polea Baja",
                "De pie con una pierna delante de otra y el cuerpo inclinado hacia delante de espaldas a la polea, coge la polea baja con la palma hacia abajo y estira el brazo sobre la cabeza. Desciende la polea por detrás de la cabeza flexionando el codo de forma controlada.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.INTERMEDIO,
                null,
                true,
                false,
                "1uyoE74Pw6CGjZNbB3K010vHnHrVzodn1");

        Ejercicio e25 = new Ejercicio(
                null,
                "Extensión Vertical Aislada en Supinación en Polea Alta",
                "De pie con las piernas ligeramente separadas y la espalda recta de cara a la polea, coge la polea alta con una mano y la palma hacia arriba y estira el brazo hacia abajo. Sube flexionando el codo de forma controlada sin llegar a relajar y vuelve a estirar.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.INTERMEDIO,
                null,
                true,
                false,
                "1QPVRx_7HR4MnNQ71x-xNzCW2LpANj8o_");

        Ejercicio e26 = new Ejercicio(
                null,
                "Extensión Vertical con Bandas",
                "De pie, coloca las bandas a una altura baja y de espaldas, coge un extremo con cada mano. Estira los brazos hacia arriba, con las palmas de las manos hacia delante. Desciende las manos por detrás de la cabeza flexionando los codos de forma controlada. Estira volviendo a la posición inicial.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.INTERMEDIO,
                null,
                false,
                true,
                "1punnjnl9UEPIk6U-5xnODobJJziFTO9x");

        Ejercicio e27 = new Ejercicio(
                null,
                "Extensión Vertical con Mancuerna",
                "Sentados en un banco o similar, cogemos una mancuerna con ambas manos y estiramos los brazos hacia arriba. Flexionamos los codos hacia atrás de forma que la mancuerna quede por detrás de la cabeza, sin dejar caer el peso. Estiramos de nuevo con un movimiento controlado. Debemos intentar mantener los codos cerrados, como si quisiéramos que se tocasen entre ellos.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.INTERMEDIO,
                null,
                true,
                false,
                "1_whpYa4JYpEzhs8ypA5OMcKh5Fv7-MAq");

        Ejercicio e28 = new Ejercicio(
                null,
                "Extensión Vertical en Pronación en Polea Alta",
                "De pie con las piernas ligeramente separadas y la espalda recta de cara a la polea, coge la barra en pronación (palmas hacia abajo) y estira los brazos hacia abajo. Sube flexionando los codos de forma controlada sin llegar a relajar y vuelve a estirar.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.PRINCIPIANTE,
                null,
                true,
                false,
                "1x_x-Jh89XkfDVZ4-EXoNRz7cYYwCF7C3");

        Ejercicio e29 = new Ejercicio(
                null,
                "Extensión Vertical Neutra en Polea Baja",
                "De pie y de espaldas a la polea, coge la cuerda de la polea baja y estira los brazos sobre la cabeza. Desciende la polea por detrás de la cabeza flexionando los codos de forma controlada.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.INTERMEDIO,
                null,
                true,
                false,
                "1HB7HZECXyZr91fVzcqAw2yV0ZU6RYRmp");

        Ejercicio e30 = new Ejercicio(
                null,
                "Extensiones en caída libre",
                "Colocamos una barra a una altura algo inferior a la de la cadera. Vamos hacia atrás con los pies, los juntamos y nos colgamos de la barra con los brazos hacia arriba, flexionados y con las manos a la anchura de los hombros. Estiramos los brazos subiendo el torso por encima de la barra. Bajamos de forma controlada.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.INTERMEDIO,
                null,
                true,
                false,
                "1jxoy7_G_dQ6O-H_v8s4MTU_ANIVUVOWL");

        Ejercicio e31 = new Ejercicio(
                null,
                "Flexiones Diamante",
                "Tumbado hacia abajo en el suelo, apoya las punteras de los pies y estira los brazos levantando el cuerpo del suelo (posición de plancha). Coloca las manos debajo del pecho y en forma de diamante. Haz que tus dedos índices y tus pulgares se toquen entre sí. De este modo formarás un diamante, o lo que se conoce también como pirámide. Baja el cuerpo hacia el suelo, y luego empuja nuevamente hacia arriba. Mantén la espalda recta y tensa los abdominales y el pecho. Asegúrate de mantener los dedos correctamente en la posición de diamante. Es un ejercicio que fuerza una rotación de hombros que puede resultar peligrosa.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.EXPERTO,
                null,
                false,
                false,
                "1T8PM3RqewM9y3rjTbOTPjJ3W2PNOtHcW");

        Ejercicio e32 = new Ejercicio(
                null,
                "Fondos Cerrados en Paralelas",
                "Ponemos las manos en las barras paralelas y estiramos los brazos, con la espalda recta y las piernas un poco recogidas. Bajamos de forma controlada sin inclinar el cuerpo hasta tener los codos flexionados a 90°. Volvemos a subir estirando bien arriba. Podemos utilizar un cinturón de lastre para añadir peso y dificultad al ejercicio.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.INTERMEDIO,
                null,
                true,
                false,
                "1TDR00eh3-gubwryz_swqT7aA54bNrXFg");

        Ejercicio e33 = new Ejercicio(
                null,
                "Press Francés con Bandas",
                "Tumbado hacia arriba en un banco plano, ancla las bandas a las patas del banco y coge un extremo con cada mano, con las palmas hacia arriba (agarre en pronación) a la altura del pecho y a la anchura de los hombros (una mano delante de cada pectoral). Estira los brazos tensando bien los tríceps, sin abrir los codos. Desciende las manos flexionando los codos a 90º. Asciende de nuevo de forma controlada.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.PRINCIPIANTE,
                null,
                false,
                true,
                "1pS0O7VRjgp2K2dIMHCJiaAOFePvB5o-3");

        Ejercicio e34 = new Ejercicio(
                null,
                "Press Francés con Barra Z",
                "Tumbado en el banco plano, sujeta la barra con los brazos extendidos por encima del pecho. Con las palmas hacia arriba, por los agarres más estrechos de la barra. Flexiona los codos y baja la barra hasta ponerla delante de la cara.Empuja la barra hacia arriba, hasta que los codos queden bloqueados. Vuelve a bajar lentamente hasta tener la barra en la cara. Puedes poner los pies sobre el suelo o sobre el banco para incrementar la dificultad.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.INTERMEDIO,
                null,
                true,
                false,
                "1KiTJeHH-R_m-OakxZXxLsKleki9-BVDO");

        Ejercicio e35 = new Ejercicio(
                null,
                "Press Francés con Barra",
                "Tumbado en el banco plano, sujeta la barra con los brazos extendidos por encima del pecho. Con las palmas hacia arriba, a una anchura inferior a la de los hombros. Flexiona los codos y baja la barra hasta ponerla delante de la cara. Empuja la barra hacia arriba, hasta que los codos queden bloqueados. Vuelve a bajar lentamente hasta tener la barra en la cara.",
                GrupoMuscular.BRAZO,
                DificultadEjercicio.INTERMEDIO,
                null,
                true,
                false,
                "143sqtx3QECYSJjNw7nhW6ZS8Kcme0XJ8");



        ejercicios.add(e);
        ejercicios.add(e1);
        ejercicios.add(e4);
        ejercicios.add(e3);
        ejercicios.add(e5);
        ejercicios.add(e6);
        ejercicios.add(e7);
        ejercicios.add(e8);
        ejercicios.add(e9);
        ejercicios.add(e10);
        ejercicios.add(e11);
        ejercicios.add(e12);
        ejercicios.add(e13);
        ejercicios.add(e14);
        ejercicios.add(e15);
        ejercicios.add(e16);
        ejercicios.add(e17);
        ejercicios.add(e18);
        ejercicios.add(e19);
        ejercicios.add(e20);
        ejercicios.add(e21);
        ejercicios.add(e22);
        ejercicios.add(e23);
        ejercicios.add(e24);
        ejercicios.add(e25);
        ejercicios.add(e26);
        ejercicios.add(e27);
        ejercicios.add(e28);
        ejercicios.add(e29);
        ejercicios.add(e30);
        ejercicios.add(e31);
        ejercicios.add(e32);
        ejercicios.add(e33);
        ejercicios.add(e34);
        ejercicios.add(e35);/*
        ejercicios.add(e36);
        ejercicios.add(e37);
        ejercicios.add(e38);
        ejercicios.add(e39);
        ejercicios.add(e40);
        ejercicios.add(e41);
        ejercicios.add(e42);
        ejercicios.add(e43);
        ejercicios.add(e44);
        ejercicios.add(e45);
        ejercicios.add(e46);
        ejercicios.add(e47);
        ejercicios.add(e48);
        ejercicios.add(e49);
        ejercicios.add(e50);
        ejercicios.add(e51);
        ejercicios.add(e52);
        ejercicios.add(e53);
        ejercicios.add(e54);
        ejercicios.add(e55);
        ejercicios.add(e56);*/


        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference eRef = db.collection("ejercicios").document();

        /*
        ArrayList<Ejercicio> leerEjercicios = new ArrayList<>();

        eRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (QueryDocumentSnapshot document: task.getResult()
                     ) {
                    Map<String, Object> e = document.getData();
                    Ejercicio ej = document.toObject(Ejercicio.class);
                    String id = document.getId();
                    ej.setUid(id);
                    leerEjercicios.add(ej);

                }
                int i = 0;
            }
        });*/


        for (int i = 0; i < ejercicios.size(); i++){
            eRef.set(ejercicios.get(i)).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void aVoid) {
                    int i = 0;
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    int i = 0;
                }
            });
            db.collection("ejercicios")
                    .add(ejercicios.get(i));
        }


    }

}
