package com.example.rigobobo;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Adding Toolbar to Main screen
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Setup the bottom bar and the content fragment
        BottomNavigationView bottomNav = findViewById(R.id.bottomnav);
        bottomNav.setOnNavigationItemSelectedListener(bottomNavListener);
        setFragment();

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
        if (id == R.id.action_settings) {
            return true;
        } else if (id == android.R.id.home) {
            mDrawerLayout.openDrawer(GravityCompat.START);
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
    private Fragment fileFragment = new FileContentFragment();
    private Fragment chatFragment = new ChatContentFragment();

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
            case R.id.nav_file:
                return fileFragment;
            case R.id.nav_chat:
                return chatFragment;
        }
        return null;
    }


    /* SIDE MENU */

    private DrawerLayout mDrawerLayout;

    private NavigationView.OnNavigationItemSelectedListener sideNavListener =
            new NavigationView.OnNavigationItemSelectedListener() {
                // This method will trigger on item Click of navigation menu
                @Override
                public boolean onNavigationItemSelected(MenuItem menuItem) {
                    // Set item in checked state
                    menuItem.setChecked(true);
                    // TODO: handle navigation
                    // Closing drawer on item click
                    mDrawerLayout.closeDrawers();
                    return true;
                }
            };
}
