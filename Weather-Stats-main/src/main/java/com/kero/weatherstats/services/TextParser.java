package com.kero.weatherstats.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class TextParser {

    // A method that takes a file name as input and returns an ArrayList of ArrayLists containing the data
    // The file must be tab-delimited
    public static ArrayList<ArrayList<String>> parseFile(String fileName) {

        ArrayList<ArrayList<String>> data = new ArrayList<>();

        try {
            Scanner sc = new Scanner(new File(fileName));

            // Read the file line by line
            while (sc.hasNextLine()) {
                String line = sc.nextLine(); // Read the line
                String[] tokens = line.split("\t", -1); // -1 to keep empty strings

                ArrayList<String> row = new ArrayList<>(Arrays.asList(tokens)); // Convert the array to an ArrayList
                data.add(row); // Add the row to the data
            }
            sc.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        return data;
    }
}
