package com.example.minalshettigar.splashscreen;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.example.minalshettigar.splashscreen.helper.UsersDataModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;
import android.widget.TextView;
import android.widget.BaseAdapter;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.view.ViewGroup;
import android.widget.Toast;


public class AddFriends  extends AppCompatActivity
{

    private EditText searchQuery;
    private ListView listViewfriendsResult;
    String currentUserId;
    List<UsersDataModel>friendlist;



    DatabaseReference dbFriendsRef;
    private FirebaseAuth mAuth;

    public AddFriends() {
        // Required empty public constructor
    }


    public static AddFriends newInstance() {
        AddFriends fragment = new AddFriends();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_add_friends);


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
                        Intent intent0 = new Intent(AddFriends.this, Dashboard.class);
                        startActivity(intent0);
                        break;

                    case R.id.action_friends:

                        break;

                    case R.id.action_addexpenses:
                        Intent intent2 = new Intent(AddFriends.this, AddExpenses.class);
                        startActivity(intent2);
                        break;

                    case R.id.action_activity:
                        Intent intent3 = new Intent(AddFriends.this, ActivityList.class);
                        startActivity(intent3);
                        break;

                    case R.id.action_settings:
                        Intent intent4 = new Intent(AddFriends.this, UserSettings.class);
                        startActivity(intent4);
                        break;
                }


                return false;
            }
        });


        dbFriendsRef= FirebaseDatabase.getInstance().getReference("friendships");

        friendlist=new ArrayList<>();
        listViewfriendsResult=(ListView)findViewById(R.id.listViewfriendsresult) ;

        //on click listViewItem

        listViewfriendsResult.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View myView, int i, long l) {
//
//   Toast.makeText(getApplicationContext(),fruitNames[i],Toast.LENGTH_LONG).show();

                String Name = ((TextView) myView.findViewById(R.id.textViewName)).getText().toString();
                String email = ((TextView) myView.findViewById(R.id.textViewEmail)).getText().toString();
                Intent intent = new Intent(getApplicationContext(),friends_detail.class);
                intent.putExtra("frndname",Name);
                intent.putExtra("frndemail",email);
                startActivity(intent);

            }
        });

        FloatingActionButton fab = (FloatingActionButton)findViewById(R.id.floatingActionButton);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(AddFriends.this, newFriend_Adding.class);
                startActivity(intent);
            }
        });



    }


    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(AddFriends.this, "on Start Method",
                Toast.LENGTH_SHORT).show();
        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        dbFriendsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Toast.makeText(AddFriends.this, "inside Method.",
                        Toast.LENGTH_SHORT).show();
                friendlist.clear();
                for(DataSnapshot frndSnap:dataSnapshot.getChildren())
                {
                    UsersDataModel udm=frndSnap.getValue(UsersDataModel.class);

                    if(udm.getUserId()!=null && udm.getUserId().equalsIgnoreCase(currentUserId))
                    {
                        UsersDataModel udm1=new UsersDataModel();
                        udm1.setFriendId(udm.getFriendId());
                        udm1.setUserId(udm.getUserId());
                        udm1.setFrndName(udm.getFrndName());
                        udm1.setPic(udm.getPic());

                        friendlist.add(udm1);

                    }
                }

                addedFriendsList adapter=new addedFriendsList(AddFriends.this,friendlist);
                listViewfriendsResult.setAdapter(adapter);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
                Intent intent7 = new Intent(AddFriends.this, LoginActivity.class);
                startActivity(intent7);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

}
