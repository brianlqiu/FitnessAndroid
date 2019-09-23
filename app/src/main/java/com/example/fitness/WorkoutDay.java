package com.example.fitness;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;

import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;

public class WorkoutDay implements Parcelable {
    private ArrayList<Exercise> exercises;
    private String path;
    private String dayName;
    private String planName;
    int dayIndex;
    Utility util = new Utility();

    public WorkoutDay() {
        path = "";
        exercises = new ArrayList<Exercise>();
        dayName = "";
        dayIndex = 0;
    }

    public WorkoutDay(String calendarPath, String dayName, String planName) {
        path = calendarPath;
        exercises = new ArrayList<Exercise>();
        this.dayName = dayName;
        dayIndex = -1;
        this.planName = planName;
        File f = new File(path);
        try {
            f.createNewFile();
            updateTextFile(true);

        } catch (Exception e) {

        }
    }



    public WorkoutDay(String dayPath, int index) {
        exercises = new ArrayList<Exercise>();
        File f = new File(dayPath);
        dayName = f.getName();
        dayName = dayName.substring(0, dayName.indexOf(".txt"));
        dayIndex = index;
        path = dayPath;

        try {
            if (!f.createNewFile()) {
                updateArray();
            }
        } catch (Exception e) {
            System.out.println("File could not be created");
        }
    }

    public WorkoutDay(String planPath, String sDayName, int index) {
        exercises = new ArrayList<Exercise>();
        dayName = sDayName;
        dayIndex = index;
        File f = new File(planPath, util.spaceToUnderscore(dayName) + ".txt");
        path = f.getPath();
        try {
            if (!f.createNewFile()) {
                updateArray();
            }
        } catch (Exception e) {
            System.out.println("File could not be created");
        }
    }

    public void addExercise(int index, Exercise e) {
        exercises.add(index, e);
    }

    public void removeExercise(int index) {
        Log.i("Exercise removed", exercises.get(index).toString());
        exercises.remove(index);
    }

    public void swapExercises(int ind1, int ind2) {
        Exercise tmp = exercises.get(ind1);
        exercises.set(ind1, exercises.get(ind2));
        exercises.set(ind2, tmp);
    }

    public void cDayIndex(int index) {
        dayIndex = index;
    }

    public void clear() {
        exercises.clear();
    }

    public int size() {
        return exercises.size();
    }

    public ArrayList<Exercise> getExercises() {
        return exercises;
    }

    public int getDayIndex() {
        return dayIndex;
    }

    public String getPath() {
        return path;
    }

    public void updateArray() {
        try {
            Log.i("Update Array Path", path);
            exercises.clear();
            BufferedReader reader = new BufferedReader(new FileReader(path));

            String line = "";
            reader.readLine();

            while (!line.equals(null)) {
                line = reader.readLine();
                Log.i("Exercise name", line);
                Exercise e = new Exercise(line);
                int sIndex = 0;

                while (true) {
                    line = reader.readLine();
                    if (line.contains("/NE/")) {
                        break;
                    }
                    int nReps = Integer.parseInt(line.substring(0, line.indexOf(' ')));
                    line = line.substring(line.indexOf(' ') + 1);
                    int rTime = Integer.parseInt(line.substring(0, line.indexOf(' ')));
                    line = line.substring(line.indexOf(' ') + 1);
                    boolean yAMRAP = Boolean.parseBoolean(line);
                    e.addSet(sIndex, -1, nReps, rTime, yAMRAP);
                    sIndex++;
                }
                exercises.add(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateTextFile(boolean calendar) { //true if it is calendar
        Log.d("Update Text Path", path);
        try {
            String info;
            if(calendar) {
                info = planName + "-"  + dayName + "\n";
            } else {
                info = dayIndex + "\n";
            }
            for(int i = 0; i < exercises.size(); i++) {
                info += exercises.get(i).getExerciseName() + "\n";
                for(int j = 0; j < exercises.get(i).size(); j++) {
                    info += exercises.get(i).getReps(j)
                            + " " + exercises.get(i).getRest(j) + " " +
                            Boolean.toString(exercises.get(i).getAMRAP(j)) + "\n";
                }
                info += "/NE/\n";
            }
            Log.i("Info", info);
            FileOutputStream fs = new FileOutputStream(path);
            fs.write(info.getBytes());
            fs.close();
        } catch(Exception e) {
            System.out.println("File doesn't exist");
        }
    }

    public void changeDayName(String dName, String pPath) {
        File myTxt = new File(pPath, dName + ".txt");
        if(path == "") {
            Log.d("Intial", "Initialization of the plan name");
            Log.d("File", myTxt.getAbsolutePath());
            try {
                myTxt.createNewFile();
                Log.d("Create File", "Directory created");
                path = myTxt.getPath();
                dayName = util.underscoreToSpace(dName);
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Create File", "Couldn't create file");
            }
        } else {
            File f = new File(path);
            Log.i("Current path", f.getPath());
            Log.i("New path", myTxt.getPath());
            boolean renamed = f.renameTo(myTxt);
            path = f.getPath();
            Log.d("Renamed", Boolean.toString(renamed));
            Log.d("File path", myTxt.getAbsolutePath());
            dayName = util.underscoreToSpace(dName);
        }
    }

    public boolean validDayName(String dName, String path) {
        File myDir = new File(path);
        String[] dayNames = myDir.list();
        Log.i("Check", dName);
        String convert = dName + ".txt";
        for(int i = 0; i < dayNames.length; i++) {
            if(convert.equals(dayNames[i])) {
                Log.i("Invalid name", "name exists");
                return false;
            }
        }
        return true;
    }

    public String getDayName() {
        return dayName;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeList(exercises);
        out.writeString(path);
        out.writeString(dayName);
        out.writeString(planName);
        out.writeInt(dayIndex);
    }

    public static final Parcelable.Creator<WorkoutDay> CREATOR = new Parcelable.Creator<WorkoutDay>() {
        public WorkoutDay createFromParcel(Parcel in) {
            return new WorkoutDay(in);
        }

        public WorkoutDay[] newArray(int size) {
            return new WorkoutDay[size];
        }
    };

    private WorkoutDay(Parcel in) {
        in.readList(exercises, null);
        path = in.readString();
        dayName = in.readString();
        planName = in.readString();
        dayIndex = in.readInt();
    }

}
