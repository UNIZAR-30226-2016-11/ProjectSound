package proyectosoftware.projectsound;

import android.content.Context;
import android.media.Image;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by novales35 on 30/03/16.
 */
public class SongAdapter extends ArrayAdapter<Song> {
    private final Context context;
    private final ArrayList<Song> songArrayList;

    public SongAdapter(Context context, ArrayList<Song> songArrayList) {

        super(context, R.layout.song_row, songArrayList);

        this.context = context;
        this.songArrayList = songArrayList;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // 1. Creamos el inflater
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // 2. Obtenemmos el rowview
        View rowView = inflater.inflate(R.layout.song_row, parent, false);

        // 3. Obtenemos los elementos de la vista
        TextView reproductions = (TextView) rowView.findViewById(R.id.num_repro);
        TextView songTitle = (TextView) rowView.findViewById(R.id.item_title);
        ImageView isFavorite = (ImageView) rowView.findViewById(R.id.item_fav);
        Log.d("reproductions",reproductions.toString());
        // 4. Actualizamos el listview
        reproductions.setText(songArrayList.get(position).getNum_reproductions());
        songTitle.setText(songArrayList.get(position).getTitle());
        if(songArrayList.get(position).getIsFavorite()){
            isFavorite.setImageResource(R.drawable.ic_favorite_black_24dp);
        }else{
            isFavorite.setImageResource(R.drawable.ic_favorite_border_black_24dp);
        }

        // 5. devolvemos la vista
        return rowView;
    }
}
