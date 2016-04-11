package proyectosoftware.projectsound.Factorys;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import proyectosoftware.projectsound.CustomAdapters.DbAdapter;
import proyectosoftware.projectsound.Tipos.Song;

/**
 * Created by novales35 on 8/04/16.
 */
public class SongFactory {
    private DbAdapter db;
    public SongFactory(DbAdapter db){
        this.db=db;
    }
    private List<Song> extractSongs(Cursor mCursor){
        List<Song> lista = new ArrayList<Song>();
        if (mCursor.moveToFirst()) {
            do {
                //Para cada fila de la base de datos, obtenemos todos los campos
                String titulo = mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_TITULO));
                String path = mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_RUTA));
                boolean fav = (mCursor.getInt(mCursor.getColumnIndex(DbAdapter.KEY_FAVORITO)) == 1);
                int duration = mCursor.getInt(mCursor.getColumnIndex(DbAdapter.KEY_DURACION));
                int reproductions = mCursor.getInt(mCursor.getColumnIndex(DbAdapter.KEY_REPRODUCCIONES));
                //Creamos y añadimos el objeto
                lista.add(new Song(titulo, path, fav, duration, reproductions));
            } while (mCursor.moveToNext());
        }
        //Terminamos de usar el cursor
        mCursor.close();
        return (List) lista;
    }
    public List<Song> getAllSongs(){
        if(!db.isOpen())
            db.open();
        return extractSongs(db.getAllCancion());
    }

    public List<Song> getAllFromPlaylist(String playlist){
        if(!db.isOpen())
            db.open();
        return extractSongs(db.getAllFromPlaylist(playlist));
    }
    /**
     * Organiza las canciones por las reproducciones
     */
    public void orderByReproductions(ArrayList<Song> canciones) {
        if (canciones.size() > 0) {
            Collections.sort(canciones, new Comparator<Song>() {
                @Override
                public int compare(final Song object1, final Song object2) {
                    return object2.getNum_reproductions().compareTo(object1.getNum_reproductions());
                }
            });
        }
    }
    /**
     * Organiza las canciones por su duración de mayor a menor
     */
    public void orderByDuration(ArrayList<Song> canciones) {
        if (canciones.size() > 0) {
            Collections.sort(canciones, new Comparator<Song>() {
                @Override
                public int compare(final Song object1, final Song object2) {
                    return Integer.valueOf(object2.getDuration_seconds())
                            .compareTo(object1.getDuration_seconds());
                }
            });
        }
    }

    /**
     * Organiza las canciones por el Título
     */
    public void orderByTitle(ArrayList<Song> canciones) {
        if (canciones.size() > 0) {
            Collections.sort(canciones, new Comparator<Song>() {
                @Override
                public int compare(final Song object1, final Song object2) {
                    return object1.getTitle().compareTo(object2.getTitle());
                }
            });
        }
    }
}
