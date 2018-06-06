package com.example.minalshettigar.splashscreen;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.content.Intent;

import com.google.firebase.auth.FirebaseAuth;

public class friends_detail extends AppCompatActivity {

    TextView frndName;
    TextView frndEmail;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_friends_detail);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        //TextView title = (TextView) findViewById(R.id.activityTitle1);
        //title.setText("This is Add Friends Activity");

        mAuth = FirebaseAuth.getInstance();
        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(1);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_dashboard:
                        Intent intent0 = new Intent(friends_detail.this, Dashboard.class);
                        startActivity(intent0);
                        break;

                    case R.id.action_friends:

                        break;

                    case R.id.action_addexpenses:
                        Intent intent2 = new Intent(friends_detail.this, AddExpenses.class);
                        startActivity(intent2);
                        break;

                    case R.id.action_activity:
                        Intent intent3 = new Intent(friends_detail.this, ActivityList.class);
                        startActivity(intent3);
                        break;

                    case R.id.action_settings:
                        Intent intent4 = new Intent(friends_detail.this, UserSettings.class);
                        startActivity(intent4);
                        break;
                }


                return false;
            }
        });

        Intent intent = getIntent();
        String receivedName=intent.getStringExtra("frndname");
        String recievedemail=intent.getStringExtra("frndemail");




    }


    @Override
    public void onBackPressed() {
        super.onBackPressed();
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
                Intent intent7 = new Intent(friends_detail.this, LoginActivity.class);
                startActivity(intent7);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
