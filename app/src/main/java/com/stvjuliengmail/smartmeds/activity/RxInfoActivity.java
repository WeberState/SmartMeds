package com.stvjuliengmail.smartmeds.activity;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.stvjuliengmail.smartmeds.R;
import com.stvjuliengmail.smartmeds.api.REQUEST_BASE;
import com.stvjuliengmail.smartmeds.api.RxInfoMayTreatsTask;

import java.util.ArrayList;

public class RxInfoActivity extends AppCompatActivity {
    private final String TAG = getClass().getSimpleName();
    private TextView tvName, tvMayTreat;
    private ImageView imageView;
    private int rxcui; // the id of the selected pill
    private String name;
    private String imageUrl;
    private ArrayList<String> mayTreatDiseaseNames;
    private FloatingActionButton fabSaveMyMeds;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rx_info);
        context = this;

        unpackIntentExtras();

        instantiateUiElements();

        wireUpSaveToMyMedsButton();

        displayName();

        new RxInfoMayTreatsTask(this, getMayTreatsRequest()).execute("");
    }

    public void unpackIntentExtras() {
        Bundle extras = getIntent().getExtras();
        rxcui = extras.getInt("rxcui");
        name = extras.getString("name");
        imageUrl = extras.getString("imageUrl");
    }

    public void instantiateUiElements() {
        tvName = (TextView) findViewById(R.id.tvName);
        tvMayTreat = (TextView) findViewById(R.id.tvMayTreat);
        fabSaveMyMeds = (FloatingActionButton) findViewById(R.id.fabSaveMyMeds);
        imageView = (ImageView) findViewById(R.id.imageView);
    }

    public void wireUpSaveToMyMedsButton() {
        fabSaveMyMeds.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, "This should open a form", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void displayName(){
        tvName.setText(name);
    }

    public void displayImage(){

    }

    public String getMayTreatsRequest() {
        return REQUEST_BASE.CLASS_BY_RXCUI + Integer.toString(rxcui) + "&relaSource=NDFRT&relas=may_treat";
    }

    public void populateMayTreat(String diseases) {
        tvMayTreat.setText(diseases);
    }

}
