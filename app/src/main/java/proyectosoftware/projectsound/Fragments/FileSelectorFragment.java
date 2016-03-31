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

import proyectosoftware.projectsound.R;


public class FileSelectorFragment extends Fragment {

    private ArrayList<String> canciones = new ArrayList<String>();
    private ArrayList<String> rutas = new ArrayList<String>();

    public FileSelectorFragment() {
    }

    private void getMusic() {
        final Cursor mCursor = getContext().getContentResolver().query(
                MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
                new String[]{MediaStore.Audio.Media.DISPLAY_NAME,"_data"}, null, null,
                "LOWER(" + MediaStore.Audio.Media.TITLE + ") ASC");

        if (mCursor.moveToFirst()) {
            do {
                //TODO verificar que no está en la base de datos
                canciones.add(mCursor.getString(0));
                rutas.add(mCursor.getString(1));
            } while (mCursor.moveToNext());
        }

        mCursor.close();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fileselector_layout, container, false);
        getActivity().setTitle("Añadir canciones");
        final ListView listview = (ListView) view.findViewById(R.id.fileSelector_listView);
        listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        getMusic();
        final ArrayAdapter<String> listas = new ArrayAdapter<String>(view.getContext(), android.R.layout.simple_list_item_multiple_choice, canciones);
        listview.setAdapter(listas);

        Button aceptar = (Button) view.findViewById(R.id.fileSelector_add);
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
                for (Integer position : positions){
                    canciones.remove(position.intValue()-deleted);
                    rutas.remove(position.intValue()-deleted);
                    deleted++;
                }
                for (String s : selectedItems) {
                    Toast.makeText(v.getContext(), s, Toast.LENGTH_SHORT).show();
                    //TODO hacer insert en la base de datos
                }
                listview.clearChoices();
                listas.notifyDataSetChanged();
            }
        });

        return view;
    }
}
