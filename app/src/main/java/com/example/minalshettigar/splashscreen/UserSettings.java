package com.example.minalshettigar.splashscreen;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

public class UserSettings extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_activity);

        TextView title = (TextView) findViewById(R.id.activityTitle4);
        title.setText("This is Settings Activity");

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(4);
        menuItem.setChecked(true);

        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.action_dashboard:
                        Intent intent0 = new Intent(UserSettings.this, Dashboard.class);
                        startActivity(intent0);
                        break;

                    case R.id.action_friends:
                        Intent intent1 = new Intent(UserSettings.this, Friends.class);
                        startActivity(intent1);
                        break;

                    case R.id.action_addexpenses:
                        Intent intent2 = new Intent(UserSettings.this, AddExpenses.class);
                        startActivity(intent2);
                        break;

                    case R.id.action_activity:
                        Intent intent3 = new Intent(UserSettings.this, ActivityList.class);
                        startActivity(intent3);
                        break;

                    case R.id.action_settings:
                        break;
                }


                return false;
            }
        });
    }
}
