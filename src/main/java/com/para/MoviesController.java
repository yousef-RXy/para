package com.para;

import java.io.IOException;
import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

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
          Thread.interrupted();
        }
      });

      MoviesContainer.getChildren().add(movieButton);
    }
  }

  @FXML
  void onMovieClicked(ActionEvent event) throws IOException {
    utils.onButtonToDisplayClicked("movie", event);
    // Stage newStage = new Stage();
    // FXMLLoader fxmlLoader = App.loadFXML("movie");
    // Parent loader = fxmlLoader.load();

    // MovieController controller = fxmlLoader.getController();
    // Button button = (Button) (event.getSource());
    // int id = Integer.parseInt(button.getId());
    // controller.setId(id);

    // Scene scene = new Scene(loader);
    // newStage.setScene(scene);
    // newStage.initModality(Modality.WINDOW_MODAL);
    // newStage.show();
  }
}
