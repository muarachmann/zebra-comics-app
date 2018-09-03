package com.level500.ub.zebracomics;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Toast;

import java.util.Timer;
import java.util.TimerTask;

public class WelcomeActivity extends AppCompatActivity {

    LinearLayout linearLayout;
    int[] drawablearray=new int[]{
            R.drawable.b1,
            R.drawable.b2,
            R.drawable.b3,
            R.drawable.b4,
            R.drawable.b5,
            R.drawable.b6,
            R.drawable.b7
    };
    Timer _t;
    public static int count=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Intent iin= getIntent();
        Bundle b = iin.getExtras();
        if(b != null)
        {
            String errormsg =(String) b.get("messageError");
            Toast.makeText(WelcomeActivity.this, errormsg, Toast.LENGTH_SHORT).show();
        }

        linearLayout = (LinearLayout) findViewById(R.id.welcome_bg);
        linearLayout.setBackgroundResource(R.drawable.b4);
        _t = new Timer();
        _t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (count < drawablearray.length) {

                            linearLayout.setBackgroundResource(drawablearray[count]);
                            count = (count + 1) % drawablearray.length;
                        }
                    }
            });
        }
    }, 3000, 3000);

    }

    public void gotoHome(View view){
        startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
        finish();
    }


}

