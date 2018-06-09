package com.example.minalshettigar.splashscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.minalshettigar.splashscreen.helper.FriendsExpense;
import com.example.minalshettigar.splashscreen.helper.Item;
import com.example.minalshettigar.splashscreen.helper.UsersDataModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import android.widget.Spinner;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;

import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android .widget.TextView;
import android.text.TextWatcher;
import java.util.HashMap;

public class ManualFragment extends AppCompatActivity {

    EditText inputCategory;
    EditText inputItem;
    EditText inputPrice;
    AutoCompleteTextView searchQuery;

    Button buttonNewItem;
    Button buttonFinish;
    Button buttonRemoveFriend;

    List<UsersDataModel> selectedFriendList = new ArrayList<UsersDataModel>();
    ListView listViewSelectedFriends;
    ListView listViewFriends;
    String currentUserId;
    String itemName;
    double itemPrice;
    Spinner staticSpinner;
    DatabaseReference addItemToFrnds;
    TextView peopleEmail;
    String splitPeopleEmail;
    DatabaseReference addExpenseValueToFrnds;
    double amountFrmCurrUser;
    DatabaseReference dbFriendsRef;
    List<String>list;
    //String personName;
    HashMap<String,String>map;
    private FirebaseAuth mAuth;



    @Nullable
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_manual);

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
                        Intent intent0 = new Intent(ManualFragment.this, Dashboard.class);
                        startActivity(intent0);
                        break;

                    case R.id.action_friends:

                        break;

                    case R.id.action_addexpenses:
                        Intent intent2 = new Intent(ManualFragment.this, AddExpenses.class);
                        startActivity(intent2);
                        break;

                    case R.id.action_activity:
                        Intent intent3 = new Intent(ManualFragment.this, ActivityList.class);
                        startActivity(intent3);
                        break;

                    case R.id.action_settings:
                        Intent intent4 = new Intent(ManualFragment.this, UserSettings.class);
                        startActivity(intent4);
                        break;
                }


                return false;
            }
        });

        inputCategory = (EditText) findViewById(R.id.input_category);
        inputItem = (EditText) findViewById(R.id.input_item);
        inputPrice = (EditText) findViewById(R.id.input_price);
        searchQuery = (AutoCompleteTextView) findViewById(R.id.input_people);
        peopleEmail=(TextView) findViewById(R.id.input_email);
        splitPeopleEmail=peopleEmail.toString();

        buttonNewItem = (Button) findViewById(R.id.button_new_item);
        buttonFinish = (Button) findViewById(R.id.button_finish);
        //buttonRemoveFriend = (Button) view.findViewById(R.id.button_remove_friend);

         staticSpinner = (Spinner) findViewById(R.id.category_manual);
        ArrayAdapter<CharSequence> staticAdapter = ArrayAdapter
                .createFromResource(this, R.array.category_array,
                        android.R.layout.simple_spinner_item);

        staticAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        staticSpinner.setAdapter(staticAdapter);

        addItemToFrnds=FirebaseDatabase.getInstance().getReference("items");
        dbFriendsRef= FirebaseDatabase.getInstance().getReference("friendships");

        currentUserId = FirebaseAuth.getInstance().getCurrentUser().getEmail();

        list=new ArrayList<String>();
        map=new HashMap<String,String>();


        ArrayAdapter<String> name_adapter=new ArrayAdapter<String>(this, android.R.layout.select_dialog_item,list);
        searchQuery = (AutoCompleteTextView) findViewById(R.id.input_people);
        searchQuery.setThreshold(1);
        searchQuery.setAdapter(name_adapter);


        searchQuery.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                }

            @Override
            public void afterTextChanged(Editable s) {

                for(Map.Entry<String,String>entry:map.entrySet())
                {
                    if(entry.getKey().equalsIgnoreCase(s.toString()))
                    {
                        peopleEmail.setText(entry.getValue());
                    }
                }
            }
        });





        buttonNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO save data to db
                itemName = inputItem.getText().toString();
                itemPrice = Double.parseDouble(inputPrice.getText().toString());

                //double splitPrice = splitCost(itemPrice, selectedFriendList.size());

                for (int i = 0; i < selectedFriendList.size(); i++) {
                    String friendId = selectedFriendList.get(i).getFriendId();

                }

                // clear form fields
                inputItem.setText(null);
                inputPrice.setText(null);
                searchQuery.setText(null);
            }
        });

        buttonFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO save data to db
                itemName = inputItem.getText().toString();
                itemPrice = Double.parseDouble(inputPrice.getText().toString());

                String dropdownValue = staticSpinner.getSelectedItem().toString();

                addItemTofriends(dropdownValue);
                addExpensessToUserFriends();

                // clear form fields
                inputItem.setText(null);
                inputPrice.setText(null);
                searchQuery.setText(null);


            }
        });


    }

    private void addItemTofriends(String dropVal)
    {
        if(!itemName.isEmpty() )
        {

            Item itemObj = new Item(itemName,Double.toString(itemPrice));
           String id = addItemToFrnds.push().getKey();

            addItemToFrnds.child(currentUserId.replace(".","")).child(id).setValue(itemObj);
            addItemToFrnds.child(currentUserId.replace(".","")).child("people").
                    child(splitPeopleEmail.replace(".","")).setValue("true");
            addItemToFrnds.child(currentUserId.replace(".","")).child("category").setValue(dropVal);


        }
        }



        private void addExpensessToUserFriends()
        {

            String amtToBeUpdated=Double.toString(amountFrmCurrUser+itemPrice);

            DatabaseReference updateExpenseValue = FirebaseDatabase.getInstance().getReference("user_friends").
                    child(currentUserId.replace(".","")).child("friends");


            updateExpenseValue.child(splitPeopleEmail.replace(".", "")).setValue(amtToBeUpdated);
        }

    @Override
    protected void onStart() {
        super.onStart();


        addExpenseValueToFrnds.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {

                for (DataSnapshot frndSnap : dataSnapshot.getChildren()) {

                    if (frndSnap.getKey().equalsIgnoreCase(currentUserId.replace(".", ""))) {
                        FriendsExpense udm = frndSnap.getValue(FriendsExpense.class);

                        //System.out.println("gugigigigigi "+udm.getFriends().entrySet().size());

                        for (Map.Entry<String, String> entry : udm.getFriends().entrySet()) {
                            if (entry.getKey().equalsIgnoreCase(splitPeopleEmail.replace(".", ""))) {
                                // System.out.println("entry.getValue()******"+entry.getValue());
                                amountFrmCurrUser = Double.parseDouble(entry.getValue());
                            }
                        }
                    }
                    }
                    }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        dbFriendsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Toast.makeText(ManualFragment.this, "inside Method.",
                        Toast.LENGTH_SHORT).show();
                //friendlist.clear();
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

                        selectedFriendList.add(udm1);

                    }
                }

                for(UsersDataModel udm:selectedFriendList)
                {
                    list.add(udm.getFrndName());
                    map.put(udm.getFrndName(),udm.getFriendId());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /*public double splitCost(double price, int numPeople) {
        return price / numPeople;
    }*/


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
                Intent intent7 = new Intent(ManualFragment.this, LoginActivity.class);
                startActivity(intent7);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }
}
