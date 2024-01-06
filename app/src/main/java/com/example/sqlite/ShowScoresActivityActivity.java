package com.example.sqlite;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import com.example.sqlite.HelperDB;
import java.util.ArrayList;

public class ShowScoresActivityActivity extends AppCompatActivity {
    SQLiteDatabase db;
    HelperDB hlp;
    Cursor crsr;
    String TABLE = Scores.TABLE_SCORES;
    String[] columns = {Scores.SCORE,Scores.TYPE,Scores.QUARTER,Scores.SUBJECT,Scores.STUDENT_SCORE_KEY_ID};
    String selection = Scores.STUDENT_SCORE_KEY_ID +"=?";
    String[] selectionArgs = new String[1];
    String groupBy = null;
    String having = null;
    String orderBy = null;
    String limit = null;
    ContentValues cv;
    Boolean fromInput ;
    ArrayList<String> tbl = new ArrayList<>();
    ArrayAdapter<String> adp;
    ArrayAdapter<String> spinneradp;
    ListView lv;
    Spinner spinner;
    String id_student;
    String[] sorts = {"all scores","by subject","by score","by quarter"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hlp = new HelperDB(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_scores_activity);
        lv = (ListView) findViewById(R.id.listview2);
        spinner = (Spinner) findViewById(R.id.spinner);
        adp = new ArrayAdapter<String>(this,androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
                ,tbl);
        lv.setAdapter(adp);
        spinneradp = new ArrayAdapter<String>(this,androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
                , sorts);
        spinner.setAdapter(spinneradp);
        fromInput = false;
    }

    @Override
    protected void onStart() {
        id_student = getIntent().getStringExtra("id");
        fromInput = getIntent().getBooleanExtra("frominput",false);
        super.onStart();
        if (fromInput){
            tbl.clear();
            db = hlp.getReadableDatabase();
            selectionArgs[0] = id_student;
            crsr = db.query(TABLE, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            int col1 = crsr.getColumnIndex(Scores.SCORE);
            int col2 = crsr.getColumnIndex(Scores.QUARTER);
            int col3 = crsr.getColumnIndex(Scores.SUBJECT);
            int col4 = crsr.getColumnIndex(Scores.TYPE);
            crsr.moveToFirst();
            while (!crsr.isAfterLast()) {
                tbl.add("Subject: "+ crsr.getString(col3)+" Type: "+crsr.getString(col4)+
                        " Quarter: "+crsr.getString(col2)+" Score: "+crsr.getInt(col1));
            }
            crsr.close();
            db.close();
            adp.notifyDataSetChanged();
        }
    }


    public void AddScore (View view){
        Intent si = new Intent(this, InputScoreActivity.class);
        si.putExtra("id", id_student);
        startActivity(si);
    }
}