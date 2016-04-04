package proyectosoftware.projectsound;

import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import proyectosoftware.projectsound.CustomAdapters.DbAdapter;
import proyectosoftware.projectsound.Fragments.AddToPlayListFragment;
import proyectosoftware.projectsound.Fragments.FileSelectorFragment;
import proyectosoftware.projectsound.Fragments.PlayerFragment;
import proyectosoftware.projectsound.Fragments.PlaylistsFragment;
import proyectosoftware.projectsound.Fragments.SongsFragment;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawer;
    private static final int FILE_SELECT_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        */
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        DbAdapter db = new DbAdapter(getApplicationContext());
        db.open();
        //Hacemos pantalla de inicio el reproductor
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, new PlayerFragment()).commit();
        navigationView.getMenu().getItem(0).setChecked(true);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
        //Intent pasoPlaylist = new Intent(this,Add_to_playlistActivity.class);
        //startActivity(pasoPlaylist);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment f = new Fragment();
        Bundle args = new Bundle();
        int id = item.getItemId();
        switch (id){
            case R.id.nav_reproductor:
                f = new PlayerFragment();
                break;
            case R.id.nav_añadir_canciones:
                f = new FileSelectorFragment();
                break;
            case R.id.nav_playlists:
                f = new PlaylistsFragment();
                break;
            case R.id.nav_favoritos:
                args.putString(SongsFragment.ARG_PLAYLIST,DbAdapter.DEFAULT_PLAYLIST_FAVORITOS);
                f = new SongsFragment();
                break;
            //TODO borrar estas opciones cuando no se necesite
            case R.id.nav_add_to_playlist:
                f = new AddToPlayListFragment();
        }
        f.setArguments(args);
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, f).addToBackStack(null).commit();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

}
