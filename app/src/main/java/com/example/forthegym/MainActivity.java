package com.example.forthegym;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.time.LocalDate;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    Button b;
    TextView t;
    Random rand;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            int day = LocalDate.now().getDayOfMonth();

            SharedPreferences sharedPreferences = getSharedPreferences("app_prefs", MODE_PRIVATE);
            int savedDay = sharedPreferences.getInt("saved_day", -1);

            if(day == savedDay){
                Intent intent = new Intent(MainActivity.this, cards_activity.class);
                startActivity(intent);
                finish();
            }else{
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt("saved_day", day);
                editor.apply();
            }
        }

        String[] quotes = getResources().getStringArray(R.array.quotes);
        int l = quotes.length;

        b = findViewById(R.id.generate);
        t = findViewById(R.id.display);
        rand = new Random();

        t.setText( quotes[rand.nextInt(l)] );

        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(t, "alpha", 1f, 0f);
        fadeOut.setDuration(1000);

        b.setOnClickListener(new View.OnClickListener()  {
            @Override
            public void onClick(View v) {
                fadeOut.start();

                Intent intent = new Intent(MainActivity.this, cards_activity.class);
                startActivity(intent);
                finish();

            }
        });

    }
}