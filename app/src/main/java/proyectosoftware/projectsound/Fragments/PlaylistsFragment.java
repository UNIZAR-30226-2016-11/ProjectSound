package proyectosoftware.projectsound.Fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

import proyectosoftware.projectsound.CustomAdapters.DbAdapter;
import proyectosoftware.projectsound.CustomAdapters.PlaylistAdapter;
import proyectosoftware.projectsound.Factorys.PlaylistFactory;
import proyectosoftware.projectsound.R;
import proyectosoftware.projectsound.Tipos.Playlist;


/**
 * TODO Modificar comportamiento boton flotante
 * TODO Modificar pulsacion larga en cada playlist (para editar/borrar)
 * TODO Conectar con BBDD
 */

/**
 * Fragmento para el contenido principal
 */
public class PlaylistsFragment extends Fragment {

    private ListView lista;
    private DbAdapter mdb;
    private Context contexto = getContext();
    private List<Playlist> datos_playlist = new ArrayList<Playlist>();
    private static String selectedPlaylist = null;


    public PlaylistsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_playlist, container, false);
        getActivity().setTitle("Playlists");


        //Ejemplos añadidos a la playlist HAY QUE MODIFICADAR LOS ULTIMOS CAMPOS !!!!
        //datos_playlist.add(new Playlist(R.drawable.ic_todas_playlist_256x256, DbAdapter.DEFAULT_PLAYLIST_TODAS, DbAdapter.KEY_NUM_CANCIONES, DbAdapter.KEY_DURACION_PLAYLIST));
        //datos_playlist.add(new Playlist(R.drawable.ic_favs_playlist_256x256, DbAdapter.DEFAULT_PLAYLIST_FAVORITOS, "0 pistas", "0 min"));


            //MODIFICAR
        //Referencia al boton para anadir nuevas playlist
        FloatingActionButton btn_anadir = (FloatingActionButton) view.findViewById(R.id.btn_anadir_playlist);


        //FIN MODIFICAR


        //Montar listado de playlist
        montarListView(view);

        registerForContextMenu(lista);

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
                Playlist elegido = (Playlist) pariente.getItemAtPosition(posicion);
                Bundle args = new Bundle();
                args.putString(SongsFragment.ARG_PLAYLIST, elegido.getTituloPlaylist());
                SongsFragment f = new SongsFragment();
                f.setArguments(args);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, f).addToBackStack(null).commit();

            }
        });

        return view;
    }

    private void montarListView(View view){

        //Obtener todos los datos sobre las playlist existentes
        mdb = new DbAdapter(contexto);
        PlaylistFactory plf = new PlaylistFactory(mdb);
        datos_playlist = plf.getAllPlaylist();
        //Creacion de lista en el listview
        lista = (ListView) view.findViewById(R.id.listview_playlist);
        lista.setAdapter(new PlaylistAdapter(view.getContext(), R.layout.entrada_playlist, (ArrayList<?>) datos_playlist) {
            /*
            * COMPRUEBA QUE EXISTEN LOS ITEMS CORRECTOS ANTES DE INSERTAR CUALQUIER ELEMENTO
            * Una vez realizada la comprobacion inserta la informacion
            */
            @Override
            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {

                    ImageView imagen_playlist = (ImageView) view.findViewById(R.id.imgeView_lista_icono);
                    if (imagen_playlist != null)
                        imagen_playlist.setImageResource(((Playlist) entrada).getIdImagenPlaylist());

                    TextView titulo_playlist = (TextView) view.findViewById(R.id.textView_lista_titulo);
                    if (titulo_playlist != null)
                        titulo_playlist.setText(((Playlist) entrada).getTituloPlaylist());

                    TextView numCanciones_playlist = (TextView) view.findViewById(R.id.textView_lista_num_canciones);
                    if (numCanciones_playlist != null)
                        numCanciones_playlist.setText(((Playlist) entrada).getNumCancionesPlaylist());

                    TextView duracion_playlist = (TextView) view.findViewById(R.id.textView_lista_duracion);
                    if (duracion_playlist != null) {
                        duracion_playlist.setText(((Playlist) entrada).getDuracionPlaylist());
                    }

                }
            }

        });
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, view, menuInfo);
        menu.setHeaderTitle("Opciones");
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        //Recoge el nombre de la playlist seleccionada por el usuario
        selectedPlaylist = (datos_playlist.get((int)info.id).getTituloPlaylist());
        //Descarta las playlist por defecto
        if(!selectedPlaylist.equals("Todas") && !selectedPlaylist.equals("Favoritos")){
            CreateMenu(menu);
        }
        //montarListView(view);

    }

    //TODO la funcionalidad de borrar no debo hacerla aqui
    private void CreateMenu(Menu menu)
    {
        MenuItem mEdit = menu.add(0, 0, 0, "Editar nombre");
        {
            mEdit.setAlphabeticShortcut('a');

        }
        MenuItem mDelete = menu.add(0, 1, 1, "Eliminar");
        {
            //if(!mdb.isOpen()){
              //  mdb.open();
            //}
            //mdb.deletePlaylist(selectedPlaylist);
        }
    }


}