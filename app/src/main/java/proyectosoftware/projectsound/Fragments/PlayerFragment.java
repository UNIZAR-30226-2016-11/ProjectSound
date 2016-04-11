package proyectosoftware.projectsound.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import proyectosoftware.projectsound.CustomAdapters.DbAdapter;
import proyectosoftware.projectsound.CustomAdapters.SongAdapter;
import proyectosoftware.projectsound.Factorys.SongFactory;
import proyectosoftware.projectsound.R;
import proyectosoftware.projectsound.Tipos.Song;

/**
 * Fragmento para el contenido principal
 */
public class PlayerFragment extends Fragment {

    ImageButton botonFav;
    ImageButton botonListaPlaylist;
    ImageButton play_button;  // Botón play
    ImageButton anteriorCancion; // Botón atras
    ImageButton siguienteCancion; // Botón adelante
    public static final String ARG_LAYOUT = "Layout";
    private static String PLAYLIST;
    private static int POS;
    private static String ORDER;
    public static final String ARG_PLAYLIST = "reproductor";
    public static final String ARG_SONG = "posicion";
    public static final String ARG_ORDERBY = "ordenacion";
    private DbAdapter mDb;
    private static SongAdapter adaptador;
    private static ArrayList<Song> canciones;


    public PlayerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.reproductor, container, false);

        getActivity().setTitle("Reproductor");
        // Si tiene argumentos, es decir, si venimos de haber pulsado una canción de un playlist,
        // mostramos dichos argumentos en la pantalla.
        if (getArguments() != null && getArguments().getString(ARG_PLAYLIST) != null){
              //  && getArguments().getString(ARG_SONG)!=null && getArguments().getString(ARG_ORDERBY)!=null) {

            //Accedemos a la base de datos
            mDb = new DbAdapter(view.getContext());
            if(!mDb.isOpen()) mDb.open();
            //obtenemos las canciones
            SongFactory sf = new SongFactory(mDb);
            canciones = new ArrayList<Song>();
            PLAYLIST=getArguments().getString(ARG_PLAYLIST);
            canciones.addAll(sf.getAllFromPlaylist(PLAYLIST));
            //Aqui cojo la canción que acaba de ser pulsada
            final Song cancionActual = canciones.get(getArguments().getInt(ARG_SONG));


            // nombre_playlist tiene el playlist actual
            TextView nombre_playlist = (TextView) view.findViewById(R.id.texto_playlist);

            // mostramos el playlist actual
            nombre_playlist.setText("Playlist: "+getArguments().getString(ARG_PLAYLIST));

            // titulo_cancion tiene el titulo de la cancion actual (nombre del archivo)
            TextView titulo_cancion = (TextView) view.findViewById(R.id.titulo_cancion_reproductor);

            //Le pongo el título
            titulo_cancion.setText(cancionActual.getTitle());

            // subtitulo_cancion tiene el subtitulo de la cancion actual (numero de reproducciones)
            TextView subtitulo_cancion = (TextView) view.findViewById(R.id.subtitulo_cancion_reproductor);

            //Le pongo el numero de reproducciones
            subtitulo_cancion.setText("Reproducciones: " + cancionActual.getNum_reproductions());

            // subtitulo_cancion tiene el subtitulo de la cancion actual (numero de reproducciones)
            TextView duracion = (TextView) view.findViewById(R.id.duracion);

            //Le pongo el numero de reproducciones
            duracion.setText(cancionActual.getDuration());


            final ImageButton isFavorite = (ImageButton) view.findViewById(R.id.favoritoCancion);

            if (cancionActual.getIsFavorite()) {
                isFavorite.setImageResource(R.drawable.ic_favorite_black_24dp);
            } else {
                isFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
            }
            // 5. Ponemos el listener del boton de favoritos
            isFavorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (cancionActual.getIsFavorite()) {
                        //Ya no es favorito, cambiammos icono y guardamos en la BD
                        isFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                        cancionActual.setIsFavorite(false, v.getContext());
                    } else {
                        //Ahora es favorito, cambiamos icono y guardamos en la BD
                        isFavorite.setImageResource(R.drawable.ic_favorite_black_24dp);
                        cancionActual.setIsFavorite(true, v.getContext());
                    }
                }
            });


            //TODO:FALTA ACTUALIZAR NUMERO DE REPRODUCCIONES AQUI EN VEZ DE EN LA PARTE DE CEBO. ADEMAS DE REDIMENSIONAR LA IMAGEN. FALTA PROGRAMAR LOS BOTONES SIGUIENTE; PLAY; Y ANTERIOR; Y TAMBIEN LA BARRA DE ESTADO DEL REPRODUCTOR Y LA REPRODUCCION EN SI.




        }

        botonFav = (ImageButton) view.findViewById(R.id.favoritoCancion);
        botonFav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Favorita",Toast.LENGTH_SHORT).show();
            }
        });

        botonListaPlaylist = (ImageButton) view.findViewById(R.id.botonListaPlaylist);
        botonListaPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Obtenemos el playlist
                Log.w("PRUEBA", "Estoy antes del if");
                if (getArguments() == null || getArguments().getString(ARG_PLAYLIST) == null) {
                    Toast.makeText(getContext(),"ES NULL",Toast.LENGTH_SHORT).show();
                    PLAYLIST = "None";
                    Fragment f = new PlaylistsFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, f).addToBackStack(null).commit();
                } else {
                    PLAYLIST = getArguments().getString(ARG_PLAYLIST);
                    POS = getArguments().getInt(ARG_SONG);
                    ORDER = getArguments().getString(ARG_ORDERBY);







                    Toast.makeText(getContext(),"PLAYLIST: "+PLAYLIST+"POSICION: "+POS+"" +
                            "ORDENACION ",Toast.LENGTH_SHORT).show();
                    Log.w("VAMOS BIEN", "" + PLAYLIST);
                }

                //Machaca la posición en el navigationDrawer por si hemos pulsado atrás desde otra pantalla
                //y tenía otro valor
                NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                navigationView.setCheckedItem(R.id.nav_reproductor);


            }
        });

        anteriorCancion = (ImageButton) view.findViewById(R.id.anteriorCancion);
        anteriorCancion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Anterior canción",Toast.LENGTH_SHORT).show();
            }
        });

        play_button = (ImageButton) view.findViewById(R.id.play_button);
        play_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Play",Toast.LENGTH_SHORT).show();
            }
        });


        siguienteCancion = (ImageButton) view.findViewById(R.id.siguienteCancion);
        siguienteCancion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Siguiente canción",Toast.LENGTH_SHORT).show();
            }
        });

        return view;
    }

}
