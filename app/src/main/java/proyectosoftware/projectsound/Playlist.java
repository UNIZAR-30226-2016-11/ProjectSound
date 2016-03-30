package proyectosoftware.projectsound;

import java.util.ArrayList;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class Playlist extends AppCompatActivity{
    private ListView lista;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        ArrayList<Playlist_entrada> datos_playlist = new ArrayList<Playlist_entrada>();

        //Ejemplos añadidos a la playlist
        datos_playlist.add(new Playlist_entrada(R.drawable.ic_todas_playlist_256x256,"Todas","0 pistas","0 min"));
        datos_playlist.add(new Playlist_entrada(R.drawable.ic_favs_playlist_256x256,"Favoritos","0 pistas","0 min"));
        datos_playlist.add(new Playlist_entrada(R.drawable.ic_nueva_playlist_256x256,"Calle 13","0 pistas","0 min"));

        lista = (ListView) findViewById(R.id.listview_playlist);
        lista.setAdapter(new Playlist_adapter(this, R.layout.entrada_playlist, datos_playlist){


	    /*
	    * COMPRUEBA QUE EXISTE LOS ITEMS CORRECTOS ANTES DE INSERTAR CUALQUIER ELEMENTO Y LOS INCLUYE
	    */
            @Override
            public void onEntrada(Object entrada, View view) {
                if (entrada != null) {

                    ImageView imagen_playlist = (ImageView) view.findViewById(R.id.imgeView_lista_icono);
                    if (imagen_playlist != null)
                        imagen_playlist.setImageResource(((Playlist_entrada) entrada).getIdImagenPlaylist());

                    TextView titulo_playlist = (TextView) view.findViewById(R.id.textView_lista_titulo);
                    if (titulo_playlist != null)
                        titulo_playlist.setText(((Playlist_entrada) entrada).getTituloPlaylist());

                    TextView numCanciones_playlist = (TextView) view.findViewById(R.id.textView_lista_num_canciones);
                    if (numCanciones_playlist != null)
                        numCanciones_playlist.setText(((Playlist_entrada) entrada).getNumCancionesPlaylist());

                    TextView duracion_playlist = (TextView) view.findViewById(R.id.textView_lista_duracion);
                    if (duracion_playlist != null){
                        duracion_playlist.setText(((Playlist_entrada) entrada).getDuracionPlaylist());
                    }

                }
            }

        });
        
        //LO SIGUIENTE SUPONGO QUE CUANDO SE LE DE FUNCIONALIDAD SERVIRA ALGO, O NO, QUE ESTA COPIADO TAL CUAL DE UN EJEMPLO

/*
        lista.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> pariente, View view, int posicion, long id) {
                Playlist_entrada elegido = (Playlist_entrada) pariente.getItemAtPosition(posicion);

                CharSequence texto = "Seleccionado: " + elegido.get_textoDebajo();
                Toast toast = Toast.makeText(MainActivity.this, texto, Toast.LENGTH_LONG);
                toast.show();
            }

        });
*/


    }


}
