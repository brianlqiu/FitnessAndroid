package com.example.fitness;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {
    static final int EDIT_PLAN_REQUEST_CODE = 0;
    static final int START_WORKOUT_REQUEST_CODE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void selectCreateOrEditPlan(View view) {
        Intent intent = new Intent(this, CreateOrEditPlanActivity.class);
        startActivityForResult(intent, EDIT_PLAN_REQUEST_CODE);
    }

    public void startWorkout(View view) {
        Intent intent = new Intent(this, SelectPlanActivity.class);
        startActivityForResult(intent, START_WORKOUT_REQUEST_CODE);
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // First we need to check if the requestCode matches the one we used.
        if(requestCode == EDIT_PLAN_REQUEST_CODE) {
            Log.i("Returned", "check");
            // The resultCode is set by the AnotherActivity
            // By convention RESULT_OK means that what ever
            // AnotherActivity did was successful
            if(resultCode == Activity.RESULT_OK) {
                // Get the result from the returned Intent
            } else {
                // AnotherActivity was not successful. No data to retrieve.
                Log.i("Failed", resultCode + "");
            }
        }
        if(requestCode == START_WORKOUT_REQUEST_CODE) {
            Log.i("Returned", "check");
            // The resultCode is set by the AnotherActivity
            // By convention RESULT_OK means that what ever
            // AnotherActivity did was successful
            if(resultCode == Activity.RESULT_OK) {
                // Get the result from the returned Intent
            } else {
                // AnotherActivity was not successful. No data to retrieve.
                Log.i("Failed", resultCode + "");
            }
        }

    }

}
