package com.para;

import java.io.IOException;
import java.util.HashMap;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    public static HashMap<Integer, Movie> movieMap = new HashMap<>();

    public static HashMap<Integer, Movie> getMovieMap() {
        return movieMap;
    }

    @Override
    public void start(Stage stage) throws IOException {
        Scene scene = new Scene(loadFXML("movies").load());
        stage.setTitle("Booking System");
        stage.setScene(scene);
        stage.show();
    }

    public static FXMLLoader loadFXML(String fxml) throws IOException {
        return new FXMLLoader(App.class.getResource(fxml + ".fxml"));
    }

    public static void main(String[] args) {
        DatabaseConnection.connect();
        movieMap = DatabaseConnection.FetchMovies();
        launch();
    }

}