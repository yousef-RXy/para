package com.para;

import java.io.IOException;
import java.util.HashMap;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

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
      Button timeSlotsButton = utils.addButtonToVBox(timeSlot.toString(), timeSlot.getId());
      timeSlotsButton.setOnAction(event -> {
        try {
          onTimeSlotClicked(event);
        } catch (IOException e) {
          Thread.interrupted();
        }
      });

      TimeSlotsContainer.getChildren().add(timeSlotsButton);
    }
  }

  @FXML
  void onTimeSlotClicked(ActionEvent event) throws IOException {
    Controller controller = utils.onButtonToDisplayClicked("timeSlots", event);
    controller.setParentId(movieId);

    // Stage newStage = new Stage();
    // FXMLLoader fxmlLoader = App.loadFXML("timeSlots");
    // Parent loader = fxmlLoader.load();

    // TimeSlotController controller = fxmlLoader.getController();
    // Button button = (Button) (event.getSource());
    // int id = Integer.parseInt(button.getId());
    // controller.setId(id);

    // Scene scene = new Scene(loader);
    // newStage.setScene(scene);
    // newStage.initModality(Modality.WINDOW_MODAL);
    // newStage.show();
  }
}
