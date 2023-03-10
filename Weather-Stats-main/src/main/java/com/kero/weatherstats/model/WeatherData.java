package com.kero.weatherstats.model;

import java.time.LocalDateTime;
import java.util.Date;

public class WeatherData {
    //Attributes for WeatherData
    private final int weatherID;
    private final int stationID;
    private final LocalDateTime data_Time;
    private final double precip;
    private final double precip_minutes;
    private final double avg_temp;
    private final double max_temp;
    private final double min_temp;
    private final double sunshine;
    private final double avg_windspeed;
    private final double max_windspeed;
    private final int cloud_height;
    private final int cloud_cover;

    //Construction
    public WeatherData(int weatherID, int stationID, LocalDateTime data_Time, double precip, double precip_minutes, double avg_temp, double max_temp, double min_temp, double sunshine, double avg_windspeed, double max_windspeed, int cloud_height, int cloud_cover) {
        this.weatherID = weatherID;
        this.stationID = stationID;
        this.data_Time = data_Time;
        this.precip = precip;
        this.precip_minutes = precip_minutes;
        this.avg_temp = avg_temp;
        this.max_temp = max_temp;
        this.min_temp = min_temp;
        this.sunshine = sunshine;
        this.avg_windspeed = avg_windspeed;
        this.max_windspeed = max_windspeed;
        this.cloud_height = cloud_height;
        this.cloud_cover = cloud_cover;
    }

    //This gets the Weather ID
    public int getWeatherID() {
        return weatherID;
    }

    //This is the station ID from our station objects so we make a connection for the weather data
    public int getStationID() {
        return stationID;
    }

    //This gets us the date and time when the data was collected
    public LocalDateTime getData_Time() {
        return data_Time;
    }

    //This gets us the precipitation in amount it rained in a given period
    public double getPrecip() {
        return precip;
    }

    //This gets us the precipitation in minutes in a period
    public double getPrecip_minutes() {
        return precip_minutes;
    }

    //This shows the average temperature for a given day or period
    public double getAvg_temp() {
        return avg_temp;
    }

    //This shows us the maximum temperature for a given day or period
    public double getMax_temp() {
        return max_temp;
    }

    //This shows us the minimum temperature for a given day or period
    public double getMin_temp() {
        return min_temp;
    }

    //This gets us the amount of sunshine that was on a given day or for a period
    public double getSunshine() {
        return sunshine;
    }

    //This gets us the average windspeed
    public double getAvg_windspeed() {
        return avg_windspeed;
    }

    //This gets us the max windspeed
    public double getMax_windspeed() {
        return max_windspeed;
    }

    //This shows us how heigh the clouds are up in the sky
    public int getCloud_height() {
        return cloud_height;
    }

    //This shows us the coverage for clouds
    public int getCloud_cover() {
        return cloud_cover;
    }
}
