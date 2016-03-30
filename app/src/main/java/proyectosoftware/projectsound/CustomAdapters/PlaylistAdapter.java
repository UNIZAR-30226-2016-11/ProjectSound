package proyectosoftware.projectsound.CustomAdapters;


import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * Created by dani on 9/03/16.
 */
public abstract class PlaylistAdapter extends BaseAdapter{
    private ArrayList<?> entradasPlaylist;
    private int R_layout_IdView;
    private Context contexto;

    public PlaylistAdapter(Context contexto, int R_layout_IdView, ArrayList<?> entradas) {
        super();
        this.contexto = contexto;
        this.entradasPlaylist = entradas;
        this.R_layout_IdView = R_layout_IdView;
    }

    @Override
    public View getView(int posicion, View view, ViewGroup pariente) {
        if (view == null) {
            LayoutInflater vi = (LayoutInflater) contexto.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = vi.inflate(R_layout_IdView, null);
        }
        onEntrada (entradasPlaylist.get(posicion), view);
        return view;
    }

    @Override
    public int getCount() {

        return entradasPlaylist.size();
    }

    @Override
    public Object getItem(int posicion) {

        return entradasPlaylist.get(posicion);
    }

    @Override
    public long getItemId(int posicion) {

        return posicion;
    }

    /** Devuelve cada una de las entradas con cada una de las vistas a la que debe de ser asociada
     * @param entrada La entrada que será la asociada a la view. La entrada es del tipo del paquete/handler
     * @param view View particular que contendrá los datos del paquete/handler
     */
    public abstract void onEntrada (Object entrada, View view);

}
