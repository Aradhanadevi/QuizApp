package com.example.quizapp;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class QuizDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "quiz_db";
    public static final int DATABASE_VERSION = 1;

    public QuizDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE quiz_results (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "score INTEGER, " +
                "attempt_number INTEGER);";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS quiz_results");
        onCreate(db);
    }
}
