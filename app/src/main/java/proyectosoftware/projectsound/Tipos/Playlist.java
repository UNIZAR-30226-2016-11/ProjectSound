package proyectosoftware.projectsound.Tipos;

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

    public String getNumCancionesPlaylist(){

        return numCancionesPlaylist;
    }

    public String getDuracionPlaylist(){

        return duracionPlaylist;
    }

}
