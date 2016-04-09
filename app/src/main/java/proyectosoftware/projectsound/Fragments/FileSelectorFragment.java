package proyectosoftware.projectsound.Fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.SparseBooleanArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import proyectosoftware.projectsound.CustomAdapters.DbAdapter;
import proyectosoftware.projectsound.R;


public class FileSelectorFragment extends Fragment {

    private ArrayList<String> canciones = new ArrayList<String>();
    private ArrayList<String> rutas = new ArrayList<String>();
    private DbAdapter mDb;
    private static boolean all_selected = true;

    public FileSelectorFragment() {
    }

    private void getMusic() {
        final Cursor mCursor = getContext().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.DISPLAY_NAME, "_data"}, null, null,
                "LOWER(" + MediaStore.Audio.Media.TITLE + ") ASC");
        String cancion;
        if (mCursor.moveToFirst()) {
            do {
                cancion=mCursor.getString(0);
                canciones.add(cancion.substring(0,cancion.lastIndexOf('.')));
                rutas.add(mCursor.getString(1));
            } while (mCursor.moveToNext());
        }

        mCursor.close();
        purgeMusicFromDatabase();
    }

    private int getDuration(String path) {
        final Cursor mCursor = getContext().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.DURATION}, "_data = '" + path + "'", null,
                null);

        if (mCursor.moveToFirst()) {
            return mCursor.getInt(0) / 1000;
        }

        mCursor.close();
        return -1;
    }

    private void purgeMusicFromDatabase() {
        Cursor mCursor = mDb.getAllCancion();
        if (mCursor.moveToFirst()) {
            do {
                String item = mCursor.getString(mCursor.getColumnIndex(mDb.KEY_RUTA));
                if (rutas.contains(item)) {
                    int position = rutas.indexOf(item);
                    rutas.remove(position);
                    canciones.remove(position);
                }
            } while (mCursor.moveToNext());
        }
        mCursor.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fileselector_layout, container, false);
        getActivity().setTitle("Añadir canciones");
        mDb = new DbAdapter(view.getContext());
        mDb.open();
        final ListView listview = (ListView) view.findViewById(R.id.fileSelector_listView);
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        getMusic();
        final ArrayAdapter<String> listas = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_multiple_choice, canciones);
        listview.setAdapter(listas);

        final Button aceptar = (Button) view.findViewById(R.id.fileSelector_add);
        aceptar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                SparseBooleanArray checked = listview.getCheckedItemPositions();
                ArrayList<String> selectedItems = new ArrayList<String>();
                ArrayList<Integer> positions = new ArrayList<Integer>();
                for (int i = 0; i < checked.size(); i++) {
                    // Item position in adapter
                    int position = checked.keyAt(i);
                    if (checked.valueAt(i)) {
                        selectedItems.add(rutas.get(position));
                        positions.add(position);
                    }
                }
                int deleted = 0;
                for (Integer position : positions) {
                    canciones.remove(position.intValue() - deleted);
                    rutas.remove(position.intValue() - deleted);
                    deleted++;
                }
                for (String path : selectedItems) {
                    String titulo = path.substring(path.lastIndexOf('/') + 1, path.length());
                    titulo = titulo.substring(0,titulo.lastIndexOf('.'));
                    int favorito = 0; //Default false = 0
                    int duration_seconds = getDuration(path);
                    int num_repro = 0; //Default 0
                    mDb.insertCancion(path, titulo, favorito, duration_seconds, num_repro);
                }
                listview.clearChoices();
                listas.notifyDataSetChanged();
                //Simulamos que se ha pulsado la tecla atrás
                String msg;
                int elementos = selectedItems.size();
                if (elementos == 0) {
                    msg = "No se ha seleccionado ninguna canción";
                } else if (elementos == 1) {
                    msg = "Canción añadida correctamente";
                } else if (elementos > 1) {
                    msg = "Canciones añadidas correctamente";
                } else {
                    msg = "Ha ocurrido un error";
                    Log.w("FileSelectorFragment", "Número de insercciones negativo");
                }
                Toast.makeText(getContext(),msg,Toast.LENGTH_LONG).show();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        final Button selection = (Button) view.findViewById(R.id.fileSelector_select);
        selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(int i = 0;i<listview.getCount();i++)
                    listview.setItemChecked(i,all_selected);
                if(all_selected) {
                    selection.setText("Ninguna");
                }else {
                    selection.setText("Todas");
                }
                all_selected= !all_selected;


            }
        });
        return view;
    }
}
