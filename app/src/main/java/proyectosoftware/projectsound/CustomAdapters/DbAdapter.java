package proyectosoftware.projectsound.CustomAdapters;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import proyectosoftware.projectsound.SQLiteRelacional;

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
            Log.d("SQLite", DATABASE_CREATE_CANCION);
            Log.d("SQLite", DATABASE_CREATE_PLAYLIST);
            Log.d("SQLite", DATABASE_CREATE_PERTENECE);
            Log.d("SQLite", INSERT_PLAYLIST_TODAS);
            Log.d("SQLite", INSERT_PLAYLIST_FAVORITOS);
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

}
