package iesnervion.fjmarquez.pdam.Repositorios;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

import iesnervion.fjmarquez.pdam.Entidades.Ejercicio;

/**
 * Clase mediante la cual se obtiene y persiste la informacion relativa a ejercicios.
 */
public class RepositorioFirestoreEjercicios {

    /* ATRIBUTOS */

    private FirebaseFirestore mDbFirestore;
    private CollectionReference mEjerciciosColRef;

    /* CONSTRUCTOR */
    public RepositorioFirestoreEjercicios() {

        mDbFirestore = FirebaseFirestore.getInstance();
        mEjerciciosColRef = mDbFirestore.collection("ejercicios");

    }

    /**
     * Obtiene todos los registros dentro de la coleccion de Ejercicios que tenemos en Firestore.
     *
     * @return Devuelve una tarea, mediante la cual podra realizar una accion cuando esta sea completada (puede finalizar
     * correctamente o no).
     */
    public Task<QuerySnapshot> obtenerEjerciciosFirestore(){

        ArrayList<Ejercicio> mListaEjercicios = new ArrayList<>();

        return mEjerciciosColRef.get();

    }

    /**
     * Obtiene un unico ejercicio cuyo UID coincida con el recibidor por parametros.
     *
     * @param EjercicioUID String que contiene el UID de un ejercicio.
     * @return Devuelve una tarea, mediante la cual podra realizar una accion cuando esta sea completada (puede finalizar
     * correctamente o no).
     */
    public Task<DocumentSnapshot> obtenerEjercicio(String EjercicioUID){

        DocumentReference mEjercicioDocRef = mEjerciciosColRef.document(EjercicioUID);

        return mEjercicioDocRef.get();

    }

}
