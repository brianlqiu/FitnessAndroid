package com.example.fitness;

import java.io.File;
import java.util.ArrayList;
import java.util.Set;

public class Exercise {
    private String exerciseName;
    private String fileName;
    private ArrayList<Set> sets;
    private Utility util = new Utility();

    public Exercise(String name) {
        sets = new ArrayList<Set>();
        exerciseName = name;
        fileName = util.spaceToUnderscore(name) + ".txt";
        /*
        try {
            File f = new File(fileName);
            f.createNewFile();
        } catch(Exception e) {
            System.out.println(fileName + " could not be created");
        }*/
    }

    public void addSet(int index, double sWeight, int nReps, int rTime, boolean yAMRAP) {
        sets.add(index, new Set(sWeight, nReps, rTime, yAMRAP));
    }

    public void removeSet(int index) {
        sets.remove(index);
    }

    public void clear() {
        sets.clear();
    }

    public void cAllReps(int nReps, boolean yAMRAP) {
        for(int i = 0; i < sets.size(); i++) {
            cRep(i, nReps, yAMRAP);
        }
    }

    public void cAllRest(int rTime) {
        for(int i = 0; i < sets.size(); i++) {
            cRest(i, rTime);
        }
    }

    public void cRep(int index, int nReps, boolean yAMRAP) {
        sets.get(index).reps = nReps;
        sets.get(index).AMRAP = yAMRAP;
    }

    public void cRest(int index, int rTime) {
        sets.get(index).rest = rTime;
    }

    public void cName(String name) {
        exerciseName = name;
    }

    public void fastAdd(int nSets, double sWeight, int nReps, int rTime, boolean yAMRAP) {
        for(int i = 0; i < nSets; i++) {
            addSet(i, sWeight, nReps, rTime, yAMRAP);
        }
    }

    public int size() {
        return sets.size();
    }

    public String getExerciseName() {
        return exerciseName;
    }

    public double getWeight(int index) {
        return sets.get(index).weight;
    }

    public int getReps(int index) {
        return sets.get(index).reps;
    }

    public int getRest(int index) {
        return sets.get(index).rest;
    }

    public boolean getAMRAP(int index) {
        return sets.get(index).AMRAP;
    }

    public String toString() {
        String info = "";
        info += exerciseName + "\n";
        for(int i = 0; i < sets.size(); i++) {
            info += "Set " + (i + 1) + ": " + sets.get(i).toString();
        }
        return info;
    }

    public void swapSets(int ind1, int ind2) {
        Set tmp = sets.get(ind1);
        sets.set(ind1, sets.get(ind2));
        sets.set(ind2, tmp);
    }

    public ArrayList<Set> getSets() {
        return sets;
    }

    public class Set {
        public double weight;
        public int reps;
        public int rest;
        public boolean AMRAP;

        Set(double sWeight, int nReps, int rTime, boolean yAMRAP) {
            weight = sWeight;
            reps = nReps;
            rest = rTime;
            AMRAP = yAMRAP;
        }

        public String toString() {
            String info = reps + "";
            if(AMRAP) {
                info += "+";
            }
            info += " reps | (" + rest + "s rest)\n";
            return info;
        }
    }
}
