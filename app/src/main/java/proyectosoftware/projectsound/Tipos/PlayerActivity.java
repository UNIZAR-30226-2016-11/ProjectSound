package proyectosoftware.projectsound.Tipos;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import proyectosoftware.projectsound.R;

/**
 * Created by CABI on 06/04/2016.
 */
public class PlayerActivity extends Activity implements View.OnClickListener{

    ImageButton botonFavorito;
    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.reproductor);

        // Botón de favoritos:
        botonFavorito = (ImageButton) findViewById(R.id.favoritoCancion);
        botonFavorito.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        //Paso 1: Obtener la instancia del administrador de fragmentos
        FragmentManager fragmentManager = getFragmentManager();

        //Paso 2: Crear una nueva transacción
        FragmentTransaction transaction = fragmentManager.beginTransaction();

        //Paso 3: Crear un nuevo fragment y añadirlo
        Fragment fragment = new Fragment ();
        transaction.add(R.id.layout_reproductor,fragment);

        transaction.commit();
    }
}
