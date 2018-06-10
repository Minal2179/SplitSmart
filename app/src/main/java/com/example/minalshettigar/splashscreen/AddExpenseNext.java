package com.example.minalshettigar.splashscreen;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.util.Log;

import com.example.minalshettigar.splashscreen.helper.ExpenseDataModel;
import com.example.minalshettigar.splashscreen.helper.FriendListViewAdapter;
import com.example.minalshettigar.splashscreen.helper.UsersDataModel;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.TextBlock;
import com.google.android.gms.vision.text.TextRecognizer;
import com.google.firebase.auth.FirebaseAuth;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;

public class AddExpenseNext extends AppCompatActivity implements SearchView.OnQueryTextListener {

    private static final String TAG = "AddExpenseNext Activity";

    private FirebaseAuth mAuth;

    private TextView textView;
    private final int CAMERA_SCAN_TEXT = 0;
    private final int LOAD_IMAGE_RESULTS = 1;

    // vars
    private String mAppend = "file:/";
    private String imgUrl;
    private Bitmap bitmap;
    private Intent intent;

    ArrayList<ExpenseDataModel> expenseDataModels;
    ListView itemListView;
    private static CustomExpenseAdapter adapter;

    SearchView editSearch;
    ListView allFriendsListView;
    String[] allFriendsNameList;
    ArrayList<UsersDataModel> allFriendsArrayList = new ArrayList<UsersDataModel>();
    FriendListViewAdapter allFriendsAdapter;

    ListView selectedFriendsListView;
    String[] selectedFriendsNameList;
    ArrayList<UsersDataModel> selectedFriendsArrayList = new ArrayList<UsersDataModel>();
    FriendListViewAdapter selectedFriendsAdapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense_next);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        mAuth= FirebaseAuth.getInstance();
        TextView textView = (TextView) findViewById(R.id.textDetected);
        textView.setText("TESTING: DETECTED TEXT SHOULD BE SHOWN HERE");

        itemListView = (ListView) findViewById(R.id.list);
        expenseDataModels = new ArrayList<>();

        // add parsed text info to expenseDataModels
        expenseDataModels.add(new ExpenseDataModel("item1", 1.00));
        expenseDataModels.add(new ExpenseDataModel("item2", 4.00));

        adapter = new CustomExpenseAdapter(expenseDataModels, getApplicationContext());
        itemListView.setAdapter(adapter);
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ExpenseDataModel expenseDataModel = expenseDataModels.get(position);
                // assign friends

                // Search view for assigning friends
                editSearch = (SearchView) findViewById(R.id.searchView);
                allFriendsListView = (ListView) findViewById(R.id.allFriendsListView);

                // generate sample data for all friends
                allFriendsNameList = new String[] {"ALL FRIENDS: A", "ALL FRIENDS: B", "ALL FRIENDS: C" };
                for (int i = 0; i < allFriendsNameList.length; i++) {
                    UsersDataModel usersDataModel = new UsersDataModel();
                    allFriendsArrayList.add(usersDataModel);
                }
                // pass results to FriendListViewAdapter class
                allFriendsAdapter = new FriendListViewAdapter(getApplicationContext(), allFriendsArrayList);
                // bind the allFriendsAdapter to the listview
                allFriendsListView.setAdapter(allFriendsAdapter);


            }
        });





        // test - add friend names to friendArrayList
        selectedFriendsNameList = new String[] { "Friend A", "Friend B", "Friend C" };
        selectedFriendsListView = findViewById(R.id.selectedFriendsListView);
        for (int i = 0; i < selectedFriendsNameList.length; i++) {
            UsersDataModel usersDataModel = new UsersDataModel();
            selectedFriendsArrayList.add(usersDataModel);
        }

        if (selectedFriendsArrayList.size() > 0) {
            Log.d(TAG, "SELECTEDFRIENDARRAYLIST SIZE IS " + selectedFriendsArrayList.size());
        }
        else {
            Log.d(TAG, "SELECTEDFRIENDARRAYLIST SIZE IS NOT MORE THAN 0");
        }

        for (int i = 0; i < selectedFriendsArrayList.size(); i++) {
            Log.d(TAG, "SELECTEDfriendArrayList: " + selectedFriendsArrayList.get(i).getFrndName());
        }
        /*
        selectedFriendsAdapter = new FriendListViewAdapter(this, selectedFriendsArrayList);
        selectedFriendsListView.setAdapter(selectedFriendsAdapter);

        editSearch.setOnQueryTextListener(this);
        */

        // get img info from AddExpenses Activity
        intent = getIntent();
        if (intent.hasExtra(getString(R.string.selected_image))) {
            imgUrl = intent.getStringExtra(getString(R.string.selected_image));
            bitmap = getBitmap(imgUrl);
        }
        else if (intent.hasExtra(getString(R.string.selected_bitmap))) {
            bitmap = (Bitmap) intent.getParcelableExtra(getString(R.string.selected_bitmap));
        }


        TextRecognizer textRecognizer = new TextRecognizer.Builder(getApplicationContext()).build();

        if (!textRecognizer.isOperational()) {
            Log.d(TAG, "Could not get the text");
        }
        else {
            Frame frame = new Frame.Builder().setBitmap(bitmap).build();
            SparseArray<TextBlock> items = textRecognizer.detect(frame);
            StringBuilder sb = new StringBuilder();

            for (int i = 0; i < items.size(); i++) {
                TextBlock myItem = items.valueAt(i);
                sb.append(myItem.getValue());
                sb.append("\n");

            }

            if (sb.length() > 0) {
                textView.setText(sb.toString());
            }
            else {
                textView.setText("Could not detect any text");
            }

            ExpenseDataModel expense;


            String[] listOfItems = sb.toString().split("\n");

            String[] names = Arrays.copyOfRange(listOfItems, 0, listOfItems.length/2);
            String[] amounts = Arrays.copyOfRange(listOfItems, listOfItems.length/2, listOfItems.length);
            double[] parsedAmounts = new double[amounts.length];

            for (int i = 0; i < names.length; i++) {
                Log.d(TAG, "ARRAY OF ITEM  NAMES " + i + ": " + names[i]);
            }
            for (int i = 0; i < amounts.length; i++) {
                Log.d(TAG, "ARRAY OF ITEM  AMOUNTS " + i + ": " + amounts[i]);
            }

            for(int i=0; i<amounts.length; i++){
                String s = amounts[i];
                Log.d(TAG, "PARSED AMOUNTS: " + s);
                // if the first character is $ or S (which should always be the case cause these are amounts
                if (s.substring(0, 1).equals("$")|| s.substring(0, 1).equals("S")) {
                    s = s.substring(1, s.length());
                    Log.d(TAG, "AFTER REMOVING S OR $ AMOUNT IS " + s);
                    Double amount = Double.parseDouble(s);
                    parsedAmounts[i] = amount;
                }

                /*
                String[] itemdetails = s.split(" ");
                String itemname = itemdetails[0];
                Double amount = Double.parseDouble(itemdetails[1]);
                expense = new ExpenseDataModel(itemname, amount);
                Log.d(TAG, "ITEM NAME: " + itemname);
                Log.d(TAG, "ITEM PRICE: " + amount);
                expenseDataModels.add(expense);
                */
            }
/*
        for(int i=0;i<expenseDataModels.size();i++){
            Intent intent = new Intent(this,AfterImage_Click_add_Expence.class);
            StringBuilder s = new StringBuilder();
            s.append(expenseDataModels.get(i).getItemName());
            s.append(',');
            s.append(expenseDataModels.get(i).getItemPrice());
            String sentData = s.toString();
            intent.putExtra("a",sentData);
            startActivity(intent);
        }
*/
        }



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
                        Intent intent0 = new Intent(AddExpenseNext.this, Dashboard.class);
                        startActivity(intent0);
                        break;

                    case R.id.action_friends:
                        Intent intent1 = new Intent(AddExpenseNext.this, AddFriends.class);
                        startActivity(intent1);
                        break;

                    case R.id.action_addexpenses:
                        break;

                    case R.id.action_activity:
                        Intent intent3 = new Intent(AddExpenseNext.this, ActivityList.class);
                        startActivity(intent3);
                        break;

                    case R.id.action_settings:
                        Intent intent4 = new Intent(AddExpenseNext.this, UserSettings.class);
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
                Intent intent6 = new Intent(AddExpenseNext.this, LoginActivity.class);
                startActivity(intent6);
                return true;

            default:
                // If we got here, the user's action was not recognized.
                // Invoke the superclass to handle it.
                return super.onOptionsItemSelected(item);

        }
    }

    public Bitmap getBitmap(String imgUrl) {
        File imageFile = new File(imgUrl);
        FileInputStream fis = null;
        Bitmap bitmap = null;
        try{
            fis = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(fis);
        }catch (FileNotFoundException e){
            Log.e(TAG, "getBitmap: FileNotFoundException: " + e.getMessage() );
        }finally {
            try{
                fis.close();
            }catch (IOException e){
                Log.e(TAG, "getBitmap: FileNotFoundException: " + e.getMessage() );
            }
        }
        return bitmap;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {

        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        allFriendsAdapter.filter(text);
        return false;
    }
}
