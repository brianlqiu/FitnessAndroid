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

public class EditPlanActivity extends AppCompatActivity implements MyRecyclerViewAdapter.ItemClickListener {
    public static final String EXTRA_MESSAGE = "com.example.fitness.MESSAGE";
    static final int ADD_DAY_REQUEST_CODE = 0;
    private WorkoutPlan plan;
    TextView editPlanName;
    Utility util = new Utility();
    MyRecyclerViewAdapter adapter;
    ArrayList<String> dayNames;
    RecyclerView dayList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_plan);
        dayNames = new ArrayList<String>();
        Intent intent = getIntent();
        dayList = findViewById(R.id.dayList);
        adapter = new MyRecyclerViewAdapter(this, dayNames);
        editPlanName = findViewById(R.id.editplanname);
        if(intent.hasExtra("Plan name")) {
            plan = new WorkoutPlan(intent.getStringExtra("Plan name"), getApplicationContext());
            updateDayNames();
            setUpRecyclerView();
            editPlanName.setText(plan.getPlanName());
        } else {
            plan = new WorkoutPlan();
        }

        adapter.setClickListener(this);
        editPlanName.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE) {
                    String name = util.spaceToUnderscore(editPlanName.getText().toString());
                    if(setPlanName(name)) {
                        setUpRecyclerView();
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
        editDay(position);
    }

    public void updateDayNames() {

        dayNames.clear();
        for(int i = 0; i < plan.size(); i++) {
            dayNames.add(plan.getDays().get(i).getDayName());
        }
        adapter.notifyDataSetChanged();
    }

    public boolean setPlanName(String s) {
        Log.i("Changing name", "Calling");
        if(plan.validPlanName(s, this.getApplicationContext())) {
            plan.changeName(s, this.getApplicationContext());
            return true;
        }
        return false;
    }

    public void addDay(View view) {
        plan.loadPlan(plan.getPlanName(), getApplicationContext());
        Intent intent = new Intent(this, AddOrEditDayActivity.class);
        intent.putExtra("Plan path", plan.getPath());
        intent.putExtra("Day index", plan.size());
        Log.i("Day index", plan.size() + "");
        startActivityForResult(intent, ADD_DAY_REQUEST_CODE);

    }

    public void editDay(int index) {
        Intent intent = new Intent(this, AddOrEditDayActivity.class);
        intent.putExtra("Day path", plan.getDays().get(index).getPath());
        intent.putExtra("Day index", index);
        startActivityForResult(intent, ADD_DAY_REQUEST_CODE);


    }

    private void setUpRecyclerView() {
        Log.i("Setting up recycler", "check");
        dayList.setAdapter(adapter);
        dayList.setLayoutManager(new LinearLayoutManager(this));
        Log.i("ItemTouchHelper setup", plan.getPlanName() + "x");
        ItemTouchHelper itemTouchHelper = new
                ItemTouchHelper(new SwipeToDeleteCallback(adapter, plan.getPlanName(), getApplicationContext()));
        itemTouchHelper.attachToRecyclerView(dayList);
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // First we need to check if the requestCode matches the one we used.
        if(requestCode == ADD_DAY_REQUEST_CODE) {
            Log.i("Returned", "check");
            // The resultCode is set by the AnotherActivity
            // By convention RESULT_OK means that what ever
            // AnotherActivity did was successful
            if(resultCode == Activity.RESULT_OK) {
                // Get the result from the returned Intent
                Log.i("Returned to EditPlan", "check");
                plan.loadPlan(plan.getPlanName(), getApplicationContext());
                updateDayNames();

            } else {
                // AnotherActivity was not successful. No data to retrieve.
                Log.i("Failed", resultCode + "");
            }
        }

    }

    public void done(View view) {
        setResult(Activity.RESULT_OK);
        finish();
    }





}
