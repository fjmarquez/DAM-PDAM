package iesnervion.fjmarquez.pdam.Repositorios;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import iesnervion.fjmarquez.pdam.Entidades.Rutina;
import iesnervion.fjmarquez.pdam.Entidades.Usuario;

public class RepositorioFirebaseRutinas {

    RepositorioFirebaseUsuario mRepoUsuario;
    FirebaseFirestore mFirestoreDB;
    CollectionReference mRutinaColRef;

    public RepositorioFirebaseRutinas() {

        this.mRepoUsuario = new RepositorioFirebaseUsuario();
        this.mFirestoreDB = FirebaseFirestore.getInstance();
        this.mRutinaColRef = mFirestoreDB.collection("rutinas");

    }

    /**
     * Añade la rutina a la base de datos de Firestore una vez el usuario la especifica.
     * @param rutina Objeto Rutina con la informacion sobre la rutina del usuario actual.
     * @return Devuelve una tarea, mediante la cual podra realizar una accion cuando esta sea completada (puede finalizar
     * correctamente o no).
     */
    public Task añadirRutinaFirestore(Rutina rutina){

        DocumentReference mRutinaDocRef = mRutinaColRef.document(mRepoUsuario.usuarioActual().getUid());

        return mRutinaDocRef.set(rutina);

    }

    public Task<DocumentSnapshot> obtenerRutinaActualUsuario(){

        DocumentReference mRutinaDocRef = mRutinaColRef.document(mRepoUsuario.usuarioActual().getUid());

        return mRutinaDocRef.get();

    }

}
