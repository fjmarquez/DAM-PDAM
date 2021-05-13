package iesnervion.fjmarquez.pdam.Repositorios;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Map;

import iesnervion.fjmarquez.pdam.Entidades.Ejercicio;

public class RepositorioFirestoreEjercicios {

    private FirebaseFirestore mDbFirestore;
    private CollectionReference mERef;

    public RepositorioFirestoreEjercicios() {

        mDbFirestore = FirebaseFirestore.getInstance();
        mERef = mDbFirestore.collection("ejercicios");

    }

    public Task<QuerySnapshot> obtenerEjerciciosFirestore(){

        ArrayList<Ejercicio> mListaEjercicios = new ArrayList<>();

        return mERef.get();

    }

}
