package proyectosoftware.projectsound.CustomAdapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
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

    public boolean isOpen(){
        return  mDb.isOpen();
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
            db.execSQL(trigger_delete_duracion);
            db.execSQL(trigger_delete_numero);
            db.execSQL(trigger_insert_duracion);
            db.execSQL(trigger_insert_numero);
            db.execSQL(trigger_insert_cancion_numero);
            db.execSQL(trigger_delete_cancion_numero);
            db.execSQL(trigger_insert_cancion_duracion);
            db.execSQL(trigger_delete_cancion_duracion);
            //db.execSQL(trigger_fav_numero);
            //db.execSQL(trigger_fav_duracion);
            Log.d("SQLite", DATABASE_CREATE_CANCION);
            Log.d("SQLite", DATABASE_CREATE_PLAYLIST);
            Log.d("SQLite", DATABASE_CREATE_PERTENECE);
            Log.d("SQLite", INSERT_PLAYLIST_TODAS);
            Log.d("SQLite", INSERT_PLAYLIST_FAVORITOS);
            Log.d("SQLite",trigger_delete_duracion);
            Log.d("SQLite",trigger_delete_numero);
            Log.d("SQLite",trigger_insert_duracion);
            Log.d("SQLite",trigger_insert_numero);


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
    public long insertNewPlaylist(String nombrePlaylist){
        if(nombrePlaylist.length()>0){
            ContentValues initialValues = new ContentValues();
            initialValues.put(KEY_NOMBRE_PLAYLIST,nombrePlaylist);
            initialValues.put(KEY_DURACION_PLAYLIST, 0);
            initialValues.put(KEY_NUM_CANCIONES,0);
            return mDb.insert(DATABASE_TABLE_PLAYLIST, null, initialValues);
        }else{
            Log.d(TAG,"Insercción erronea ("+nombrePlaylist+","+0+","+ 0);
            return -1;
        }
    }
    public long insertToPlaylist(String rutaCancion, String playlist){
        if(rutaCancion.length()>0 && playlist.length()>0){
            ContentValues initialValues = new ContentValues();
            initialValues.put(KEY_CANCION_PERTENECE,rutaCancion);
            initialValues.put(KEY_NOM_PLAYLIST_PERTENCE, playlist);
            return mDb.insert(DATABASE_TABLE_PERTENECE, null, initialValues);
        }else{
            Log.d(TAG,"Insercción erronea ("+rutaCancion+","+playlist);
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
                String[] playlist_array = {playlist};

                //Creamos el constructor de consultas
                SQLiteQueryBuilder QB = new SQLiteQueryBuilder();

                //Especificamos el JOIN
                QB.setTables(DATABASE_TABLE_CANCION +
                      " LEFT OUTER JOIN " + DATABASE_TABLE_PERTENECE + " ON " +
                      KEY_RUTA + " = " + KEY_CANCION_PERTENECE);
                //Especificamos el select, en este caso, todos los atributos de la tabla Cancion
                String[] select = {KEY_TITULO,KEY_RUTA,KEY_REPRODUCCIONES,KEY_FAVORITO,KEY_DURACION};
                //Consultamos y devolvemos resultado
                return QB.query(mDb, select, KEY_NOM_PLAYLIST_PERTENCE+"=?", playlist_array, null, null, KEY_TITULO);
        }
    }


    //devuelve todas las playlist
    public Cursor getAllPlaylist(){
        return mDb.query(DATABASE_TABLE_PLAYLIST,new String[]{KEY_NOMBRE_PLAYLIST,KEY_NUM_CANCIONES,KEY_DURACION_PLAYLIST},null,null,null,null,KEY_NOMBRE_PLAYLIST);
    }

    //comprueba si una playlist esta en la bd
    public boolean comprobarPlaylist(String nombre){
        Cursor c= getAllPlaylist();
        boolean pertenece  = false;
        if(c.moveToFirst()){
            do {
                String ruta = c.getString(c.getColumnIndex(DbAdapter.KEY_NOMBRE_PLAYLIST));
                if(ruta.equals(nombre)){
                    pertenece = true;
                    return pertenece;
                }
            } while(c.moveToNext());
        }
        return  pertenece;
    }
//TODO NO SE DEBERIA USAR ESTO
    /*
    //devuelve numero de canciones en una playlist
    /*public int  getNumero(String playlist){
        return mDb.
    }

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
    */




    //Metodo para actualizar un playlist
   public void actualizar_playlist(String playlist, long dur, int num){
       //TODO no hace falta, esto lo hará un trigger
       Log.i("SQLite", "UPDATE: KEY_NOMBRE_PLAYLIST=" + playlist+"-"+dur+","+num);
        ContentValues values = new ContentValues();
        values.put(KEY_NOMBRE_PLAYLIST,playlist);
        values.put(KEY_DURACION_PLAYLIST,dur);
        values.put(KEY_NUM_CANCIONES,num);
        mDb.update(playlist,values,KEY_NOMBRE_PLAYLIST+"="+ playlist,null);
    }


    public boolean cambiar_nombre_playlist(String nombre_viejo, String nombre_nuevo){
        if(insertNewPlaylist(nombre_nuevo)<0){
            return false;
        }
        else{
            Log.d(nombre_nuevo,"insertado");
        }
        Cursor c= getAllFromPlaylist(nombre_viejo);
        if(c.moveToFirst()){

            do {
                String ruta = c.getString(c.getColumnIndex(DbAdapter.KEY_RUTA));
                Log.d(nombre_nuevo, ruta);

                if(insertToPlaylist(ruta, nombre_nuevo)<0){

                    return false;
                }
                else{
                    Log.d("insertado",ruta);
                }
            } while(c.moveToNext());
        }
        return deletePlaylist(nombre_viejo);


    }

    public  Cursor getNumSongFromPlaylist(String playlist){
        String[] playlist_array = {playlist};
        return mDb.query(DATABASE_TABLE_PERTENECE, new String[]{"COUNT("+KEY_CANCION_PERTENECE+")"},KEY_NOM_PLAYLIST_PERTENCE +"= ?",playlist_array,null,null,KEY_CANCION_PERTENECE);
    }

    public  Cursor getDuracionLista(String playlist){
        String[] playlist_array = {playlist};
        return mDb.query(DATABASE_TABLE_PLAYLIST,new String[]{KEY_DURACION_PLAYLIST},KEY_NOMBRE_PLAYLIST+" =?",playlist_array,null,null,KEY_NOMBRE_PLAYLIST);
    }

    public boolean deleteCancion(String cancion){

       boolean borrar;
        boolean borrado;
        borrar = mDb.delete(DATABASE_TABLE_PERTENECE,KEY_CANCION_PERTENECE+"=?", new String[] {cancion})>0;
        if(borrar){
            Log.d("borrado","cancion de pertenece");
        }

        else{
            Log.d("no borrado","cancion de pertenece");
        }
        borrado = mDb.delete(DATABASE_TABLE_CANCION,KEY_RUTA+"=?", new String[] {cancion})>0;
        if(borrado){
            Log.d("borrado","cancion de tabla");
        }

        else{
            Log.d("no borrado","cancion de tabla");
        }
        return borrar&&borrado;
    }

    public boolean deletePlaylist(String playlist){
        boolean borrar;
        boolean borrado;
        borrar = mDb.delete(DATABASE_TABLE_PERTENECE,KEY_NOM_PLAYLIST_PERTENCE+"=?", new String[] {playlist})>0;
        if(borrar){
            Log.d("borrado","playlist de pertenece");
        }

        else{
            Log.d("no borrado","playlist de pertenece");
        }
        borrado = mDb.delete(DATABASE_TABLE_PLAYLIST,KEY_NOMBRE_PLAYLIST+"=?", new String[] {playlist})>0;
        if(borrado){
            Log.d("borrado","playlist de tabla");
        }

        else{
            Log.d("no borrado","playlist de tabla");
        }
        return borrar&&borrado;


    }


    public boolean deleteFromPlaylist(String cancion, String playlist){
           return mDb.delete(DATABASE_TABLE_PERTENECE,KEY_CANCION_PERTENECE+"=?"+" AND "+KEY_NOM_PLAYLIST_PERTENCE+"=?",new String[]{cancion,playlist})>0;

    }


   /* public int incrementarReproduccionesCancion(String ruta){




        Cursor c = mDb.query(DATABASE_TABLE_CANCION, new String[] {KEY_REPRODUCCIONES}, KEY_RUTA+"= ?"+ruta, null,null , null,null);






    }*/
    public void incrementarReproduccionCancion(String ruta){
        Cursor c = mDb.query(DATABASE_TABLE_CANCION, new String[]{KEY_REPRODUCCIONES} ,KEY_RUTA+"= ?",new String[]{ruta},null,null,null,null);

        c.moveToFirst();
        int reproducciones = c.getInt(c.getColumnIndex(DbAdapter.KEY_REPRODUCCIONES));

        ContentValues args = new ContentValues();
        reproducciones++;
        args.put(KEY_REPRODUCCIONES, reproducciones);
        mDb.update(DATABASE_TABLE_CANCION, args, KEY_RUTA + " =?", new String[]{ruta});



    }


}