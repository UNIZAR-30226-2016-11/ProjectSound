package proyectosoftware.projectsound;

/**
 * Created by Guillermo on 9/03/16.
 */
public class SQLiteRelacional {

    public static final String DATABASE_NAME = "data";
    public static final String KEY_TITULO = "titulo";
    public static final String KEY_RUTA = "ruta";
    public static final String KEY_REPRODUCCIONES = "reproducciones";
    public static final String KEY_FAVORITO = "favorita";
    public static final String KEY_DURACION = "duracion";
    public static final String DATABASE_TABLE_CANCION = "CANCION";
    public static final String KEY_NOMBRE_PLAYLIST = "nombre";
    public static final String KEY_DURACION_PLAYLIST = "duracion";
    public static final String KEY_NUM_CANCIONES = "num_canciones";
    public static final String DATABASE_TABLE_PLAYLIST = "PLAYLIST";
    public static final String DATABASE_TABLE_PERTENECE = "PERTENECE";
    public static final String KEY_CANCION_PERTENECE = "CANCION_ruta";
    public static final String KEY_NOM_PLAYLIST_PERTENCE = "PLAYLIST_nombre";
    public static final String DEFAULT_PLAYLIST_TODAS = "Todas";
    public static final String DEFAULT_PLAYLIST_FAVORITOS = "Favoritos";
    public static final int DATABASE_VERSION = 2;

    /**
     * Database creation sql statement
     */
    protected static final String DATABASE_CREATE_CANCION =
            "create table "+DATABASE_TABLE_CANCION+" ("+KEY_RUTA+" text primary key, "
                    + KEY_TITULO+" text not null,"+KEY_REPRODUCCIONES+" integer not null, "+KEY_DURACION+" integer not null,"
                    + KEY_FAVORITO+" integer not null);";
    protected static final String DATABASE_CREATE_PLAYLIST =
            "create table "+DATABASE_TABLE_PLAYLIST+" ("+KEY_NOMBRE_PLAYLIST+" text primary key,"
                    + KEY_DURACION_PLAYLIST+" integer not null,"+KEY_NUM_CANCIONES+" integer not null);";
    protected static final String DATABASE_CREATE_PERTENECE =
            "create table "+DATABASE_TABLE_PERTENECE+" ("+KEY_CANCION_PERTENECE+" TEXT PRIMARY KEY,"
                    + KEY_NOM_PLAYLIST_PERTENCE+" TEXT, "
                    + "FOREIGN KEY ("+KEY_CANCION_PERTENECE+") REFERENCES "+DATABASE_TABLE_CANCION+"("+KEY_RUTA+"),"
                    + "FOREIGN KEY ("+KEY_NOM_PLAYLIST_PERTENCE+") REFERENCES "+DATABASE_TABLE_PLAYLIST+"("+KEY_NOMBRE_PLAYLIST+"));";
    protected static final String INSERT_PLAYLIST_TODAS =
            "insert into "+DATABASE_TABLE_PLAYLIST+" ("+KEY_NOMBRE_PLAYLIST+" , "+KEY_DURACION_PLAYLIST+" , "+KEY_NUM_CANCIONES+") values ( "+DEFAULT_PLAYLIST_TODAS+" , 0 , 0 );";
    protected static final String INSERT_PLAYLIST_FAVORITOS=
            "insert into "+DATABASE_TABLE_PLAYLIST+" ("+KEY_NOMBRE_PLAYLIST+" , "+KEY_DURACION_PLAYLIST+" , "+KEY_NUM_CANCIONES+") values ( "+DEFAULT_PLAYLIST_FAVORITOS+" , 0 , 0 );";
}
