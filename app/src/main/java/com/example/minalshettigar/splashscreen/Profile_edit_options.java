package com.example.minalshettigar.splashscreen;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

public class Profile_edit_options extends AppCompatActivity {
    private static final String TAG = "Profile_edit_options";

    TextView Name, Email, Conatct;
    Button Edit;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private FirebaseDatabase database;
    private DatabaseReference myRef;
    private String userId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit_options);

        Edit = (Button) findViewById(R.id.Edit);
        Name = (TextView) findViewById(R.id.Name);
        Email = (TextView) findViewById(R.id.Email);
        Conatct = (TextView) findViewById(R.id.Contact);

        mAuth = FirebaseAuth.getInstance();
        database  = FirebaseDatabase.getInstance();
        myRef  = database.getReference("users");
        FirebaseUser user = mAuth.getCurrentUser();
        userId = user.getUid();




        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newName, newEmail, newContact;
                newName= Name.getText().toString();
                newEmail = Email.getText().toString();
                newContact = Conatct.getText().toString();
                if(newName.equals("")){
                    toastMessage("Enter Name");

                }
                else if(newEmail.equals("")){
                    toastMessage("Enter Email");


                }
                else if(newContact.equals("")){
                    toastMessage("Enter Contact");

                }else {
                    myRef.child(userId).setValue(newContact);
                    myRef.child(userId).setValue(newEmail);
                    myRef.child(userId).setValue(newName);
                    toastMessage("Data Updated");
                }



            }
        });
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                String contact=dataSnapshot.child(userId).getValue().toString();
                String email = dataSnapshot.child(userId).getValue().toString();
                String name = dataSnapshot.child(userId).getValue().toString();
                Name.setText(name);
                Email.setText(email);
                Conatct.setText(contact);

            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });

        /*mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    // User is signed in
                    Log.d(TAG, "onAuthStateChanged:signed_in:" + user.getUid());

                } else {
                    // User is signed out
                    Log.d(TAG, "onAuthStateChanged:signed_out");

                }
                // ...
            }
        };*/


    }
    @Override
    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }
    private void toastMessage(String s){
        Toast.makeText(this,s,Toast.LENGTH_SHORT).show();
    }
}
