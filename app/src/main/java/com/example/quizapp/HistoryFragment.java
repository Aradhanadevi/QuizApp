package com.example.quizapp;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class HistoryFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);

        TextView historyText = view.findViewById(R.id.historyText);
        TextView highScoreText = view.findViewById(R.id.highScoreText);

        // Fetch data from the database
        QuizDatabaseHelper dbHelper = new QuizDatabaseHelper(getContext());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Fetch the high score
        Cursor highScoreCursor = db.rawQuery("SELECT MAX(score) AS high_score FROM quiz_results", null);
        if (highScoreCursor.moveToFirst()) {
            int highScore = highScoreCursor.getInt(highScoreCursor.getColumnIndexOrThrow("high_score"));
            highScoreText.setText("High Score: " + highScore);
        } else {
            highScoreText.setText("High Score: N/A");
        }
        highScoreCursor.close();

        // Fetch the history
        Cursor cursor = db.query(
                "quiz_results", // Table name
                new String[]{"attempt_number", "score"}, // Columns
                null, null, null, null, null);

        StringBuilder history = new StringBuilder();
        while (cursor.moveToNext()) {
            int attempt = cursor.getInt(cursor.getColumnIndexOrThrow("attempt_number"));
            int score = cursor.getInt(cursor.getColumnIndexOrThrow("score"));
            history.append("Attempt ").append(attempt).append(": Score ").append(score).append("\n");
        }
        cursor.close();

        if (history.length() == 0) {
            historyText.setText("No history available.");
        } else {
            historyText.setText(history.toString());
        }

        return view;
    }
}
