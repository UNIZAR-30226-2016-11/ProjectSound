package proyectosoftware.projectsound.CustomAdapters;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import proyectosoftware.projectsound.R;
import proyectosoftware.projectsound.Tipos.Song;

/**
 * Created by novales35 on 30/03/16.
 */
public class SongAdapter extends ArrayAdapter<Song> {
    private final Context context;
    private ArrayList<Song> songArrayList;
    private ArrayList<Song> backup;

    public SongAdapter(Context context, ArrayList<Song> songArrayList) {

        super(context, R.layout.song_row, songArrayList);

        this.context = context;
        this.songArrayList = songArrayList;
        this.backup=new ArrayList<Song>(songArrayList);
    }

    @Override
    public int getCount(){
        return songArrayList.size();
    }
    @Override
    public View getView(final int position, View convertView, final ViewGroup parent) {

        // 1. Creamos el inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Obtenemmos el rowview
        View rowView = inflater.inflate(R.layout.song_row, null);

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
            rowView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(getContext(), "Reproducción no disponible de " + songArrayList.get(position).getTitle(), Toast.LENGTH_LONG).show();
                }
            });
            rowView.setOnLongClickListener(new View.OnLongClickListener() {

                @Override
                public boolean onLongClick(View v) {
                    v.showContextMenu();
                    return true;
                }
            });

        // 6. devolvemos la vista
        return rowView;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {

            @SuppressWarnings("unchecked")
            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                Log.d("count",""+results.count);
                if (results.count == 0) {
                    songArrayList = new ArrayList<Song>();
                    notifyDataSetInvalidated();
                } else {
                    songArrayList = (ArrayList<Song>) results.values;
                    notifyDataSetChanged();
                }

            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {

                String filterString = constraint.toString().toLowerCase();
                FilterResults results = new FilterResults();
                int count = songArrayList.size();

                ArrayList<Song> filteredList = new ArrayList<Song>();

                String filterableString ;

                for (int i = 0; i < count; i++) {
                    filterableString = songArrayList.get(i).getTitle();
                    if (filterableString.toLowerCase().contains(filterString)) {
                        filteredList.add(songArrayList.get(i));
                        Log.d("Filter","Añadido: "+songArrayList.get(i).getTitle());
                    } else if (songArrayList.get(i).getTitle().toLowerCase().contains(filterString)) {
                        filteredList.add(songArrayList.get(i));
                        Log.d("Filter","Añadido: "+songArrayList.get(i).getTitle());
                    }
                }

                results.values = filteredList;
                results.count = filteredList.size();

                return results;
            }
        };

        return filter;

    }
    public void restore(){
        songArrayList = new ArrayList<Song>(backup);
    }
}
