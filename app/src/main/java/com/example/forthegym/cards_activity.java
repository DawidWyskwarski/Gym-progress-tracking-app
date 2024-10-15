package com.example.forthegym;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class cards_activity extends AppCompatActivity {

    private LinearLayout LL;
    private Button chest_b;
    private Button back_b;
    private Button arms_b;
    private Button legs_b;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_cards);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        chest_b = findViewById(R.id.chestButton);
        back_b = findViewById(R.id.backButton);
        arms_b = findViewById(R.id.armsButton);
        legs_b = findViewById(R.id.legsButton);

        LL = findViewById(R.id.all);
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(LL,"alpha", 0f,1f);
        fadeIn.setDuration(2000);

        fadeIn.start();

        chest_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(cards_activity.this, chest_activity.class);
                startActivity(intent);
            }
        });

        back_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cards_activity.this, back_activity.class);
                startActivity(intent);
            }
        });

        arms_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cards_activity.this, arms_activity.class);
                startActivity(intent);
            }
        });

        legs_b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(cards_activity.this, legs_activity.class);
                startActivity(intent);
            }
        });
    }
}