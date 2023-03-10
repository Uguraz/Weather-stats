package com.kero.weatherstats.model;

import java.util.ArrayList;
public class MinimumStrategy implements Strategy {
    @Override
    public double calculate(ArrayList<Double> list) {
        // Check if list is empty
        if (list.isEmpty()) {
            return 0;
        }
        // Find the minimum element in the list
        double min = list.get(0);
        for (double num : list) {
            if (num < min) {
                min = num;
            }
        }
        return min;
    }
}