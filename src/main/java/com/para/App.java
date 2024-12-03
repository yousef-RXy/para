package com.para;

import java.io.IOException;
import java.util.HashSet;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    // TODO change hash set to hashmap
    public static HashSet<Movie> set = new HashSet<>();

    public static HashSet<Movie> getMovieSet() {
        return set;
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
        set = DatabaseConnection.FetchMovies();
        launch();
    }

}