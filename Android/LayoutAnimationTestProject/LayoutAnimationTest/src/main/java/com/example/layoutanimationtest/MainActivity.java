package com.example.layoutanimationtest;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button b = (Button) findViewById(R.id.button);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                View v = findViewById(R.id.greenbox);
                v.setVisibility(View.GONE);
                /* This doesn't work
                LinearLayout.LayoutParams xx = (LinearLayout.LayoutParams) v.getLayoutParams();

                if (xx.weight == 0.0f)
                    xx.weight = 0.35f;
                else
                    xx.weight = 0.0f;

                v.setLayoutParams(xx);*/
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    
}
