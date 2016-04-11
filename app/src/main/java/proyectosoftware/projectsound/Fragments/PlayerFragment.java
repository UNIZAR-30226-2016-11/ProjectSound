package proyectosoftware.projectsound.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
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
    public static final String ARG_PLAYLIST = "reproductor";
    private static SongAdapter adaptador;
    private static ArrayList<Song> canciones;
    private DbAdapter mDb;


    public PlayerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.reproductor, container, false);
        getActivity().setTitle("Reproductor");



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

                Toast.makeText(getContext(),"Playlist el que sea",Toast.LENGTH_SHORT).show();
                //Obtenemos el playlist
                Log.w("PRUEBA", "Estoy antes del if");
                if (getArguments()==null) {
                    PLAYLIST = "None";
                    Log.w("SONGFRAGMENT", "WARNING: null argument ARG_PLAYLIST");
                } else {
                    PLAYLIST = getArguments().getString(ARG_PLAYLIST);
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
