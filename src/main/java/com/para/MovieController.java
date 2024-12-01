package com.para;

import java.io.IOException;
import java.util.HashSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;

public class MovieController extends Controller {
  int movieId;

  @FXML
  private VBox TimeSlotsController;

  @Override
  public void setId(int movieId) {
    System.out.println("set on MovieController " + movieId);
    this.movieId = movieId;
    onMovieIdSet();
  }

  private void onMovieIdSet() {
    HashSet<Movie> set = App.getMovieSet();
    for (Movie movie : set) {
      if (movie.getId() != this.movieId) {
        continue;
      }
      for (TimeSlot timeSlot : movie.getTimeslots()) {
        Button timeSlotsButton = utils.addToVBox(timeSlot.toString(), timeSlot.getId());
        timeSlotsButton.setOnAction(event -> {
          try {
            onTimeSlotClicked(event);
          } catch (IOException e) {
            e.printStackTrace();
          }
        });

        TimeSlotsController.getChildren().add(timeSlotsButton);
      }
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
