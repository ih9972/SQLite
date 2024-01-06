package com.example.sqlite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
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
import android.widget.Toast;

import java.util.ArrayList;

public class ShowStudentActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    SQLiteDatabase db;
    HelperDB hlp;
    Cursor crsr;
    ListView lv;
    ArrayList<String> tbl = new ArrayList<>();
    ArrayList<String> ids_list = new ArrayList<>();
    String TABLE = "Student";
    String[] columns = {Student.NAME, Student.ACTIVE, Student.KEY_ID};
    String selection =null;
    String[] selectionArgs = null;
    String groupBy = null;
    String having = null;
    String orderBy = Student.NAME;
    String limit = null;
    Context context = this;
    ContentValues cv;
    String id;
    ArrayAdapter<String> adp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_student);
        lv = (ListView) findViewById(R.id.listview2);
        hlp = new HelperDB(this);
        adp = new ArrayAdapter<String>(this,
                androidx.appcompat.R.layout.support_simple_spinner_dropdown_item
                , tbl);
        lv.setAdapter(adp);
        lv.setOnItemClickListener((AdapterView.OnItemClickListener)this);
        registerForContextMenu(lv);
        Toast.makeText(context,"Press On Student For More Data" , Toast.LENGTH_SHORT).show();
        cv = new ContentValues();
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo info){
        Log.d("ContextMenu", "onCreateContextMenu called");
        menu.setHeaderTitle("Possible Operations");
        menu.add("Student Data");
        menu.add("Activate/ Deactivate Student");
        menu.add("Student Score");
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        String action=item.getTitle().toString();
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        id = ids_list.get(info.position);
        if (action.equals("Student Data")){
            Intent si = new Intent(this, com.example.sqlite.ShowStudentDataActivity.class);
            si.putExtra("id",id);
            startActivity(si);
        }
        else if (action.equals("Activate/ Deactivate Student")){
            String selection =Student.KEY_ID +"=?";
            String[] selectionArgs = {id};
            int temp=0;
            db = hlp.getReadableDatabase();
            crsr = db.query(TABLE, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
            int col1 = crsr.getColumnIndex(Student.ACTIVE);
            crsr.moveToFirst();
            while (!crsr.isAfterLast()) {
                if (crsr.getInt(col1) == 1) temp = 0;
                else temp = 1;
                crsr.moveToNext();
            }
            crsr.close();
            db.close();
            cv.clear();
            cv.put(Student.ACTIVE,temp);
            db = hlp.getWritableDatabase();
            db.update(Student.TABLE_STUDENTS, cv, Student.KEY_ID + "=?", new String[]{"" + id});
            db.close();
            Toast.makeText(context, "Changes Saved", Toast.LENGTH_SHORT).show();
            reset();
        }
        else if(action.equals ("Student Score")){
            Intent si = new Intent(this, ShowScoresActivityActivity.class);
            si.putExtra("id",id);
            startActivity(si);
        }
        return true;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onResume() {
        super.onResume();
        reset();
    }

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        String st = item.getTitle().toString();
        if (st.equals("Credits screen")) {
            Intent si = new Intent(this, com.example.sqlite.CreditsActivity.class);
            startActivity(si);
        }
        if (st.equals("Register student")) {
            Intent si = new Intent(this, com.example.sqlite.InputStudentDataActivity.class);
            startActivity(si);
        }
        return super.onOptionsItemSelected(item);
    }

    public void reset(){
        String name_;
        String active;
        tbl.clear();
        db = hlp.getReadableDatabase();
        crsr = db.query(TABLE, columns, selection, selectionArgs, groupBy, having, orderBy, limit);
        int col1 = crsr.getColumnIndex(Student.NAME);
        int col2 = crsr.getColumnIndex(Student.ACTIVE);
        int col3 = crsr.getColumnIndex(Student.KEY_ID);
        crsr.moveToFirst();
        while (!crsr.isAfterLast()) {
            name_ = crsr.getString(col1);
            if (crsr.getInt(col2) == 1) active="not active";
            else active="active";
            tbl.add("" + name_+" "+active);
            ids_list.add("" + crsr.getInt(col3));
            crsr.moveToNext();
        }
        crsr.close();
        db.close();
        adp.notifyDataSetChanged();
        lv.setAdapter(adp);
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        openContextMenu(view);
    }
}