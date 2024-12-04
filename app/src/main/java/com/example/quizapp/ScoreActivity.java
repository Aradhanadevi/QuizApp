package com.example.quizapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class ScoreActivity extends AppCompatActivity {

    private TextView scoreText, highScoreText, attemptText;
    private Button historyButton, restartButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        scoreText = findViewById(R.id.scoreText);
        highScoreText = findViewById(R.id.highScoreText);
        attemptText = findViewById(R.id.attemptText);
        historyButton = findViewById(R.id.historyButton);
        restartButton = findViewById(R.id.restartButton);

        // Receive score and attempt number from QuizActivity
        Intent intent = getIntent();
        int score = intent.getIntExtra("score", 0);
        int attemptNumber = intent.getIntExtra("attempt_number", 1);

        scoreText.setText("Score: " + score);
        attemptText.setText("Attempt Number: " + attemptNumber);

        // Retrieve and update the high score using SharedPreferences
        SharedPreferences prefs = getSharedPreferences("QuizPrefs", MODE_PRIVATE);
        int highScore = prefs.getInt("highScore", 0);

        if (score > highScore) {
            highScore = score;
            SharedPreferences.Editor editor = prefs.edit();
            editor.putInt("highScore", highScore);
            editor.apply(); // Save the new high score
        }

        highScoreText.setText("High Score: " + highScore);

        // Set up the history button
        historyButton.setOnClickListener(v -> {
            // Display the history fragment within this activity
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(android.R.id.content, new HistoryFragment()) // Replace the entire activity content
                    .addToBackStack(null)
                    .commit();
        });

        // Set up the restart button
        restartButton.setOnClickListener(v -> {
            // Start QuizActivity to restart the quiz
            Intent restartIntent = new Intent(ScoreActivity.this, QuizActivity.class);
            restartIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // Clear the back stack
            startActivity(restartIntent);
            finish(); // Close the current activity
        });
    }
}
