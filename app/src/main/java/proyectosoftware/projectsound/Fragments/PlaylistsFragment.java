package proyectosoftware.projectsound.Fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import proyectosoftware.projectsound.R;

/**
 * Fragmento para el contenido principal
 */
public class PlaylistsFragment extends Fragment {


    public PlaylistsFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.entrada_playlist, container, false);

        return view;
    }

}
