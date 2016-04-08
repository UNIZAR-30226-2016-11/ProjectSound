package proyectosoftware.projectsound.Factorys;

import android.database.Cursor;

import java.util.ArrayList;
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
        return extractSongs(db.getAllCancion());
    }
    
    public List<Song> getAllFromPlaylist(String playlist){
        return extractSongs(db.getAllFromPlaylist(playlist)):
    }
}
