package com.example.part2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

public class DescriptionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        Intent intent = getIntent();
        String title = intent.getStringExtra("TITLE_TEXT");
        String desc = intent.getStringExtra("DESC_TEXT");

        ((TextView)(findViewById(R.id.title_text))).setText(title);
        ((TextView)(findViewById(R.id.description))).setText(desc);
    }
}