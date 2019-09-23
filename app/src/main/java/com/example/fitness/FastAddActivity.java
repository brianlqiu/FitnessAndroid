package com.example.fitness;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

public class FastAddActivity extends AppCompatActivity{

    EditText setEditText;
    EditText repEditText;
    EditText restEditText;
    Switch AMRAPSwitch;
    Button doneButton;
    Intent data;

    boolean validReps;
    boolean validRest;
    boolean validSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fast_add);
        setEditText = findViewById(R.id.setEditText);
        repEditText = findViewById(R.id.repEditText);
        restEditText = findViewById(R.id.restEditText);
        AMRAPSwitch = findViewById(R.id.AMRAPSwitch);
        doneButton = findViewById(R.id.addDayButton);
        data = new Intent();
        validReps = false;
        validRest = false;
        validSet = false;
        data.putExtra("AMRAP", false);

        AMRAPSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                data.removeExtra("AMRAP");
                data.putExtra("AMRAP", b);
            }
        });
        doneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("Done?" ,"done");
                done();
            }
        });
    }

    public boolean setSets() {
        int sets = Integer.parseInt(setEditText.getText().toString());
        if(sets > 0) {
            data.putExtra("Sets", sets);
            return true;
        } else {
            return false;
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

    public void done() {
        data.putExtra("AMRAP", AMRAPSwitch.isChecked());
        if(setSets() && setReps() && setRest()) {
            setResult(Activity.RESULT_OK, data);
            finish();
        } else {
            Toast.makeText(this, "Invalid sets, reps, or rest", Toast.LENGTH_SHORT);
        }
    }
}
