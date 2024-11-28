package com.para;

import java.io.IOException;
import java.util.HashMap;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class App extends Application {
    public static HashMap<Integer, Movie> movieMap = new HashMap<>();
    private static Scene scene;

    public static HashMap<Integer, Movie> getMovieMap() {
        return movieMap;
    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("movies").load());
        stage.initModality(Modality.WINDOW_MODAL);
        stage.setTitle("Booking System");
        stage.setScene(scene);
        stage.show();

        Stage timerStage = new Stage();
        Scene timerScene = new Scene(loadFXML("timer").load());
        timerStage.initModality(Modality.WINDOW_MODAL);
        timerStage.setTitle("Timer");
        timerStage.setScene(timerScene);
        timerStage.show();

    }

    static FXMLLoader setRoot(String fxml) throws IOException {
        FXMLLoader fXMLLoader = loadFXML(fxml);
        scene.setRoot(fXMLLoader.load());
        return fXMLLoader;
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