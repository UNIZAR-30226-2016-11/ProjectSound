package proyectosoftware.projectsound.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import proyectosoftware.projectsound.CustomAdapters.DbAdapter;
import proyectosoftware.projectsound.R;

/**
 * Fragmento para el contenido principal
 */
public class PlayerFragment extends Fragment {
    /**
     * Este argumento del fragmento representa el título de cada
     * sección
     */

    ImageButton botonFav;
    public static final String ARG_LAYOUT = "Layout";


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
                Toast.makeText(getContext(),"Esto es una prueba",Toast.LENGTH_LONG).show();
            }

        });
        return view;
    }

}
