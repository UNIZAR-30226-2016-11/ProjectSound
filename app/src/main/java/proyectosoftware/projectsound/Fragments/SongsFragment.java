package proyectosoftware.projectsound.Fragments;


import android.app.SearchManager;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import proyectosoftware.projectsound.CustomAdapters.DbAdapter;
import proyectosoftware.projectsound.CustomAdapters.SongAdapter;
import proyectosoftware.projectsound.R;
import proyectosoftware.projectsound.Tipos.Song;

/**
 * Fragmento para mostrar las canciones
 */
public class SongsFragment extends Fragment {

    public static final String ARG_PLAYLIST = "play";
    private static final String TAB_TITULO = "Título";
    private static final String TAB_DURACION = "Duración";
    private static final String TAB_REPRODUCCIONES = "Reproducciones";
    private static SongAdapter adaptador;
    private static ArrayList<Song> canciones;
    private final int MENU_CONTEXT_DELETE_FROM_PLAYLIST = Menu.FIRST;
    private final int MENU_CONTEXT_EDIT_PLAYLIST = Menu.FIRST + 1;
    private final int MENU_CONTEXT_DELETE_SONG = Menu.FIRST + 2;
    private DbAdapter mDb;
    private static String PLAYLIST;

    /**
     * Constructor por defencto
     */
    public SongsFragment() {
    }

    /**
     * Sobreescritura del método para generar la vista
     *
     * @param inflater           El inflater
     * @param container          Contenedor que va a recibir la vista
     * @param savedInstanceState Bundle con los parámetros
     * @return View
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Generamos la vista
        View view = inflater.inflate(R.layout.song_layout, container, false);
        //Obtemos la playlist
        if (getArguments().getString(ARG_PLAYLIST) == null) {
            PLAYLIST = "None";
            Log.w("SONGFRAGMENT", "WARNING: null argument ARG_PLAYLIST");
        } else {
            PLAYLIST = getArguments().getString(ARG_PLAYLIST);
        }
        //Machaca la posición en el navigationDrawer por si hemos pulsado atrás desde otra pantalla
        //y tenía otro valor
        NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
        if(PLAYLIST.equals(DbAdapter.DEFAULT_PLAYLIST_FAVORITOS)){
            navigationView.setCheckedItem(R.id.nav_favoritos);
        }else{
            navigationView.setCheckedItem(R.id.nav_playlists);
        }
        navigationView=null; //liberamos memoria
        //Habilitamos la opción de búsqueda
        setHasOptionsMenu(true);
        canciones = new ArrayList<>();
        //Ponemos el titulo a la pestaña
        getActivity().setTitle(PLAYLIST);
        //Accedemos a la base de datos
        mDb = new DbAdapter(view.getContext());
        mDb.open();
        //obtenemos las canciones
        Cursor mCursor = mDb.getAllFromPlaylist(PLAYLIST);
        if (mCursor.moveToFirst()) {
            do {
                //Para cada fila de la base de datos, obtenemos todos los campos
                String titulo = mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_TITULO));
                String path = mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_RUTA));
                boolean fav = (mCursor.getInt(mCursor.getColumnIndex(DbAdapter.KEY_FAVORITO)) == 1);
                int duration = mCursor.getInt(mCursor.getColumnIndex(DbAdapter.KEY_DURACION));
                int reproductions = mCursor.getInt(mCursor.getColumnIndex(DbAdapter.KEY_REPRODUCCIONES));
                //Creamos y añadimos el objeto
                canciones.add(new Song(titulo, path, fav, duration, reproductions));
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

        //Obtenemos el ListView
        ListView listView_songs = (ListView) view.findViewById(R.id.listview_canciones);
        listView_songs.setItemsCanFocus(true);
        //Habilitamos la creación de los contextmenu
        registerForContextMenu(listView_songs);
        adaptador = new SongAdapter(view.getContext(), canciones);
        //Ponemos el adaptador
        listView_songs.setAdapter(adaptador);
        tabs.setOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        switch (tab.getText().toString()) {
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
                                //No deberíamos llegar aquí
                                Toast.makeText(getContext(), "Ha ocurrido un error", Toast.LENGTH_LONG).show();
                        }
                        adaptador.notifyDataSetChanged();
                    }

                    @Override
                    public void onTabUnselected(TabLayout.Tab tab) {
                        // No necesiamos un comportamiento diferente
                    }

                    @Override
                    public void onTabReselected(TabLayout.Tab tab) {
                        // No necesitamos un comportamiento diferente
                    }
                }
        );
        return view;
    }

    /**
     * Método que define como crear el ContextMenu
     *
     * @param menu     El ContextMenu que se está creando
     * @param v        La vista
     * @param menuInfo La información del ContextMenu
     */
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        //Le ponemos el titulo
        menu.setHeaderTitle(getContext().getString(R.string.options));
        //Obtenemos el nombre del playlist en el que se está creando
        String playList = getArguments().getString(ARG_PLAYLIST);
        //Si no es ni Todas ni Favoritos
        if (!playList.equals(DbAdapter.DEFAULT_PLAYLIST_FAVORITOS)
                && !playList.equals(DbAdapter.DEFAULT_PLAYLIST_TODAS)) {
            //Ponemos la opción de Eliminar del PlayList
            menu.add(Menu.NONE, MENU_CONTEXT_DELETE_FROM_PLAYLIST, Menu.NONE,
                    getContext().getString(R.string.delete_from_playlist) +
                            getArguments().get(ARG_PLAYLIST));
            //Y la opción de Editar PlayList
            menu.add(Menu.NONE, MENU_CONTEXT_EDIT_PLAYLIST, Menu.NONE,
                    getContext().getString(R.string.edit_playlist)
                            + getArguments().get(ARG_PLAYLIST));
        }
        //Entrada generica a todas las PlayLists
        menu.add(Menu.NONE, MENU_CONTEXT_DELETE_SONG, Menu.NONE,
                getContext().getString(R.string.delete_song));

    }

    /**
     * Método que define el comportamiento al pulsar las opciones del ContextMenú
     *
     * @param item el item pulsado
     * @return boolean
     */
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case MENU_CONTEXT_EDIT_PLAYLIST:
                Fragment f = new AddToPlayListFragment();
                Bundle args = new Bundle();
                args.putString(AddToPlayListFragment.ARG_PLAYLIST,PLAYLIST);
                f.setArguments(args);
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, f).addToBackStack(null).commit();
                return true;
            case MENU_CONTEXT_DELETE_FROM_PLAYLIST:
                //TODO hacer llamada a la BD
                return true;
            case MENU_CONTEXT_DELETE_SONG:
                //TODO hacer llamada a la BD
                return true;
        }
        //Si no es ninguno de los anteriores, llamamos al padre
        return super.onContextItemSelected(item);
    }

    /**
     * Organiza las canciones por su duración de mayor a menor
     */
    private void orderByDuration() {
        if (canciones.size() > 0) {
            Collections.sort(canciones, new Comparator<Song>() {
                @Override
                public int compare(final Song object1, final Song object2) {
                    return Integer.valueOf(object2.getDuration_seconds())
                            .compareTo(object1.getDuration_seconds());
                }
            });
        }
    }

    /**
     * Organiza las canciones por el Título
     */
    private void orderByTitle() {
        if (canciones.size() > 0) {
            Collections.sort(canciones, new Comparator<Song>() {
                @Override
                public int compare(final Song object1, final Song object2) {
                    return object1.getTitle().compareTo(object2.getTitle());
                }
            });
        }
    }

    /**
     * Organiza las canciones por las reproducciones
     */
    private void orderByReproductions() {
        if (canciones.size() > 0) {
            Collections.sort(canciones, new Comparator<Song>() {
                @Override
                public int compare(final Song object1, final Song object2) {
                    return object1.getNum_reproductions().compareTo(object2.getNum_reproductions());
                }
            });
        }
    }

    /**
     * Define como es el menú del actionBar
     *
     * @param menu     El menú que se está crendo
     * @param inflater El inflater que se está usando
     */
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //Le ponemos la interfaz deseada
        getActivity().getMenuInflater().inflate(R.menu.search, menu);
        //Generamos el widget de búsqueda
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(menu.findItem(R.id.action_search));
        SearchManager searchManager = (SearchManager) getContext().getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        //Deshabilitamos el botón de submit, queremos que vaya filtrando conforme escribes
        searchView.setSubmitButtonEnabled(false);
        //Introducimos el texto que aparece por defecto
        searchView.setQueryHint(getContext().getString(R.string.search_hint));
        //Generamos el Listener
        SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                //Al presionar el submit no hacemos nada
                return false;
            }

            /**
             * Listener que lanza la ejecución cuando cambias el texto
             * @param s El texto introducido
             * @return boolean
             */
            @Override
            public boolean onQueryTextChange(String s) {
                //Obtenemos el listview
                ListView listView = (ListView) getActivity().findViewById(R.id.listview_canciones);
                //Habilitamos el filtro
                listView.setTextFilterEnabled(true);
                //Usamos el filtro de nuestro customAdaptor
                adaptador.getFilter().filter(s);
                return true;
            }
        };
        //Asignamos el listener
        searchView.setOnQueryTextListener(queryTextListener);
    }


}
