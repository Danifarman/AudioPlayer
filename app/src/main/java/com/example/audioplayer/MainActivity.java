package com.example.audioplayer;


import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.widget.*;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // VideoView ki jagah MediaPlayer aa gaya
    MediaPlayer mediaPlayer;

    SeekBar seekBar;
    Button btnPlay, btnPause, btnRewind;
    TextView tvTime;

    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Views dhoondo
        seekBar = findViewById(R.id.seekBar);
        btnPlay = findViewById(R.id.btnPlay);
        btnPause = findViewById(R.id.btnPause);
        btnRewind = findViewById(R.id.btnRewind);
        tvTime = findViewById(R.id.tvTime);

        // --- CHANGE 1: Audio Load karna ---
        // Video mein: setVideoURI use kiya tha
        // Audio mein: MediaPlayer.create use karenge
        // (Make sure 'sample' naam ka song raw folder mein ho)
        mediaPlayer = MediaPlayer.create(this, R.raw.bg_music);
        mediaPlayer.setLooping(true);

        // SeekBar ki max length set karo
        seekBar.setMax(mediaPlayer.getDuration());

        // SeekBar update shuru karo
        updateSeekBar();

        // --- Controls (Bilkul Video jaise) ---

        // Play
        btnPlay.setOnClickListener(v -> mediaPlayer.start());

        // Pause
        btnPause.setOnClickListener(v -> mediaPlayer.pause());

        // Rewind (-5 sec)
        btnRewind.setOnClickListener(v -> {
            int currentPos = mediaPlayer.getCurrentPosition();
            // Math.max is liye taake 0 se neeche na jaye
            mediaPlayer.seekTo(Math.max(currentPos - 5000, 0));
        });

        // SeekBar ko hath se move karna
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    mediaPlayer.seekTo(progress);
                }
            }
            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });
    }

    // --- Time Update Loop (Bilkul Same) ---
    private void updateSeekBar() {
        handler.postDelayed(() -> {
            if (mediaPlayer != null && mediaPlayer.isPlaying()) {
                seekBar.setProgress(mediaPlayer.getCurrentPosition());

                int current = mediaPlayer.getCurrentPosition() / 1000;
                int total = mediaPlayer.getDuration() / 1000;

                tvTime.setText(current + "s / " + total + "s");
            }
            updateSeekBar();
        }, 1000);
    }

    // --- CHANGE 2: App band hone par Music band karna ---
    // VideoView ye khud karta hai, lekin MediaPlayer chalta rehta hai.
    // Is liye hum 'onStop' mein usay zabardasti rokty hain.
    @Override
    protected void onStop() {
        super.onStop();
        if (mediaPlayer != null) {
            mediaPlayer.pause(); // Jab app minimize ho, gaana ruk jaye
        }
    }
}