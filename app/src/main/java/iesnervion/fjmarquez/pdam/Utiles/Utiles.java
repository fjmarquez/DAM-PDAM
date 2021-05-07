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

import iesnervion.fjmarquez.pdam.Entidades.Ejercicio;

import static android.content.ContentValues.TAG;

public class Utiles {

    public static String capitalizar(String cadena){

        String capitalizado = cadena.substring(0, 1).toUpperCase() + cadena.substring(1).toLowerCase();
        return capitalizado;

    }


    public static void pruebaFirebase(){
        ArrayList<Ejercicio> ejercicios = new ArrayList<>();



        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference eRef = db.collection("ejercicios")/*.document()*/;

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
        });

        /*
        for (int i = 0; i < ejercicios.size(); i++){
            /*eRef.set(ejercicios.get(i)).addOnSuccessListener(new OnSuccessListener<Void>() {
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
*/

    }

}
