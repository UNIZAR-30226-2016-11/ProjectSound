package proyectosoftware.projectsound;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.w3c.dom.Text;

import proyectosoftware.projectsound.R;

/**
 * Created by DAVID on 03/03/2016.
 */
public class LayoutActivity extends Activity {

    private TextView reproductor;
    private TextView addcanciones;
    private TextView playlist;
    private TextView favoritos;

    @Override
    public void onCreate (Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.linear_layout);

        reproductor = (TextView) findViewById(R.id.reproductor);
        addcanciones = (TextView) findViewById(R.id.canciones);
        playlist = (TextView) findViewById(R.id.playlists);
        favoritos = (TextView) findViewById(R.id.favoritos);

        reproductor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        addcanciones.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

        playlist.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });

        favoritos.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){

            }
        });
    }
}
