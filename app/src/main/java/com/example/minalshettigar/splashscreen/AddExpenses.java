package com.example.minalshettigar.splashscreen;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.example.minalshettigar.splashscreen.helper.SectionsPagerAdapter;
import com.google.firebase.auth.FirebaseAuth;

public class AddExpenses extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final int ACTIVITY_NUM = 2;
    private static final int VERIFY_PERMISSIONS_REQUEST = 1;

    private ViewPager mViewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_expenses_activity);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        mAuth = FirebaseAuth.getInstance();
        TextView title = (TextView) findViewById(R.id.activityTitle2);
        title.setText("This is Add Expenses");

        // TODO: Check if camera/photo gallery permissions are granted
        setupViewPager();

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(2);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.action_dashboard:
                        Intent intent0 = new Intent(AddExpenses.this, Dashboard.class);
                        startActivity(intent0);
                        break;

                    case R.id.action_friends:
                        Intent intent1 = new Intent(AddExpenses.this, AddFriends.class);
                        startActivity(intent1);
                        break;

                    case R.id.action_addexpenses:

                        break;

                    case R.id.action_activity:
                        Intent intent3 = new Intent(AddExpenses.this, ActivityList.class);
                        startActivity(intent3);
                        break;

                    case R.id.action_settings:
                        Intent intent4 = new Intent(AddExpenses.this, UserSettings.class);
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
                Intent intent5 = new Intent(AddExpenses.this, LoginActivity.class);
                startActivity(intent5);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    /*
     * return the current tab number
     * 0 = GalleryFragment
     * 1 = PhotoFragment
     * 2 = ManualFragment
     * @return
     */
    public int getCurrentTabNumber() {
        return mViewPager.getCurrentItem();
    }

    /*
     * setup viewpager for managing the tabs
     */
    private void setupViewPager() {
        SectionsPagerAdapter adapter = new SectionsPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new GalleryFragment());
        adapter.addFragment(new PhotoFragment());
        adapter.addFragment(new ManualFragment());


        mViewPager = (ViewPager) findViewById(R.id.viewpager_container);
        mViewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) findViewById(R.id.tabsBottom);
        tabLayout.setupWithViewPager(mViewPager);

        tabLayout.getTabAt(0).setText(getString(R.string.gallery));
        tabLayout.getTabAt(1).setText(getString(R.string.photo));
        tabLayout.getTabAt(2).setText(getString(R.string.manual));

    }

    public int getTask() {
        return getIntent().getFlags();
    }

}
