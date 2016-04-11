package proyectosoftware.projectsound.Factorys;

import android.database.Cursor;

import java.util.ArrayList;
import java.util.List;

import proyectosoftware.projectsound.CustomAdapters.DbAdapter;
import proyectosoftware.projectsound.R;
import proyectosoftware.projectsound.Tipos.Playlist;

/**
 * Created by dani on 9/04/16.
 */
public class PlaylistFactory {
    private DbAdapter db;

    /**
     * Constructor de la clase
     * @param db adaptador de la base de datos
     */
    public PlaylistFactory(DbAdapter db){
        this.db = db;
    }

    private List<Playlist> extractPlaylist(Cursor mCursor){
        List<Playlist> lista = new ArrayList<Playlist>();
        List<Playlist> head = new ArrayList<Playlist>();
        if (mCursor.moveToFirst()) {
            do {
                //Para cada fila de la base de datos, obtenemos todos los campos
                String nombrePlaylist = mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_NOMBRE_PLAYLIST));
                String durationPlaylist = mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_DURACION_PLAYLIST));
                String numCanciones = mCursor.getString(mCursor.getColumnIndex(DbAdapter.KEY_NUM_CANCIONES));
                //Creamos y añadimos el objeto
                if(nombrePlaylist.equals("Todas")){
                    head.add(0,new Playlist(R.drawable.ic_todas_playlist_256x256,nombrePlaylist,numCanciones + " pistas",durationPlaylist + " min."));
                }else if(nombrePlaylist.equals("Favoritos")){
                    head.add(head.size(),new Playlist(R.drawable.ic_favs_playlist_256x256,nombrePlaylist,numCanciones + " pistas",durationPlaylist + " min."));
                }else{
                    lista.add(new Playlist(R.drawable.ic_nueva_playlist_256x256,nombrePlaylist,numCanciones + " pistas",durationPlaylist + " min."));
                }

                } while (mCursor.moveToNext());
        }
        //Terminamos de usar el cursor
        mCursor.close();
        head.addAll(lista);
        return (List) head;
    }

    public List<Playlist> getAllPlaylist(){
        if(!db.isOpen())
            db.open();
        return extractPlaylist(db.getAllPlaylist());
    }

}
