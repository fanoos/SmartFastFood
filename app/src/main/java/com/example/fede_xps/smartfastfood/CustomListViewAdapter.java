package com.example.fede_xps.smartfastfood;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.apache.http.HttpStatus;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
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

    public void setImage(Bitmap image) {
        holder.imView.setImageBitmap(image);
    }

    /*private view holder class*/
    private class ViewHolder {
        TextView txtPrice;
        TextView txtTitle;
        ImageView imView;
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
            holder.imView = (ImageView) convertView.findViewById(R.id.image1);




            /*
            String imageUri="http://www.ricettapizzanapoletana.it/images/pizza-napoletana.jpg";
            Picasso.with(context).load(imageUri).into(holder.imView);
            */
            //if(layout==R.layout.item)
            holder.check = (CheckBox) convertView.findViewById(R.id.check);
            convertView.setTag(holder);
        } else
            holder = (ViewHolder) convertView.getTag();

        holder.txtTitle.setText(name);
        holder.txtPrice.setText("€ "+price);
        holder.check.setTag(position);

        DownloadImage di = new DownloadImage(holder.imView);
        di.execute(image);


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

    class DownloadImage extends AsyncTask<String, Void, Bitmap> {
        private final ImageView imageViewReference;

        public DownloadImage(ImageView imageView) {
            imageViewReference = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... params) {
            return downloadImage(params[0]);
        }

        private Bitmap downloadImage(String url) {

            Log.d("DOWNLOAD", "url: "+url);
            HttpURLConnection urlConnection = null;
            try {
                URL uri = new URL(url);
                urlConnection = (HttpURLConnection) uri.openConnection();
                int statusCode = urlConnection.getResponseCode();
                if (statusCode != HttpStatus.SC_OK) {
                    return null;
                }

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    Log.d("IMAGE", "OK");
                    return bitmap;
                }
            } catch (Exception e) {
                urlConnection.disconnect();
                Log.w("ImageDownloader", "Error downloading image from " + url);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (isCancelled()) {
                bitmap = null;
            }



            if (imageViewReference != null) {
                    Log.d("DOWNLOAD", "start");
                    if (bitmap != null) {
                        Log.d("DOWNLOAD", "setting");
                        imageViewReference.setImageBitmap(bitmap);
                    }

            }
        }
    }
}
