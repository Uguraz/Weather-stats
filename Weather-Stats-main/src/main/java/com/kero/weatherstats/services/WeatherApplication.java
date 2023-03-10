package com.kero.weatherstats.services;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

public class WeatherApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/kero/weatherstats/WeatherStats.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setMinHeight(700);
        stage.setMinWidth(880);
        stage.setTitle("DMI Weather Statistics");
        stage.getIcons().add(new Image("file:src/main/resources/com/kero/weatherstats/weather_icon.png"));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}