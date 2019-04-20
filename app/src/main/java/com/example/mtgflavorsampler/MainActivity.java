package com.example.mtgflavorsampler;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


public class MainActivity extends AppCompatActivity {
    RetrieveDataTask dataTask = new RetrieveDataTask();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataTask.execute(this);

        final Button nextFlavor = findViewById(R.id.flavorButton);
        nextFlavor.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                //this crashes my app 100% of the time
                dataTask = new RetrieveDataTask();
                dataTask.execute(MainActivity.this);
            }
        });
    }



}
