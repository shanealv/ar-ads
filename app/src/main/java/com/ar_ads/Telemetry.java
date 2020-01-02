package com.ar_ads;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

import android.util.Log;

public class Telemetry {
    public final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy-hh-mm-ss");
    public final Path directory;

    public Telemetry(String directory) {
        super();
        this.directory = Paths.get(directory, "telemetry");
        createDirectory();
        try {
            Log.i("telemetry", "saving telemetry to " + this.directory.toRealPath().toString());
        } catch (IOException e) {
            Log.e("telemetry", "could not create telemetry directory", e);
        }
    }

    public void logFaceTracking(String tag) {
        Path filepath = directory.resolve("facecounts.txt");

        String datetime = dateFormat.format(new Date());
        String message = tag.replaceAll(" ", "_").toLowerCase();

        createDirectory();
        try (FileWriter fw = new FileWriter(filepath.toFile(), true);
             BufferedWriter bw = new BufferedWriter(fw);
             PrintWriter out = new PrintWriter(bw)) {
            out.println(datetime + " " + message);
            Log.v("telemetry", "updated face counts");
        } catch (IOException e) {
            Log.e("telemetry", "could not update face counts", e);
        }
    }

    private void createDirectory() {
        if (Files.exists(directory))
            return;

        try {
            Files.createDirectories(directory);
        } catch (IOException e) {
            Log.e("telemetry", "could not create telemetry directory", e);
        }

    }
}