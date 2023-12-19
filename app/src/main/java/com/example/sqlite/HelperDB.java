package com.example.sqlite;

import static android.provider.Telephony.Carriers.PASSWORD;
import static com.example.sqlite.Scores.QUARTER;
import static com.example.sqlite.Scores.SCORE;
import static com.example.sqlite.Scores.SCORE_KEY_ID;
import static com.example.sqlite.Scores.SUBJECT;
import static com.example.sqlite.Scores.TABLE_SCORES;
import static com.example.sqlite.Scores.TYPE;
import static com.example.sqlite.Student.ADDRESS;
import static com.example.sqlite.Student.HOME_PHONE_NUMBER;
import static com.example.sqlite.Student.KEY_ID;
import static com.example.sqlite.Student.NAME;
import static com.example.sqlite.Student.PARENT_1_NAME;
import static com.example.sqlite.Student.PARENT_1_PHONE_NUMBER;
import static com.example.sqlite.Student.PARENT_2_NAME;
import static com.example.sqlite.Student.PARENT_2_PHONE_NUMBER;
import static com.example.sqlite.Student.PERSONAL_PHONE_NUMBER;
import static com.example.sqlite.Student.TABLE_STUDENTS;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class HelperDB extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "dbexam.db";
    String strCreate,strDelete;
    private static final int DATABASE_VERSION = 1;

    public HelperDB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        strCreate="CREATE TABLE "+TABLE_STUDENTS;
        strCreate+=" ("+KEY_ID+" INTEGER PRIMARY KEY,";
        strCreate+=" "+NAME+" TEXT,";
        strCreate+=" "+ADDRESS+" TEXT,";
        strCreate+=" "+PERSONAL_PHONE_NUMBER+" INTEGER,";
        strCreate+=" "+HOME_PHONE_NUMBER+" INTEGER,";
        strCreate+=" "+PARENT_1_NAME+" TEXT,";
        strCreate+=" "+PARENT_2_NAME+" TEXT,";
        strCreate+=" "+PARENT_1_PHONE_NUMBER+" INTEGER";
        strCreate+=" "+PARENT_2_PHONE_NUMBER+" INTEGER";
        strCreate+=");";
        db.execSQL(strCreate);
        strCreate="CREATE TABLE "+TABLE_SCORES;
        strCreate+=" ("+SCORE_KEY_ID+" INTEGER PRIMARY KEY,";
        strCreate+=" "+SCORE+" INTEGER,";
        strCreate+=" "+SUBJECT+" TEXT,";
        strCreate+=" "+TYPE+" TEXT,";
        strCreate+=" "+QUARTER+" TEXT";
        strCreate+=");";
        db.execSQL(strCreate);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        strDelete="DROP TABLE IF EXISTS "+TABLE_STUDENTS;
        db.execSQL(strDelete);
        strDelete="DROP TABLE IF EXISTS "+TABLE_SCORES;
        db.execSQL(strDelete);
    }
}
