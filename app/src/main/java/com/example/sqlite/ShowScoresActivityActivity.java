package com.example.sqlite;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.sqlite.HelperDB;
import java.util.ArrayList;

public class ShowScoresActivityActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{
    SQLiteDatabase db;
    HelperDB hlp;
    Cursor crsr;
    String tableName = Scores.TABLE_SCORES;
    String[] columns = {Scores.SCORE,Scores.TYPE,Scores.QUARTER,Scores.SUBJECT,Scores.SCORE_KEY_ID};
    String selection = Scores.STUDENT_SCORE_KEY_ID +"=?";
    String[] selectionArgs = {"someValue"};
    String groupBy = null;
    String having = null;
    String orderBy = null;
    String limit = null;
    ContentValues cv;
    ArrayList<String> tbl = new ArrayList<>();
    ArrayList<Integer> ids_list = new ArrayList<>();
    ArrayAdapter<String> adp;
    ArrayAdapter<String> sortespinneradp;
    ListView lv;
    Spinner sortespinner;
    int id_student;
    int score_id;
    String[] sorts = {"all scores","by subject","by score","by quarter"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        hlp = new HelperDB(this);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_scores_activity);
        lv = (ListView) findViewById(R.id.listview2);
        sortespinner = (Spinner) findViewById(R.id.spinner);
        adp = new ArrayAdapter<String>(this,androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
                ,tbl);
        lv.setAdapter(adp);
        registerForContextMenu(lv);
        sortespinneradp = new ArrayAdapter<String>(this,androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
                , sorts);
        sortespinner.setAdapter(sortespinneradp);
        sortespinner.setOnItemSelectedListener(this);
    }




    @Override
    protected void onStart() {
        super.onStart();
        id_student = getIntent().getIntExtra("the_id",-1);
        update();
    }

    public void update(){
        if (id_student != -1){
            tbl.clear();
            selectionArgs[0] = ""+id_student;
            db = hlp.getReadableDatabase();
            crsr = db.query(tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            int col1 = crsr.getColumnIndex(Scores.SCORE);
            int col2 = crsr.getColumnIndex(Scores.QUARTER);
            int col3 = crsr.getColumnIndex(Scores.SUBJECT);
            int col4 = crsr.getColumnIndex(Scores.TYPE);
            crsr.moveToFirst();
            while (!crsr.isAfterLast()) {
                tbl.add("Subject: " + crsr.getString(col3) + " Type: " + crsr.getString(col4) +
                        " Quarter: " + crsr.getString(col2) + " Score: " + crsr.getInt(col1));
                crsr.moveToNext();
            }
            crsr.close();
            db.close();
            adp.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo info){
        Log.d("ContextMenu", "onCreateContextMenu called");
        menu.setHeaderTitle("Possible Operations");
        menu.add("Delete Score");
        menu.add("Edit Score");
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {
        String action=item.getTitle().toString();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        score_id = ids_list.get(info.position);
        if (action.equals("Delete Score")){
            /*Intent si = new Intent(this, com.example.sqlite.ShowStudentDataActivity.class);
            si.putExtra("id",score_id);
            startActivity(si);*/
        }
        else if(action.equals("Edit Score")){
            Intent si = new Intent(this, InputScoreActivity.class);
            si.putExtra("the_score_id",score_id);
            si.putExtra("edit",true);
            startActivity(si);
        }
        return true;
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String st = item.getTitle().toString();
        if (st.equals("Credits screen")) {
            Intent si = new Intent(this, CreditsActivity.class);
            startActivity(si);
        }
        if (st.equals("Register student")) {
            Intent si = new Intent(this, InputStudentDataActivity.class);
            startActivity(si);
        }
        if (st.equals("Students List")) {
            Intent si = new Intent(this,ShowStudentActivity.class);
            si.putExtra("the_id", id_student);
            startActivity(si);
        }
        return super.onOptionsItemSelected(item);
    }

    public void AddScore (View view){
        Intent si = new Intent(this, InputScoreActivity.class);
        si.putExtra("the_id", id_student);
        startActivity(si);
    }
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position == 3){
            orderBy = "Quarter";
        }
        else if(position == 1){
            orderBy = "SUBJECT";
        }
        else if(position == 2){
            orderBy = "Score";
        }
        else orderBy = null;
            selectionArgs[0] = ""+id_student;
            tbl.clear();
            db = hlp.getReadableDatabase();
            crsr = db.query(tableName, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            int col1 = crsr.getColumnIndex(Scores.SCORE);
            int col2 = crsr.getColumnIndex(Scores.QUARTER);
            int col3 = crsr.getColumnIndex(Scores.SUBJECT);
            int col4 = crsr.getColumnIndex(Scores.TYPE);
            int col5 = crsr.getColumnIndex(Scores.SCORE_KEY_ID);
        crsr.moveToFirst();
            while (!crsr.isAfterLast()) {
                tbl.add("Subject: "+ crsr.getString(col3)+" Type: "+crsr.getString(col4)+
                        " Quarter: "+crsr.getString(col2)+" Score: "+crsr.getInt(col1));
                ids_list.add(crsr.getInt(col5));
                crsr.moveToNext();
            }
            crsr.close();
            db.close();
            adp.notifyDataSetChanged();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}