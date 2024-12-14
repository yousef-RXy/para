package com.para;

import java.io.IOException;
import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MoviesController extends Controller {

  @FXML
  private VBox MoviesContainer;

  @FXML
  public void initialize() {
    HashMap<Integer, Movie> movieMap = App.getMovieMap();
    for (Movie movie : movieMap.values()) {
      Button movieButton = utils.addButtonToVBox(movie.toString(), movie.getId());
      movieButton.setOnAction(event -> {
        try {
          onMovieClicked(event);
        } catch (IOException e) {
          Thread.currentThread().interrupt();
        }
      });

      MoviesContainer.getChildren().add(movieButton);
    }
  }

  @FXML
  void onMovieClicked(ActionEvent event) throws IOException {
    Stage newStage = new Stage();
    utils.onButtonToDisplayClicked(newStage, "movie", event);
  }
}
