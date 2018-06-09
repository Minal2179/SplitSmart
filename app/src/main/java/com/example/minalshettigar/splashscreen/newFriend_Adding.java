package com.example.minalshettigar.splashscreen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.provider.ContactsContract;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import com.example.minalshettigar.splashscreen.helper.UsersDataModel;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;


public class newFriend_Adding extends AppCompatActivity
{
    private EditText name;
    private EditText contactNo;
    private EditText Email;
    private Button btnAddFriend;

    private String currentUserId;
    static final int PICK_CONTACT_REQUEST = 1;
    DatabaseReference addfrnddb;
    DatabaseReference adduserfrnddb;
    private FirebaseAuth mAuth;


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend__adding);

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
                        Intent intent0 = new Intent(newFriend_Adding.this, Dashboard.class);
                        startActivity(intent0);
                        break;

                    case R.id.action_friends:

                        break;

                    case R.id.action_addexpenses:
                        Intent intent2 = new Intent(newFriend_Adding.this, AddExpenses.class);
                        startActivity(intent2);
                        break;

                    case R.id.action_activity:
                        Intent intent3 = new Intent(newFriend_Adding.this, ActivityList.class);
                        startActivity(intent3);
                        break;

                    case R.id.action_settings:
                        Intent intent4 = new Intent(newFriend_Adding.this, UserSettings.class);
                        startActivity(intent4);
                        break;
                }


                return false;
            }
        });


        addfrnddb=FirebaseDatabase.getInstance().getReference("friendships");
       adduserfrnddb=FirebaseDatabase.getInstance().getReference("user_friends");
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        findViewById(R.id.Name).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pickContact = new Intent(Intent.ACTION_PICK, ContactsContract.CommonDataKinds.Phone.CONTENT_URI);
                startActivityForResult(pickContact, PICK_CONTACT_REQUEST);
            }
        });
        name=(EditText)findViewById(R.id.Name);
        contactNo=(EditText)findViewById(R.id.ContactNo);
        Email   =(EditText)findViewById(R.id.Email);
        btnAddFriend=(Button)findViewById(R.id.addFriend);


        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                Toast.makeText(newFriend_Adding.this, "Authentication failed.",
                        Toast.LENGTH_SHORT).show();

               // System.out.println("Hi");
                addfriendInDB();
            }
        });





    }

private void addfriendInDB()
{

String frndName=name.getText().toString().trim();
String frndemail=Email.getText().toString().trim();

    //Log.d("inside db Method", "addfriendInDB: ");

if(!TextUtils.isEmpty(frndemail))
{
    Date date = Calendar.getInstance().getTime();
    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
    String strDate = dateFormat.format(date);
    UsersDataModel udm=new UsersDataModel(currentUserId,frndemail,frndName,strDate,strDate);
    String id=addfrnddb.push().getKey();
    //String email=
    Log.d("printing vale of id", "addfriendInDB: "+id);
    addfrnddb.child(id).setValue(udm);

    adduserfrnddb.child(currentUserId.replace(".","")).child("friends").child(frndemail.replace(".","")).setValue(0);



}




}
    @Override
    protected void onActivityResult ( int requestCode, int resultCode, Intent intent){
        switch (requestCode) {
            case PICK_CONTACT_REQUEST:
                if (resultCode == RESULT_OK) {
                    Uri contactUri = intent.getData();
                    Cursor nameCursor = getContentResolver().query(contactUri, null, null, null, null);
                    if (nameCursor.moveToFirst()) {
                        String name = nameCursor.getString(nameCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                        String number = nameCursor.getString(nameCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                        ((EditText) findViewById(R.id.Name)).setText(name);
                        ((EditText) findViewById(R.id.ContactNo)).setText(number);
                        nameCursor.close();
                    }
                }
                break;
        }
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
                Intent intent7 = new Intent(newFriend_Adding.this, LoginActivity.class);
                startActivity(intent7);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
