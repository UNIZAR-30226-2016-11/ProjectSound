package proyectosoftware.projectsound.Tipos;

import android.content.Context;
import android.database.Cursor;
import android.util.StringBuilderPrinter;

import proyectosoftware.projectsound.CustomAdapters.DbAdapter;

/**
 * Created by novales35 on 30/03/16.
 */
public class Song {
    private String path,title;
    private boolean isFavorite;
    private int duration_seconds,num_reproductions;

    public Song(String title,String path,boolean isFavorite,int duration_seconds,int num_reproductions){
        this.title=title;
        this.path=path;
        this.isFavorite=isFavorite;
        this.duration_seconds=duration_seconds;
        this.num_reproductions=num_reproductions;
    }

    public String getTitle(){
        return title;
    }
    public String getPath(){
        return path;
    }
    public boolean getIsFavorite(){
        return isFavorite;
    }
    public void setIsFavorite(boolean isFavorite, Context ctx){
        this.isFavorite=isFavorite;
        DbAdapter mDb = new DbAdapter(ctx);
        mDb.open();
        mDb.updateFavorite(path,isFavorite);
        mDb.close();

    }
    public int getDuration_seconds(){
        return duration_seconds;
    }
    public String getDuration() {
        int min = duration_seconds / 60;
        int sec = duration_seconds % 60;
        if (sec < 10) {
            return min + ":0" + sec;
        } else {
            return min + ":" + sec;
        }
    }
    public String getNum_reproductions(){
        return ""+num_reproductions;
    }
    public void increment_num_reproductions(){
        num_reproductions++;
    }
}
