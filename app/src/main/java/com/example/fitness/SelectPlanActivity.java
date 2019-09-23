package com.example.fitness;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.io.File;
import java.util.ArrayList;

public class SelectPlanActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {
    MyRecyclerViewAdapter adapter;
    ArrayList<String> planNames;
    RecyclerView planList;
    Utility util = new Utility();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_plan);
        planNames = new ArrayList<String>();
        updatePlanNames();
        planList = findViewById(R.id.planList);
        adapter = new MyRecyclerViewAdapter(this, planNames);
        setUpRecyclerView();
        adapter.setClickListener(this);
    }

    public void updatePlanNames() {
        File planDir = getApplicationContext().getDir("Plans", Context.MODE_PRIVATE);
        String names[] = planDir.list();
        planNames.clear();
        for(int i = 0; i < names.length; i++) {
            planNames.add(util.underscoreToSpace(names[i]));
        }

    }

    private void setUpRecyclerView() {
        planList.setAdapter(adapter);
        planList.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(adapter, getApplicationContext()));
        itemTouchHelper.attachToRecyclerView(planList);
    }

    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, SelectDayActivity.class);
        intent.putExtra("Plan name", planNames.get(position));
        startActivity(intent);
    }
}
