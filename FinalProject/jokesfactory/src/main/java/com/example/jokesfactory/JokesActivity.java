package com.example.jokesfactory;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class JokesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_jokes);
        TextView textview = (TextView) findViewById(R.id.tvMsg2);
        Intent intentThatStartedThisActivity= getIntent();
        if(intentThatStartedThisActivity!=null){
            String str = intentThatStartedThisActivity.getStringExtra(getString(R.string.key_val));
            if(str==null){
                textview.setText("Joke Not Found...please try again later");
            }
            else{
                textview.setText(str);
            }
        }
    }
}
