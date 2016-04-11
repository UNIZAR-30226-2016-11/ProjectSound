package proyectosoftware.projectsound.CustomAdapters;

import android.content.ContentValues;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import proyectosoftware.projectsound.Fragments.PlayerFragment;
import proyectosoftware.projectsound.R;
import proyectosoftware.projectsound.Tipos.Song;

/**
 * CustomAdapter para mostrar las canciones
 */
public class SongAdapter extends ArrayAdapter<Song> {
    private final Context context;
    private ArrayList<Song> songArrayList;
    private ArrayList<Song> backup;
    private static String playlist ="";

    /**
     * Constructor por defecto
     *
     * @param context       Context donde se va a usar
     * @param songArrayList ArrayList de las canciones
     */
    public SongAdapter(Context context, ArrayList<Song> songArrayList,String playlist) {

        super(context, R.layout.song_row, songArrayList);
        this.context = context;
        this.songArrayList = songArrayList;
        this.backup = new ArrayList<>(songArrayList);
        this.playlist=playlist;
    }

    /**
     * Sobreescritura del método para que devuelva la lista de canciones que se muestran
     *
     * @return int
     */
    @Override
    public int getCount() {
        return songArrayList.size();
    }

    /**
     * Método que genera el View para cada fila del listview de canciones
     *
     * @param position    Su posición dentro del ListView en número
     * @param convertView El ViewGroup donde está el ListView
     * @param parent      La View de donde está el ListView
     * @return View
     */
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        // 1. Creamos el inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Obtenemmos el rowview
        final View rowView = inflater.inflate(R.layout.song_row, null);

        // 3. Obtenemos los elementos de la vista
        TextView reproductions = (TextView) rowView.findViewById(R.id.num_repro);
        final TextView songTitle = (TextView) rowView.findViewById(R.id.item_title);
        final ImageButton isFavorite = (ImageButton) rowView.findViewById(R.id.item_fav);
        // 4. Actualizamos el listview
        reproductions.setText(songArrayList.get(position).getNum_reproductions());
        songTitle.setText(songArrayList.get(position).getTitle());
        if (songArrayList.get(position).getIsFavorite()) {
            isFavorite.setImageResource(R.drawable.ic_favorite_black_24dp);
        } else {
            isFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }
        // 5. Ponemos el listener del boton de favoritos
        isFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (songArrayList.get(position).getIsFavorite()) {
                    //Ya no es favorito, cambiammos icono y guardamos en la BD
                    isFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
                    songArrayList.get(position).setIsFavorite(false, v.getContext());
                } else {
                    //Ahora es favorito, cambiamos icono y guardamos en la BD
                    isFavorite.setImageResource(R.drawable.ic_favorite_black_24dp);
                    songArrayList.get(position).setIsFavorite(true, v.getContext());
                }
            }
        });
        //6. Ponemos el listener en la celda
        rowView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO enviar canción al reproductor
                Toast.makeText(getContext(), "Reproducción no disponible de " +
                        songArrayList.get(position).getTitle(), Toast.LENGTH_LONG).show();
                DbAdapter db = new DbAdapter(getContext());
                if(!db.isOpen())
                    db.open();
                db.incrementarReproduccionCancion(songArrayList.get(position).getPath());
                PlayerFragment f = new PlayerFragment();
                Bundle args = new Bundle();
                args.putString(PlayerFragment.ARG_PLAYLIST,playlist);
                args.putInt(PlayerFragment.ARG_SONG,position);
                f.setArguments(args);
                FragmentActivity fa =  (FragmentActivity)getContext();
                FragmentManager fragmentManager = fa.getSupportFragmentManager();
                fragmentManager.beginTransaction().replace(R.id.content_frame, f).addToBackStack(null).commit();
            }
        });
        //7. Ponemos el listener en la celda de larga pulsación
        rowView.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                //Mostramos el menú contextual
                v.showContextMenu();
                return true;
            }
        });

        // 8. Devolvemos la vista
        return rowView;
    }

    /**
     * Genera un filter sobreescribiendo los métodos por defecto
     *
     * @return Filter
     */
    @Override
    @SuppressWarnings("UnnecessaryLocalVariable")
    public Filter getFilter() {
        Filter filter = new Filter() {

            /**
             * Publica los resultados generados por el filtro
             * @param constraint Cadena de texto que debe aparecer
             * @param results los resultados obtenidos
             */
            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                if (results.count == 0) {
                    //Si no coincide nada, limpiamos la lista y notificamos los cambios
                    //PD: si no se limpia la lista, aparece el último resultado
                    songArrayList.clear();
                    notifyDataSetInvalidated();
                } else {
                    //Ponemos los resultados que coinciden y notificamos los cambios
                    songArrayList = (ArrayList<Song>) results.values;
                    notifyDataSetChanged();
                }

            }

            /**
             * Método que realiza el filtrado
             * @param constraint Cadena de texto que debe aparecer
             * @return FilterResults
             */
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                //La pasamos a minúsculas para que no sea casesensitive
                String filterString = constraint.toString().toLowerCase();
                FilterResults results = new FilterResults();
                //Donde almacenaremos los filtrados
                ArrayList<Song> filteredList = new ArrayList<>();

                String filterableString;

                for (Song s : backup) {
                    filterableString = s.getTitle();
                    if (filterableString.toLowerCase().contains(filterString) ||
                            filterString.contains(s.getTitle().toLowerCase())) {
                        filteredList.add(s);
                    }
                }
                //Generamos los resultados
                results.values = filteredList;
                results.count = filteredList.size();
                //Los devolvemos
                return results;
            }
        };
        //Devolvemos el filtro generado
        return filter;
    }
}