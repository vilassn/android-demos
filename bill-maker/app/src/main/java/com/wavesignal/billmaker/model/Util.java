package com.wavesignal.billmaker.model;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.wavesignal.billmaker.view.AppMainActivity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Type;

public class Util {

    public static final String TAG = "BillMaker";

    public static String getBackupDir() {
        return AppMainActivity.getContext().getExternalFilesDir("bills").getAbsolutePath();
    }

    private static Gson getGsonObj() {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        return gsonBuilder.create();
    }

    private static Object getObjFromString(String str, Type type) {
        try {
            return getGsonObj().fromJson(str, type);
        } catch (Exception e) {
            // e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    private static String getStringFromObj(Object obj) {
        try {
            return getGsonObj().toJson(obj);
        } catch (Exception e) {
            // e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    public static Object readObjFromFile(File file, Type type) {
        try {
            String str = readStringFromFile(file);
            return getObjFromString(str, type);
        } catch (Exception e) {
            // e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    public static void writeObjToFile(File file, Object obj) {
        try {
            String str = getStringFromObj(obj);
            writeStringToFile(file, str);
        } catch (Exception e) {
            // e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        }
    }

    private static String readStringFromFile(File file) {
        try {
            String line;
            StringBuilder sb = new StringBuilder();
            BufferedReader reader = new BufferedReader(new FileReader(file));
            while ((line = reader.readLine()) != null)
                sb.append(line).append(System.lineSeparator());
            reader.close();
            return sb.toString();
        } catch (Exception e) {
            // e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        }
        return null;
    }

    private static void writeStringToFile(File file, String str) {
        try {
            file.getParentFile().mkdirs();
            BufferedWriter writer = new BufferedWriter(new FileWriter(file));
            writer.write(str);
            writer.close();
        } catch (Exception e) {
            // e.printStackTrace();
            Log.e(TAG, e.getMessage(), e);
        }
    }
}
