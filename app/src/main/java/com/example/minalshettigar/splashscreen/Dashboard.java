package com.example.minalshettigar.splashscreen;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.example.minalshettigar.splashscreen.helper.UserDbFormat;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.squareup.picasso.Picasso;


public class Dashboard extends AppCompatActivity {

    private static final String TAG = "DashboardActivity";

    TextView category1,category2,category3,category4,category5,category6;
    ImageView user_profile,cat1_img,cat2_img,cat3_img,cat4_img,cat5_img,cat6_img;
    CollapsingToolbarLayout collapsingToolbarLayout;
    FloatingActionButton addBtn;
    //Firebase
    private FirebaseAuth mAuth;
    FirebaseUser user;

    FirebaseDatabase database;

    DatabaseReference users;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);
        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);
        mAuth=FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();
        database = FirebaseDatabase.getInstance();
        users = database.getReference(getString(R.string.dbnode_users));
        Intent myintent = getIntent();


        //UI Components
        category1 = findViewById(R.id.category1_val);
        cat1_img = findViewById(R.id.category1_img);
        category2 = findViewById(R.id.category2_val);
        cat2_img = findViewById(R.id.category2_img);
        category3 = findViewById(R.id.category3_val);
        cat3_img = findViewById(R.id.category3_img);
        category4 = findViewById(R.id.category4_val);
        cat4_img = findViewById(R.id.category4_img);
        category5 = findViewById(R.id.category5_val);
        cat5_img = findViewById(R.id.category5_img);
        category6 = findViewById(R.id.category6_val);
        cat6_img = findViewById(R.id.category6_img);
        user_profile = findViewById(R.id.user_profile);
        addBtn = findViewById(R.id.btnAdd);

        collapsingToolbarLayout = findViewById(R.id.collapsing);
        collapsingToolbarLayout.setExpandedTitleTextAppearance((R.style.ExpandedAppBar));
        collapsingToolbarLayout.setCollapsedTitleTextAppearance((R.style.CollapsedAppBar));
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(Dashboard.this, AddExpenses.class);
                startActivity(intent);
            }
        });

        BottomNavigationView bottomNavigationView = (BottomNavigationView) findViewById(R.id.bottomNavView_Bar);
        BottomNavigationViewHelper.disableShiftMode(bottomNavigationView);
        Menu menu = bottomNavigationView.getMenu();
        MenuItem menuItem = menu.getItem(0);
        menuItem.setChecked(true);
        initFCM();

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

        getUserDetail(user.getUid());

    }

    private void getUserDetail(String userID) {

        System.out.println(userID + users.child(userID).toString());

        users.child(userID).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                UserDbFormat current_user = dataSnapshot.getValue(UserDbFormat.class);
                //Set image
                Picasso.with(getBaseContext()).load(current_user.getPic())
                        .into(user_profile);
                collapsingToolbarLayout.setTitle(current_user.getName());
                System.out.println("name is : "+ current_user.getName());
                category1.setText(current_user.getContact());
                category2.setText(current_user.getEmail());
                category3.setText(current_user.getUid());
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void sendRegistrationToServer(String token) {
        Log.d(TAG, "sendRegistrationToServer: sending token to server: " + token);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        reference.child(getString(R.string.dbnode_notification))
                .child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".",""))
                .child(getString(R.string.field_messaging_token))
                .setValue(token);
        reference.child(getString(R.string.dbnode_notification))
                .child(FirebaseAuth.getInstance().getCurrentUser().getEmail().replace(".",""))
                .child(getString(R.string.field_user_name))
                .setValue(mAuth.getCurrentUser().getDisplayName());

    }


    private void initFCM(){
        String token = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "initFCM: token: " + token);
        sendRegistrationToServer(token);

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


}
