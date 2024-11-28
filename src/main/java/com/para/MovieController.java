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

public class MovieController {
  int movieId;

  @FXML
  private VBox TimeSlotsController;

  public void setMovieId(int movieId) {
    this.movieId = movieId;
    onMovieIdSet();
  }

  private void onMovieIdSet() {
    System.out.println("Movie ID set: " + this.movieId);
    HashSet<Movie> set = App.getMovieSet();
    for (Movie movie : set) {
      if (movie.getId() != this.movieId) {
        continue;
      }
      for (String timeSlot : movie.getTimeslots()) {
        Button movieButton = new Button(timeSlot);
        movieButton.setPrefWidth(200);
        movieButton.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));
        movieButton.setId("" + this.movieId);

        movieButton.setOnAction(event -> {
          try {
            onMovieClicked(event);
          } catch (IOException e) {
            e.printStackTrace();
          }
        });

        TimeSlotsController.getChildren().add(movieButton);
      }
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
