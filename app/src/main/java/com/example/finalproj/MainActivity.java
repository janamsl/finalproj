package com.example.finalproj;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.VideoView;
import android.content.Intent;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {
    Button exploreButton;
    VideoView appVideo;

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


        appVideo = findViewById(R.id.appVideo);
        exploreButton = findViewById(R.id.exploreButton);


        Uri videoUri = Uri.parse("android.resource://" + getPackageName() + "/" + R.raw.food); // Replace intro_video with your video file name
        appVideo.setVideoURI(videoUri);
        appVideo.start();


        appVideo.setOnCompletionListener(mp -> appVideo.start());


        exploreButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, FoodNamesActivity.class);
                startActivity(intent);
            }
        });
    }
}
