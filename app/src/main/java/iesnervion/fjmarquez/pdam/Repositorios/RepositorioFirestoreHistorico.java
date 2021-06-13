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

    /* ATRIBUTOS */

    RepositorioFirestoreUsuario mRepoUsuario;
    FirebaseFirestore mFirestoreDB;
    CollectionReference mHistoricoColRef;

    /* CONSTRUCTOR */

    public RepositorioFirestoreHistorico() {

        this.mRepoUsuario = new RepositorioFirestoreUsuario();
        this.mFirestoreDB = FirebaseFirestore.getInstance();
        this.mHistoricoColRef = mFirestoreDB.collection("historico");

    }

    /**
     * Añade o actualiza un dia como historico en Firestore.
     *
     * @param dia Dia a añadir o actualizar en Firestore
     * @return Devuelve una tarea, mediante la cual podra realizar una accion cuando esta sea completada (puede finalizar
     * correctamente o no).
     */
    public Task añadirOActualizarDiaHistoricoFirebase(Dia dia){

        DocumentReference mHistoricoDocRef = mHistoricoColRef.document(dia.getUid());

        return mHistoricoDocRef.set(dia);

    }

    /**
     * Obtiene el historico correspondiente al dia de hoy.
     *
     * @return Devuelve una tarea, mediante la cual podra realizar una accion cuando esta sea completada (puede finalizar
     * correctamente o no).
     */
    public Task<QuerySnapshot> obtenerHistoricosUsuarioHoy(){

        String hoy = DateFormat.format("dd-MM-yyyy", Calendar.getInstance()).toString();

        Query query = mHistoricoColRef.whereEqualTo("usuario", mRepoUsuario.usuarioActual().getUid()) //usuario igual a uid del usuario actual
                                        .whereEqualTo("fecha", hoy); //fecha igual a la fecha de hoy

        return query.get();

    }

    /**
     * Obtiene todos los historicos cuya fecha se encuentre en el rango de fechas recibidas.
     *
     * @param dias ArrayList<String> con todos los dias del rango
     * @return Devuelve una tarea, mediante la cual podra realizar una accion cuando esta sea completada (puede finalizar
     * correctamente o no).
     */
    public Task<QuerySnapshot> obtenerHistoricosRangoFechas(List<String> dias){

        Query query = mHistoricoColRef.whereIn("fecha", dias)
                                        .whereEqualTo("usuario", mRepoUsuario.usuarioActual().getUid());

        return query.get();

    }

    /**
     * Obtiene todos los historicos cuya fecha se encuentre en el rango de fechas recibidas.
     *
     * @return Devuelve una tarea, mediante la cual podra realizar una accion cuando esta sea completada (puede finalizar
     * correctamente o no).
     */
    public Task<QuerySnapshot> obtenerListaHistoricoUsuarioActual(){

        Query query = mHistoricoColRef.whereEqualTo("usuario", mRepoUsuario.usuarioActual().getUid());

        return query.get();

    }

}
