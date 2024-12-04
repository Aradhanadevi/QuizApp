package com.example.quizapp;

import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class QuizActivity extends AppCompatActivity {

    private int currentQuestionIndex = 0;
    private int score = 0;
    private QuizDatabaseHelper dbHelper;
    private TextView questionText, questionNumber, questionsRemaining;
    private RadioButton optionA, optionB, optionC, optionD;
    private Button submitButton;

    private String[] questions = {
            "What is 2 + 2?",
            "What is the capital of France?",
            "What is the largest ocean?",
            "Which planet is known as the Red Planet?",
            "What is the boiling point of water?",
            "Who wrote 'Romeo and Juliet'?",
            "What is the square root of 64?",
            "Which element has the symbol 'O'?",
            "What is the fastest land animal?",
            "Which country invented pizza?"
    };

    private String[][] options = {
            {"4", "5", "6", "7"},
            {"Paris", "London", "Berlin", "Madrid"},
            {"Atlantic", "Indian", "Pacific", "Arctic"},
            {"Mars", "Venus", "Jupiter", "Saturn"},
            {"100°C", "90°C", "110°C", "120°C"},
            {"William Shakespeare", "Mark Twain", "Charles Dickens", "J.K. Rowling"},
            {"6", "8", "9", "10"},
            {"Oxygen", "Gold", "Silver", "Nitrogen"},
            {"Cheetah", "Lion", "Leopard", "Tiger"},
            {"Italy", "USA", "India", "China"}
    };

    private int[] correctAnswers = {0, 0, 2, 0, 0, 0, 1, 0, 0, 0};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz);

        dbHelper = new QuizDatabaseHelper(this);
        questionText = findViewById(R.id.questionText);
        questionNumber = findViewById(R.id.questionNumber);
        questionsRemaining = findViewById(R.id.questionsRemaining);
        optionA = findViewById(R.id.optionA);
        optionB = findViewById(R.id.optionB);
        optionC = findViewById(R.id.optionC);
        optionD = findViewById(R.id.optionD);
        submitButton = findViewById(R.id.submitButton);

        loadQuestion();

        SharedPreferences sharedPreferences = getSharedPreferences("QuizAppPrefs", MODE_PRIVATE);
        int attempts = sharedPreferences.getInt("attempts", 0);
        attempts++;

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("attempts", attempts);
        editor.apply();

        int finalAttempts = attempts;
        submitButton.setOnClickListener(v -> {
            highlightAnswer(); // Highlight the selected answer
            submitButton.postDelayed(() -> {
                resetOptions(); // Reset option styles
                if (checkAnswer()) {
                    score++;
                }
                currentQuestionIndex++;

                if (currentQuestionIndex < questions.length) {
                    loadQuestion();
                } else {
                    saveScore(finalAttempts);

                    Intent intent = new Intent(QuizActivity.this, ScoreActivity.class);
                    intent.putExtra("score", score);
                    intent.putExtra("attempt_number", finalAttempts);
                    startActivity(intent);
                    finish();
                }
            }, 1000); // Delay moving to the next question to show the highlight
        });
    }

    private void loadQuestion() {
        questionText.setText(questions[currentQuestionIndex]);
        optionA.setText(options[currentQuestionIndex][0]);
        optionB.setText(options[currentQuestionIndex][1]);
        optionC.setText(options[currentQuestionIndex][2]);
        optionD.setText(options[currentQuestionIndex][3]);

        questionNumber.setText((currentQuestionIndex + 1) + "/" + questions.length);
        questionsRemaining.setText((questions.length - currentQuestionIndex - 1) + " Questions Left");

        RadioGroup group = findViewById(R.id.radioGroup);
        group.clearCheck(); // Deselect previous selection
    }

    private boolean checkAnswer() {
        RadioGroup group = findViewById(R.id.radioGroup);
        int selectedId = group.getCheckedRadioButtonId();

        if (selectedId == -1) {
            return false;
        }

        if (selectedId == optionA.getId() && correctAnswers[currentQuestionIndex] == 0) {
            return true;
        } else if (selectedId == optionB.getId() && correctAnswers[currentQuestionIndex] == 1) {
            return true;
        } else if (selectedId == optionC.getId() && correctAnswers[currentQuestionIndex] == 2) {
            return true;
        } else return selectedId == optionD.getId() && correctAnswers[currentQuestionIndex] == 3;
    }

    private void highlightAnswer() {
        RadioGroup group = findViewById(R.id.radioGroup);
        int selectedId = group.getCheckedRadioButtonId();

        if (selectedId != -1) {
            if (selectedId == optionA.getId()) {
                optionA.setTextColor(correctAnswers[currentQuestionIndex] == 0 ? Color.GREEN : Color.RED);
            } else if (selectedId == optionB.getId()) {
                optionB.setTextColor(correctAnswers[currentQuestionIndex] == 1 ? Color.GREEN : Color.RED);
            } else if (selectedId == optionC.getId()) {
                optionC.setTextColor(correctAnswers[currentQuestionIndex] == 2 ? Color.GREEN : Color.RED);
            } else if (selectedId == optionD.getId()) {
                optionD.setTextColor(correctAnswers[currentQuestionIndex] == 3 ? Color.GREEN : Color.RED);
            }
        }
    }

    private void resetOptions() {
        optionA.setTextColor(Color.BLACK);
        optionB.setTextColor(Color.BLACK);
        optionC.setTextColor(Color.BLACK);
        optionD.setTextColor(Color.BLACK);
    }

    private void saveScore(int attemptNumber) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("score", score);
        values.put("attempt_number", attemptNumber);
        db.insert("quiz_results", null, values);
    }
}
