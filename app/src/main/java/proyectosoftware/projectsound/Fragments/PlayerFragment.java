package proyectosoftware.projectsound.Fragments;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.TextView;
import java.io.IOException;
import android.widget.Toast;

import java.util.ArrayList;

import proyectosoftware.projectsound.CustomAdapters.DbAdapter;
import proyectosoftware.projectsound.CustomAdapters.SongAdapter;
import proyectosoftware.projectsound.Factorys.SongFactory;
import proyectosoftware.projectsound.R;
import proyectosoftware.projectsound.Tipos.Song;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;

/**
 * Fragmento para el contenido principal
 */
public class PlayerFragment extends Fragment {


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

    MediaPlayer mediaPlayer = new MediaPlayer();
    ImageButton play_button;  // Botón play
    ImageButton botonAnteriorCancion; // Botón atras
    ImageButton botonSiguienteCancion; // Botón adelante
    private int state = 1;
    private final int playing = 1;
    private final int Pausing = 2;

    private TextView titulo_cancion;
    private TextView subtitulo_cancion;
    private TextView duracion;

    private SeekBar seekBar;
    private Handler mHandler = new Handler();

    public PlayerFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.reproductor, container, false);

        getActivity().setTitle("Reproductor");

        // Si tiene argumentos, es decir, si venimos de haber pulsado una canción de un playlist,
        // mostramos dichos argumentos en la pantalla.
        if (getArguments() != null && getArguments().getString(ARG_PLAYLIST) != null){

            //Accedemos a la base de datos
            mDb = new DbAdapter(view.getContext());
            if(!mDb.isOpen()) mDb.open();
            //obtenemos las canciones
            SongFactory sf = new SongFactory(mDb);
            canciones = new ArrayList<Song>();
            PLAYLIST=getArguments().getString(ARG_PLAYLIST);
            ORDER=getArguments().getString(ARG_ORDERBY);

            canciones.addAll(sf.getAllFromPlaylist(PLAYLIST));
            if (ARG_ORDERBY == "titulo"){
                sf.orderByTitle(canciones);
            }
            else if (ARG_ORDERBY == "duracion"){
                sf.orderByDuration(canciones);
            }
            else if (ARG_ORDERBY == "reproducciones"){
                sf.orderByReproductions(canciones);
            }


            //Aqui cojo la canción que acaba de ser pulsada
            final Song cancionActual = canciones.get(getArguments().getInt(ARG_SONG));

            //incremento en 1 la reproducción de la cancion.
            mDb.incrementarReproduccionCancion(cancionActual.getPath());

            // nombre_playlist tiene el playlist actual
            TextView nombre_playlist = (TextView) view.findViewById(R.id.texto_playlist);

            // mostramos el playlist actual
            nombre_playlist.setText("Playlist: "+getArguments().getString(ARG_PLAYLIST));

            // titulo_cancion tiene el titulo de la cancion actual (nombre del archivo)
            titulo_cancion = (TextView) view.findViewById(R.id.titulo_cancion_reproductor);

            //Le pongo el título
            titulo_cancion.setText(cancionActual.getTitle());

            // subtitulo_cancion tiene el subtitulo de la cancion actual (numero de reproducciones)
            subtitulo_cancion = (TextView) view.findViewById(R.id.subtitulo_cancion_reproductor);

            //Le pongo el numero de reproducciones
            subtitulo_cancion.setText("Reproducciones: " + cancionActual.getNum_reproductions());

            // subtitulo_cancion tiene el subtitulo de la cancion actual (numero de reproducciones)
            duracion = (TextView) view.findViewById(R.id.duracion);

            //Le pongo el numero de reproducciones
            duracion.setText(cancionActual.getDuration());

            seekBar = (SeekBar) view.findViewById(R.id.barra_tiempo);

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


            //TODO: FALTA TAMBIEN LA BARRA DE ESTADO DEL REPRODUCTOR Y LA REPRODUCCION EN SI.


            // TRATAMIENTO DE LA REPRODUCCION

            //Cogemos la ruta de la cancion de la BD.
            String ruta = cancionActual.getPath();
            Log.w("rutaActual ", "rutaActual " + ruta);

            try {
                mediaPlayer.reset();
                //ponemos el reproductor en estado de preparado.
                mediaPlayer.setDataSource(ruta);
                mediaPlayer.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }


            //Cuidado con este método que igual hace que no se pare la cancion nunca
            mediaPlayer.setLooping(true);
            seekBar.setMax(mediaPlayer.getDuration());

            //TODO: Botón de play/pause
            play_button = (ImageButton) view.findViewById(R.id.play_button);
            play_button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mediaPlayer.isPlaying()) {
                        play_button.setImageResource(R.drawable.ic_play_arrow_black_24dp);
                        mediaPlayer.pause();
                    }
                    else if(!mediaPlayer.isPlaying()){
                        play_button.setImageResource(R.drawable.ic_pause_black_24dp);
                        mediaPlayer.start();
                        //Make sure you update Seekbar on UI thread
                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                if(mediaPlayer != null){
                                    int mCurrentPosition = mediaPlayer.getCurrentPosition();
                                    seekBar.setProgress(mCurrentPosition);
                                }
                                mHandler.postDelayed(this, 1000);
                            }
                        });

                        // Barra de tiempo
                        seekBar.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {
                            boolean userTouch;

                            @Override
                            public void onProgressChanged(SeekBar seekBar, int progressValue, boolean fromUser) {
                                if(mediaPlayer != null && userTouch){
                                    mediaPlayer.seekTo(progressValue);
                                    seekBar.animate();
                                }
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {
                                userTouch = true;
                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                                userTouch = false;
                            }
                        });
                    }
                }
            });





            //TODO: Botón para volver a la canción anterior
            botonAnteriorCancion = (ImageButton) view.findViewById(R.id.anteriorCancion);
            botonAnteriorCancion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int indiceAnteriorCancion = 0;
                    int indiceCancionActual = getArguments().getInt(ARG_SONG);
                    if (indiceCancionActual == 0){
                        indiceAnteriorCancion = canciones.size()-1;
                    }
                    else{
                        indiceAnteriorCancion = indiceCancionActual-1;
                    }
                    final Song anteriorCancion = canciones.get(indiceAnteriorCancion);
                    final String rutaAnteriorCancion  = anteriorCancion.getPath();
                    if(!mDb.isOpen()) mDb.open();
                    //incremento en 1 la reproducción de la cancion.
                    mDb.incrementarReproduccionCancion(rutaAnteriorCancion);

                    // titulo_cancion tiene el titulo de la cancion actual (nombre del archivo)
                    titulo_cancion = (TextView) PlayerFragment.this.getView().findViewById(R.id.titulo_cancion_reproductor);

                    //Le pongo el título
                    titulo_cancion.setText(anteriorCancion.getTitle());

                    // subtitulo_cancion tiene el subtitulo de la cancion actual (numero de reproducciones)
                    subtitulo_cancion = (TextView)  PlayerFragment.this.getView().findViewById(R.id.subtitulo_cancion_reproductor);

                    //Le pongo el numero de reproducciones
                    subtitulo_cancion.setText("Reproducciones: " + anteriorCancion.getNum_reproductions());

                    // subtitulo_cancion tiene el subtitulo de la cancion actual (numero de reproducciones)
                    duracion = (TextView) PlayerFragment.this.getView().findViewById(R.id.duracion);

                    //Le pongo el numero de reproducciones
                    duracion.setText(anteriorCancion.getDuration());

                    final ImageButton isFavorite = (ImageButton) PlayerFragment.this.getView().findViewById(R.id.favoritoCancion);

                    if (anteriorCancion.getIsFavorite()) {
                        isFavorite.setImageResource(R.drawable.ic_favorite_black_24dp);
                    } else {
                        isFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    }
                    // 5. Ponemos el listener del boton de favoritos
                    isFavorite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (anteriorCancion.getIsFavorite()) {
                                //Ya no es favorito, cambiammos icono y guardamos en la BD
                                isFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                                anteriorCancion.setIsFavorite(false, v.getContext());
                            } else {
                                //Ahora es favorito, cambiamos icono y guardamos en la BD
                                isFavorite.setImageResource(R.drawable.ic_favorite_black_24dp);
                                anteriorCancion.setIsFavorite(true, v.getContext());
                            }
                        }
                    });

                    //Coger la cancion anterior del playlist de la BD
                    if(mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    }
                    try {
                        mediaPlayer.reset();
                        //Preparamos la cancion anterior como reproducible
                        mediaPlayer.setDataSource(rutaAnteriorCancion);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //Cuidado con este método que igual hace que no se pare la cancion nunca
                    //mediaPlayer.setLooping(true);
                    play_button.setImageResource(R.drawable.ic_pause_black_24dp);
                    //Reproducir la cancion.
                    mediaPlayer.start();
                }
            });






            //TODO: Boton para reproducir a la siguiente cancion
            botonSiguienteCancion = (ImageButton) view.findViewById(R.id.siguienteCancion);
            botonSiguienteCancion.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(mediaPlayer.isPlaying()) {
                        mediaPlayer.stop();
                    }

                    //Coger la cancion anterior del playlist de la BD
                    int indiceCancionActual = getArguments().getInt(ARG_SONG);
                    Log.w("INDICE ", "INDICE " + indiceCancionActual);
                    int indiceSiguienteCancion = 0;
                    if (indiceCancionActual == canciones.size()-1){
                        indiceSiguienteCancion = 0;
                    }
                    else{
                        indiceSiguienteCancion = indiceCancionActual+1;
                    }
                    final Song siguienteCancion = canciones.get(indiceSiguienteCancion);
                    Log.w("siguienteCancion ", "siguienteCancion " + siguienteCancion.getTitle());

                    final String rutaSiguienteCancion  = siguienteCancion.getPath();
                    Log.w("rutaSiguienteCancion ", "rutaSiguienteCancion " + rutaSiguienteCancion);
                    if(!mDb.isOpen()) mDb.open();
                    //incremento en 1 la reproducción de la cancion.
                    mDb.incrementarReproduccionCancion(rutaSiguienteCancion);

                    // titulo_cancion tiene el titulo de la cancion actual (nombre del archivo)
                    titulo_cancion = (TextView) PlayerFragment.this.getView().findViewById(R.id.titulo_cancion_reproductor);

                    //Le pongo el título
                    titulo_cancion.setText(siguienteCancion.getTitle());

                    // subtitulo_cancion tiene el subtitulo de la cancion actual (numero de reproducciones)
                    subtitulo_cancion = (TextView)  PlayerFragment.this.getView().findViewById(R.id.subtitulo_cancion_reproductor);

                    //Le pongo el numero de reproducciones
                    subtitulo_cancion.setText("Reproducciones: " + siguienteCancion.getNum_reproductions());

                    // subtitulo_cancion tiene el subtitulo de la cancion actual (numero de reproducciones)
                    duracion = (TextView) PlayerFragment.this.getView().findViewById(R.id.duracion);

                    //Le pongo el numero de reproducciones
                    duracion.setText(siguienteCancion.getDuration());

                    final ImageButton isFavorite = (ImageButton) PlayerFragment.this.getView().findViewById(R.id.favoritoCancion);

                    if (siguienteCancion.getIsFavorite()) {
                        isFavorite.setImageResource(R.drawable.ic_favorite_black_24dp);
                    } else {
                        isFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    }
                    // 5. Ponemos el listener del boton de favoritos
                    isFavorite.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (siguienteCancion.getIsFavorite()) {
                                //Ya no es favorito, cambiammos icono y guardamos en la BD
                                isFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                                siguienteCancion.setIsFavorite(false, v.getContext());
                            } else {
                                //Ahora es favorito, cambiamos icono y guardamos en la BD
                                isFavorite.setImageResource(R.drawable.ic_favorite_black_24dp);
                                siguienteCancion.setIsFavorite(true, v.getContext());
                            }
                        }
                    });


                    try {
                        mediaPlayer.reset();
                        //Preparamos la cancion anterior como reproducible
                        mediaPlayer.setDataSource(rutaSiguienteCancion);
                        mediaPlayer.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    //Cuidado con este método que igual hace que no se pare la cancion nunca
                    //mediaPlayer.setLooping(true);

                    play_button.setImageResource(R.drawable.ic_pause_black_24dp);
                    //Reproducir la cancion.
                    mediaPlayer.start();
                }
            });

        }


        ImageButton botonListaPlaylist = (ImageButton) view.findViewById(R.id.botonListaPlaylist);
        botonListaPlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //Obtenemos el playlist
                Log.w("PRUEBA", "Estoy antes del if");

                mediaPlayer.reset();

                if (getArguments() == null || getArguments().getString(ARG_PLAYLIST) == null) {
                    PLAYLIST = "None";
                    Fragment f = new PlaylistsFragment();
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, f).addToBackStack(null).commit();
                } else {
                    PLAYLIST = getArguments().getString(ARG_PLAYLIST);
                    POS = getArguments().getInt(ARG_SONG);
                    ORDER = getArguments().getString(ARG_ORDERBY);

                    Bundle args = new Bundle();
                    args.putString(SongsFragment.ARG_PLAYLIST, PLAYLIST);
                    SongsFragment f = new SongsFragment();
                    f.setArguments(args);
                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    fragmentManager.beginTransaction().replace(R.id.content_frame, f).addToBackStack(null).commit();

                    Log.w("VAMOS BIEN", "" + PLAYLIST);
                }

                //Machaca la posición en el navigationDrawer por si hemos pulsado atrás desde otra pantalla
                //y tenía otro valor
                NavigationView navigationView = (NavigationView) getActivity().findViewById(R.id.nav_view);
                navigationView.setCheckedItem(R.id.nav_reproductor);


            }
        });



        return view;
    }


}
