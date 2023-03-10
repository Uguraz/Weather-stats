module com.kero.weatherstats {
    requires javafx.controls;
    requires javafx.fxml;
    requires com.microsoft.sqlserver.jdbc;
    requires java.sql;
    requires java.naming;
    requires MaterialFX;

    exports com.kero.weatherstats.dao;
    opens com.kero.weatherstats.dao to javafx.fxml;
    exports com.kero.weatherstats.controllers;
    opens com.kero.weatherstats.controllers to javafx.fxml;
    exports com.kero.weatherstats.services;
    opens com.kero.weatherstats.services to javafx.fxml;
}