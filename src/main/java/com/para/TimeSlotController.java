package com.para;

import java.io.IOException;
import java.util.HashSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TimeSlotController extends Controller {
  int timeSlotId;
  int movieId;
  HashSet<String> set = new HashSet<>();

  @Override
  public void setId(int id) {
    this.timeSlotId = id;
  }

  @Override
  public void setParentId(int id) {
    this.movieId = id;
  }

  @FXML
  void toggleChooseSeat(ActionEvent event) {
    CheckBox checkBox = (CheckBox) (event.getSource());
    String id = checkBox.getId();
    if (this.set.contains(id))
      this.set.remove(id);
    else
      this.set.add(id);
  }

  @FXML
  void onBookClicked(ActionEvent event) throws IOException {
    FXMLLoader fxmlLoader = App.loadFXML("message");
    Parent loader = fxmlLoader.load();
    MessageController controller = fxmlLoader.getController();

    Stage newStage = new Stage();
    newStage.setTitle("Booking in Progress");

    Scene scene = new Scene(loader);
    newStage.setScene(scene);

    newStage.initModality(Modality.WINDOW_MODAL);
    Stage parentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    newStage.initOwner(parentStage);

    HashSet<Movie> MovieSet = App.getMovieSet();
    for (Movie movie : MovieSet) {
      if (movie.getId() != this.movieId) {
        continue;
      }
      for (TimeSlot timeSlot : movie.getTimeslots()) {
        if (timeSlot.getId() != this.timeSlotId) {
          continue;
        }
        timeSlot.bookSeat(controller, set);
      }
    }
    newStage.showAndWait();
  }

}
