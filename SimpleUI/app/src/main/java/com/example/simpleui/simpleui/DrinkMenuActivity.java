package com.example.simpleui.simpleui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DrinkMenuActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_drink_menu);
    }

    public void add (View view)  //View是Button的父類別，所以View可以接Button
    {
        Button button = (Button)view;  //因為傳進來的view別是Button，所以可以在這邊轉型為Button
        int number = Integer.parseInt(button.getText().toString());
        number++;
        button.setText(String.valueOf(number));
    }

    public void cancel (View view)
    {
        finish();  //結束goto destory，則原本的Activity會resume
    }

    public void done (View view)
    {
        JSONArray array = getData();

        Intent data = new Intent();
        //data.putExtra("result", "order done");  //result是key, order done是值
        data.putExtra("result", array.toString());

        setResult(RESULT_OK, data);  //設定resultcode 值為RESULT_OK以及data
        finish();
    }

    //利用 JSONArray 型別，傳遞資料。
    public JSONArray getData()
    {
        //取得LinearLayout(id=root)，利用for loop去抓下面Layout的Button。
        LinearLayout rootLinearLayout = (LinearLayout)findViewById(R.id.root);
        int count = rootLinearLayout.getChildCount();  //計算出 Child's Layout個數

        JSONArray array = new JSONArray();

        for (int i = 0; i < count -1; i++)
        {
            LinearLayout lx = (LinearLayout)rootLinearLayout.getChildAt(i);  //取出第i個Child物件=>lx
            TextView drinkNameTextView = (TextView)lx.getChildAt(0);  //取出Layout lx的 3個 Child(Button)
            Button lButton = (Button)lx.getChildAt(1);
            Button mButton = (Button)lx.getChildAt(2);

            String drinkName = drinkNameTextView.getText().toString();
            int lNumber = Integer.parseInt(lButton.getText().toString());
            int mNumber = Integer.parseInt(mButton.getText().toString());

            try {
                JSONObject object = new JSONObject();

                object.put("name", drinkName);  //JSON object 是 Key Value結構
                object.put("lNumber", lNumber);
                object.put("mNumber", mNumber);

                array.put(object);  //把json object丟進 JSONArray
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return array;
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d("debug", "[DEBUG]DrinkMenu onStart");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("debug", "[DEBUG]DrinkMenu onRestart");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.d("debug", "[DEBUG]DrinkMenu onResume");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d("debug", "[DEBUG]DrinkMenu onPause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d("debug", "[DEBUG]DrinkMenu onStop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("debug", "[DEBUG]DrinkMenu onDestroy");
    }
}
