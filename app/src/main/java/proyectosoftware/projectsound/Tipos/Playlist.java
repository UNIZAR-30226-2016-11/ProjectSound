package proyectosoftware.projectsound.Tipos;

import android.content.Context;

import proyectosoftware.projectsound.CustomAdapters.DbAdapter;
//TODO hacer setters que modifiquen los valores segun bd????
/**
 * Created by dani on 9/03/16.
 */
public class Playlist {
    private int idImagenPlaylist;
    private String tituloPlaylist;
    private String numCancionesPlaylist;
    private String duracionPlaylist;

    public Playlist(int idImagenPlaylist, String tituloPlaylist, String numCancionesPlaylist, String duracionPlaylist){
        this.idImagenPlaylist = idImagenPlaylist;
        this.tituloPlaylist = tituloPlaylist;
        this.numCancionesPlaylist = numCancionesPlaylist;
        this.duracionPlaylist = duracionPlaylist;
    }

    public int getIdImagenPlaylist(){

        return idImagenPlaylist;
    }

    public String getTituloPlaylist(){

        return tituloPlaylist;
    }

    //TODO debera coger el num_canc de la bd
    public String getNumCancionesPlaylist(){

        return numCancionesPlaylist;
    }

    //TODO debera coger la duracion de la bd
    public String getDuracionPlaylist(){

        return duracionPlaylist;
    }

    //TODO terminar cambio de nombre
    public void setTituloPlaylist(Context titulo){
        this.tituloPlaylist = titulo.toString();
        DbAdapter db = new DbAdapter(titulo);
        if(!db.isOpen()){
            db.open();
        }

    }

}
