package com.example.simpleui.simpleui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    EditText editText;
    CheckBox hideCheckbox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView)findViewById(R.id.textView);
        editText = (EditText)findViewById(R.id.editText);
        hideCheckbox = (CheckBox)findViewById(R.id.checkBox);

        editText.setOnKeyListener(new View.OnKeyListener()
        {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event)
            {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    submit(v);
                    return true;
                }
                return false;
            }
        });

        //開起虛擬鍵盤的輸入
        editText.setOnEditorActionListener(new TextView.OnEditorActionListener()
        {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if (actionId == EditorInfo.IME_ACTION_DONE)
                {
                    submit(v);
                    return true;
                }
                return false;
            }
        });

    }

    public void submit (View view)
    {
        //Toast.makeText(this, "Hello World!!", Toast.LENGTH_LONG).show();
        //textView.setText("setTest text");

        String text = editText.getText().toString();

        if (hideCheckbox.isChecked())
        {
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
            textView.setText("*****");
            editText.setText("*****");
            return;  //直接結束
        }
        textView.setText(text);
        editText.setText("");
    }
}
