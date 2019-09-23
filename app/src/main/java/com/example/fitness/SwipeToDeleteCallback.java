package com.example.fitness;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
    private MyRecyclerViewAdapter mAdapter;
    private WorkoutDay day;
    private WorkoutPlan plan;
    private int exerciseIndex;
    private int code;
    Utility util = new Utility();
    Context context;
    private final static int REMOVE_SET = 0;
    private final static int REMOVE_EXERCISE = 1;
    private final static int REMOVE_DAY = 2;
    private final static int REMOVE_PLAN = 3;

    public SwipeToDeleteCallback(MyRecyclerViewAdapter adapter, WorkoutDay d, int exerciseInd) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mAdapter = adapter;
        day = new WorkoutDay(d.getPath(), d.getDayIndex());
        day.updateArray();
        exerciseIndex = exerciseInd;
        code = REMOVE_SET;
    }

    public SwipeToDeleteCallback(MyRecyclerViewAdapter adapter, WorkoutDay d) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mAdapter = adapter;
        day = new WorkoutDay(d.getPath(), d.getDayIndex());
        day.updateArray();
        code = REMOVE_EXERCISE;
    }

    public SwipeToDeleteCallback(MyRecyclerViewAdapter adapter, String p, Context context) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.context = context;
        mAdapter = adapter;
        plan = new WorkoutPlan(p, context);
        code = REMOVE_DAY;
    }

    public SwipeToDeleteCallback(MyRecyclerViewAdapter adapter, Context context) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        mAdapter = adapter;
        this.context = context;
        code = REMOVE_PLAN;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        int position = viewHolder.getAdapterPosition();
        if(code == REMOVE_SET) {
            Log.i("Day check", day.getPath());
            day.updateArray();
            mAdapter.deleteItem(position);
            day.getExercises().get(exerciseIndex).removeSet(position);
            day.updateTextFile(false);
        }
        if(code == REMOVE_EXERCISE) {
            day.updateArray();
            mAdapter.deleteItem(position);
            day.removeExercise(position);
            day.updateTextFile(false);
        }
        if(code == REMOVE_DAY) {
            plan.loadPlan(plan.getPlanName(), context);
            mAdapter.deleteItem(position);
            plan.removeDay(position);
        }
        if(code == REMOVE_PLAN) {
            String plan = util.spaceToUnderscore(mAdapter.getItem(position));
            File f = new File(context.getDir("Plans", Context.MODE_PRIVATE), plan);
            util.deleteRecursive(f);
            Log.i("Deletion check", f.exists() + "");
            mAdapter.deleteItem(position);
        }

    }
}
