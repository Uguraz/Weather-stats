package com.kero.weatherstats.dao;

import com.kero.weatherstats.model.WeatherData;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class WeatherDataDaoImpl implements WeatherDataDao{

    //This connects us to the database
    private final DatabaseConnector databaseConnector;

    public WeatherDataDaoImpl() {
        databaseConnector = new DatabaseConnector();
    }

    //This gets us the list of WeatherData from our database, with all the columns
    @Override
    public ArrayList<WeatherData> getAllWeatherData() {
        ArrayList<WeatherData> weatherdata = new ArrayList<>();
        try (Connection con = databaseConnector.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM WeatherData;");
            ResultSet rs = ps.executeQuery();

            WeatherData weatherData;
            while (rs.next()) {
                int weatherID = rs.getInt(1);
                int stationID = rs.getInt(2);
                LocalDateTime data_time = rs.getTimestamp(3).toLocalDateTime();
                double precip = rs.getDouble(4);
                double precip_minutes = rs.getDouble(5);
                double avg_temp = rs.getDouble(6);
                double max_temp = rs.getDouble(7);
                double min_temp = rs.getDouble(8);
                double sunshine = rs.getDouble(9);
                double avg_windspeed = rs.getDouble(10);
                double max_windspeed = rs.getDouble(11);
                int cloud_height = rs.getInt(12);
                int cloud_cover = rs.getInt(13);


                weatherData = new WeatherData(weatherID, stationID, data_time, precip, precip_minutes, avg_temp, max_temp, min_temp, sunshine, avg_windspeed, max_windspeed, cloud_height, cloud_cover);
                weatherdata.add(weatherData);
            }

        } catch (SQLException e) {
            System.err.println("cannot access records (WeatherDataDaoImpl)");
        }
        return weatherdata;
    }
}

