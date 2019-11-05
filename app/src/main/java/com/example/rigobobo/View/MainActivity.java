package com.example.rigobobo.View;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.work.Constraints;
import androidx.work.NetworkType;
import androidx.work.PeriodicWorkRequest;
import androidx.work.WorkInfo;
import androidx.work.WorkManager;

import com.example.rigobobo.DataManager.AppelloManager;
import com.example.rigobobo.DataManager.InfoManager;
import com.example.rigobobo.DataManager.NotificaManager;
import com.example.rigobobo.DataManager.PrenotazioneManager;
import com.example.rigobobo.DataManager.TassaManager;
import com.example.rigobobo.DataManager.VotoManager;
import com.example.rigobobo.Parser.Esse3Parser;
import com.example.rigobobo.R;
import com.example.rigobobo.Service.Esse3Synchronizer;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.common.util.concurrent.ListenableFuture;

import java.util.List;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {

    private static MainActivity instance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        instance = this;

        //Check if the user has logged in
        if(!Esse3Parser.getInstance().checkCredentials()){
            Context context = MainActivity.this;
            Intent loginActivity = new Intent(context, LoginActivity.class);
            context.startActivity(loginActivity);
            return;
        }
        //Ok, the user has logged in
        setContentView(R.layout.activity_main);

        //Setup the Esse3Synchronizer
        //TODO: known issue: in some devices this run only when the app is open
        ListenableFuture<List<WorkInfo>> future = WorkManager.getInstance().getWorkInfosByTag(Esse3Synchronizer.TAG);
        try {
            List<WorkInfo> list = future.get();
            // start only if no such tasks present
            if ((list == null) || (list.size() == 0)) {
                Constraints constraints = new Constraints.Builder()
                        .setRequiredNetworkType(NetworkType.CONNECTED).build();
                PeriodicWorkRequest periodicWorkRequest = new PeriodicWorkRequest.Builder(
                        Esse3Synchronizer.class, 15, TimeUnit.MINUTES)
                        //Esse3Synchronizer.class, 1, TimeUnit.HOURS)
                        .addTag(Esse3Synchronizer.TAG)
                        .setConstraints(constraints)
                        .build();
                WorkManager.getInstance().enqueue(periodicWorkRequest);
            }
        } catch (Exception e){ System.out.println("EXCEPTION"); }

        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup the bottom bar and the content fragment
        BottomNavigationView bottomNav = findViewById(R.id.bottomnav);
        bottomNav.setOnNavigationItemSelectedListener(bottomNavListener);
        setFragment();

        /** HIDE SIDE BAR
        // Create Navigation drawer and inflate layout
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        navigationView.setNavigationItemSelectedListener(sideNavListener);

        // Adding menu icon to Toolbar
        ActionBar supportActionBar = getSupportActionBar();
        if (supportActionBar != null) {
            supportActionBar.setHomeAsUpIndicator(R.drawable.ic_menu);
            supportActionBar.setDisplayHomeAsUpEnabled(true);
        }
        */
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        /**if (id == R.id.action_settings) {
            return true;
        }*/
        /** HIDE SIDE MENU else if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
        }*/
        if (id == R.id.action_logout) {
            Esse3Parser.getInstance().setCredentials(null, null);
            AppelloManager.getInstance().clear();
            VotoManager.getInstance().clear();
            PrenotazioneManager.getInstance().clear();
            TassaManager.getInstance().clear();
            InfoManager.getInstance().clear();
            NotificaManager.getInstance().clear();
            finish();
            Context context = MainActivity.this;
            Intent intent = new Intent(context,MainActivity.class);
            context.startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


    /* BOTTOM BAR AND FRAGMENT MANEGEMENT */

    private BottomNavigationView.OnNavigationItemSelectedListener bottomNavListener =
            new BottomNavigationView.OnNavigationItemSelectedListener(){
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item){
                    Fragment selectedFragment = getFrameById(item.getItemId());
                    setFragment(selectedFragment);
                    return true;
                }
            };

    private Fragment homeFragment = new HomeContentFragment();
    private Fragment calendarFragment = new CalendarContentFragment();
    private Fragment chatFragment = new NotificaContentFragment();

    private void setFragment(){ setFragment(homeFragment); }
    private void setFragment(Fragment fragment){
        getSupportFragmentManager().beginTransaction().replace(
                R.id.fragment_container, fragment).commit();
    }

    private Fragment getFrameById(int id){
        switch (id){
            case R.id.nav_home:
                return homeFragment;
            case R.id.nav_calendar:
                return calendarFragment;
            case R.id.nav_chat:
                return chatFragment;
        }
        return null;
    }


    /* SIDE MENU */

    /** HIDE SIDE BAR
    private DrawerLayout mDrawerLayout;

    private NavigationView.OnNavigationItemSelectedListener sideNavListener =
            new NavigationView.OnNavigationItemSelectedListener() {
                // This method will trigger on item Click of navigation menu
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    // Set item in checked state
                    menuItem.setChecked(true);
                    // Closing drawer on item click
                    mDrawerLayout.closeDrawers();
                    return true;
                }
            };
     */

    /* useful getContext method */

    public static Context getContext()
    {
        return instance;
    }

}
