package proyectosoftware.projectsound.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import proyectosoftware.projectsound.R;

/**
 * Fragmento para el contenido principal
 */
public class AddToPlayListFragment extends Fragment {

    public AddToPlayListFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.add_to_playlist, container, false);

        //Canciones
        List<String> canciones = new ArrayList<String>();
        canciones.add("Whispers in my head - Onlap");
        canciones.add("X Gon' Give it to ya - DMX");
        canciones.add("La ramona - Fernando Esteso");
        canciones.add("El perdon - Enrique Iglesias, Nicky Jam");
        ListView songs = (ListView) view.findViewById(R.id.cancionesSistema); //LisView izquierdo
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1, canciones);
        songs.setAdapter(adaptador);
        //Nombre de la columna de la lista
        TextView nombreLista = (TextView) view.findViewById(R.id.nombreLista);
        nombreLista.setText("San pepe 2016");
        //Lista
        ListView lists = (ListView) view.findViewById(R.id.userPlaylists);
        List<String> playLists = new ArrayList<String>();
        playLists.add("Whispers in my head - Onlap");
        playLists.add("La ramona - Fernando Esteso");
        playLists.add("El perdon - Enrique Iglesias, Nicky Jam");
        ArrayAdapter<String> listas = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_1,playLists);
        lists.setAdapter(listas);
        return view;

    }

}
