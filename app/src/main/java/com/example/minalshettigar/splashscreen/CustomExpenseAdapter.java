package com.example.minalshettigar.splashscreen;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;

import com.example.minalshettigar.splashscreen.helper.ExpenseDataModel;
import com.example.minalshettigar.splashscreen.helper.users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.example.minalshettigar.splashscreen.AddExpenseNext;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class CustomExpenseAdapter extends ArrayAdapter<ExpenseDataModel> implements View.OnClickListener {

    private ArrayList<ExpenseDataModel> expenseDataSet;
    Context mContext;


    // View lookup cache
    private static class ViewHolder {
        TextView txtItemName;
        TextView txtItemPrice;
        Button btnRemoveItem;
    }

    public CustomExpenseAdapter(ArrayList<ExpenseDataModel> data, Context context) {
        super(context, R.layout.layout_expense_row_item, data);
        this.expenseDataSet = data;
        this.mContext = context;
    }


    @Override
    public void onClick(View v) {
        int position = (Integer) v.getTag();
        Object object = getItem(position);
        ExpenseDataModel expenseDataModel = (ExpenseDataModel) object;


        switch (v.getId()) {
            case R.id.buttonDelete:
                Log.d("TEST", "delete item " + position);
                //((AddExpenseNext) mContext).removeItemFromData(position);
                
                break;
        }

    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ExpenseDataModel expenseDataModel = getItem(position);
        final ViewHolder viewHolder;
        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.layout_expense_row_item, parent, false);
            viewHolder.txtItemName = (TextView) convertView.findViewById(R.id.itemName);
            viewHolder.txtItemPrice = (TextView) convertView.findViewById(R.id.itemPrice);
            viewHolder.btnRemoveItem = (Button) convertView.findViewById(R.id.buttonDelete);
            viewHolder.btnRemoveItem.setOnClickListener(this);
            viewHolder.btnRemoveItem.setTag(position);

            result = convertView;
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.txtItemName.setText(expenseDataModel.getItemName());
        viewHolder.txtItemPrice.setText(Double.toString(expenseDataModel.getItemPrice()));

        return convertView;
    }

}
