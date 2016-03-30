package proyectosoftware.projectsound.Fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import proyectosoftware.projectsound.R;
import proyectosoftware.projectsound.Tipos.Song;
import proyectosoftware.projectsound.CustomAdapters.SongAdapter;

/**
 * Fragmento para el contenido principal
 */
public class SongsFragment extends Fragment {
    /**
     * Este argumento del fragmento representa el título de cada
     * sección
     */
    public static final String ARG_SECTION_TITLE = "section_number";
    public static final String ARG_LAYOUT = "Layout";


    public SongsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.song_layout, container, false);
        getActivity().setTitle("Canciones");
        TabLayout tabs = (TabLayout) view.findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("TODAS"));
        tabs.addTab(tabs.newTab().setText("DURACIÓN"));
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);

        ArrayList<Song> canciones = new ArrayList<Song>();
        canciones.add(new Song("Canción 1","",false,1,1));
        canciones.add(new Song("Canción 2","",true,2,2));
        //canciones.add("C1");
        //canciones.add("C2");
        ListView listView_songs = (ListView) view.findViewById(R.id.listview_canciones);
        SongAdapter adaptador = new SongAdapter(view.getContext(), canciones);
        listView_songs.setAdapter(adaptador);
        tabs.setOnTabSelectedListener(
                new TabLayout.OnTabSelectedListener() {
                    @Override
                    public void onTabSelected(TabLayout.Tab tab) {
                        // ...
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
}
