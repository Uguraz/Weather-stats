package com.kero.weatherstats.model;

import java.util.ArrayList;
import java.util.Collections;
// Commit drillede lidt så Silas skulle ordne det for mig, ellers så har jeg lavet det (Adman).
public class AverageMedianStrategy implements Strategy {
    @Override
    public double calculate(ArrayList<Double> list) {
        // Check if list is empty
        if (list.isEmpty()) {
            return 0;
        }
        // Sort the list in ascending order
        Collections.sort(list);
        int size = list.size();
        // Find the indices of the three middlemost numbers
        int index1 = size / 2 - 1; // the first number in the middle
        int index2 = size / 2; // The second number in the middle
        int index3 = size / 2 + 1; // The third number in the middle
        // Return the average of the three numbers
        return (list.get(index1) + list.get(index2) + list.get(index3)) / 3.0;
    }
}
