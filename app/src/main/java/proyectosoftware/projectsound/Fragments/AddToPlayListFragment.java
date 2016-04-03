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
                Log.d("Olia",c.getString(c.getColumnIndex(DbAdapter.KEY_TITULO)));
                //todasCanciones.add(c.getString(1));
                c.moveToNext();
            }
        }
        //SACAR CANCIONES DE UN PLAYLIST, todo rulando hasta este punto
        c = datos.getAllFromPlaylist(getArguments().getString("Calle 13"));//En realidad va ARG_PLAYLIST
        List<String> cancionesPlaylist = new ArrayList<String>();
        String [] caca = c.getColumnNames();
        for(int i=0;i<caca.length;i++){
            Log.d("Playlist",caca[i]);
        }

        TextView nombreLista = (TextView) view.findViewById(R.id.nombreLista);
        nombreLista.setText("Favoritos");

        /*//Canciones
        List<String> canciones = new ArrayList<String>();
        canciones.add("Whispers in my head - Onlap");
        canciones.add("X Gon' Give it to ya - DMX");
        canciones.add("La ramona - Fernando Esteso");
        canciones.add("El perdon - Enrique Iglesias, Nicky Jam");
        ListView songs = (ListView) view.findViewById(R.id.cancionesSistema); //LisView izquierdo
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, canciones);
        songs.setAdapter(adaptador);
        //Nombre de la columna de la lista
        TextView nombreLista = (TextView) view.findViewById(R.id.nombreLista);
        nombreLista.setText("San pepe 2016");
        //Lista
        ListView lists = (ListView) view.findViewById(R.id.userPlaylists);
        List<String> playLists = new ArrayList<String>();
        playLists.add("Whispers in my head - Onlap");
        playLists.add("La ramona - Fernando Esteso");
        playLists.add("El perdon - Enrique Iglesias, Nicky Jam");
        ArrayAdapter<String> listas = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1,playLists);
        lists.setAdapter(listas);*/
        return view;

    }
}
