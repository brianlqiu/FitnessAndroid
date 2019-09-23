package com.example.fitness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class CreateOrEditPlanActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener{
    MyRecyclerViewAdapter adapter;
    ArrayList<String> planNames;
    RecyclerView planList;
    Utility util = new Utility();
    final static int CREATE_PLAN_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_or_edit_plan);
        planNames = new ArrayList<String>();
        updatePlanNames();
        Log.i("Updating names", planNames.size() + "");
        planList = findViewById(R.id.planList);
        adapter = new MyRecyclerViewAdapter(this, planNames);
        setUpRecyclerView();
        adapter.setClickListener(this);


    }

    public void onItemClick(View view, int position) {
        editPlan(position);
    }

    public void updatePlanNames() {
        File planDir = getApplicationContext().getDir("Plans", Context.MODE_PRIVATE);
        String names[] = planDir.list();
        planNames.clear();
        for(int i = 0; i < names.length; i++) {
            planNames.add(util.underscoreToSpace(names[i]));
        }

    }

    public void createPlan(View view) {
        Intent intent = new Intent(this, EditPlanActivity.class);

        startActivityForResult(intent, CREATE_PLAN_REQUEST_CODE);
    }

    public void editPlan(int index) {
        Intent intent = new Intent(this, EditPlanActivity.class);
        intent.putExtra("Plan name", planNames.get(index));
        startActivityForResult(intent, CREATE_PLAN_REQUEST_CODE);

    }

    public void done(View view) {
        setResult(Activity.RESULT_OK);
        finish();
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // First we need to check if the requestCode matches the one we used.
        if(requestCode == CREATE_PLAN_REQUEST_CODE) {
            Log.i("Returned", "check");
            // The resultCode is set by the AnotherActivity
            // By convention RESULT_OK means that what ever
            // AnotherActivity did was successful
            if(resultCode == Activity.RESULT_OK) {
                // Get the result from the returned Intent
                updatePlanNames();
                adapter.notifyDataSetChanged();

            } else {
                // AnotherActivity was not successful. No data to retrieve.
                Log.i("Failed", resultCode + "");
            }
        }

    }

    private void setUpRecyclerView() {
        planList.setAdapter(adapter);
        planList.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(adapter, getApplicationContext()));
        itemTouchHelper.attachToRecyclerView(planList);
    }




}
