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
    public static String PLAYLIST_ESPECIFICA = "";
    public static final int DATABASE_VERSION = 2;
    public static final String  KEY_ID="id";
    public static final String TRIGGER_INSERT_CANCION_PLAYLIST_DURACION="insert_duracion";
    public static final String  TRIGGER_DELETE_CANCION_PLAYLIST_DURACION="delete_duracion";
    public static final String TRIGGER_INSERT_CANCION_PLAYLIST_NUMERO="insert_numero";
    public static final String  TRIGGER_DELETE_CANCION_PLAYLIST_NUMERO="delete_numero";
    public static final String TRIGGER_INSERT_CANCION_NUMERO="INSERT_CANCION_NUMERO";
    public static final String  TRIGGER_DELETE_CANCION_NUMERO="DELETE_CANCION_NUMERO";
    public static final String TRIGGER_INSERT_CANCION_DURACION="INSERT_CANCION_DURACION";
    public static final String  TRIGGER_DELETE_CANCION_DURACION="DELETE_CANCION_DURACION";
    public static final String  TRIGGER_FAV_NUMERO="FAV_NUMERO";
    public static final String  TRIGGER_FAV_DURACION="FAV_DURACION";

    /**
     * Database creation sql statement
     */
    protected static final String DATABASE_CREATE_CANCION =
            "create table " + DATABASE_TABLE_CANCION + " (" + KEY_RUTA + " text primary key, "
                    + KEY_TITULO + " text not null," + KEY_REPRODUCCIONES + " integer not null, " + KEY_DURACION + " integer not null,"
                    + KEY_FAVORITO + " integer not null);";
    protected static final String DATABASE_CREATE_PLAYLIST =
            "create table " + DATABASE_TABLE_PLAYLIST + " ( "+ KEY_NOMBRE_PLAYLIST + " text primary key,"
                    + KEY_DURACION_PLAYLIST + " integer not null," + KEY_NUM_CANCIONES + " integer not null);";
    protected static final String DATABASE_CREATE_PERTENECE =
            "create table " + DATABASE_TABLE_PERTENECE + " ("+KEY_ID+ " integer primary key autoincrement," + KEY_CANCION_PERTENECE + " TEXT,"
                    + KEY_NOM_PLAYLIST_PERTENCE + " TEXT, "
                    + "FOREIGN KEY (" + KEY_CANCION_PERTENECE + ") REFERENCES " + DATABASE_TABLE_CANCION + "(" + KEY_RUTA + "),"
                    + "FOREIGN KEY (" + KEY_NOM_PLAYLIST_PERTENCE + ") REFERENCES " + DATABASE_TABLE_PLAYLIST + "(" + KEY_NOMBRE_PLAYLIST + "));";
    protected static final String INSERT_PLAYLIST_TODAS =
            "insert into " + DATABASE_TABLE_PLAYLIST + " (" + KEY_NOMBRE_PLAYLIST + " , " + KEY_DURACION_PLAYLIST + " , " + KEY_NUM_CANCIONES + ") values ( '" + DEFAULT_PLAYLIST_TODAS + "' , 0 , 0 );";
    protected static final String INSERT_PLAYLIST_FAVORITOS =
            "insert into " + DATABASE_TABLE_PLAYLIST + " (" + KEY_NOMBRE_PLAYLIST + " , " + KEY_DURACION_PLAYLIST + " , " + KEY_NUM_CANCIONES + ") values ( '" + DEFAULT_PLAYLIST_FAVORITOS + "' , 0 , 0 );";


    protected static final String trigger_insert_duracion =  "CREATE TRIGGER " +TRIGGER_INSERT_CANCION_PLAYLIST_DURACION +
            " BEFORE INSERT ON " + DATABASE_TABLE_PERTENECE + " FOR EACH ROW " + " BEGIN " + " UPDATE "+
            DATABASE_TABLE_PLAYLIST  + " SET " + KEY_DURACION_PLAYLIST +" = " +KEY_DURACION_PLAYLIST+" +(SELECT " + KEY_DURACION+
            " FROM " + DATABASE_TABLE_CANCION+  " WHERE " +  KEY_RUTA+ " =NEW." +KEY_CANCION_PERTENECE+ " ) "+
            " WHERE " +  KEY_NOMBRE_PLAYLIST +" = NEW." +KEY_NOM_PLAYLIST_PERTENCE+ " ; " +" END ";

    protected static final String trigger_delete_duracion =  "CREATE TRIGGER " + TRIGGER_DELETE_CANCION_PLAYLIST_DURACION+
            " BEFORE DELETE ON " + DATABASE_TABLE_PERTENECE + " FOR EACH ROW " + " BEGIN " + " UPDATE "+
            DATABASE_TABLE_PLAYLIST  + " SET " + KEY_DURACION_PLAYLIST +" = " +KEY_DURACION_PLAYLIST+" -(SELECT " + KEY_DURACION+
            " FROM " + DATABASE_TABLE_CANCION+  " WHERE " +  KEY_RUTA+ " =OLD." +KEY_CANCION_PERTENECE+ " ) "+
            " WHERE " +  KEY_NOMBRE_PLAYLIST +" = OLD." +KEY_NOM_PLAYLIST_PERTENCE+ " ; " +" END ";

    protected static final String trigger_insert_numero = "CREATE TRIGGER "+ TRIGGER_INSERT_CANCION_PLAYLIST_NUMERO+
            " BEFORE INSERT ON " + DATABASE_TABLE_PERTENECE + " FOR EACH ROW " + " BEGIN " +" UPDATE "+
            DATABASE_TABLE_PLAYLIST  + " SET "+ KEY_NUM_CANCIONES +" = " + KEY_NUM_CANCIONES+" + 1 "+
            " WHERE "+  KEY_NOMBRE_PLAYLIST+" =NEW."+KEY_NOM_PLAYLIST_PERTENCE+" ; "+" END ";

    protected static final String trigger_delete_numero = "CREATE TRIGGER "+ TRIGGER_DELETE_CANCION_PLAYLIST_NUMERO+
            " BEFORE DELETE ON " + DATABASE_TABLE_PERTENECE + " FOR EACH ROW " + " BEGIN " +" UPDATE "+
            DATABASE_TABLE_PLAYLIST  + " SET "+ KEY_NUM_CANCIONES +" = " + KEY_NUM_CANCIONES+" - 1 "+
            " WHERE "+  KEY_NOMBRE_PLAYLIST+" =OLD."+KEY_NOM_PLAYLIST_PERTENCE+" ; "+" END ";

    protected static final String trigger_insert_cancion_numero = "CREATE TRIGGER "+ TRIGGER_INSERT_CANCION_NUMERO+
            " BEFORE INSERT ON " + DATABASE_TABLE_CANCION + " FOR EACH ROW " + " BEGIN " +" UPDATE "+
            DATABASE_TABLE_PLAYLIST  + " SET "+ KEY_NUM_CANCIONES +" = " + KEY_NUM_CANCIONES+" + 1 "+
            " WHERE "+  KEY_NOMBRE_PLAYLIST+" = '"+DEFAULT_PLAYLIST_TODAS+"' ; "+" END ";

    protected static final String trigger_delete_cancion_numero = "CREATE TRIGGER "+ TRIGGER_DELETE_CANCION_NUMERO+
            " BEFORE DELETE ON " + DATABASE_TABLE_CANCION + " FOR EACH ROW " + " BEGIN " +" UPDATE "+
            DATABASE_TABLE_PLAYLIST  + " SET "+ KEY_NUM_CANCIONES +" = " + KEY_NUM_CANCIONES+" - 1 "+
            " WHERE "+  KEY_NOMBRE_PLAYLIST+" = '"+DEFAULT_PLAYLIST_TODAS+"' ; "+" END ";

    protected static final String trigger_insert_cancion_duracion =  "CREATE TRIGGER " +TRIGGER_INSERT_CANCION_DURACION +
            " BEFORE INSERT ON " + DATABASE_TABLE_CANCION + " FOR EACH ROW " + " BEGIN " + " UPDATE "+
            DATABASE_TABLE_PLAYLIST  + " SET " + KEY_DURACION_PLAYLIST +" = " +KEY_DURACION_PLAYLIST+" + NEW."+KEY_DURACION+
            " WHERE " +  KEY_NOMBRE_PLAYLIST +" = '" +DEFAULT_PLAYLIST_TODAS+ "' ; " +" END ";

    protected static final String trigger_delete_cancion_duracion =  "CREATE TRIGGER " + TRIGGER_DELETE_CANCION_DURACION+
            " BEFORE DELETE ON " + DATABASE_TABLE_CANCION + " FOR EACH ROW " + " BEGIN " + " UPDATE "+
            DATABASE_TABLE_PLAYLIST  + " SET " + KEY_DURACION_PLAYLIST +" = " +KEY_DURACION_PLAYLIST+" - OLD."+KEY_DURACION+
            " WHERE " +  KEY_NOMBRE_PLAYLIST +" = '" +DEFAULT_PLAYLIST_TODAS+ "' ; " +" END ";

    protected static final String trigger_fav_numero = "CREATE TRIGGER "+ TRIGGER_FAV_NUMERO+
            " AFTER UPDATE ON " + DATABASE_TABLE_CANCION + " FOR EACH ROW " + " BEGIN " +" UPDATE "+
            DATABASE_TABLE_PLAYLIST  + " SET "+ KEY_NUM_CANCIONES +" = (SELECT COUNT("+KEY_RUTA+") FROM "+DATABASE_TABLE_CANCION+" WHERE "+KEY_FAVORITO+"='1')"+
            " WHERE "+  KEY_NOMBRE_PLAYLIST+" = '"+DEFAULT_PLAYLIST_FAVORITOS+"' ; "+" END ";

    protected static final String trigger_fav_duracion = "CREATE TRIGGER "+ TRIGGER_FAV_DURACION+
            " AFTER UPDATE ON " + DATABASE_TABLE_CANCION + " FOR EACH ROW " + " BEGIN " +" UPDATE "+
            DATABASE_TABLE_PLAYLIST  + " SET "+ KEY_DURACION_PLAYLIST +" = (SELECT SUM("+KEY_DURACION+") FROM "+DATABASE_TABLE_CANCION+" WHERE "+KEY_FAVORITO+"='1')"+
            " WHERE "+  KEY_NOMBRE_PLAYLIST+" = '"+DEFAULT_PLAYLIST_FAVORITOS+"' ; "+" END ";
}

