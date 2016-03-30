package proyectosoftware.projectsound.Tipos;

import android.database.Cursor;
import android.util.StringBuilderPrinter;

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

    public Song(Cursor c){
        //TODO
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
    public void setIsFavorite(boolean isFavorite){
        this.isFavorite=isFavorite;
    }
    public int getDuration_seconds(){
        return duration_seconds;
    }
    public String getDuration(){
        return duration_seconds/60+":"+duration_seconds%60;
    }
    public String getNum_reproductions(){
        return ""+num_reproductions;
    }
    public void increment_num_reproductions(){
        num_reproductions++;
    }
}
