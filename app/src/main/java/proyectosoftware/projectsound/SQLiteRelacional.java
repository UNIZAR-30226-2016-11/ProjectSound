package proyectosoftware.projectsound;

/**
 * Created by Guillermo on 9/03/16.
 */
public class SQLiteRelacional {

    protected static final String DATABASE_NAME = "data";
    protected static final String KEY_TITULO = "titulo";
    protected static final String KEY_RUTA = "ruta";
    protected static final String KEY_REPRODUCCIONES = "reproducciones";
    protected static final String KEY_FAVORITO = "favorita";
    protected static final String KEY_DURACION = "duracion";
    protected static final String DATABASE_TABLE_CANCION = "CANCION";
    protected static final String KEY_NOMBRE_PLAYLIST = "nombre";
    protected static final String KEY_DURACION_PLAYLIST = "duracion";
    protected static final String KEY_NUM_CANCIONES = "num_canciones";
    protected static final String DATABASE_TABLE_PLAYLIST = "PLAYLIST";
    protected static final String DATABASE_TABLE_PERTENECE = "PERTENECE";
    protected static final String KEY_CANCION_PERTENECE = "CANCION_ruta";
    protected static final String KEY_NOM_PLAYLIST_PERTENCE = "PLAYLIST_nombre";
    protected static final int DATABASE_VERSION = 2;

    /**
     * Database creation sql statement
     */
    protected static final String DATABASE_CREATE_CANCION =
            "create table "+DATABASE_TABLE_CANCION+" ("+KEY_RUTA+" text primary key, "
                    + KEY_TITULO+" text not null,"+KEY_REPRODUCCIONES+" integer not null, "+KEY_DURACION+" integer not null,"
                    + KEY_FAVORITO+"integer not null);";
    protected static final String DATABASE_CREATE_PLAYLIST =
            "create table "+DATABASE_TABLE_PLAYLIST+" ("+KEY_NOMBRE_PLAYLIST+" text primary key,"
                    + KEY_DURACION_PLAYLIST+" integer not null,"+KEY_NUM_CANCIONES+" integer not null);";
    protected static final String DATABASE_CREATE_PERTENECE =
            "create table "+DATABASE_TABLE_PERTENECE+" ("+KEY_CANCION_PERTENECE+" TEXT PRIMARY KEY,"
                    + KEY_NOM_PLAYLIST_PERTENCE+" TEXT, "
                    + "FOREIGN KEY ("+KEY_CANCION_PERTENECE+") REFERENCES "+DATABASE_TABLE_CANCION+"("+KEY_RUTA+"),"
                    + "FOREIGN KEY ("+KEY_NOM_PLAYLIST_PERTENCE+") REFERENCES "+DATABASE_TABLE_PLAYLIST+"("+KEY_NOMBRE_PLAYLIST+"));";
    protected static final String INSERT_PLAYLIST_TODAS =
            "insert into "+DATABASE_TABLE_PLAYLIST+" ("+KEY_NOMBRE_PLAYLIST+" , "+KEY_DURACION_PLAYLIST+" , "+KEY_NUM_CANCIONES+") values ( 'Todas' , 0 , 0 );";
    protected static final String INSERT_PLAYLIST_FAVORITOS=
            "insert into "+DATABASE_TABLE_PLAYLIST+" ("+KEY_NOMBRE_PLAYLIST+" , "+KEY_DURACION_PLAYLIST+" , "+KEY_NUM_CANCIONES+") values ( 'Favoritos' , 0 , 0 );";
}
