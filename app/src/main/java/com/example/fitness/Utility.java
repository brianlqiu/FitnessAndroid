package com.example.fitness;

import android.app.Activity;
import android.view.inputmethod.InputMethodManager;

import java.io.File;

public class Utility {

    public String spaceToUnderscore(String s) {
        String convert = "";
        for(int i = 0; i < s.length(); i++) {
            if(s.charAt(i) == ' ') {
                convert += "_";
            } else {
                convert += s.charAt(i);
            }
        }
        return convert;
    }

    public String underscoreToSpace(String s) {
        String convert = "";
        for(int i = 0; i < s.length(); i++) {
            if(s.charAt(i) == '_') {
                convert += " ";
            } else {
                convert += s.charAt(i);
            }
        }
        return convert;
    }

    public boolean rename(File from, File to) {
        return from.getParentFile().exists() && from.exists() && from.renameTo(to);
    }

    public static void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void deleteRecursive(File fileOrDirectory) {
        if (fileOrDirectory.isDirectory())
            for (File child : fileOrDirectory.listFiles())
            {
                child.delete();
                deleteRecursive(child);
            }

        fileOrDirectory.delete();
    }
}
