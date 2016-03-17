package com.example.simpleui.simpleui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.SaveCallback;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_MENU_ACTIVITY = 0;

    TextView textView;
    EditText editText;
    CheckBox hideCheckBox;
    ListView listView;
    Spinner spinner;

    SharedPreferences sp;
    Editor editor;

    String menuResult;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.textView);
        editText = (EditText)findViewById(R.id.editText);
        listView = (ListView)findViewById(R.id.listView);
        spinner = (Spinner)findViewById(R.id.spinner);


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

        //parse.com

        Parse.enableLocalDatastore(this);

        Parse.initialize(this);

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
        String[] data = Utils.readFile(this, "history.txt").split("\n");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, data);
        listView.setAdapter(adapter);
    }

    private void setSpinner()
    {
        String[] data = getResources().getStringArray(R.array.storyInfo);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, data);
        spinner.setAdapter(adapter);
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
