package com.kero.weatherstats.dao;

import com.kero.weatherstats.model.Station;

import java.util.ArrayList;
import java.util.List;

public interface StationDao {

    //Reads all stations from the database
    public ArrayList<Station> getAllStations();
}
