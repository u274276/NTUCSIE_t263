package com.example.simpleui.simpleui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void submit (View view)
    {
        Toast.makeText(this, "Hello World!!", Toast.LENGTH_LONG).show();

    }
}
