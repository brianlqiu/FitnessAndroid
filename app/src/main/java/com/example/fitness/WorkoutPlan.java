package com.example.fitness;

import android.content.Context;
import android.content.ContextWrapper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

public class WorkoutPlan {
    ArrayList<WorkoutDay> days;
    String planName;
    String path;
    Utility util = new Utility();

    public WorkoutPlan() {
        days = new ArrayList<WorkoutDay>();
        planName = "";
        path = "";
    }

    public WorkoutPlan(String pName, Context context) {
        days = new ArrayList<WorkoutDay>();
        loadPlan(pName, context);
    }

    public void loadPlan(String pName, Context context) {
        days.clear();
        planName = pName;
        File f = new File("/data/user/0/com.example.fitness/app_Plans/" + util.spaceToUnderscore(planName));
        path = f.getPath();
        Log.i("Plan path", f.getPath());
        File[] dayFiles = f.listFiles();
        BufferedReader reader = null;
        for(int i = 0; i < dayFiles.length; i++) {
            try {
                Log.i("File", dayFiles[i].getPath());
                reader = new BufferedReader(new FileReader(dayFiles[i]));
                String line = reader.readLine();
                int ind = Integer.parseInt(line);
                String convert = util.underscoreToSpace(dayFiles[i].getName().substring(0, dayFiles[i].getName().indexOf(".txt")));
                WorkoutDay d = new WorkoutDay(path, convert, ind);
                days.add(d);
            } catch (Exception e) {
                Log.e("File error","File could not be opened");
                e.printStackTrace();
            }
        }
        try {
            reader.close();
        } catch (Exception e) {
            Log.e("File error", "File could not be closed");
        }

        sortDays();
    }

    public void changeName(String pName, Context context) {
        File myDir = new File(context.getApplicationContext().getDir("Plans", Context.MODE_PRIVATE).getPath(), pName);
        if(path == "") {
            Log.d("Intial", "Initialization of the plan name");
            Log.d("File", myDir.getAbsolutePath());
            try {
                myDir.mkdir();
                Log.d("Create File", "Directory created");
                path = myDir.getPath();
                planName = util.underscoreToSpace(pName);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Create File", "Couldn't create file");
            }
        } else {
            File f = new File(path);
            Log.i("Current path", f.getPath());
            Log.i("New path", myDir.getPath());
            boolean renamed = f.renameTo(myDir);
            path = f.getPath();
            Log.d("Renamed", Boolean.toString(renamed));
            Log.d("File path", myDir.getAbsolutePath());
            planName = util.underscoreToSpace(pName);
        }
    }

    public boolean validPlanName(String pName, Context context) {
        File myDir = context.getApplicationContext().getDir("Plans", Context.MODE_PRIVATE);
        String[] planNames = myDir.list();
        Log.i("Check", pName);
        for(int i = 0; i < planNames.length; i++) {
            if(pName.equals(planNames[i])) {
                Log.i("Invalid name", "name exists");
                return false;
            }
        }
        return true;
    }

    public void addDay(int index, WorkoutDay d) {
        for(int i = index + 1; i < days.size(); i++) {
            days.get(i).cDayIndex(i + 1);
        }
        days.add(index, d);
    }

    public void removeDay(int index) {
        File f = new File(days.get(index).getPath());
        f.delete();
        days.remove(index);
        for(int i = index; i < days.size(); i++) {
            days.get(i).cDayIndex(i);
        }
        Log.i("Days size", days.size() + "");
        update();

    }

    public void swapDays(int ind1, int ind2) {
        Log.i("Swapping", days.get(ind1).getDayName() + ", " + days.get(ind2).getDayName());
        WorkoutDay tmp = days.get(ind1);
        days.set(ind1, days.get(ind2));
        days.set(ind2, tmp);
    }

    public void clear() {
        for(int i = 0; i < days.size(); i++) {
            removeDay(i);
        }
    }

    public void update() {
        for(int i = 0; i < days.size(); i++) {
            days.get(i).updateTextFile(false);
        }
    }

    public String getPath() {
        return path;
    }

    public int size() {
        return days.size();
    }

    public String getPlanName() {
        return planName;
    }

    public ArrayList<WorkoutDay> getDays() {
        return days;
    }

    public void sortDays() {
        for(int i = 0; i < days.size(); i++) {
            if(days.get(i).getDayIndex() != i) {
                for(int j = i; j < days.size(); j++) {
                    if(days.get(j).getDayIndex() == i) {
                        swapDays(i, j);
                    }
                }
            }
        }
    }

}
