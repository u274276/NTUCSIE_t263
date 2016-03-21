package com.example.simpleui.simpleui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.jar.Manifest;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_MENU_ACTIVITY = 0;
    private static final int REQUEST_CODE_CAMERA = 1;

    TextView textView;
    EditText editText;
    CheckBox hideCheckBox;
    ListView listView;
    Spinner spinner;
    ImageView photoView;

    SharedPreferences sp;
    Editor editor;

    String menuResult;
    List<ParseObject> queryResults;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.textView);
        editText = (EditText)findViewById(R.id.editText);
        listView = (ListView)findViewById(R.id.listView);
        spinner = (Spinner)findViewById(R.id.spinner);
        photoView = (ImageView)findViewById(R.id.photoView);


        sp = getSharedPreferences("setting", Context.MODE_PRIVATE);
        editor = sp.edit();

        editText.setText(sp.getString("editText", ""));

        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                editor.putString("editText", editText.getText().toString());
                editor.apply();
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {
                    submit(v);
                    return true;
                }
                return false;
            }
        });

        //開起虛擬鍵盤的輸入
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    submit(v);
                    return true;
                }
                return false;
            }
        });

        hideCheckBox = (CheckBox)findViewById(R.id.checkBox);

        hideCheckBox.setChecked(sp.getBoolean("hideCheckBox", false));
        hideCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                editor.putBoolean("hideCheckBox", hideCheckBox.isChecked());
                editor.apply();
            }
        });

        setListView();
        setSpinner();



        //ParseObject testObject = new ParseObject("HomeworkParse");
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("sid", "And26308");

        testObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null)
                {
                    Log.d("debug", "[DEBUG]" + e.toString());
                }
            }
        });



    }

    private void setListView()
    {
        //第6堂課被註解掉
        //String[] data = Utils.readFile(this, "history.txt").split("\n");
        //ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        //listView.setAdapter(adapter);

        //第6堂課改寫的內容
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Order");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                queryResults = list;

                List<Map<String, String>> data = new ArrayList<Map<String, String>>();
                for (int i = 0; i < queryResults.size(); i++) {
                    ParseObject object = queryResults.get(i);
                    String note = object.getString("note");
                    String storeInfo = object.getString("storeInfo");
                    String menu = object.getString("menu");

                    Map<String, String> item = new HashMap<String, String>();
                    item.put("note", note);
                    item.put("storeInfo", storeInfo);
                    item.put("drinkNum", "15");

                    data.add(item);
                }
                String[] from = {"note", "storeInfo", "drinkNum"};
                int[] to = {R.id.note, R.id.storeInfo, R.id.drinkNum};
                SimpleAdapter adapter = new SimpleAdapter(MainActivity.this, data, R.layout.listview_item, from, to);
                listView.setAdapter(adapter);
            }
        });
    }

    private void setSpinner()
    {
        /*
        String[] data = getResources().getStringArray(R.array.storyInfo);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, data);
        spinner.setAdapter(adapter);
        */

        //第6堂課改寫
        ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("StoreInfo");

        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> list, ParseException e) {
                if (e != null) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    return;
                }

                String[] stores = new String[list.size()];
                for (int i = 0; i < list.size(); i++) {
                    ParseObject object = list.get(i);
                    stores[i] = object.getString("name") + "," + object.getString("address");
                }
                ArrayAdapter<String> storeAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.simple_spinner_dropdown_item, stores);
                spinner.setAdapter(storeAdapter);
            }
        });
    }

    public void submit (View view)
    {
        //Toast.makeText(this, "Hello World!!", Toast.LENGTH_LONG).show();
        //textView.setText("setTest text");

        String text = editText.getText().toString();

        ParseObject orderObject = new ParseObject("Order");
        orderObject.put("note", text);
        orderObject.put("storeInfo", spinner.getSelectedItem());
        orderObject.put("menu", menuResult);

        orderObject.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null)
                {
                    Toast.makeText(MainActivity.this, "Submit OK", Toast.LENGTH_LONG).show();
                }
            }
        });

        Utils.writeFile(this, "history.txt", text + '\n');

        if (hideCheckBox.isChecked())
        {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            textView.setText("*****");
            editText.setText("*****");
            return;  //直接結束
        }
        textView.setText(text);
        editText.setText("");

        setListView();
    }

    public void goToMenu(View view)
    {
        //Intent傳遞Activity
        Intent intent = new Intent();
        intent.setClass(this, DrinkMenuActivity.class);

        //startActivity(intent);  //啟動Activity為Start狀態
        startActivityForResult(intent, REQUEST_CODE_MENU_ACTIVITY);  //啟動Activity為Start狀態BY REQUEST_CODE
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d("debug", "[DEBUG]main activity result");

        if (requestCode == REQUEST_CODE_MENU_ACTIVITY)
        {
            //textView.setText(data.getStringExtra("result"));  //result的值為 order done(定義在DrinkMenuActivity的done function())
            if (resultCode == RESULT_OK)
            {
                menuResult = data.getStringExtra("result");

                try {
                    JSONArray array = new JSONArray(menuResult);

                    String text = "";

                    for (int i = 0; i < array.length(); i++)
                    {
                        JSONObject order = array.getJSONObject(i);

                        String name = order.getString("name");
                        String lNumber = String.valueOf(order.getInt("lNumber"));
                        String mNumber = String.valueOf(order.getInt("mNumber"));

                        text = text + name +" l:" + lNumber + " m:" + mNumber + "\n";
                    }

                    textView.setText(text);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        else if (requestCode == REQUEST_CODE_CAMERA)
        {
            if (resultCode == RESULT_OK)
            {
                photoView.setImageURI(Utils.getPhotoUri());
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        if (id == R.id.action_take_photo)
        {
            goToCamera();
            Toast.makeText(this, "take photo", Toast.LENGTH_LONG).show();
        }

        return super.onOptionsItemSelected(item);

    }

    private void goToCamera()
    {
        /*if (Build.VERSION.SDK_INT >= 23)
        {
            if(checkSelfPermission(Manifest.))
        }*/
        Intent intent = new Intent();
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Utils.getPhotoUri());

        //startActivity(intent);
        startActivityForResult(intent, REQUEST_CODE_CAMERA);
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("debug", "[DEBUG]Main onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("debug", "[DEBUG]Main onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("debug", "[DEBUG]Main onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("debug", "[DEBUG]Main onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("debug", "[DEBUG]Main onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("debug", "[DEBUG]Main onDestroy");
    }
}
