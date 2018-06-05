package com.example.minalshettigar.splashscreen;

import android.os.Bundle;
import android.support.annotation.NonNull;
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


public class AddFriends  extends AppCompatActivity
{

    private EditText searchQuery;
    private ListView listViewfriendsResult;
    String currentUserId;
    List<UsersDataModel>friendlist;



    DatabaseReference dbFriendsRef;

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

        dbFriendsRef= FirebaseDatabase.getInstance().getReference("friendships");

        friendlist=new ArrayList<>();
        listViewfriendsResult=(ListView)findViewById(R.id.listViewfriendsresult) ;

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

        currentUserId = FirebaseAuth.getInstance().getUid();
        dbFriendsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                friendlist.clear();
                for(DataSnapshot frndSnap:dataSnapshot.getChildren())
                {
                    UsersDataModel udm=frndSnap.getValue(UsersDataModel.class);

                    if(udm.getUserId().equalsIgnoreCase(currentUserId))
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
}
