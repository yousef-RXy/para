package com.para;

import java.io.IOException;
import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class MovieController extends Controller {
  int movieId;

  @FXML
  private VBox TimeSlotsContainer;

  @Override
  public void setId(int movieId) {
    this.movieId = movieId;
    onMovieIdSet();
  }

  private void onMovieIdSet() {
    HashMap<Integer, Movie> movieMap = App.getMovieMap();
    Movie movie = movieMap.get(this.movieId);
    HashMap<Integer, TimeSlot> timeSlotMap = movie.getTimeSlotsMap();

    for (TimeSlot timeSlot : timeSlotMap.values()) {
      if (!DatabaseConnection.isTimeSlotsAvailable(timeSlot.getId(), 50)) {
        continue;
      }
      Button timeSlotsButton = utils.addButtonToVBox(timeSlot.toString(), timeSlot.getId());
      timeSlotsButton.setOnAction(event -> {
        try {
          onTimeSlotClicked(event);
        } catch (IOException e) {
          Thread.currentThread().interrupt();
        }
      });

      TimeSlotsContainer.getChildren().add(timeSlotsButton);
    }
  }

  @FXML
  void onTimeSlotClicked(ActionEvent event) throws IOException {
    Stage newStage = new Stage();
    TimeSlotController controller = (TimeSlotController) utils.onButtonToDisplayClicked(newStage, "timeSlots", event);
    controller.setParentId(movieId);
    newStage.setOnCloseRequest(controller::onCloseRequest);
  }

  @FXML
  void onBackButtonClicked(ActionEvent event) throws IOException {
    utils.returnParent("movies", 0, 0);
  }
}
