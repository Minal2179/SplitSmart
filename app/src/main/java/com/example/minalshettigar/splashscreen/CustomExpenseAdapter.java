package com.example.minalshettigar.splashscreen;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;

import com.example.minalshettigar.splashscreen.helper.ExpenseDataModel;

import java.util.ArrayList;
import java.util.List;

public class CustomExpenseAdapter extends ArrayAdapter<ExpenseDataModel> implements View.OnClickListener {

    private ArrayList<ExpenseDataModel> expenseDataSet;
    Context mContext;

    // View lookup cache
    private static class ViewHolder {
        EditText txtItemName;
        EditText txtItemPrice;
        EditText txtItemPeople;
        SearchView editSearch;
        ListView allFriendsListView;
        ListView selectedFriendsListView;
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
            // case R.id.item_info
        }
    }

    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ExpenseDataModel expenseDataModel = getItem(position);
        ViewHolder viewHolder;
        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.layout_expense_row_item, parent, false);
            viewHolder.txtItemName = (EditText) convertView.findViewById(R.id.itemName);
            viewHolder.txtItemPrice = (EditText) convertView.findViewById(R.id.itemPrice);
            viewHolder.txtItemPeople = (EditText) convertView.findViewById(R.id.itemPerson);
            viewHolder.editSearch = (SearchView) convertView.findViewById(R.id.searchView);
            viewHolder.allFriendsListView = (ListView) convertView.findViewById(R.id.allFriendsListView);
            viewHolder.selectedFriendsListView = (ListView) convertView.findViewById(R.id.selectedFriendsListView);

            result = convertView;
            convertView.setTag(viewHolder);
        }
        else {
            viewHolder = (ViewHolder) convertView.getTag();
            result = convertView;
        }

        viewHolder.txtItemName.setText(expenseDataModel.getItemName());
        viewHolder.txtItemPrice.setText(Double.toString(expenseDataModel.getItemPrice()));
        // should be an array of friends
        //viewHolder.txtItemPeople.setText(expenseDataModel.getFriendIds());
        viewHolder.editSearch.setQueryHint("TEST SEARCH VIEW");
        // need adapter for the list of friends
        return convertView;
    }
}
