package com.para;

import java.io.IOException;
import java.util.HashSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class MoviesController extends Controller {

  @FXML
  private VBox MoviesContainer;

  @FXML
  public void initialize() {
    HashSet<Movie> set = App.getMovieSet();
    for (Movie movie : set) {
      Button movieButton = utils.addToVBox(movie.toString(), movie.getId());
      movieButton.setOnAction(event -> {
        try {
          onMovieClicked(event);
        } catch (IOException e) {
          e.printStackTrace();
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
