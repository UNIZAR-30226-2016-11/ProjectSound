package proyectosoftware.projectsound;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

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

    /**
     * Crea una instancia prefabricada de {@link SongsFragment}
     *
     * @param sectionTitle Título usado en el contenido
     * @return Instancia dle fragmento
     */
    public static SongsFragment newInstance(String sectionTitle, int layout) {
        SongsFragment fragment = new SongsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_SECTION_TITLE, sectionTitle);
        args.putInt(ARG_LAYOUT, R.layout.song_layout);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        int layout = getArguments().getInt(ARG_LAYOUT);
        View view = inflater.inflate(layout, container, false);

        // Ubicar argumento en el text view de section_fragment.xml
        String title = getArguments().getString(ARG_SECTION_TITLE);
        TextView titulo = (TextView) view.findViewById(R.id.title);
        titulo.setText(title);
        TabLayout tabs = (TabLayout) view.findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("TODAS"));
        tabs.addTab(tabs.newTab().setText("DURACIÓN"));
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);

        List<String> canciones = new ArrayList<String>();
        canciones.add("C1");
        canciones.add("C2");
        ListView listView_songs = (ListView) view.findViewById(R.id.listview_canciones);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(view.getContext(), R.layout.song_row, canciones);
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
