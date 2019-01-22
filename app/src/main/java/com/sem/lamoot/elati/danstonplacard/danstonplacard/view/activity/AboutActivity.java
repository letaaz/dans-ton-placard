package com.sem.lamoot.elati.danstonplacard.danstonplacard.view.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sem.lamoot.elati.danstonplacard.danstonplacard.R;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        ImageView close_btn = findViewById(R.id.about_close_btn);
        close_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView credits_content = findViewById(R.id.about_credit_content);
        credits_content.setMovementMethod(LinkMovementMethod.getInstance());
    }

    @Override
    public void onBackPressed(){
        finish();
    }
}
