package com.stvjuliengmail.smartmeds.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.stvjuliengmail.smartmeds.R;
import com.stvjuliengmail.smartmeds.api.ImageDownloadTask;
import com.stvjuliengmail.smartmeds.database.SmartMedsDbOpenHelper;
import com.stvjuliengmail.smartmeds.model.MyMed;

public class AddOrEditMyMedActivity extends AppCompatActivity {

    private String rxcuiAsString, name, dosage, doctor, directions, pharmacy, imageUrl;
    private TextView tvName;
    private EditText etDosage, etDoctor, etDirections, etPharmacy;
    private ImageView ivPillImage;
    private FloatingActionButton fabSave;
    private MyMed myMed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_pill);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        tvName = (TextView) findViewById(R.id.tvName);
        etDosage = (EditText) findViewById(R.id.etDosage);
        etDoctor = (EditText) findViewById(R.id.etDoctor);
        etDirections = (EditText) findViewById(R.id.etDirections);
        etPharmacy = (EditText) findViewById(R.id.etPharmacy);
        ivPillImage = (ImageView) findViewById(R.id.ivPillImage);
        fabSave = (FloatingActionButton) findViewById(R.id.fabSave) ;
        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                save();
                routeToMyMeds();
            }
        });
        unpackIntentExtras();

        setImage();
        displayDataFromIntent();
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id==android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void unpackIntentExtras() {
        Bundle extras = getIntent().getExtras();
        MyMed myMed = null;
        myMed = extras.getParcelable("myMed");
        if(myMed != null){
            rxcuiAsString = myMed.getRxcui();
            name = myMed.getName();
            dosage = myMed.getDosage();
            doctor = myMed.getDoctor();
            directions = myMed.getDirections();
            pharmacy = myMed.getPharmacy();
            imageUrl = myMed.getImageUrl();
        }
        else{
            rxcuiAsString = extras.getString("rxcui");
            name = extras.getString("name");
            doctor = "";
            directions = "";
            pharmacy = "";
            imageUrl = extras.getString("imageUrl");
        }
        if(dosage == null || dosage.isEmpty()){
            dosage = name.replaceAll("\\D+", "") + " mg";
        }
    }

    private void setImage() {
        ImageDownloadTask task = new ImageDownloadTask();
        try {
            Bitmap myBitmap = task.execute(imageUrl).get();
            if (myBitmap == null) {
                ivPillImage.setImageResource(R.drawable.no_img_avail);
            } else {
                ivPillImage.setImageBitmap(myBitmap);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayDataFromIntent() {
        tvName.setText(name);
        etDosage.setText(dosage);
        etDoctor.setText(doctor);
        etDirections.setText(directions);
        etPharmacy.setText(pharmacy);
    }


    private void save() {
        // Get singleton instance of database
        SmartMedsDbOpenHelper dbOpenHelper = SmartMedsDbOpenHelper.getInstance(this);
        dbOpenHelper.addOrUpdateMyMed(new MyMed(
                name,
                rxcuiAsString,
                etDosage.getText().toString(),
                etDoctor.getText().toString(),
                etDirections.getText().toString(),
                etPharmacy.getText().toString(),
                imageUrl
        ));
    }

    private void routeToMyMeds() {
        Intent intent = new Intent(this, MyMedsActivity.class);
        startActivity(intent);
        finish();
    }
}



