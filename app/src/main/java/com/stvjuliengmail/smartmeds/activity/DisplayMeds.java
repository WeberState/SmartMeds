package com.stvjuliengmail.smartmeds.activity;

import android.app.Activity;

import android.content.Intent;
import android.database.Cursor;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import android.widget.Button;
import android.widget.TextView;

import com.stvjuliengmail.smartmeds.R;

public class DisplayMeds extends Activity {
    int from_Where_I_Am_Coming = 0;

    TextView RXid;
    TextView RXname;
    TextView dosage;
    TextView doc;

    int id_To_Update = 0;

    protected void onCreate(Integer id) {
        setContentView(R.layout.activity_display_meds);
        RXid = (TextView) findViewById(R.id.editRXid1);
        RXname = (TextView) findViewById(R.id.editRXName);
        dosage = (TextView) findViewById(R.id.editDosage1);
        doc = (TextView) findViewById(R.id.editDoc1);

        DBHelper mydb = new DBHelper(this);

//        Bundle extras = getIntent().getExtras();
//        if (extras != null) {
//            int Value = extras.getInt("id");
//
//            if (Value > 0) {
        //means this is the view part not the add meds part.
        Cursor rs = mydb.getData(id);
        id_To_Update = id;
        rs.moveToFirst();

        String rxID = rs.getString(rs.getColumnIndex(DBHelper.COLUMN_RXid));
        String rxName = rs.getString(rs.getColumnIndex(DBHelper.COLUMN_Name));
        String dose = rs.getString(rs.getColumnIndex(DBHelper.COLUMN_DOSAGE));
        String doctor = rs.getString(rs.getColumnIndex(DBHelper.COLUMN_RXDOC));

        if (!rs.isClosed()) {
            rs.close();
        }
        Button b = (Button) findViewById(R.id.button1);
        b.setVisibility(View.INVISIBLE);

        RXid.setText((CharSequence) rxID);
        RXid.setFocusable(false);
        RXid.setClickable(false);

        RXname.setText((CharSequence) rxName);
        RXname.setFocusable(false);
        RXname.setClickable(false);

        dosage.setText((CharSequence) dose);
        dosage.setFocusable(false);
        dosage.setClickable(false);

        doc.setText((CharSequence) doctor);
        doc.setFocusable(false);
        doc.setClickable(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }

        @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        int id = item.getItemId();
        if(id==R.id.action_mainmenu)
        {
            Intent mainMenuIntent = new Intent(DisplayMeds.this, MenuActivity.class);
            startActivity(mainMenuIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}