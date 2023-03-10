package com.kero.weatherstats.dao;

import com.kero.weatherstats.services.TextParser;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Objects;

public class WeatherDataImport {

    private final DatabaseConnector databaseConnector;

    public WeatherDataImport() {
        databaseConnector = new DatabaseConnector();
    }

    public void importToStation(ArrayList<ArrayList<String>> data) {
        try (Connection connection = databaseConnector.getConnection()) {
            connection.setAutoCommit(false);
            String sql = "INSERT INTO Station (stationID, station_name, position, height, setup_date) VALUES (?,?,?,?,?);";
            PreparedStatement pstmt = connection.prepareStatement(sql);

            // Create a LinkedHashSet to store and filter out the unique rows
            LinkedHashSet<ArrayList<String>> set = new LinkedHashSet<>(data);

            for(ArrayList<String> row : set) {
                pstmt.setInt(1, Integer.parseInt(row.get(1)));
                pstmt.setString(2, row.get(0));
                pstmt.setString(3, row.get(2).replace("\u0019", "")); // Remove the weird character
                pstmt.setDouble(4, Double.parseDouble(row.get(3).replace("\"", "").replace(",", "."))); // Remove the quotes and replace the comma with a dot
                pstmt.setString(5, row.get(4));
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void importToWeatherData(ArrayList<ArrayList<String>> data) {
        try (Connection connection = databaseConnector.getConnection()) {
            connection.setAutoCommit(false);
            String sql = "INSERT INTO WeatherData (stationID, data_time, precip, precip_minutes, avg_temp, max_temp, min_temp, sunshine, avg_windspeed, max_windspeed, cloud_height, cloud_cover) VALUES (?,?,?,?,?,?,?,?,?,?,?,?);";
            PreparedStatement pstmt = connection.prepareStatement(sql);

            for(ArrayList<String> row : data) {
                pstmt.setInt(1, Integer.parseInt(row.get(1)));
                pstmt.setTimestamp(2, Timestamp.valueOf(row.get(0)));
                pstmt.setDouble(3, (row.get(2).isEmpty()) ? 0 : Double.parseDouble(row.get(2)));
                pstmt.setDouble(4, (row.get(3).isEmpty()) ? 0 : Double.parseDouble(row.get(3)));
                pstmt.setDouble(5, (row.get(4).isEmpty()) ? 0 : Double.parseDouble(row.get(4)));
                pstmt.setDouble(6, (row.get(5).isEmpty()) ? 0 : Double.parseDouble(row.get(5)));
                pstmt.setDouble(7, (row.get(6).isEmpty()) ? 0 : Double.parseDouble(row.get(6)));
                pstmt.setDouble(8, (row.get(7).isEmpty()) ? 0 : Double.parseDouble(row.get(7)));
                pstmt.setDouble(9, (row.get(8).isEmpty()) ? 0 : Double.parseDouble(row.get(8)));
                pstmt.setDouble(10, (row.get(9).isEmpty()) ? 0 : Double.parseDouble(row.get(9)));
                pstmt.setInt(11, (row.get(10).isEmpty()) ? 0 : Integer.parseInt(row.get(10)));
                pstmt.setInt(12, (row.get(11).isEmpty()) ? 0 : Integer.parseInt(row.get(11)));

                pstmt.addBatch();
            }
            pstmt.executeBatch();
            connection.commit();
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        WeatherDataImport weatherImport = new WeatherDataImport();
        weatherImport.importToStation(TextParser.parseFile(Objects.requireNonNull(WeatherDataImport.class.getResource("/com/kero/weatherstats/Station.txt")).getPath()));
        weatherImport.importToWeatherData(TextParser.parseFile(Objects.requireNonNull(WeatherDataImport.class.getResource("/com/kero/weatherstats/WeatherData.txt")).getPath()));
    }
}
