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

import com.example.minalshettigar.splashscreen.helper.MessageModel;
import com.example.minalshettigar.splashscreen.helper.UserDbFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.Date;
import com.example.minalshettigar.splashscreen.helper.UsersDataModel;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Objects;
import java.util.TimeZone;


public class newFriend_Adding extends AppCompatActivity
{
    private static final String TAG = "newFriend_Adding";
    private EditText name;
    private EditText contactNo;
    private EditText Email;
    private Button btnAddFriend;
    private String currentUserId;
    static final int PICK_CONTACT_REQUEST = 1;
    DatabaseReference addfrnddb;
    DatabaseReference userDb;
    private FirebaseAuth mAuth;
    String userId;
    //vars
    private String mUserId;
    private ArrayList<UserDbFormat> mUsers;



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
        userDb = FirebaseDatabase.getInstance().getReference("users");
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();


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

                initFCM();

            }
        });



    }

    private void addfriendInDB()
    {
    
        String frnd_name=name.getText().toString().trim();
        final String frnd_email=Email.getText().toString().trim();
        
            //Log.d("inside db Method", "addfriendInDB: ");
        if(!TextUtils.isEmpty(frnd_email))
        {
            Date date = Calendar.getInstance().getTime();
            DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
            String strDate = dateFormat.format(date);
            UsersDataModel udm=new UsersDataModel(currentUserId,frnd_email,frnd_name,strDate,strDate);

            String id=addfrnddb.push().getKey();
            Log.d("printing vale of id", "addfriendInDB: "+id);
            addfrnddb.child(id).setValue(udm);

        }

        DatabaseReference msgreference = FirebaseDatabase.getInstance().getReference();

        if(!name.getText().toString().equals("")){

            Log.d(TAG, "addfriendInDB: get value of friend id"+mUserId);
            //create the new message
            MessageModel message = new MessageModel();
            message.setUser_id(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
            message.setMessage(frnd_email);
            message.setTimestamp(getTimestamp());

            //insert the new message
            msgreference
                    .child(getString(R.string.dbnode_messages))
                    .child(frnd_email.replace(".",""))
                    .child(Objects.requireNonNull(msgreference.push().getKey()))
                    .setValue(message);

            Toast.makeText(this, "message sent", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this, "enter a message", Toast.LENGTH_SHORT).show();
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

    private void sendRegistrationToServer(String token) {
        Log.d(TAG, "sendRegistrationToServer: sending token to server: " + token);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child(getString(R.string.dbnode_notification))
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child(getString(R.string.field_messaging_token))
                .setValue(token);
    }


    private void initFCM(){
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "initFCM: token: " + token);
        sendRegistrationToServer(token);

    }

    /**
     * Return the current timestamp in the form of a string
     * @return
     */
    private String getTimestamp(){
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.US);
        sdf.setTimeZone(TimeZone.getTimeZone("Canada/Pacific"));
        return sdf.format(new Date());
    }
}
