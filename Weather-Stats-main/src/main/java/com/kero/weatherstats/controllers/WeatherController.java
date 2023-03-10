package com.kero.weatherstats.controllers;

import com.kero.weatherstats.dao.StationDao;
import com.kero.weatherstats.dao.StationDaoImpl;
import com.kero.weatherstats.dao.WeatherDataDao;
import com.kero.weatherstats.dao.WeatherDataDaoImpl;
import com.kero.weatherstats.model.*;
import io.github.palexdev.materialfx.controls.MFXComboBox;
import io.github.palexdev.materialfx.controls.MFXDatePicker;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.Axis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.time.LocalDate;
import java.util.ArrayList;

public class WeatherController {

    private Calculator calculator;
    private ArrayList<WeatherData> weatherData;
    private ArrayList<Station> stations;

    private final ObservableList<Station> stationObservableList = FXCollections.observableArrayList();
    private final ObservableList<String> typeObservableList = FXCollections.observableArrayList();

    @FXML private LineChart<String, Double> lineChart;

    @FXML private VBox weatherVbox;
    @FXML private VBox stationVbox;

    @FXML private MFXDatePicker startDate;
    @FXML private MFXDatePicker endDate;

    @FXML private MFXComboBox<Station> stationComboBox;
    @FXML private MFXComboBox<String> typeComboBox;

    public void initialize() {

        // Initialize DAOs
        WeatherDataDao weatherDataDao = new WeatherDataDaoImpl();
        StationDao stationDao = new StationDaoImpl();

        // Get data from database and store it in lists
        weatherData = weatherDataDao.getAllWeatherData();
        stations = stationDao.getAllStations();

        calculator = new Calculator(new AverageStrategy());

        // Set up combo boxes
        stationObservableList.addAll(stations);
        stationComboBox.setItems(stationObservableList);
        typeObservableList.addAll("Temperature", "Precipitation", "Wind", "Sunshine", "Cloud cover", "Cloud height");
        typeComboBox.setItems(typeObservableList);

        Platform.runLater(() -> { // This is needed to fix a bug where the combo boxes are not populated properly
            // Set default values
            stationComboBox.setValue(stationObservableList.get(0));
            typeComboBox.setValue(typeObservableList.get(0));
            startDate.setValue(LocalDate.of(2023, 1, 1));
            endDate.setValue(LocalDate.of(2023, 1, 31));

            stationComboBox.setOnAction(e -> updateChartData()); // Update chart data when station is changed
            typeComboBox.setOnAction(e -> updateChartData()); // Update chart data when type is changed

            startDate.setOnAction(e -> {
                if(startDate.getValue().isAfter(endDate.getValue())) {
                    endDate.setValue(startDate.getValue()); // Adjust end date if start date is after end date
                }
                updateChartData();
            });
            endDate.setOnAction(e -> {
                if(startDate.getValue().isAfter(endDate.getValue())) {
                    startDate.setValue(endDate.getValue()); // Adjust start date if end date is before start date
                }
                updateChartData();
            });

            updateChartData(); // Shows chart data from default values when the program is started
        });
    }

    // Responsible for updating and showing the data on the line chart and the labels on the right side
    private void updateChartData() {

        // Clear the labels from the vboxes
        weatherVbox.getChildren().clear();
        stationVbox.getChildren().clear();

        // Clear the series and axis from the line chart by creating new ones
        XYChart.Series<String, Double> series1 = new XYChart.Series<>();
        XYChart.Series<String, Double> series2 = new XYChart.Series<>();
        XYChart.Series<String, Double> series3 = new XYChart.Series<>();
        Axis<String> xAxis = lineChart.getXAxis();
        Axis<Double> yAxis = lineChart.getYAxis();

        // Get the weather data that is in the given time range
        ArrayList<WeatherData> dataRange = new ArrayList<>();
        for (WeatherData data : weatherData) {
            if(data.getStationID() == stationComboBox.getValue().getStationID()
                    && data.getData_Time().isAfter(startDate.getValue().atStartOfDay().minusHours(1))
                    && data.getData_Time().isBefore(endDate.getValue().atStartOfDay().plusDays(1))) { // If the data is from the selected station and is in the given time range
                dataRange.add(data);
            }
        }

        // If startDate and endDate difference is 0, then set the xAxis to show hours instead of days
        boolean isSameDay = startDate.getValue().getDayOfMonth() == endDate.getValue().getDayOfMonth(); // If the start and end date is the same day
        if(isSameDay) {
            xAxis.setLabel("Hour of the day");
        } else {
            xAxis.setLabel("Day of the month");
        }

        // Set the data for the stationVbox
        stationVbox.getChildren().add(new Label("Name: " + stationComboBox.getValue().getStation_name()));
        stationVbox.getChildren().add(new Label("Location: " + stationComboBox.getValue().getPosition()));
        stationVbox.getChildren().add(new Label("Height: " + stationComboBox.getValue().getHeight()));
        stationVbox.getChildren().add(new Label("Setup date: " + stationComboBox.getValue().getSetup_date()));

        // Depending on the type of data that is selected, set the data for the line chart and weatherVbox
        switch (typeComboBox.getValue()) {
            case "Temperature" -> {
                series1.setName("Minimum temperature");
                series2.setName("Average temperature");
                series3.setName("Maximum temperature");
                yAxis.setLabel("Celsius");

                // Set the data for the chart
                for (WeatherData data : dataRange) {
                    if(isSameDay) { // If the date range is the same day
                        series1.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getHour()), data.getMin_temp()));
                        series2.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getHour()), data.getAvg_temp()));
                        series3.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getHour()), data.getMax_temp()));
                    } else { // If the date range is more than one day
                        double minTemp = dataRange.get(0).getMin_temp();
                        double maxTemp = dataRange.get(0).getMax_temp();
                        double avgTemp = 0;
                        int count = 0;
                        for (WeatherData data2 : dataRange) {
                            if(data2.getData_Time().getDayOfMonth() == data.getData_Time().getDayOfMonth()) { // If the data is from the same day
                                minTemp = Math.min(minTemp, data2.getMin_temp());
                                maxTemp = Math.max(maxTemp, data2.getMax_temp());
                                avgTemp += data2.getAvg_temp(); // Sum all the average temperatures for the day
                                count++; // Count the number of data points for the day, used to calculate average later
                            }
                        }
                        series1.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getDayOfMonth()), minTemp)); // Add the minimum temperature for the day
                        series2.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getDayOfMonth()), avgTemp / count)); // Add the average temperature for the day
                        series3.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getDayOfMonth()), maxTemp)); // Add the maximum temperature for the day
                    }
                }
                lineChart.getData().setAll(series1, series2, series3);

                // Create temporary arraylists to store the data for the weatherVbox
                ArrayList<Double> tempData1 = new ArrayList<>();
                for (WeatherData data : dataRange) {
                    tempData1.add(data.getMin_temp()); // Add all the minimum temperatures to the arraylist
                }
                ArrayList<Double> tempData2 = new ArrayList<>();
                for (WeatherData data : dataRange) {
                    tempData2.add(data.getAvg_temp()); // Add all the average temperatures to the arraylist
                }
                ArrayList<Double> tempData3 = new ArrayList<>();
                for (WeatherData data : dataRange) {
                    tempData3.add(data.getMax_temp()); // Add all the maximum temperatures to the arraylist
                }

                // Set the data for the weatherVbox
                weatherVbox.getChildren().add(new Label("Minimum: " + String.format("%.2f C", calculator.calculate(tempData1, new MinimumStrategy())))); // Uses the strategy pattern to calculate the different values
                weatherVbox.getChildren().add(new Label("Average: " + String.format("%.2f C", calculator.calculate(tempData2, new AverageStrategy()))));
                weatherVbox.getChildren().add(new Label("Maximum: " + String.format("%.2f C", calculator.calculate(tempData3, new MaximumStrategy()))));
                weatherVbox.getChildren().add(new Label("Median: " + String.format("%.2f C", calculator.calculate(tempData2, new MedianStrategy()))));
                weatherVbox.getChildren().add(new Label("Average median: " + String.format("%.2f C", calculator.calculate(tempData2, new AverageMedianStrategy()))));
            }

            // The process is the same for the other data types, so no comments are added below this point
            case "Precipitation" -> {
                series1.setName("Precipitation");
                yAxis.setLabel("Minutes");

                for (WeatherData data : dataRange) {
                    if(isSameDay) {
                        series1.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getHour()), data.getPrecip()));
                    } else {
                        double precip = 0;
                        int count = 0;
                        for (WeatherData data2 : dataRange) {
                            if(data2.getData_Time().getDayOfMonth() == data.getData_Time().getDayOfMonth()) {
                                precip += data2.getPrecip();
                                count++;
                            }
                        }
                        series1.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getDayOfMonth()), precip / count));
                    }
                }
                lineChart.getData().setAll(series1);

                ArrayList<Double> tempData1 = new ArrayList<>();
                for (WeatherData data : dataRange) {
                    tempData1.add(data.getPrecip());
                }

                weatherVbox.getChildren().add(new Label("Minimum: " + String.format("%.2f minutes", calculator.calculate(tempData1, new MinimumStrategy()))));
                weatherVbox.getChildren().add(new Label("Average: " + String.format("%.2f minutes", calculator.calculate(tempData1, new AverageStrategy()))));
                weatherVbox.getChildren().add(new Label("Maximum: " + String.format("%.2f minutes", calculator.calculate(tempData1, new MaximumStrategy()))));
                weatherVbox.getChildren().add(new Label("Median: " + String.format("%.2f minutes", calculator.calculate(tempData1, new MedianStrategy()))));
                weatherVbox.getChildren().add(new Label("Average median: " + String.format("%.2f minutes", calculator.calculate(tempData1, new AverageMedianStrategy()))));
            }

            case "Wind" -> {
                series1.setName("Average wind speed");
                series2.setName("Maximum wind speed");
                yAxis.setLabel("Meters per second");

                for (WeatherData data : dataRange) {
                    if(isSameDay) {
                        series1.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getHour()), data.getAvg_windspeed()));
                        series2.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getHour()), data.getMax_windspeed()));
                    } else {
                        double avgWindSpeed = 0;
                        double maxWindSpeed = dataRange.get(0).getMax_windspeed();
                        int count = 0;
                        for (WeatherData data2 : dataRange) {
                            if(data2.getData_Time().getDayOfMonth() == data.getData_Time().getDayOfMonth()) {
                                avgWindSpeed += data2.getAvg_windspeed();
                                maxWindSpeed = Math.max(maxWindSpeed, data2.getMax_windspeed());
                                count++;
                            }
                        }
                        series1.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getDayOfMonth()), avgWindSpeed / count));
                        series2.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getDayOfMonth()), maxWindSpeed));
                    }
                }
                lineChart.getData().setAll(series1, series2);

                ArrayList<Double> tempData1 = new ArrayList<>();
                for (WeatherData data : dataRange) {
                    tempData1.add(data.getAvg_windspeed());
                }
                ArrayList<Double> tempData2 = new ArrayList<>();
                for (WeatherData data : dataRange) {
                    tempData2.add(data.getMax_windspeed());
                }

                weatherVbox.getChildren().add(new Label("Minimum: " + String.format("%.2f m/s", calculator.calculate(tempData1, new MinimumStrategy()))));
                weatherVbox.getChildren().add(new Label("Average: " + String.format("%.2f m/s", calculator.calculate(tempData1, new AverageStrategy()))));
                weatherVbox.getChildren().add(new Label("Maximum: " + String.format("%.2f m/s", calculator.calculate(tempData2, new MaximumStrategy()))));
                weatherVbox.getChildren().add(new Label("Median: " + String.format("%.2f m/s", calculator.calculate(tempData1, new MedianStrategy()))));
                weatherVbox.getChildren().add(new Label("Average median: " + String.format("%.2f m/s", calculator.calculate(tempData1, new AverageMedianStrategy()))));
            }

            case "Sunshine" -> {
                series1.setName("Sunshine");
                yAxis.setLabel("Strength");

                for (WeatherData data : dataRange) {
                    if(isSameDay) {
                        series1.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getHour()), data.getSunshine()));
                    } else {
                        double sunshine = 0;
                        int count = 0;
                        for (WeatherData data2 : dataRange) {
                            if(data2.getData_Time().getDayOfMonth() == data.getData_Time().getDayOfMonth()) {
                                sunshine += data2.getSunshine();
                                count++;
                            }
                        }
                        series1.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getDayOfMonth()), sunshine / count));
                    }
                }
                lineChart.getData().setAll(series1);

                ArrayList<Double> tempData1 = new ArrayList<>();
                for (WeatherData data : dataRange) {
                    tempData1.add(data.getSunshine());
                }

                weatherVbox.getChildren().add(new Label("Minimum: " + String.format("%.2f", calculator.calculate(tempData1, new MinimumStrategy()))));
                weatherVbox.getChildren().add(new Label("Average: " + String.format("%.2f", calculator.calculate(tempData1, new AverageStrategy()))));
                weatherVbox.getChildren().add(new Label("Maximum: " + String.format("%.2f", calculator.calculate(tempData1, new MaximumStrategy()))));
                weatherVbox.getChildren().add(new Label("Median: " + String.format("%.2f", calculator.calculate(tempData1, new MedianStrategy()))));
                weatherVbox.getChildren().add(new Label("Average median: " + String.format("%.2f", calculator.calculate(tempData1, new AverageMedianStrategy()))));
            }

            case "Cloud cover" -> {
                series1.setName("Cloud cover");
                yAxis.setLabel("Percentage");

                for (WeatherData data : dataRange) {
                    if(isSameDay) {
                        series1.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getHour()), (double) data.getCloud_cover()));
                    } else {
                        double cloudCover = 0;
                        int count = 0;
                        for (WeatherData data2 : dataRange) {
                            if(data2.getData_Time().getDayOfMonth() == data.getData_Time().getDayOfMonth()) {
                                cloudCover += data2.getCloud_cover();
                                count++;
                            }
                        }
                        series1.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getDayOfMonth()), cloudCover / count));
                    }
                }
                lineChart.getData().setAll(series1);

                ArrayList<Double> tempData1 = new ArrayList<>();
                for (WeatherData data : dataRange) {
                    tempData1.add((double) data.getCloud_cover());
                }
                weatherVbox.getChildren().add(new Label("Minimum: " + String.format("%.2f%%", calculator.calculate(tempData1, new MinimumStrategy()))));
                weatherVbox.getChildren().add(new Label("Average: " + String.format("%.2f%%", calculator.calculate(tempData1, new AverageStrategy()))));
                weatherVbox.getChildren().add(new Label("Maximum: " + String.format("%.2f%%", calculator.calculate(tempData1, new MaximumStrategy()))));
                weatherVbox.getChildren().add(new Label("Median: " + String.format("%.2f%%", calculator.calculate(tempData1, new MedianStrategy()))));
                weatherVbox.getChildren().add(new Label("Average median: " + String.format("%.2f%%", calculator.calculate(tempData1, new AverageMedianStrategy()))));
            }

            case "Cloud height" -> {
                series1.setName("Cloud height");
                yAxis.setLabel("Meters");

                for (WeatherData data : dataRange) {
                    if(isSameDay) {
                        series1.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getHour()), (double) data.getCloud_height()));
                    } else {
                        double cloudHeight = 0;
                        int count = 0;
                        for (WeatherData data2 : dataRange) {
                            if(data2.getData_Time().getDayOfMonth() == data.getData_Time().getDayOfMonth()) {
                                cloudHeight += data2.getCloud_height();
                                count++;
                            }
                        }
                        series1.getData().add(new XYChart.Data<>(String.valueOf(data.getData_Time().getDayOfMonth()), cloudHeight / count));
                    }
                }
                lineChart.getData().setAll(series1);

                ArrayList<Double> tempData1 = new ArrayList<>();
                for (WeatherData data : dataRange) {
                    tempData1.add((double) data.getCloud_height());
                }

                weatherVbox.getChildren().add(new Label("Minimum: " + String.format("%.2f m", calculator.calculate(tempData1, new MinimumStrategy()))));
                weatherVbox.getChildren().add(new Label("Average: " + String.format("%.2f m", calculator.calculate(tempData1, new AverageStrategy()))));
                weatherVbox.getChildren().add(new Label("Maximum: " + String.format("%.2f m", calculator.calculate(tempData1, new MaximumStrategy()))));
                weatherVbox.getChildren().add(new Label("Median: " + String.format("%.2f m", calculator.calculate(tempData1, new MedianStrategy()))));
                weatherVbox.getChildren().add(new Label("Average median: " + String.format("%.2f m", calculator.calculate(tempData1, new AverageMedianStrategy()))));
            }
        }
    }
}
