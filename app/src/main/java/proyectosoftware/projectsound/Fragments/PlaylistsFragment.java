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
import android.text.InputType;
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
import proyectosoftware.projectsound.MainActivity;
import proyectosoftware.projectsound.R;
import proyectosoftware.projectsound.Tipos.Playlist;


/**
 * Fragmento para el contenido principal
 */
public class PlaylistsFragment extends Fragment {

    private ListView lista;
    private DbAdapter mdb;
    private Context contexto = getContext();
    private PlaylistAdapter adaptador;
    private List<Playlist> datos_playlist = new ArrayList<Playlist>();
    private static String selectedPlaylist = null;
    private final int MENU_CONTEXT_RENAME_PLAYLIST = Menu.FIRST;
    private final int MENU_CONTEXT_DELETE_PLAYLIST = Menu.FIRST+1;
    private View vista;
    public PlaylistsFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_playlist, container, false);
        getActivity().setTitle("Playlists");


        //Referencia al boton para anadir nuevas playlist
        FloatingActionButton btn_anadir = (FloatingActionButton) view.findViewById(R.id.btn_anadir_playlist);

        //Al pulsar el boton lanza el Dialog para anadir una nueva playlist
        btn_anadir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNewPlaylistDialog();
            }
        });

        //Montar listado de playlist
        montarListView(view);

        //Anadir a cada elemento de la lista la funcion de pulsacion larga
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
        vista=view;
        return view;
    }

    /**
     * Incluye en el listview el contenido de la base de datos
     * @param view
     */
    private void montarListView(View view){

        //Obtener todos los datos sobre las playlist existentes
        mdb = new DbAdapter(contexto);
        PlaylistFactory plf = new PlaylistFactory(mdb);
        datos_playlist = plf.getAllPlaylist();
        //Creacion de lista en el listview
        lista = (ListView) view.findViewById(R.id.listview_playlist);

        lista.setAdapter(adaptador = new PlaylistAdapter(view.getContext(), R.layout.entrada_playlist, (ArrayList<?>) datos_playlist) {
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
    }

    private void CreateMenu(Menu menu) {
        menu.add(Menu.NONE, MENU_CONTEXT_RENAME_PLAYLIST, Menu.NONE, "Modificar titulo");
        menu.add(Menu.NONE, MENU_CONTEXT_DELETE_PLAYLIST, Menu.NONE, "Eliminar " + selectedPlaylist);
    }

    public boolean onContextItemSelected(MenuItem item){
        switch (item.getItemId()){
            case MENU_CONTEXT_RENAME_PLAYLIST:
                if(!selectedPlaylist.equals("Todas") && !selectedPlaylist.equals("Favoritos")){
                    renamePlaylistDialog();
                }
                return true;
            case MENU_CONTEXT_DELETE_PLAYLIST:
                //Abrir la conexion con la base de datos
                if(!mdb.isOpen()){
                    mdb.open();
                }
                if(selectedPlaylist!=null){
                    //Eliminar de la base de datos la playlist con titulo = selectedPlaylist
                    mdb.deletePlaylist(selectedPlaylist);
                    //Eliminar del arraylist
                    boolean eliminada = false;
                    int i = 0;
                    while(!eliminada && i < datos_playlist.size()){
                        if(datos_playlist.get(i).getTituloPlaylist().equals(selectedPlaylist)){
                            datos_playlist.remove(i);
                            eliminada = true;
                        }else{
                            i++;
                        }
                    }
                }
                selectedPlaylist = null;
                //actualizar listado mostrado por pantalla
                adaptador.notifyDataSetChanged();
                return true;
        }
        //Si no es ninguno de los anteriores, llamamos al padre
        return super.onContextItemSelected(item);
    }

    /**
     * Genera ventana dialog para introducir titulo de la nueva playlist
     */
    private void addNewPlaylistDialog(){
        android.support.v7.app.AlertDialog ad = new android.support.v7.app.AlertDialog.Builder(getActivity()).create();
        ad.setTitle("Crear una nueva Playlist");
        ad.setMessage("Introducir titulo");

        //Introducir un editText en el dialog, para recoger el nuevo titulo
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.create_playlist_dialog, (ViewGroup) getView(), false);
        final EditText input = (EditText) viewInflated.findViewById(R.id.dialog_createplaylist);
        ad.setView(viewInflated);

        ad.setButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE,"Añadir", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                String newTitle = "";
                newTitle = input.getText().toString();
                if(!mdb.isOpen()){
                    mdb.open();
                }
                //TODO controlar que no intente insertar algo que existe
                if(!newTitle.equals("")){
                    mdb.insertNewPlaylist(newTitle);
                    montarListView(vista);
                    adaptador.notifyDataSetChanged();
                }

                ;//llamada a la creacion de playlist

            }
        });
        ad.setButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                dialog.dismiss();
            }
        });
        ad.show();


    }

    /**
     * Genera ventana dialog para modificar titulo de una playlist
     */
    private void renamePlaylistDialog(){
        android.support.v7.app.AlertDialog ad = new android.support.v7.app.AlertDialog.Builder(getActivity()).create();
        ad.setTitle("Editar nombre de Playlist");
        ad.setMessage("Introducir nuevo titulo");

        //Introducir un editText en el dialog, para recoger el nuevo titulo
        View viewInflated = LayoutInflater.from(getContext()).inflate(R.layout.create_playlist_dialog, (ViewGroup) getView(), false);
        final EditText input = (EditText) viewInflated.findViewById(R.id.dialog_createplaylist);
        ad.setView(viewInflated);

        ad.setButton(android.support.v7.app.AlertDialog.BUTTON_POSITIVE,"Modificar", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int id) {

                String newTitle = "";
                newTitle = input.getText().toString();
                if(!mdb.isOpen()){
                    mdb.open();
                }
                //TODO controlar que no intente insertar algo que existe
                if(!newTitle.equals("")){
                    mdb.cambiar_nombre_playlist(selectedPlaylist,newTitle);
                    montarListView(vista);
                    adaptador.notifyDataSetChanged();
                }

                ;//llamada a la creacion de playlist

            }
        });
        ad.setButton(android.support.v7.app.AlertDialog.BUTTON_NEGATIVE, "Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {

                dialog.dismiss();
            }
        });
        ad.show();


    }



}