package com.example.fede_xps.smartfastfood;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by Fede-xps on 12/04/2017.
 */

public class CustomListViewAdapter extends ArrayAdapter<Item> {


    Context context;
    ViewHolder holder;
    int layout;

    public CustomListViewAdapter(Context context, int resourceId, //resourceId=your layout
                                 List<Item> items) {
        super(context, resourceId, items);
        this.context = context;
        layout=resourceId;
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView txtPrice;
        TextView txtTitle;
        CheckBox check;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        holder = null;
        Item rowItem = getItem(position);
        String name = null;
        String price = null;
        String image = null;

        try {
            name = rowItem.getJson().getString("name");
            price = rowItem.getJson().getString("price");
            image = rowItem.getJson().getString("image");

        } catch (Exception e) {
            e.fillInStackTrace();
        }

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item, null);
            holder = new ViewHolder();
            holder.txtTitle = (TextView) convertView.findViewById(R.id.name);
            holder.txtPrice = (TextView) convertView.findViewById(R.id.price);
            //if(layout==R.layout.item)
            holder.check = (CheckBox) convertView.findViewById(R.id.check);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtTitle.setText(name);
        holder.txtPrice.setText("€ "+price);
        holder.check.setTag(position);

        if(layout==R.layout.item) {
            holder.check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CheckBox cBox = (CheckBox) v.findViewById(R.id.check);
                    //Item i = (Item) v.getTag();
                    int position = (int)v.getTag();
                    //i.setCheck(!cBox.isChecked());

                    Log.d("CHECK", "CLicked..." + position+ " " + cBox.isChecked());

                    getItem(position).setCheck(cBox.isChecked());
                }
            });
        } else {
            holder.check.setVisibility(View.INVISIBLE);
        }

        return convertView;
    }

}