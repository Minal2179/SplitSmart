package com.example.minalshettigar.splashscreen;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;


public class Dashboard extends AppCompatActivity {

    private static final String TAG = "DashboardActivity";
    private FirebaseAuth mAuth;

//    private SectionsPageAdapter mSectionsPageAdapter;
//
//    private ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

//        mSectionsPageAdapter = new SectionsPageAdapter(getSupportFragmentManager());
//
//        // Set up the ViewPager with the sections adapter.
//        mViewPager = (ViewPager) findViewById(R.id.container);
//        setupViewPager(mViewPager);
//
//        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(mViewPager);
//
//
//        tabLayout.getTabAt(0).setIcon(R.drawable.ic_notification);
//        tabLayout.getTabAt(1).setIcon(R.drawable.ic_autorenew);

        Intent myintent = getIntent();
        mAuth= FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();


        TextView title = (TextView) findViewById(R.id.activityTitlemain);
        title.setText(user.getEmail());

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.action_dashboard:

                        break;

                    case R.id.action_friends:
                        Intent intent1 = new Intent(Dashboard.this, AddFriends.class);
                        startActivity(intent1);
                        break;

                    case R.id.action_addexpenses:
                        Intent intent2 = new Intent(Dashboard.this, AddExpenses.class);
                        startActivity(intent2);
                        break;

                    case R.id.action_activity:
                        Intent intent3 = new Intent(Dashboard.this, ActivityList.class);
                        startActivity(intent3);
                        break;

                    case R.id.action_settings:
                        Intent intent4 = new Intent(Dashboard.this, UserSettings.class);
                        startActivity(intent4);
                        break;
                }


                return false;
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings:
                // User chose the "Settings" item, show the app settings UI...
                return true;

            case R.id.action_logout:
                // User chose the "Favorite" action, mark the current item
                // as a favorite...

                mAuth.signOut();
                Intent intent = new Intent(Dashboard.this, LoginActivity.class);
                startActivity(intent);

                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

//    private void setupViewPager(ViewPager viewPager) {
//        SectionsPageAdapter adapter = new SectionsPageAdapter(getSupportFragmentManager());
//        adapter.addFragment(new Tab1Fragment());
//        adapter.addFragment(new Tab2Fragment());
//        viewPager.setAdapter(adapter);
//    }

}
