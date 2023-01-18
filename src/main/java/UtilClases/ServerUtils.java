package UtilClases;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ServerUtils {
    public ServerUtils() {
    }

    public String gson(List<PageEntry> pageEntry) {
        GsonBuilder builder = new GsonBuilder();
        Gson gson = builder.setPrettyPrinting().create();
        return gson.toJson(pageEntry);
    }

    public List<String> txtDeserialize(File textFile) {
        List<String> stopList = new ArrayList<>();
        try (BufferedReader in = new BufferedReader(new FileReader(textFile))) {
            String read;
            while ((read = in.readLine()) != null) {
                String[] splitArray = read.split("\n");
                for (String splitWord : splitArray) {
                    Collections.addAll(stopList, splitWord);
                }
            }
        } catch (IOException ex) {
            throw new RuntimeException(ex);
        }
        return stopList;
    }
}
