package proyectosoftware.projectsound.Fragments;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import proyectosoftware.projectsound.CustomAdapters.DbAdapter;
import proyectosoftware.projectsound.CustomAdapters.PlaylistAdapter;
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

    public PlaylistsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_playlist, container, false);
        getActivity().setTitle("Playlists");
        ArrayList<Playlist> datos_playlist = new ArrayList<Playlist>();

        //Referencia al boton para anadir nuevas playlist
        FloatingActionButton btn_anadir = (FloatingActionButton) view.findViewById(R.id.btn_anadir_playlist);

        registerForContextMenu(btn_anadir);

        btn_anadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               v.showContextMenu();
            }
        });


        //Ejemplos añadidos a la playlist HAY QUE MODIFICADAR LOS ULTIMOS CAMPOS !!!!
        datos_playlist.add(new Playlist(R.drawable.ic_todas_playlist_256x256, DbAdapter.DEFAULT_PLAYLIST_TODAS, "0 pistas", "0 min"));
        datos_playlist.add(new Playlist(R.drawable.ic_favs_playlist_256x256, DbAdapter.DEFAULT_PLAYLIST_FAVORITOS, "0 pistas", "0 min"));


                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            
        lista = (ListView) view.findViewById(R.id.listview_playlist);
        lista.setAdapter(new PlaylistAdapter(view.getContext(), R.layout.entrada_playlist, datos_playlist) {


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

    //COPIADO NO FINAL
    @Override
    public void onCreateContextMenu(ContextMenu menu, View view,ContextMenuInfo menuInfo)
    {
        super.onCreateContextMenu(menu, view, menuInfo);
        CreateMenu(menu);
    }

    //COPIADO NO FINAL
    private void CreateMenu(Menu menu)
    {
        MenuItem mnu1 = menu.add(0, 0, 0, "Item 1");
        {
            mnu1.setAlphabeticShortcut('a');

        }
        MenuItem mnu2 = menu.add(0, 1, 1, "Item 2");
        {
            mnu2.setAlphabeticShortcut('b');

        }
    }


}