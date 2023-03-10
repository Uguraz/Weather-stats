package com.kero.weatherstats.dao;

import com.kero.weatherstats.model.Station;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;



public class StationDaoImpl implements StationDao{

    //This connects us to the database
    private final DatabaseConnector databaseConnector;

    public StationDaoImpl() {
        databaseConnector = new DatabaseConnector();
    }

    //This gets us the list of stations from our database, with all the columns
    @Override
    public ArrayList<Station> getAllStations() {
        ArrayList<Station> stations = new ArrayList<>();
        try (Connection con = databaseConnector.getConnection()) {
            PreparedStatement ps = con.prepareStatement("SELECT * FROM Station;");
            ResultSet rs = ps.executeQuery();

            Station station;
            while (rs.next()) {
                int stationID = rs.getInt(1);
                String station_name = rs.getString(2);
                String position = rs.getString(3);
                double height = rs.getDouble(4);
                String setup_date = rs.getString(5);


                station = new Station(stationID, station_name, position, height, setup_date);
                stations.add(station);
            }

        } catch (SQLException e) {
            System.err.println("cannot access records (StationDaoImpl)");

        }
        return stations;
    }
}
