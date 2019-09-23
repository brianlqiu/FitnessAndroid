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
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

public class AddOrEditExerciseActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener{
    MyRecyclerViewAdapter adapter;
    ArrayList<String> setInfo;
    WorkoutDay workoutDay;
    int exerciseIndex;
    int dayIndex;
    EditText exerciseNameEditText;
    Utility util = new Utility();
    RecyclerView setList;

    boolean validName;

    private final static int ADD_SET_REQUEST_CODE = 0;
    private final static int FAST_ADD_REQUEST_CODE = 1;
    private final static int EDIT_SET_REQUEST_CODE = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_exercise);
        exerciseNameEditText = findViewById(R.id.exerciseNameEditText);
        exerciseIndex = getIntent().getIntExtra("Exercise index", 0);
        dayIndex = getIntent().getIntExtra("Day index", 0);
        workoutDay = new WorkoutDay(getIntent().getStringExtra("Day path"),
                dayIndex);
        setInfo = new ArrayList<String>();
        updateSetInfo();
        if(exerciseIndex < workoutDay.size()) {
            exerciseNameEditText.setText(workoutDay.getExercises().get(exerciseIndex).getExerciseName());
        }
        validName = false;
        setList = findViewById(R.id.setList);
        adapter = new MyRecyclerViewAdapter(this, setInfo);

        setUpRecyclerView();
        adapter.setClickListener(this);

        exerciseNameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE) {
                    String name = util.spaceToUnderscore(exerciseNameEditText.getText().toString());
                    setExerciseName();
                    InputMethodManager imm = (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });


    }

    public boolean setExerciseName() {
        String name = exerciseNameEditText.getText().toString();
        if(workoutDay.size() == exerciseIndex) {

            workoutDay.getExercises().add(new Exercise(name));
            Log.i("Exercise added, size", workoutDay.size() + "");
        } else {
            workoutDay.getExercises().get(exerciseIndex).cName(name);
        }
        return true;
    }

    public void onItemClick(View view, int position) {
        editSet(position);
    }

    public void updateSetInfo() {
        if(workoutDay.getExercises().size() > getIntent().getIntExtra("Exercise index", 0)) {
            ArrayList<Exercise.Set> s = workoutDay.getExercises().get(getIntent().getIntExtra("Exercise index", 0)).getSets();
            setInfo.clear();
            for (int i = 0; i < s.size(); i++) {
                String info = "Set " + (i + 1) + ": " + s.get(i).toString();
                setInfo.add(info);
            }
        }

    }
    

    public void editSet(int index) {
        Exercise.Set set = workoutDay.getExercises().get(getIntent().getIntExtra("Exercise index", 0)).getSets().get(index);
        Intent intent = new Intent(this, EditSetActivity.class);
        intent.putExtra("Set index", index);
        intent.putExtra("Reps", set.reps);
        intent.putExtra("Rest", set.rest);
        intent.putExtra("AMRAP", set.AMRAP);
        startActivityForResult(intent, EDIT_SET_REQUEST_CODE);

    }

    public void addSet(View view) {
        Intent intent = new Intent(this, EditSetActivity.class);
        startActivityForResult(intent, ADD_SET_REQUEST_CODE);
    }

    public void fastAdd(View view) {
        Intent intent = new Intent(this, FastAddActivity.class);
        startActivityForResult(intent, FAST_ADD_REQUEST_CODE);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // First we need to check if the requestCode matches the one we used.
        if(requestCode == ADD_SET_REQUEST_CODE) {

            // The resultCode is set by the AnotherActivity
            // By convention RESULT_OK means that what ever
            // AnotherActivity did was successful
            if(resultCode == Activity.RESULT_OK) {
                // Get the result from the returned Intent
                workoutDay.updateArray();
                workoutDay.getExercises().get(exerciseIndex).addSet(workoutDay.getExercises().get(exerciseIndex).size(), -1,
                        data.getIntExtra("Reps", 0), data.getIntExtra("Rest",0 ),
                        data.getBooleanExtra("AMRAP", false));
                workoutDay.updateTextFile(false);
                updateSetInfo();
                adapter.notifyDataSetChanged();


            } else {
                // AnotherActivity was not successful. No data to retrieve.
            }
        }
        if(requestCode == FAST_ADD_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                Log.i("Entered", "check");
                Log.i("Sets", data.getIntExtra("Sets", 0) + "");
                Log.i("Reps", data.getIntExtra("Reps", 0) + "");
                Log.i("Rest", data.getIntExtra("Rest", 0) + "");

                workoutDay.getExercises().get(exerciseIndex).fastAdd(data.getIntExtra("Sets", 0),
                -1, data.getIntExtra("Reps", 0), data.getIntExtra("Rest", 0),
                data.getBooleanExtra("AMRAP", false));
                workoutDay.updateTextFile(false);
                updateSetInfo();
                adapter.notifyDataSetChanged();
            }
        }
        if(requestCode == EDIT_SET_REQUEST_CODE) {
            if(resultCode == Activity.RESULT_OK) {
                workoutDay.getExercises().get(exerciseIndex).cRep(data.getIntExtra("Set index", 0),
                        data.getIntExtra("Reps", 0), data.getBooleanExtra("AMRAP", false));
                workoutDay.getExercises().get(exerciseIndex).cRest(data.getIntExtra("Set index", 0),
                        data.getIntExtra("Rest", 0));
                updateSetInfo();
                adapter.notifyDataSetChanged();
            }
        }
    }

    public void done(View view) {
        workoutDay.updateTextFile(false);
        setResult(Activity.RESULT_OK);
        finish();

    }

    private void setUpRecyclerView() {
        setList.setAdapter(adapter);
        setList.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(adapter, workoutDay, exerciseIndex));
        itemTouchHelper.attachToRecyclerView(setList);
    }


}
