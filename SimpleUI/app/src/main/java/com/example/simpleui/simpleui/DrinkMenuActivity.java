package com.example.simpleui.simpleui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

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
        Intent data = new Intent();
        data.putExtra("result", "order done");  //result是key, order done是值
        setResult(RESULT_OK, data);  //設定resultcode 值為RESULT_OK以及data
        finish();
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
