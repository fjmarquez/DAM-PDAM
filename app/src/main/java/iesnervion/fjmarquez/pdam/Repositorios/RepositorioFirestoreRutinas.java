package iesnervion.fjmarquez.pdam.Repositorios;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import iesnervion.fjmarquez.pdam.Entidades.Rutina;

public class RepositorioFirestoreRutinas {

    RepositorioFirestoreUsuario mRepoUsuario;
    FirebaseFirestore mFirestoreDB;
    CollectionReference mRutinaColRef;

    public RepositorioFirestoreRutinas() {

        this.mRepoUsuario = new RepositorioFirestoreUsuario();
        this.mFirestoreDB = FirebaseFirestore.getInstance();
        this.mRutinaColRef = mFirestoreDB.collection("rutinas");

    }

    public Task<DocumentSnapshot> comprobarSiExisteRutinaUsuarioActual(){

        DocumentReference mRutinaDocRef = mRutinaColRef.document(mRepoUsuario.usuarioActual().getUid());

        return mRutinaDocRef.get();

    }

    /**
     * Añade la rutina a la base de datos de Firestore una vez el usuario la especifica.
     * @param rutina Objeto Rutina con la informacion sobre la rutina del usuario actual.
     * @return Devuelve una tarea, mediante la cual podra realizar una accion cuando esta sea completada (puede finalizar
     * correctamente o no).
     */
    public Task añadirRutinaFirestore(Rutina rutina){

        DocumentReference mRutinaDocRef;

        if (rutina.getUid() != null){
            mRutinaDocRef = mRutinaColRef.document(rutina.getUid());
        }else{
            mRutinaDocRef = mRutinaColRef.document(/*mRepoUsuario.usuarioActual().getUid()*/);
        }

        return mRutinaDocRef.set(rutina);

    }

    public Task<DocumentSnapshot> obtenerRutinaActualUsuario(String uid){

        DocumentReference mRutinaDocRef = mRutinaColRef.document(uid);

        return mRutinaDocRef.get();

    }

    public Task<QuerySnapshot> obtenerListaRutinasUsuario(){

        Query query = mRutinaColRef.whereEqualTo("usuario", mRepoUsuario.usuarioActual().getUid());

        return query.get();

    }

    public Task<Void> eliminarRutinaFirestore(Rutina rutina){

        DocumentReference mRutinaDocRef = mRutinaColRef.document(rutina.getUid());

        return mRutinaDocRef.delete();

    }


}
