package com.example.minalshettigar.splashscreen;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.minalshettigar.splashscreen.helper.UsersDataModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ManualFragment extends Fragment {

    EditText inputCategory;
    EditText inputItem;
    EditText inputPrice;
    EditText searchQuery;

    Button buttonNewItem;
    Button buttonFinish;
    Button buttonRemoveFriend;

    List<UsersDataModel> selectedFriendList = new ArrayList<>();
    ListView listViewSelectedFriends;
    ListView listViewFriends;
    String currentUserId;
    String itemName;
    double itemPrice;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_manual, container, false);

        inputCategory = (EditText) view.findViewById(R.id.input_category);
        inputItem = (EditText) view.findViewById(R.id.input_item);
        inputPrice = (EditText) view.findViewById(R.id.input_price);
        searchQuery = (EditText) view.findViewById(R.id.input_people);

        buttonNewItem = (Button) view.findViewById(R.id.button_new_item);
        buttonFinish = (Button) view.findViewById(R.id.button_finish);
        //buttonRemoveFriend = (Button) view.findViewById(R.id.button_remove_friend);

        buttonNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO save data to db
                itemName = inputItem.getText().toString();
                itemPrice = Double.parseDouble(inputPrice.getText().toString());

                double splitPrice = splitCost(itemPrice, selectedFriendList.size());

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


                // clear form fields
                inputItem.setText(null);
                inputPrice.setText(null);
                searchQuery.setText(null);

                // go back to dashboard
                getActivity().finish();
            }
        });

        return view;
    }

    public double splitCost(double price, int numPeople) {
        return price / numPeople;
    }
}
