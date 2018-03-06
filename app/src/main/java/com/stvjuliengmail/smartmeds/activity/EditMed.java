package com.stvjuliengmail.smartmeds.activity;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.stvjuliengmail.smartmeds.R;
import com.stvjuliengmail.smartmeds.model.BitmapUtility;

public class EditMed extends AppCompatActivity {
    private Context context;
    private TextView textRXid;
    private TextView textRXName;
    private TextView textdosage;
    private TextView textdoc;
    private ImageView editPillImage;
    private Button btnSave;
    private Bundle bundle;
    private DBHelper db;
    private BitmapUtility bu;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_med);
        context = this;

        textRXid = (TextView) findViewById(R.id.editRXid1);
        textRXName = (TextView) findViewById(R.id.editRXName);
        textdosage = (TextView) findViewById(R.id.editDosage1);
        textdoc = (TextView) findViewById(R.id.editDoc1);
        btnSave = (Button) findViewById(R.id.btnSave);
        editPillImage = (ImageView) findViewById(R.id.editPillImage);

        String id = null;
        String name = null;
        String dose= null;
        String doc= null;
        byte[] pillImageArray = null;
        bu = new BitmapUtility();
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        db = new DBHelper(this);

        bundle = getIntent().getExtras();
        if(bundle != null)
        {
            Cursor rs = db.getData(bundle.getInt("RXid"));
            rs.moveToFirst();
            while (!rs.isAfterLast()) {
                id = rs.getString(1);
                name = rs.getString(2);
                dose = rs.getString(3);
                doc = rs.getString(4);
                pillImageArray = rs.getBlob(5);
                rs.moveToNext();
            }
            if (!rs.isClosed()) {
                rs.close();
            }

            editPillImage.setImageBitmap(bu.getImage(pillImageArray));

            textRXid.setText(id);
            textRXid.setFocusable(false);
            textRXid.setClickable(false);

            textRXName.setText(name);
            textRXName.setFocusable(false);
            textRXName.setClickable(false);

            textdosage.setText(dose);
            textdosage.setEnabled(true);
            textdosage.setFocusableInTouchMode(true);
            textdosage.setClickable(true);

            textdoc.setText(doc);
            textdoc.setFocusableInTouchMode(true);
            textdoc.setEnabled(true);
            textdoc.setClickable(true);
        }

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.updateMed(textRXid.getText().toString(),
                        textRXName.getText().toString(),
                        textdosage.getText().toString(),
                        textdoc.getText().toString());
                hideKeyboard();
                Toast.makeText(context, "Medication Saved.", Toast.LENGTH_SHORT).show();


            }
        });
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
            Intent mainMenuIntent = new Intent(EditMed.this, MenuActivity.class);
            startActivity(mainMenuIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void hideKeyboard() {
        InputMethodManager inputManager = (InputMethodManager)
                getSystemService(context.INPUT_METHOD_SERVICE);
        inputManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);
    }
//
//    public void run(View view) {
//        int x = bundle.getInt("RXid");
//        Integer rxint = null;
//            if(x>0){
//                if(db.updateMed(x,textRXid.getText().toString(), textRXName.getText().toString(), textdosage.getText().toString(), textdoc.getText().toString()))
//                {
//                    Toast.makeText(getApplicationContext(), "Updated", Toast.LENGTH_SHORT).show();
//                    Intent intent = new Intent(getApplicationContext(),MyMedsActivity.class);
//                    startActivity(intent);
//                } else{
//                    Toast.makeText(getApplicationContext(), "not Updated", Toast.LENGTH_SHORT).show();
//                }
//            } else{
//
//                try
//                {
//                    rxint = Integer.parseInt(textRXid.getText().toString());
//                }
//                catch(NumberFormatException nfe)
//                {
//                }
//
//                if(db.insertRX(rxint, textRXName.getText().toString(), textdosage.getText().toString(), textdoc.getText().toString())){
//                    Toast.makeText(getApplicationContext(), "done",
//                            Toast.LENGTH_SHORT).show();
//                } else{
//                    Toast.makeText(getApplicationContext(), "not done",
//                            Toast.LENGTH_SHORT).show();
//                }
//                Intent intent = new Intent(getApplicationContext(),MyMedsActivity.class);
//                startActivity(intent);
//            }
//        }
//    private void update(View view){
//        String RXID = textRXid.getText().toString();
//        String newDosage = textdosage.getText().toString();
//        String newDoc = textdoc.getText().toString();
//
//        db.updateMed(bundle.getInt("RXid"), RXID, newDosage, newDoc);
//    }
}
