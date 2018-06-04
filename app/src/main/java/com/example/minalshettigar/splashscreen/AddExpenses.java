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

public class AddExpenses extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_expenses_activity);

        TextView title = (TextView) findViewById(R.id.activityTitle2);
        title.setText("This is Add Expenses");

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
                        Intent intent1 = new Intent(AddExpenses.this, Friends.class);
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

}
