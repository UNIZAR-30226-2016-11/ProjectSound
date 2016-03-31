package proyectosoftware.projectsound.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import proyectosoftware.projectsound.CustomAdapters.DbAdapter;
import proyectosoftware.projectsound.R;
import proyectosoftware.projectsound.Tipos.Playlist;
import proyectosoftware.projectsound.Tipos.Song;
import proyectosoftware.projectsound.CustomAdapters.SongAdapter;

/**
 * Fragmento para el contenido principal
 */
public class SongsFragment extends Fragment {

    public static final String ARG_PLAYLIST = "play";
    private DbAdapter mDb;
    private static SongAdapter adaptador;
    private static ArrayList<Song> canciones;
    private final int MENU_CONTEXT_DELETE_FROM_PLAYLIST= Menu.FIRST;
    private final int MENU_CONTEXT_EDIT_PLAYLIST= Menu.FIRST+1;
    private final int MENU_CONTEXT_DELETE_FROM_APP= Menu.FIRST+2;
    private static final String TAB_TITULO = "Título";
    private static final String TAB_DURACION = "Duración";
    private static final String TAB_REPRODUCCIONES = "Reproducciones";

    public SongsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.song_layout, container, false);
        canciones = new ArrayList<Song>();
        //Ponemos el titulo a la pestaña
        getActivity().setTitle(getArguments().getString(ARG_PLAYLIST));
        //Accedemos a la base de datos
        mDb = new DbAdapter(view.getContext());
        mDb.open();
        //obtenemos las canciones
        Cursor mCursor = mDb.getAllFromPlaylist(getArguments().get(ARG_PLAYLIST).toString());
        if (mCursor.moveToFirst()) {
            do {
                //Para cada fila de la base de datos, obtenemos todos los campos
                String titulo = mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_TITULO));
                String path = mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_RUTA));
                boolean fav = (mCursor.getInt(mCursor.getColumnIndex(DbAdapter.KEY_FAVORITO))==1);
                int duration = mCursor.getInt(mCursor.getColumnIndex(DbAdapter.KEY_DURACION));
                int reproductions = mCursor.getInt(mCursor.getColumnIndex(DbAdapter.KEY_REPRODUCCIONES));
                //Creamos y añadimos el objeto
                canciones.add(new Song(titulo,path,fav,duration,reproductions));
            } while (mCursor.moveToNext());
        }
        //Terminamos de usar el cursor
        mCursor.close();
        //Definimos el sistema de pestañas
        TabLayout tabs = (TabLayout) view.findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText(TAB_TITULO));
        tabs.addTab(tabs.newTab().setText(TAB_DURACION));
        tabs.addTab(tabs.newTab().setText(TAB_REPRODUCCIONES));
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);


        ListView listView_songs = (ListView) view.findViewById(R.id.listview_canciones);
        registerForContextMenu(listView_songs);
        adaptador = new SongAdapter(view.getContext(), canciones);
        listView_songs.setAdapter(adaptador);
        tabs.setOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        switch (tab.getText().toString()){
                            case TAB_TITULO:
                                orderByTitle();
                                break;
                            case TAB_DURACION:
                                orderByDuration();
                                break;
                            case TAB_REPRODUCCIONES:
                                orderByReproductions();
                                break;
                            default:
                                Toast.makeText(getContext(),"Ha ocurrido un error",Toast.LENGTH_LONG).show();
                        }
                        adaptador.notifyDataSetChanged();
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        // ...
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        // ...
                    }
                }
        );
        return view;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.setHeaderTitle("Opciones");
        String playList = getArguments().getString(ARG_PLAYLIST);
        if (!playList.equals(DbAdapter.DEFAULT_PLAYLIST_FAVORITOS) && !playList.equals(DbAdapter.DEFAULT_PLAYLIST_TODAS)) {
            menu.add(Menu.NONE, MENU_CONTEXT_DELETE_FROM_PLAYLIST, Menu.NONE, "Eliminar del Playlist " + getArguments().get(ARG_PLAYLIST));
            menu.add(Menu.NONE, MENU_CONTEXT_EDIT_PLAYLIST, Menu.NONE, "Editar Playlist " + getArguments().get(ARG_PLAYLIST));
        }
        menu.add(Menu.NONE, MENU_CONTEXT_DELETE_FROM_APP, Menu.NONE, "Borrar canción");

    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_CONTEXT_EDIT_PLAYLIST:
                //TODO juntar con la parte de dani
                return true;
            case MENU_CONTEXT_DELETE_FROM_PLAYLIST:
                //TODO hacer llamada a la BD
                return true;
            case MENU_CONTEXT_DELETE_FROM_APP:
                //TODO hacer llamada a la BD
                return true;
        }
        return super.onContextItemSelected(item);
    }
    private void orderByDuration(){
        if (canciones.size() > 0) {
            Collections.sort(canciones, new Comparator<Song>() {
                @Override
                public int compare(final Song object1, final Song object2) {
                    return new Integer(object1.getDuration_seconds()).compareTo(new Integer(object2.getDuration_seconds()));
                }
            } );
        }
    }
    private void orderByTitle(){
        if (canciones.size() > 0) {
            Collections.sort(canciones, new Comparator<Song>() {
                @Override
                public int compare(final Song object1, final Song object2) {
                    return object1.getTitle().compareTo(object2.getTitle());
                }
            } );
        }
    }
    private void orderByReproductions(){
        if (canciones.size() > 0) {
            Collections.sort(canciones, new Comparator<Song>() {
                @Override
                public int compare(final Song object1, final Song object2) {
                    return object1.getNum_reproductions().compareTo(object2.getNum_reproductions());
                }
            } );
        }
    }
}
