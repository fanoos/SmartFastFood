package com.example.fede_xps.smartfastfood;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Fede-xps on 23/05/2017.
 */

public class CustomListViewAdapterOrder extends ArrayAdapter<Item3> {

    Context context;
    CustomListViewAdapterOrder.ViewHolder holder;
    int layout;


    public CustomListViewAdapterOrder(Context context, int resource, List<Item3> lists) {
        super(context, resource, lists);


        this.context = context;
        layout=resource;

    }


    public class ViewHolder {
        TextView txtNumber;
        TextView txtPay;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        holder = null;
        Item3 rowItem = getItem(position);
        String code;
        String pay;

        code = rowItem.getCode();
        pay = rowItem.getPay();

        LayoutInflater mInflater = (LayoutInflater) context
                .getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item3, null);
            holder = new CustomListViewAdapterOrder.ViewHolder();
            holder.txtNumber = (TextView) convertView.findViewById(R.id.number);
            holder.txtPay = (TextView) convertView.findViewById(R.id.payed);

            convertView.setTag(holder);
        } else
            holder = (CustomListViewAdapterOrder.ViewHolder) convertView.getTag();

        holder.txtNumber.setText(code);
        holder.txtPay.setText(pay);




        return convertView;
    }
}
