package proyectosoftware.projectsound.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
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
        getActivity().setTitle("Add to playlist");
        //PONIENDO EL BOTON DE CONFIRMACION
        setHasOptionsMenu(true);
        Context contexto = getContext();
        DbAdapter datos = new DbAdapter(contexto);
        //SACAR TODAS LAS CANCIONES QUE ESTAN ANIADIDAS
        Cursor c = datos.getAllCancion();
        List<String> todasCanciones = new ArrayList<String>();
        if(c.moveToFirst()){
            for (int i=0; i<c.getCount();i++){ //Canciones de toda la BBDD
                todasCanciones.add(c.getString(c.getColumnIndex(DbAdapter.KEY_TITULO)));
                c.moveToNext();
            }
        }
        //SACAR CANCIONES DE UN PLAYLIST
        c = datos.getAllFromPlaylist(DbAdapter.DEFAULT_PLAYLIST_FAVORITOS);//En realidad va ARG_PLAYLIST
        List<String> cancionesPlaylist = new ArrayList<String>();
        if(c.moveToFirst()){
            for (int i=0;i<c.getCount();i++){
                cancionesPlaylist.add(c.getString(c.getColumnIndex(DbAdapter.KEY_TITULO)));
                c.moveToNext();
            }
        }
        c.close();//cerramos el cursor
        TextView nombreLista = (TextView) view.findViewById(R.id.nombreLista);
        nombreLista.setText(DbAdapter.DEFAULT_PLAYLIST_FAVORITOS);
        //LLENANDO LOS LISTVIEWS
        ListView allSongs = (ListView) view.findViewById(R.id.cancionesSistema); //LisView izquierdo
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, todasCanciones);
        allSongs.setAdapter(adaptador);
        ListView playlistSongs = (ListView) view.findViewById(R.id.playlist); //LisView derecho
        adaptador = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, cancionesPlaylist);
        playlistSongs.setAdapter(adaptador);
        return view;
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater){
        getActivity().getMenuInflater().inflate(R.menu.done,menu);
    }
}
