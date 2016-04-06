package proyectosoftware.projectsound.CustomAdapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import proyectosoftware.projectsound.SQLiteRelacional;
import proyectosoftware.projectsound.Tipos.Playlist;

/**
 * Created by Guillermo on 9/03/16.
 */
public class DbAdapter extends SQLiteRelacional {
    private final Context mCtx;
    private DatabaseHelper mDbHelper;
    private static SQLiteDatabase mDb;
    private static final String TAG =" DBADAPTER";
    /**
     * Constructor - takes the context to allow the database to be
     * opened/created
     *
     * @param ctx the Context within which to work
     */
    public DbAdapter(Context ctx) {
        this.mCtx = ctx;
    }

    /**
     * Open the database. If it cannot be opened, try to create a new
     * instance of the database. If it cannot be created, throw an exception to
     * signal the failure
     *
     * @return this (self reference, allowing this to be chained in an
     * initialization call)
     * @throws SQLException if the database could be neither opened or created
     */
    public DbAdapter open() throws SQLException {
        mDbHelper = new DatabaseHelper(mCtx);
        mDb = mDbHelper.getWritableDatabase();
        return this;
    }

    public void close() {
        mDbHelper.close();
    }

   public boolean updateFavorite(String path, boolean isFavorite) {
        ContentValues args = new ContentValues();
        String value;
        if(isFavorite){
            value="1";
        }else{
            value="0";
        }
        args.put(KEY_FAVORITO, value);
        return mDb.update(DATABASE_TABLE_CANCION, args, KEY_RUTA + " =?", new String[]{path}) > 0;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {

            db.execSQL(DATABASE_CREATE_CANCION);
            db.execSQL(DATABASE_CREATE_PLAYLIST);
            db.execSQL(DATABASE_CREATE_PERTENECE);
            db.execSQL(INSERT_PLAYLIST_TODAS);
            db.execSQL(INSERT_PLAYLIST_FAVORITOS);

            /*db.execSQL(TRIGGER_DELETE_CANCION);
            db.execSQL(TRIGGER_DELETE_PLAYLIST);
            db.execSQL(TRIGGER_DELETE_CANCION_PLAYLIST);*/
            Log.d("SQLite", DATABASE_CREATE_CANCION);
            Log.d("SQLite", DATABASE_CREATE_PLAYLIST);
            Log.d("SQLite", DATABASE_CREATE_PERTENECE);
            Log.d("SQLite", INSERT_PLAYLIST_TODAS);
            Log.d("SQLite", INSERT_PLAYLIST_FAVORITOS);
           /* Log.d("SQLite",TRIGGER_DELETE_CANCION);
            Log.d("SQLite", TRIGGER_DELETE_PLAYLIST);
            Log.d("SQLite", TRIGGER_DELETE_CANCION_PLAYLIST);*/

            Log.d("SQLite", "DATABASES CREATED");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            Log.w("DbAdapter", "Upgrading database from version " + oldVersion + " to "
                    + newVersion + ", which will destroy all old data");
            db.execSQL("DROP TABLE IF EXISTS notes");
            onCreate(db);
        }
    }
        public long insertCancion(String path, String titulo, int favoritos,int duracion,int reproducciones){
            if(path.length()>0 && titulo.length()>0 && (favoritos==0 || favoritos==1) && duracion>0 && reproducciones>=0){
                ContentValues initialValues = new ContentValues();
                initialValues.put(KEY_RUTA,path);
                initialValues.put(KEY_TITULO, titulo);
                initialValues.put(KEY_FAVORITO,favoritos);
                initialValues.put(KEY_DURACION,duracion);
                initialValues.put(KEY_REPRODUCCIONES,reproducciones);

                return mDb.insert(DATABASE_TABLE_CANCION, null, initialValues);
            }else{
                Log.d(TAG,"Insercción erronea ("+path+","+titulo+","+favoritos+","+duracion+","+reproducciones);
                return -1;
            }
        }
    public long insertPlaylist(String nombre, int duracion,int numero){
        if(nombre.length()>0 && duracion>=0 && numero>=0){
            ContentValues initialValues = new ContentValues();
            initialValues.put(KEY_NOMBRE_PLAYLIST,nombre);
            initialValues.put(KEY_DURACION_PLAYLIST, duracion);
            initialValues.put(KEY_NUM_CANCIONES,numero);
            return mDb.insert(DATABASE_TABLE_PLAYLIST, null, initialValues);
        }else{
            Log.d(TAG,"Insercción erronea ("+nombre+","+duracion+","+ numero);
            return -1;
        }
    }
    public long insertPertenece(String ruta, String nombre){
        if(ruta.length()>0 && nombre.length()>0){
            ContentValues initialValues = new ContentValues();
            initialValues.put(KEY_CANCION_PERTENECE,ruta);
            initialValues.put(KEY_NOM_PLAYLIST_PERTENCE, nombre);
            return mDb.insert(DATABASE_TABLE_PLAYLIST, null, initialValues);
        }else{
            Log.d(TAG,"Insercción erronea ("+ruta+","+nombre);
            return -1;
        }
    }

    public Cursor getAllCancion() {
            return mDb.query(DATABASE_TABLE_CANCION, new String[] {"*"}, null, null,null , null,KEY_TITULO);
    }
    public Cursor getAllFromPlaylist(String playlist){
        switch (playlist){
            case DEFAULT_PLAYLIST_TODAS:
                return getAllCancion();
            case DEFAULT_PLAYLIST_FAVORITOS:
                return  mDb.query(DATABASE_TABLE_CANCION, new String[] {"*"}, KEY_FAVORITO+" = 1", null,null , null,KEY_TITULO);
            default:
                return null;
        }
    }



    //devuelve todas las playlist
    public Cursor fetchAllPlaylist(){
        return mDb.query(DATABASE_TABLE_PLAYLIST,new String[]{KEY_NOMBRE_PLAYLIST},null,null,null,null,KEY_NOMBRE_PLAYLIST);
    }

    //devuelve todas las canciones que hay en una playlist
    public  Cursor canciones_en_playlis(String playlist){
        return mDb.query(DATABASE_TABLE_PERTENECE, new String[]{KEY_CANCION_PERTENECE},KEY_NOM_PLAYLIST_PERTENCE+"="+playlist,null,null,null,KEY_CANCION_PERTENECE);
    }
    //devuelve numero de canciones en una playlist
    /*public int  getNumero(String playlist){
        return mDb.
    }*/

    //devuelve numero de playlist a las que pertenece una cancion


    //devuelve la duracion de una playlist

    //devuelve el nombre de una cancion dada su ruta
    public  Cursor nombre_cancion(String ruta){
        return mDb.query(DATABASE_TABLE_CANCION, new String[]{KEY_TITULO},KEY_RUTA+"="+ruta,null,null,null,KEY_TITULO);
    }
    //devuelve duracion
   public  Cursor DURACION_CANCION(String ruta){
        return mDb.query(DATABASE_TABLE_CANCION, new String[]{KEY_DURACION},KEY_RUTA+"="+ruta,null,null,null,KEY_DURACION);
    }

    //devuelve reproduccion
    public  Cursor num_reproduccion(String ruta){
        return mDb.query(DATABASE_TABLE_CANCION, new String[]{KEY_REPRODUCCIONES},KEY_RUTA+"="+ruta,null,null,null,KEY_REPRODUCCIONES);
    }

    //devuelve favorito
    public  Cursor es_favorito(String ruta){
        return mDb.query(DATABASE_TABLE_CANCION, new String[]{KEY_FAVORITO},KEY_RUTA+"="+ruta,null,null,null,KEY_FAVORITO);
    }

    public  Cursor ruta (String nombre){
        return mDb.query(DATABASE_TABLE_CANCION, new String[]{KEY_RUTA},KEY_TITULO+"="+nombre,null,null,null,KEY_RUTA);
    }
    //devuelve duracion
    public  Cursor DURACION_CANCION_nom(String  nombre){
        return mDb.query(DATABASE_TABLE_CANCION, new String[]{KEY_DURACION},KEY_TITULO+"="+nombre,null,null,null,KEY_DURACION);
    }

    //devuelve reproduccion
    public  Cursor num_reproduccion_nom(String nombre){
        return mDb.query(DATABASE_TABLE_CANCION, new String[]{KEY_REPRODUCCIONES},KEY_TITULO+"="+nombre,null,null,null,KEY_REPRODUCCIONES);
    }

    //devuelve favorito
    public  Cursor es_favorito_nom(String nombre){
        return mDb.query(DATABASE_TABLE_CANCION, new String[]{KEY_FAVORITO},KEY_TITULO+"="+nombre,null,null,null,KEY_FAVORITO);
    }

    //devuelve lista con nombre de favoritos
    public  Cursor lista_nombre_favoritos(){
        return mDb.query(DATABASE_TABLE_CANCION, new String[]{KEY_TITULO},KEY_FAVORITO+"="+1,null,null,null,null,KEY_TITULO);
    }

    //devuelve lista con rutas de favoritos
    public  Cursor lista_ryta_favoritos(){
        return mDb.query(DATABASE_TABLE_CANCION, new String[]{KEY_RUTA},KEY_FAVORITO+"="+1,null,null,null,KEY_RUTA);
    }





    //Metodo para actualizar un playlist
   public void actulizar_playlist(String playlist, long dur, int num){
       Log.i("SQLite", "UPDATE: KEY_NOMBRE_PLAYLIST=" + playlist+"-"+dur+","+num);
        ContentValues values = new ContentValues();
        values.put(KEY_NOMBRE_PLAYLIST,playlist);
        values.put(KEY_DURACION_PLAYLIST,dur);
        values.put(KEY_NUM_CANCIONES,num);
        mDb.update(playlist,values,KEY_NOMBRE_PLAYLIST+"="+ playlist,null);
    }

    public  Cursor getNumLista(String playlist){
        //return mDb.query(DATABASE_TABLE_PERTENECE, new String[]{"COUNT(*)"},KEY_NOM_PLAYLIST_PERTENCE +"="+ playlist,null,null,null,KEY_RUTA);
        return mDb.query(DATABASE_TABLE_PERTENECE, new String[]{"COUNT("+KEY_RUTA+")"},KEY_NOM_PLAYLIST_PERTENCE +"="+ playlist,null,null,null,KEY_RUTA);
    }

    public  Cursor getDuracionLista(String playlist){
        //SELECT SUM(KEY_DURACION)
        //FROM DATABASE_TABLE_CANCION
        //JOIN DATABASE_CREATE_PERTENECE
        //ON DATABASE_TABLE_CANCION.KEY_RUTA=DATABASE_CREATE_PERTENECE.KEY_CANCION_PERTENECE
        //WHERE DATABASE_CREATE_PERTENECE.KEY_NOM_PLAYLIST_PERTENCE='FAVORITOS'
        return mDb.query(DATABASE_TABLE_PERTENECE, new String[]{"SUM("+KEY_DURACION+")"},KEY_NOM_PLAYLIST_PERTENCE +"="+ playlist,null,null,null,KEY_RUTA);
    }
}