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
import android.widget.TextView;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class WorkoutActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener{
    MyRecyclerViewAdapter adapter;
    ArrayList<String> exerciseInfo;
    WorkoutDay workoutDay;
    WorkoutDay progress;
    RecyclerView exerciseList;
    final static int RETURN_REQUEST_CODE = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_workout);
        TextView planDayNameTextView = findViewById(R.id.planDayNameTextView);
        workoutDay = new WorkoutDay(getIntent().getStringExtra("Day path"), getIntent().getIntExtra("Day index", 0));
        planDayNameTextView.setText(getIntent().getStringExtra("Plan name") + " - " + workoutDay.getDayName());
        exerciseInfo = new ArrayList<String>();
        updateExerciseInfo();
        exerciseList = findViewById(R.id.exerciseList);
        adapter = new MyRecyclerViewAdapter(this, exerciseInfo);
        setUpRecyclerView();
        adapter.setClickListener(this);
        Calendar c = Calendar.getInstance();
        String cName = c.get(Calendar.MONTH) + "-" + c.get(Calendar.DAY_OF_MONTH) + "-" + c.get(Calendar.YEAR) + ".txt";
        File log = new File(getDir("WorkoutData", Context.MODE_PRIVATE).getPath(), cName);
        progress = new WorkoutDay(log.getPath(), workoutDay.getDayName(), getIntent().getStringExtra("Plan name"));




    }

    public void updateExerciseInfo() {
        exerciseInfo.clear();
        ArrayList<Exercise> exercises = workoutDay.getExercises();
        ArrayList<Exercise> doneExercises = progress.getExercises();
        for(int i = 0; i < exercises.size(); i++) {
            String info = exercises.get(i).getExerciseName() + "\n";
            for(int j = 0; j < exercises.get(i).size(); j++) {
                boolean dataExists = i < doneExercises.size() && j < doneExercises.get(i).size();
                info += "Set " + (j + 1);
                if(dataExists) {
                    info += " (" + doneExercises.get(i).getWeight(j) + " lbs)";
                }
                info += ": ";
                if(dataExists) {
                    info += doneExercises.get(i).getReps(j) + "/" + exercises.get(i).getReps(j);
                } else {
                    info += "0/" + exercises.get(i).getReps(j);
                }
                if(exercises.get(i).getAMRAP(j)) {
                    info += "+";
                }
                info += "\n";
            }
            exerciseInfo.add(info);
        }

    }

    private void setUpRecyclerView() {
        exerciseList.setAdapter(adapter);
        exerciseList.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(adapter, workoutDay));
        itemTouchHelper.attachToRecyclerView(exerciseList);
    }

    public void onItemClick(View view, int position) {
        Intent intent = new Intent(this, ExerciseActivity.class);
        intent.putExtra("workoutDay", workoutDay);
        intent.putExtra("Exercise index", position);
        startActivityForResult(intent, RETURN_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // First we need to check if the requestCode matches the one we used.
        if(requestCode == RETURN_REQUEST_CODE) {
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
