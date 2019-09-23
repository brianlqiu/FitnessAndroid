package com.example.fitness;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class EditSetActivity extends AppCompatActivity {

    EditText repEditText;
    EditText restEditText;
    Switch AMRAPSwitch;
    Intent data;
    Button doneButton;

    boolean validReps;
    boolean validRest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_set);
        repEditText = findViewById(R.id.repEditText);
        restEditText = findViewById(R.id.restEditText);
        AMRAPSwitch = findViewById(R.id.AMRAPSwitch);
        doneButton = findViewById(R.id.addDayButton);
        data = new Intent();
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                done();
            }
        });
        if(getIntent().hasExtra("Reps")) {
            repEditText.setText(getIntent().getIntExtra("Reps", 0) + "");
            restEditText.setText(getIntent().getIntExtra("Rest", 0) + "");
            AMRAPSwitch.setChecked(getIntent().getBooleanExtra("AMRAP", false));
            data.putExtra("Set index", getIntent().getIntExtra("Set index", 0));
        }
    }

    public boolean setReps() {
        int reps = Integer.parseInt(repEditText.getText().toString());
        if(reps > 0) {
            data.putExtra("Reps", reps);
            return true;
        } else {
            return false;
        }
    }

    public boolean setRest() {
        int rest = Integer.parseInt(restEditText.getText().toString());
        if(rest > 0) {
            data.putExtra("Rest", rest);
            return true;
        } else {
            return false;
        }
    }

    public void setAMRAP() {
        data.putExtra("AMRAP", AMRAPSwitch.isChecked());
    }

    public void done() {
        setAMRAP();
        if(setReps() && setRest()) {
            setResult(Activity.RESULT_OK, data);
            finish();
        } else {
            Toast.makeText(this, "Invalid reps or rest", Toast.LENGTH_SHORT);
        }
    }
}
