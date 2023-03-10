package com.kero.weatherstats.model;

import java.util.ArrayList;
public class MaximumStrategy implements Strategy {
    @Override
    public double calculate(ArrayList<Double> list) {
        // Check if list is empty
        if (list.isEmpty()) {
            return 0;
        }
        // Find the maximum element in the list
        double max = list.get(0);
        for (double num : list) {
            if (num > max) {
                max = num;
            }
        }
        return max;
    }
}