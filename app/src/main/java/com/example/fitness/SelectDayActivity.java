package com.example.fitness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.io.File;
import java.util.ArrayList;

public class SelectDayActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener{

    private static final int FINISH_WORKOUT_REQUEST_CODE = 0;
    MyRecyclerViewAdapter adapter;
    ArrayList<String> dayNames;
    RecyclerView dayList;
    Utility util = new Utility();
    WorkoutPlan p;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_day);
        dayNames = new ArrayList<String>();
        p = new WorkoutPlan(getIntent().getStringExtra("Plan name"), getApplicationContext());
        updateDayNames();
        dayList = findViewById(R.id.planList);
        adapter = new MyRecyclerViewAdapter(this, dayNames);
        setUpRecyclerView();
        adapter.setClickListener(this);
        TextView text = findViewById(R.id.planNameText);
        text.setText(getIntent().getStringExtra("Plan name"));
    }

    private void setUpRecyclerView() {
        dayList.setAdapter(adapter);
        dayList.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(adapter, getApplicationContext()));
        itemTouchHelper.attachToRecyclerView(dayList);
    }

    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, WorkoutActivity.class);
        intent.putExtra("Day path", p.getDays().get(position).getPath());
        intent.putExtra("Day index", position);
        intent.putExtra("Plan name", p.getPlanName());
        startActivityForResult(intent, FINISH_WORKOUT_REQUEST_CODE);
    }

    public void updateDayNames() {
        dayNames.clear();
        for(int i = 0; i < p.size(); i++) {
            dayNames.add(p.getDays().get(i).getDayName());
        }
        adapter.notifyDataSetChanged();

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // First we need to check if the requestCode matches the one we used.
        if(requestCode == FINISH_WORKOUT_REQUEST_CODE) {
            Log.i("Returned", "check");
            // The resultCode is set by the AnotherActivity
            // By convention RESULT_OK means that what ever
            // AnotherActivity did was successful
            if(resultCode == Activity.RESULT_OK) {
                // Get the result from the returned Intent
                Log.i("Returned to EditPlan", "check");

            } else {
                // AnotherActivity was not successful. No data to retrieve.
                Log.i("Failed", resultCode + "");
            }
        }

    }
}
