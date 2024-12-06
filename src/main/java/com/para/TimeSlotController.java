package com.para;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TimeSlotController extends Controller {
  int timeSlotId;
  int movieId;
  HashSet<String> set = new HashSet<>();

  @FXML
  private GridPane seatsContainer;

  @FXML
  private Button bookButton;

  @Override
  public void setId(int id) {
    this.timeSlotId = id;
    onTimeSlotIdSet();
  }

  private void onTimeSlotIdSet() {
    char[] availableRows = { 'A', 'B', 'C', 'D', 'E' };

    for (int j = 0; j < availableRows.length; j++) {
      char rowChar = availableRows[j];
      for (int i = 1; i <= 10; i++) {
        int colIndex = i - 1;
        CheckBox seat = new CheckBox();
        String id = "" + rowChar + i;
        seat.setId(id);

        if (DatabaseConnection.isBooked(this.timeSlotId, id)) {
          seat.setDisable(true);
          seat.setSelected(true);
        }

        seat.setOnAction(this::toggleChooseSeat);
        seatsContainer.add(seat, colIndex > 4 ? colIndex + 15 : colIndex, j);
      }
    }
    bookButton.setDisable(true);
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

    bookButton.setDisable(this.set.isEmpty());
  }

  @FXML
  void onBackButtonClicked(ActionEvent event) throws IOException {
    utils.returnParent("movie", movieId, 0);
  }

  @FXML
  void onBookClicked(ActionEvent event) throws IOException {
    FXMLLoader fxmlLoader = App.loadFXML("message");
    Parent loader = fxmlLoader.load();
    MessageController controller = fxmlLoader.getController();

    Stage newStage = new Stage();
    newStage.setTitle("Booking in Progress Movie: " + movieId + " timeSlot: " +
        timeSlotId);

    Scene scene = new Scene(loader);
    newStage.setScene(scene);

    newStage.initModality(Modality.WINDOW_MODAL);
    Stage parentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    newStage.initOwner(parentStage);

    HashMap<Integer, Movie> movieMap = App.getMovieMap();
    Movie movie = movieMap.get(this.movieId);

    HashMap<Integer, TimeSlot> timeSlotMap = movie.getTimeSlotsMap();
    TimeSlot timeSlot = timeSlotMap.get(this.timeSlotId);
    timeSlot.bookSeat(controller, set);

    controller.setId(timeSlotId);
    controller.setParentId(movieId);
    newStage.showAndWait();
    // HashMap<Integer, Movie> movieMap = App.getMovieMap();
    // Movie movie = movieMap.get(this.movieId);

    // HashMap<Integer, TimeSlot> timeSlotMap = movie.getTimeSlotsMap();
    // TimeSlot timeSlot = timeSlotMap.get(this.timeSlotId);

    // FXMLLoader fxmlLoader = App.setRoot("message");
    // MessageController controller = fxmlLoader.getController();

    // controller.setId(timeSlotId);
    // controller.setParentId(movieId);
    // timeSlot.bookSeat(controller, set);
  }
}
