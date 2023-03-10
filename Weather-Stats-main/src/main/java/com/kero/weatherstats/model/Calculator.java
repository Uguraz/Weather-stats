package com.kero.weatherstats.model;

import java.util.ArrayList;
// Commit drillede lidt så Silas skulle ordne det for mig, ellers så har jeg lavet det (Adman).
public class Calculator {
    // The strategy to use
    private Strategy strategy;

    // The constructor that takes a strategy as an argument
    public Calculator(Strategy strategy) {
        this.strategy = strategy;
    }

    // The method that sets a new strategy
    public void setStrategy(Strategy strategy) {
        this.strategy = strategy;
    }

    // The method that performs the calculation using the current strategy
    public double calculate(ArrayList<Double> list, Strategy strategy) {
        this.strategy = strategy;
        return this.strategy.calculate(list);
    }
}
