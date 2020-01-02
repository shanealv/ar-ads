package com.ar_ads;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    /**
     * Called when the user taps the Launch button
     */
    public void handle_ar_launch(View view) {
        Intent intent = new Intent(this, AugmentedFacesActivity.class);
        startActivity(intent);
    }

    public void handle_video_launch(View view) {
        Intent intent = new Intent(this, VideoActivity.class);
        startActivity(intent);
    }

}
