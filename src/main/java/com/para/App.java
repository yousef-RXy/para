package com.para;

import java.io.IOException;
import java.util.HashMap;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application {
    private static HashMap<Integer, Movie> movieMap = new HashMap<>();
    private static SnacksStore snacksStore;
    private static Scene scene;
    private static ModeEnum threadingMode;

    public static HashMap<Integer, Movie> getMovieMap() {
        return movieMap;
    }

    public static SnacksStore getSnacksStore() {
        return snacksStore;
    }

    public static ModeEnum getThreadingMode() {
        return threadingMode;
    }

    @Override
    public void start(Stage stage) throws IOException {
        scene = new Scene(loadFXML("movies").load());
        stage.setTitle("Booking System");
        stage.setScene(scene);
        stage.show();

        Stage timerStage = new Stage();
        Scene timerScene = new Scene(loadFXML("timer").load());
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
        threadingMode = ModeEnum.PARALLEL;
        DatabaseConnection.connect();
        movieMap = DatabaseConnection.FetchMovies();
        if (threadingMode.equals(ModeEnum.PARALLEL)) {
            snacksStore = new SnacksStore(3, 2, 0, 0, 30, 30);
            snacksStore.start();
        }
        launch();
        if (threadingMode.equals(ModeEnum.PARALLEL)) {
            snacksStore.stop();
        }
    }
}