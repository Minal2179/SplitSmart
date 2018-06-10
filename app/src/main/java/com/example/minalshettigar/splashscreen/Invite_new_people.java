package com.example.minalshettigar.splashscreen;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import static android.content.Intent.*;

public class Invite_new_people extends AppCompatActivity {


    EditText Name, Email, Contact;
    Button invite;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_new_people);

        Name = (EditText) findViewById(R.id.Name);
        Email = (EditText) findViewById(R.id.Email);
        Contact = (EditText) findViewById(R.id.ContactNo);
        invite = (Button) findViewById(R.id.invite);

        invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email;

                if(Name.getText().equals("") || Email.getText().equals("")||Contact.getText().equals("")){
                    showToast("Enter all 3 fields");
                }else{
                     email = Email.getText().toString();
                    Intent intent =new Intent(ACTION_SEND);
                    intent.putExtra(Intent.EXTRA_EMAIL,email);
                    intent.putExtra(Intent.EXTRA_SUBJECT,"App Invitation");
                    intent.putExtra(Intent.EXTRA_TEXT,"Enjoy the new world of splitting bills, just by clicking a picture");
                    intent.setType("message/rfc822");
                    startActivity(Intent.createChooser(intent,"choose an email client"));
                }
            }
        });


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
                        Intent intent0 = new Intent(Invite_new_people.this, Dashboard.class);
                        startActivity(intent0);
                        break;

                    case R.id.action_friends:
                        Intent intent1 = new Intent(Invite_new_people.this, AddFriends.class);
                        startActivity(intent1);
                        break;

                    case R.id.action_addexpenses:
                        Intent intent4 = new Intent(Invite_new_people.this, AddExpenses.class);
                        startActivity(intent4);
                        break;

                    case R.id.action_activity:
                        Intent intent3 = new Intent(Invite_new_people.this, ActivityList.class);
                        startActivity(intent3);
                        break;

                    case R.id.action_settings:

                        break;
                }


                return false;
            }
        });

    }

    private void showToast(String message) {
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

}
