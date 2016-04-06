package proyectosoftware.projectsound.Fragments;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import proyectosoftware.projectsound.CustomAdapters.DbAdapter;
import proyectosoftware.projectsound.R;

/**
 * Fragmento para el contenido principal
 */
public class AddToPlayListFragment extends Fragment {

    public static final String ARG_PLAYLIST = "play";
    private final List<String> porAniadir = new ArrayList<>();
    private final List<String> porBorrar = new ArrayList<>();
    private Context contexto = getContext();
    private DbAdapter datos;

    public AddToPlayListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_to_playlist, container, false);
        getActivity().setTitle("Add to playlist"); //titulo del activity
        //PONIENDO EL BOTON DE CONFIRMACION
        setHasOptionsMenu(true);
        datos = new DbAdapter(contexto);
        //SACAR TODAS LAS CANCIONES QUE ESTAN ANIADIDAS
        Cursor c = datos.getAllCancion();
        final List<String> todasCanciones = new ArrayList<String>();
        if(c.moveToFirst()){
            for (int i=0; i<c.getCount();i++){ //Canciones de toda la BBDD
                todasCanciones.add(c.getString(c.getColumnIndex(DbAdapter.KEY_TITULO)));
                c.moveToNext();
            }
        }
        //SACAR CANCIONES DE UN PLAYLIST
        c = datos.getAllFromPlaylist(DbAdapter.DEFAULT_PLAYLIST_FAVORITOS); //TODO cambiar por el playlist bueno
        final List<String> cancionesPlaylist = new ArrayList<String>();
        if(c.moveToFirst()){
            for (int i=0;i<c.getCount();i++){
                cancionesPlaylist.add(c.getString(c.getColumnIndex(DbAdapter.KEY_TITULO)));
                c.moveToNext();
            }
        }
        c.close();//cerramos el cursor
        TextView nombreLista = (TextView) view.findViewById(R.id.nombreLista);
        nombreLista.setText(DbAdapter.DEFAULT_PLAYLIST_FAVORITOS); //TODO cambiar por el bueno
        //LLENANDO LOS LISTVIEWS
        ListView allSongs = (ListView) view.findViewById(R.id.cancionesSistema); //LisView izquierdo
        final ArrayAdapter<String> adaptador = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, todasCanciones);
        allSongs.setAdapter(adaptador);
        ListView playlistSongs = (ListView) view.findViewById(R.id.playlist); //LisView derecho
        final ArrayAdapter<String> lista = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, cancionesPlaylist);
        playlistSongs.setAdapter(lista);
        //CAPTURANDO ACCIONES EN EL LISTVIEW IZQUIERDO
        allSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cancion = parent.getItemAtPosition(position).toString();
                if(!cancionesPlaylist.contains(cancion)){ //si la cancion no esta en el playlist de aniade
                    porAniadir.add(parent.getItemAtPosition(position).toString());//cancion por aniadir
                    todasCanciones.remove(position);
                    cancionesPlaylist.add(cancion);
                    adaptador.notifyDataSetChanged();
                    lista.notifyDataSetChanged();
                }
            }
        });
        //CAPTURANDO ACCIONES EN EL LISTVIEW DERECHO
        playlistSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cancion = parent.getItemAtPosition(position).toString();
                porBorrar.add(parent.getItemAtPosition(position).toString());//cancion por borrar
                cancionesPlaylist.remove(position);
                /* Puede pasar que pulsemos en el lado izquierdo para aniadir,
                * luego nos arrepentimos y pulsemos en el derecho la misma cancion
                * para que vuelva a su sitio anterior.*/
                if(!todasCanciones.contains(cancion)){ //Si no la contiene la aniadimos
                    todasCanciones.add(cancion);
                    adaptador.notifyDataSetChanged();
                }
                lista.notifyDataSetChanged();
            }
        });
        //CAPTURANDO ACCIONES EN EL LISTVIEW DERECHO
        return view;
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater){
        getActivity().getMenuInflater().inflate(R.menu.done,menu);
    }

    /* Metodo al que se llama cuando se pulsa el boton de confirmar*/
    @Override
    public boolean onOptionsItemSelected (MenuItem menu){
        /* Solo falta utilizar los metodos de la BBDD cuando este terminada*/
        for(int i=0;i<porAniadir.size();i++){
            //TODO aniadir elemento a la playlist
        }
        for(int i=0;i<porBorrar.size();i++){
            //TODO quitar elemento de la playlist
        }
        Toast.makeText(getContext(),"Cambios guardados",Toast.LENGTH_LONG).show();
        getActivity().getSupportFragmentManager().popBackStack();
        return true;
    }
}
