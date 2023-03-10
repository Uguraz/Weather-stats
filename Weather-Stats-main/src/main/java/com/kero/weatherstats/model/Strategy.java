package com.kero.weatherstats.model;

import java.util.ArrayList;
// Commit drillede lidt så Silas skulle ordne det for mig, ellers så har jeg lavet det (Adman).
public interface Strategy {
    double calculate(ArrayList<Double>list);
}
