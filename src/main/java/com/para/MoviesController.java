package com.para;

import java.io.IOException;
import java.util.HashSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MoviesController {

  @FXML
  private VBox MoviesContainer;

  @FXML
  public void initialize() {
    HashSet<Movie> set = App.getMovieSet();
    for (Movie movie : set) {
      Button movieButton = new Button(movie.toString());
      movieButton.setPrefWidth(200);
      movieButton.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));
      movieButton.setId("" + movie.getId());

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
    Stage newStage = new Stage();
    FXMLLoader fxmlLoader = App.loadFXML("movie");
    Parent loader = fxmlLoader.load();

    MovieController controller = fxmlLoader.getController();
    Button button = (Button) (event.getSource());
    int id = Integer.parseInt(button.getId());
    controller.setMovieId(id);

    Scene scene = new Scene(loader);
    newStage.setScene(scene);
    newStage.initModality(Modality.WINDOW_MODAL);
    newStage.show();
  }
}
