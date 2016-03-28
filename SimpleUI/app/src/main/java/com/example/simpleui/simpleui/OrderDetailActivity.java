package com.example.simpleui.simpleui;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class OrderDetailActivity extends AppCompatActivity {

//    private String url;
//    private String address;

    TextView note;
    TextView storeInfo;
    TextView menu;
    ImageView photo;
    ImageView staticMapimageView;
    WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_detail);

        note = (TextView)findViewById(R.id.noteView);
        storeInfo = (TextView)findViewById(R.id.storeInfoView);
        menu = (TextView)findViewById(R.id.menuView);
        photo = (ImageView)findViewById(R.id.photoView);
        staticMapimageView = (ImageView)findViewById(R.id.staticMapimageView);
        webView = (WebView)findViewById(R.id.webView);


        note.setText(getIntent().getStringExtra("note"));

        String storeInformation = getIntent().getStringExtra("storeInfo");

        storeInfo.setText(getIntent().getStringExtra("storeInfo"));

        String menuResult = getIntent().getStringExtra("menu");

        try {
            JSONArray array = new JSONArray(menuResult);  //將menuResult(JSONArray=>String)轉成JSONArray(String=>JSONArray)

            String text = "";

            for (int i = 0; i < array.length(); i++)
            {
                JSONObject order = array.getJSONObject(i);

                String name = order.getString("name");
                String lNumber = String.valueOf(order.getInt("lNumber"));
                String mNumber = String.valueOf(order.getInt("mNumber"));

                text = text + name +" l:" + lNumber + " m:" + mNumber + "\n";
            }

            menu.setText(text);
        } catch (JSONException e) {
            e.printStackTrace();
        }

//        String address = storeInformation.split(",")[1];

//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                double[] locations = Utils.addressToLatLng(address);
//                String debugLog = "lat:" + String.valueOf(locations[0] + "lng: " + String.valueOf(locations[1]));
//
//                //Log.d("debug", result);
//                Log.d("debug", debugLog);
//
//            }
//        });
//        thread.start();


//        不用Picasso這個網站的方法，改用Thread或是改用AsyncTask
//        String url = getIntent().getStringExtra("photoURL");
//        if (url != null)
//        {
//            Picasso.with(this).load(url).into(photo);
//        }


//        String url = getIntent().getStringExtra("photoURL");
        //避免url是空值，導致APP當掉。
//        if(url == null)
//            return;

        String address = storeInformation.split(",")[1];
        new GeoCodingTask().execute(address);
        String url = getIntent().getStringExtra("photoURL");

        if (url == null)
            return;

        new ImageLoadingTask(photo).execute(url);


//        用Thread這個方法不好
//        Thread thread = new Thread(new Runnable() {
//            @Override
//            public void run() {
//                byte[] bytes = Utils.urlToBytes(url);
//                String result = new String(bytes);
//                Log.d("debug", result);
//                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                photo.setImageBitmap(bmp);
//            }
//        });
//        thread.start();

//        改用AsyncTask
//        new AsyncTask<String, Void, byte[]>()
//        {
//            @Override
//            protected byte[] doInBackground(String... params)
//            {
//                String url = params[0];
//                return Utils.urlToBytes(url);
//            }
//
//            @Override
//            protected void onPostExecute(byte[] bytes)
//            {
//                Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
//                photo.setImageBitmap(bmp);
//                super.onPostExecute(bytes);
//            }
//
//        }.execute(url);
    }

    class GeoCodingTask extends AsyncTask<String, Void, byte[]>
    {
//        ImageView imageView;
//
//        public GeoCodingTask(ImageView imageView) {
//            this.imageView = imageView;
//        }

        private double[] latlng;
        private String url;
        @Override
        protected byte[] doInBackground(String... params) {
            String address = params[0];
            latlng = Utils.addressToLatLng(address);
            url = Utils.getStaticMapUrl(latlng, 17);
            return Utils.urlToBytes(url);
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            webView.loadUrl(url);

            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            staticMapimageView.setImageBitmap(bmp);
            super.onPostExecute(bytes);
        }


    }

    class ImageLoadingTask extends AsyncTask<String, Void, byte[]>
    {
        ImageView imageView;

        public ImageLoadingTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected byte[] doInBackground(String... params) {
            String url = params[0];
            return Utils.urlToBytes(url);
        }

        @Override
        protected void onPostExecute(byte[] bytes) {
            Bitmap bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
            photo.setImageBitmap(bmp);
            super.onPostExecute(bytes);
        }


    }
}
