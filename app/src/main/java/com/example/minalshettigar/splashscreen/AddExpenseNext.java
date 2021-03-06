package com.example.minalshettigar.splashscreen;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseArray;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.view.View.OnClickListener;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import com.example.minalshettigar.splashscreen.helper.ExpenseDataModel;
import com.example.minalshettigar.splashscreen.helper.FriendListViewAdapter;
import com.example.minalshettigar.splashscreen.helper.UsersDataModel;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.text.Text;
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
import java.util.Map;

public class AddExpenseNext extends AppCompatActivity {

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
    String[] names;
    String[] amounts;


    ArrayList<ExpenseDataModel> expenseDataModels;
    ListView itemListView;
    private static CustomExpenseAdapter adapter;

    AlertDialog.Builder builder;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_expense_next);

        Toolbar myToolbar = findViewById(R.id.my_toolbar);
        setSupportActionBar(myToolbar);

        mAuth= FirebaseAuth.getInstance();
        TextView textView = (TextView) findViewById(R.id.textDetected);

        itemListView = (ListView) findViewById(R.id.list);
        expenseDataModels = new ArrayList<>();

        // get img info from AddExpenses Activity
        intent = getIntent();
        if (intent.hasExtra(getString(R.string.selected_image))) {
            imgUrl = intent.getStringExtra(getString(R.string.selected_image));
            bitmap = getBitmap(imgUrl);
            Log.d("IMG INFO", "IMG URL IS " + imgUrl);
        }
        else if (intent.hasExtra(getString(R.string.selected_bitmap))) {
            bitmap = (Bitmap) intent.getParcelableExtra(getString(R.string.selected_bitmap));
            if (bitmap != null) {
                Log.d("IMG INFO", "BITMAP IS NOT NULLASHCKAHJSKHA");
            }
        }

        // parse text
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

            if (sb.length() <= 0) {
                Toast.makeText(this.getApplicationContext(), "Could not detect any text", Toast.LENGTH_SHORT).show();

            }

            ExpenseDataModel expense;


            String[] listOfItems = sb.toString().split("\n");

            String[] names = Arrays.copyOfRange(listOfItems, 0, listOfItems.length / 2);
            String[] amounts = Arrays.copyOfRange(listOfItems, listOfItems.length / 2, listOfItems.length);
            double[] parsedAmounts = new double[amounts.length];

            try {
                for (int i = 0; i < amounts.length; i++) {
                    String s = amounts[i];
                    Log.d(TAG, "PARSED AMOUNTS: " + s);
                    // if the first character is $ or S (which should always be the case cause these are amounts
                    try {
                        if (s.length() > 0) {
                            if (s.substring(0, 1).equals("$") || s.substring(0, 1).equals("S")) {
                                s = s.substring(1, s.length());
                                Log.d(TAG, "AFTER REMOVING S OR $ AMOUNT IS " + s);
                                Double amount = Double.parseDouble(s);
                                parsedAmounts[i] = amount;
                            }
                            else {
                                parsedAmounts[i] = Double.parseDouble(amounts[i]);
                            }
                        }
                    } catch(NullPointerException e) {
                        Toast.makeText(this.getApplicationContext(), "Image could not be parsed correctly, please try again.", Toast.LENGTH_SHORT).show();
                    }
                }

            } catch(NumberFormatException e) {
                Toast.makeText(this.getApplicationContext(), "Image could not be parsed correctly, please try again.", Toast.LENGTH_SHORT).show();
            }

            // add parsed item name and price to expenseDataModels
            for (int i = 0; i < names.length; i++) {
                expenseDataModels.add(new ExpenseDataModel(names[i], parsedAmounts[i]));
            }
        }


        adapter = new CustomExpenseAdapter(expenseDataModels, AddExpenseNext.this);
        itemListView.setAdapter(adapter);
        itemListView.setDescendantFocusability(ViewGroup.FOCUS_BLOCK_DESCENDANTS);
        itemListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Log.d(TAG, "ADDEXPENSENEXT onITEMCLICK");
                ExpenseDataModel expenseDataModel = expenseDataModels.get(position);
                // open fragment (same as manual fragment)
                StringBuilder s = new StringBuilder();
                s.append(expenseDataModel.getItemName());
                s.append(",");
                s.append(expenseDataModel.getItemPrice());
                Bundle bundle = new Bundle();
                bundle.putString("a",s.toString());
                bundle.putInt("itemIndex", position);
                ExpenseItemFragment expenseItemFragment = new ExpenseItemFragment();
                android.support.v4.app.FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
                expenseItemFragment.setArguments(bundle);
                transaction.replace(R.id.relLayoutForListView, expenseItemFragment);
                transaction.addToBackStack(null);
                transaction.commit();
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

        // Create dialog builder to pop up message when trying to delete items from listview
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        }
        else {
            builder = new AlertDialog.Builder(this);
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

    public void checkRemoveItemFromData(final int position) {
        Log.d(TAG, "REMOVEITEM FROM DATA CALLED WHCBKJABCSKJBASKCBJKABSC");
        builder.setTitle("Delete Item")
                .setMessage("Delete this item?")
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //continue with delete
                        removeItemFromData(position);
                    }
                })
                .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        // do nothing
                        dialogInterface.cancel();
                    }
                })
                .show();

        for (int i = 0; i < expenseDataModels.size(); i++) {
            Log.d(TAG, "expenseDataModels item " + i + expenseDataModels.get(i).getItemName());
        }
    }

    public void removeItemFromData(final int position) {
        expenseDataModels.remove(position);
        adapter.notifyDataSetChanged();
        itemListView.invalidateViews();
    }
}
