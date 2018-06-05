package com.example.minalshettigar.splashscreen;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.provider.ContactsContract;
import android.content.Intent;
import android.net.Uri;

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


    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friend__adding);

        addfrnddb=FirebaseDatabase.getInstance().getReference("friendships");
        currentUserId = FirebaseAuth.getInstance().getUid();

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




    }

private void addfriendInDB()
{

String frndName=name.getText().toString().trim();
String frndemail=Email.getText().toString().trim();

if(TextUtils.isEmpty(frndemail))
{
    Date date = Calendar.getInstance().getTime();
    DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
    String strDate = dateFormat.format(date);
    UsersDataModel udm=new UsersDataModel(currentUserId,frndemail,frndName,strDate,strDate);
    String id=addfrnddb.push().getKey();
    addfrnddb.child(id).setValue(udm);
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


}
