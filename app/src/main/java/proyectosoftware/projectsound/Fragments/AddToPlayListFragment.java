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
import proyectosoftware.projectsound.Factorys.SongFactory;
import proyectosoftware.projectsound.R;
import proyectosoftware.projectsound.Tipos.Song;

/**
 * Fragmento para el contenido principal
 */
public class AddToPlayListFragment extends Fragment {

    public static final String ARG_PLAYLIST = "play";
    private final List<Song> porAniadir = new ArrayList<Song>();
    private final List<Song> porBorrar = new ArrayList<Song>();
    private Context contexto = getContext();
    private DbAdapter datos;

    public AddToPlayListFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_to_playlist, container, false);
        getActivity().setTitle("Añadir a playlist"); //titulo del activity
        //PONIENDO EL BOTON DE CONFIRMACION
        setHasOptionsMenu(true);
        datos = new DbAdapter(contexto);
        SongFactory factoriaCanciones = new SongFactory(datos);
        //SACAR TODAS LAS CANCIONES QUE ESTAN ANIADIDAS
        final List<Song> todasCanciones = factoriaCanciones.getAllSongs();
        //SACAR CANCIONES DE UN PLAYLIST
        final List<Song> cancionesPlaylist = factoriaCanciones.getAllFromPlaylist("Testeo"); //TODO cambiar por el playlist que sea
        TextView nombreLista = (TextView) view.findViewById(R.id.nombreLista);
        nombreLista.setText("Testeo"); //TODO cambiar por el bueno
        //LLENANDO LOS LISTVIEWS
        final List<String> todasCanciones_titulos = new ArrayList<String>();//Lista con los titulos
        for(int i=0;i<todasCanciones.size();i++) todasCanciones_titulos.add(todasCanciones.get(i).getTitle());
        ListView allSongs = (ListView) view.findViewById(R.id.cancionesSistema); //LisView izquierdo
        final ArrayAdapter<String> adaptador = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, todasCanciones_titulos);
        allSongs.setAdapter(adaptador);
        final List<String> cancionesPlaylist_titulos = new ArrayList<String>();
        for(int i=0;i<cancionesPlaylist.size();i++) cancionesPlaylist_titulos.add(cancionesPlaylist.get(i).getTitle());
        ListView playlistSongs = (ListView) view.findViewById(R.id.playlist); //LisView derecho
        final ArrayAdapter<String> lista = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, cancionesPlaylist_titulos);
        playlistSongs.setAdapter(lista);
        //CAPTURANDO ACCIONES EN EL LISTVIEW IZQUIERDO
        allSongs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String cancion = parent.getItemAtPosition(position).toString();
                if(!cancionesPlaylist_titulos.contains(cancion)){ //si la cancion no esta en el playlist de aniade
                    porAniadir.add(todasCanciones.get(position));//cancion por aniadir
                    cancionesPlaylist.add(todasCanciones.get(position)); //aniadimos al songfactory
                    cancionesPlaylist_titulos.add(cancion); //aniadimos a los titulos
                    todasCanciones.remove(position);//elimiamos del SongFactory
                    todasCanciones_titulos.remove(position);//eliminamos del de titulos
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
                /* Puede pasar que pulsemos en el lado izquierdo para aniadir,
                * luego nos arrepentimos y pulsemos en el derecho la misma cancion
                * para que vuelva a su sitio anterior.*/
                if(!todasCanciones_titulos.contains(cancion)){ //Si no la contiene la aniadimos
                    todasCanciones.add(cancionesPlaylist.get(position));
                    todasCanciones_titulos.add(cancion);
                    adaptador.notifyDataSetChanged();
                }
                porBorrar.add(cancionesPlaylist.get(position));//cancion por borrar
                cancionesPlaylist.remove(position);
                cancionesPlaylist_titulos.remove(position);
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
        try{
            /* Solo falta utilizar los metodos de la BBDD cuando este terminada*/
            for(int i=0;i<porAniadir.size();i++){
                datos.insertToPlaylist(porAniadir.get(i).getPath(),"Testeo");//TODO cambiar por la playlist conveniente
            }
            for(int i=0;i<porBorrar.size();i++){
                datos.deleteFromPlaylist(porBorrar.get(i).getPath(),"Testeo");//TODO cambiar por la playlist conveniente
            }
            Toast.makeText(getContext(),"Cambios guardados",Toast.LENGTH_LONG).show();
            getActivity().getSupportFragmentManager().popBackStack();
            return true;
        } catch (Exception e){
            Toast.makeText(getContext(),"Ha ocurrido un error",Toast.LENGTH_LONG).show();
            getActivity().getSupportFragmentManager().popBackStack();
            return false;
        }
    }
}
