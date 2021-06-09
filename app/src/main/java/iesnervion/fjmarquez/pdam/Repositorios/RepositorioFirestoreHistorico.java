package iesnervion.fjmarquez.pdam.Repositorios;

import android.text.format.DateFormat;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import iesnervion.fjmarquez.pdam.Entidades.Dia;

public class RepositorioFirestoreHistorico {

    RepositorioFirestoreUsuario mRepoUsuario;
    FirebaseFirestore mFirestoreDB;
    CollectionReference mHistoricoColRef;

    public RepositorioFirestoreHistorico() {

        this.mRepoUsuario = new RepositorioFirestoreUsuario();
        this.mFirestoreDB = FirebaseFirestore.getInstance();
        this.mHistoricoColRef = mFirestoreDB.collection("historico");

    }

    public Task añadirOActualizarDiaHistoricoFirebase(Dia dia){

        DocumentReference mHistoricoDocRef = mHistoricoColRef.document(dia.getUid());

        return mHistoricoDocRef.set(dia);

    }

    public Task<QuerySnapshot> obtenerHistoricosUsuarioHoy(){

        String hoy = DateFormat.format("dd-MM-yyyy", Calendar.getInstance()).toString();
        //String usuario = mRepoUsuario.usuarioActual().getUid();

        Query query = mHistoricoColRef.whereEqualTo("usuario", mRepoUsuario.usuarioActual().getUid())
                                        .whereEqualTo("fecha", hoy);

        return query.get();

    }

    public Task<QuerySnapshot> obtenerHistoricosRangoFechas(ArrayList<String> dias){

        Query query = mHistoricoColRef.whereIn("fecha", dias);

        return query.get();

    }

    public Task<QuerySnapshot> obtenerListaHistoricoUsuarioActual(){

        Query query = mHistoricoColRef.whereEqualTo("usuario", mRepoUsuario.usuarioActual().getUid());

        return query.get();

    }

}
