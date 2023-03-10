package com.kero.weatherstats.model;

public class Station {
    //Attributes for our station
    private final int stationID;
    private final String station_name;
    private final String position;
    private final double height;
    private final String setup_date;

    //Constructor
    public Station(int stationID, String station_name, String position, double height, String setup_date){
        this.stationID = stationID;
        this.station_name = station_name;
        this.position = position;
        this.height = height;
        this.setup_date = setup_date;
    }

    //This gets the Station ID for our objects
    public int getStationID() {
        return stationID;
    }

    //This gets the Name of the given station
    public String getStation_name() {
        return station_name;
    }

    //This gets the position of our station in coordinates
    public String getPosition() {
        return position;
    }

    //This gets the height above sea level for the station
    public double getHeight() {
        return height;
    }

    //This gets us the information where the station was build
    public String getSetup_date() {
        return setup_date;
    }

    //This does so we only show the station name
    @Override
    public String toString() {
        return getStation_name();
    }
}
