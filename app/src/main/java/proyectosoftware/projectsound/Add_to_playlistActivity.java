package proyectosoftware.projectsound;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Dani Rueda on 13/03/16.
 */
public class Add_to_playlistActivity extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_to_playlist);
        //Canciones
        List<String> canciones = new ArrayList<String>();
        canciones.add("Whispers in my head - Onlap");
        canciones.add("X Gon' Give it to ya - DMX");
        canciones.add("La ramona - Fernando Esteso");
        canciones.add("El perdon - Enrique Iglesias, Nicky Jam");
        ListView songs = (ListView) findViewById(R.id.cancionesSistema); //LisView izquierdo
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, canciones);
        songs.setAdapter(adaptador);
        //Nombre de la columna de la lista
        TextView nombreLista = (TextView) findViewById(R.id.nombreLista);
        nombreLista.setText("San pepe 2016");
        //Lista
        ListView lists = (ListView) findViewById(R.id.playlist);
        List<String> playLists = new ArrayList<String>();
        playLists.add("Whispers in my head - Onlap");
        playLists.add("La ramona - Fernando Esteso");
        playLists.add("El perdon - Enrique Iglesias, Nicky Jam");
        ArrayAdapter<String> listas = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,playLists);
        lists.setAdapter(listas);
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
