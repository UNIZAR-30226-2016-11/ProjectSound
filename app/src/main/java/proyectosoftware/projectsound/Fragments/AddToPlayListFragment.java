package proyectosoftware.projectsound.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import proyectosoftware.projectsound.CustomAdapters.DbAdapter;
import proyectosoftware.projectsound.R;

/**
 * Fragmento para el contenido principal
 */
public class AddToPlayListFragment extends Fragment {

    public static final String ARG_PLAYLIST = "play";

    public AddToPlayListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_to_playlist, container, false);
        Context contexto = getContext();
        DbAdapter datos = new DbAdapter(contexto);
        Cursor c = datos.getAllCancion();
        //Obtener todas las canciones e introducirlas en un ArrayList
        List<String> todasCanciones = new ArrayList<String>();
        //ruta, titulo, reporducciones, duracion, favorita
        if(c.moveToFirst()){
            for (int i=0; i<c.getCount();i++){ //Canciones de toda la BBDD
                todasCanciones.add(c.getString(c.getColumnIndex(DbAdapter.KEY_TITULO)));
                c.moveToNext();
            }
        }
        //SACAR CANCIONES DE UN PLAYLIST, todo rulando hasta este punto
        c = datos.getAllFromPlaylist(DbAdapter.DEFAULT_PLAYLIST_FAVORITOS);//En realidad va ARG_PLAYLIST
        Log.d("jijiji","he llegado");
        List<String> cancionesPlaylist = new ArrayList<String>();
        if(c.moveToFirst()){
            for (int i=0;i<c.getCount();i++){
                cancionesPlaylist.add(c.getString(c.getColumnIndex(DbAdapter.KEY_TITULO)));
                c.moveToNext();
            }
        }
        TextView nombreLista = (TextView) view.findViewById(R.id.nombreLista);
        nombreLista.setText("Favoritos");
        return view;

    }
}
