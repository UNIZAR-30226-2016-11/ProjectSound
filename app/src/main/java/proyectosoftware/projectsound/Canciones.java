package proyectosoftware.projectsound;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by novales35 on 16/03/16.
 */
public class Canciones extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.song_layout);
        TabLayout tabs = (TabLayout) findViewById(R.id.tabs);
        tabs.addTab(tabs.newTab().setText("TODAS"));
        tabs.addTab(tabs.newTab().setText("DURACIÓN"));
        tabs.setTabMode(TabLayout.MODE_SCROLLABLE);


        List<String> canciones = new ArrayList<String>();
        canciones.add("C1");
        canciones.add("C2");
        ListView listView_songs = (ListView) findViewById(R.id.listview_canciones);
        ArrayAdapter<String> adaptador = new ArrayAdapter<String>(this, R.layout.song_row, canciones);
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
    }
}
