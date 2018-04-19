package com.stvjuliengmail.smartmeds.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.ittianyu.bottomnavigationviewex.BottomNavigationViewEx;
import com.stvjuliengmail.smartmeds.R;
import com.stvjuliengmail.smartmeds.adapter.MyMedsAdapter;
import com.stvjuliengmail.smartmeds.adapter.RecyclerViewItemClickListener;
import com.stvjuliengmail.smartmeds.api.InteractionsListTask;
import com.stvjuliengmail.smartmeds.api.MyInteractionsTask;
import com.stvjuliengmail.smartmeds.database.GetDBMedsTask;
import com.stvjuliengmail.smartmeds.model.MyInteraction;
import com.stvjuliengmail.smartmeds.model.MyMed;

import java.util.ArrayList;

public class MyMedsActivity extends AppCompatActivity implements BottomNavigationViewEx.OnNavigationItemSelectedListener {
    private final String TAG = getClass().getSimpleName();
    private CoordinatorLayout coordinatorLayout;
    private RecyclerView recyclerView;
    private MyMedsAdapter adapter;
    private ArrayList<MyMed> myMedsList = new ArrayList<>();
    private ArrayList<MyInteraction> myInteractions = new ArrayList<>();
    private String interactionDisclaimer;
    private Context context;
    private String[] rxcuis;
    private BottomNavigationViewEx bottomNavigationViewEx;
    private boolean isInteractionsViewed;
    private Button btnShowInteractions;
    private LinearLayout interactionsWidget;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_meds);
        context = this;
        initializeUiComponents();
        wireAdapterToRecyclerView();

    }

    @Override
    protected void onResume(){
        hideInteractionsButton();
        new GetDBMedsTask(this).execute("");
        super.onResume();
    }

    private void initializeUiComponents(){
        recyclerView = findViewById(R.id.rvMyMeds);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bottomNavigationViewEx = findViewById(R.id.bottom_nav_view);
        bottomNavigationViewEx.setOnNavigationItemSelectedListener(this);
        bottomNavigationViewEx.setSelectedItemId(R.id.bottom_nav_my_meds);
        interactionsWidget = findViewById(R.id.interactions_widget);

        btnShowInteractions = findViewById(R.id.btnShowInteractions);
        btnShowInteractions.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startInteractions();
            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.bottom_nav_search: {
                item.setChecked(true);
                dieAndStartA(SearchActivity.class);
                break;
            }
            case R.id.bottom_nav_home:{
                item.setChecked(true);
                dieAndStartA(MenuActivity.class);
                break;
            }
            case R.id.bottom_nav_my_meds: {
                item.setChecked(true);
                break;
            }
        }
        return false;
    }

    private void wireAdapterToRecyclerView() {
        adapter = new MyMedsAdapter(myMedsList, R.layout.rv_row_my_meds,
                getApplicationContext());
        //Create custom interface object and send it to adapter for clickable list items
        adapter.setOnItemClickListener(new RecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                MyMed myMed = myMedsList.get(position);
                Intent intent = new Intent(context, RxInfoActivity.class);
                intent.putExtra("myMed", myMed);
                context.startActivity(intent);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                // TODO: Add delete functionality here
            }
        });
        recyclerView.setAdapter(adapter);
    }

    public void setMyMeds(ArrayList<MyMed> myMedsFromDb) {
        if(myMedsFromDb == null || myMedsFromDb.isEmpty()){
            notifyNoRecords();
        }
        else{
            myMedsList.clear();
            myMedsList.addAll(myMedsFromDb);
            adapter.notifyDataSetChanged();
        }
    }

    public class SnackBarListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            dieAndStartA(SearchActivity.class);
        }
    }

    private void notifyNoRecords() {
        Snackbar mySnackbar = Snackbar.make(findViewById(R.id.my_meds_layout),
                "You don't have any pills saved.", Snackbar.LENGTH_INDEFINITE);
        mySnackbar.setAction("Search Pills", new SnackBarListener());
        mySnackbar.show();
    }

//    public void checkForInteractions() {
//        setRxcuis();
//        new MyInteractionsTask(this, rxcuis).execute("");
//    }

//    private void setRxcuis() {
//        rxcuis = new String[myMedsList.size()];
//        int index = 0;
//        for (MyMed myMed : myMedsList) {
//            rxcuis[index] = myMed.getRxcui();
//            index++;
//        }
//    }

    public void setDisclaimer(String disclaimer) {
        interactionDisclaimer = disclaimer;
    }

    public void setMyInteractions(ArrayList<MyInteraction> myInteractionsFromApi) {
        if(myInteractions != null){
            myInteractions.clear();
        }
        if (myInteractionsFromApi != null && myInteractionsFromApi.size() > 0) {
            myInteractions = myInteractionsFromApi;
            warnInteractions();
            showInteractionsButton();
        }
        else{
            hideInteractionsButton();
        }
    }

    private void warnInteractions() {
        if(isInteractionsViewed == false){
            AlertDialog alertDialog = new AlertDialog.Builder(this).create();
            alertDialog.setTitle("Drug Interactions!");
            alertDialog.setMessage("Your medicines have interactions that may be hazardous.");
            alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "CANCEL",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
            alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "VIEW INTERACTIONS",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            startInteractions();
                        }
                    });
            alertDialog.show();
        }
    }

    private void showInteractionsButton() {
        interactionsWidget.setVisibility(View.VISIBLE);
    }
    private void hideInteractionsButton() {
        if(interactionsWidget != null && interactionsWidget.getVisibility() == View.VISIBLE){
            interactionsWidget.setVisibility(View.INVISIBLE);
        }
    }

    private void startInteractions() {
        isInteractionsViewed = true;
        Intent intent = new Intent(this, MyInteractionsActivity.class);
        intent.putExtra("my_interactions", myInteractions);
        intent.putExtra("disclaimer", interactionDisclaimer);
        startActivity(intent);
    }

    private void dieAndStartA(Class c){
        Intent intent = new Intent(this, c);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

}



