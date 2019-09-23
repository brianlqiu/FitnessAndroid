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

import java.util.ArrayList;

public class AddOrEditDayActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener{
    MyRecyclerViewAdapter adapter;
    ArrayList<String> exerciseInfo;
    WorkoutDay workoutDay;
    EditText dayNameEditText;
    Utility util = new Utility();
    final static int RETURN_REQUEST_CODE = 0;
    RecyclerView exerciseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_or_edit_day);
        Log.i("Entered", "AddOrEditDayActivity");
        dayNameEditText = findViewById(R.id.dayNameEditText);
        if(getIntent().hasExtra("Day path")) {
            workoutDay = new WorkoutDay(getIntent().getStringExtra("Day path"), getIntent().getIntExtra("Day index", 0));
            dayNameEditText.setText(workoutDay.getDayName());
        } else {
            workoutDay = new WorkoutDay();
            workoutDay.cDayIndex(getIntent().getIntExtra("Day index",0 ));
        }
        exerciseInfo = new ArrayList<String>();
        updateExerciseInfo();
        exerciseList = findViewById(R.id.exerciseList);
        adapter = new MyRecyclerViewAdapter(this, exerciseInfo);
        if(workoutDay.size() > 0) {
            setUpRecyclerView();
            adapter.setClickListener(this);
        }

        dayNameEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE) {
                    String name = util.spaceToUnderscore(dayNameEditText.getText().toString());
                    if(setDayName(name)) {
                        InputMethodManager imm = (InputMethodManager) textView.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(textView.getWindowToken(), 0);
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Plan Already Exists", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 0);
                        toast.show();
                    }
                    return true;
                }
                return false;
            }
        });

    }

    public void onItemClick(View view, int position) {
        editExercise(position);
    }

    public void updateExerciseInfo() {
        exerciseInfo.clear();
        ArrayList<Exercise> exercises = workoutDay.getExercises();
        for(int i = 0; i < exercises.size(); i++) {
            Log.i("Exercise " + i, exercises.get(i).toString());
            exerciseInfo.add(exercises.get(i).toString());
        }
    }

    public void addExercise(View view) {
        workoutDay.updateArray();
        Intent intent = new Intent(this, AddOrEditExerciseActivity.class);
        intent.putExtra("Day path", workoutDay.getPath());
        Log.i("Exercise index", workoutDay.size() + "");
        intent.putExtra("Exercise index", workoutDay.size());
        intent.putExtra("Day index", workoutDay.getDayIndex());
        startActivityForResult(intent, RETURN_REQUEST_CODE);
    }

    public void editExercise(int index) {
        workoutDay.updateArray();
        Intent intent = new Intent(this, AddOrEditExerciseActivity.class);
        intent.putExtra("Day path", workoutDay.getPath());
        intent.putExtra("Exercise index", index);
        intent.putExtra("Day index", workoutDay.getDayIndex());
        startActivityForResult(intent, RETURN_REQUEST_CODE);
    }

    public boolean setDayName(String s) {
        if(workoutDay.validDayName(s, getIntent().getStringExtra("Plan path"))) {
            workoutDay.changeDayName(s, getIntent().getStringExtra("Plan path"));
            return true;
        }
        return false;

    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // First we need to check if the requestCode matches the one we used.
        if(requestCode == RETURN_REQUEST_CODE) {

            // The resultCode is set by the AnotherActivity
            // By convention RESULT_OK means that what ever
            // AnotherActivity did was successful
            if(resultCode == Activity.RESULT_OK) {
                // Get the result from the returned Intent
                workoutDay.updateArray();
                setUpRecyclerView();
                updateExerciseInfo();
                adapter.notifyDataSetChanged();

            } else {
                // AnotherActivity was not successful. No data to retrieve.
            }
        }

    }

    public void done(View view) {
        workoutDay.updateTextFile(false);
        setResult(Activity.RESULT_OK);
        finish();
    }

    private void setUpRecyclerView() {
        exerciseList.setAdapter(adapter);
        exerciseList.setLayoutManager(new LinearLayoutManager(this));
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(adapter, workoutDay));
        itemTouchHelper.attachToRecyclerView(exerciseList);
    }
}
