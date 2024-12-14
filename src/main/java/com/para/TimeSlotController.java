package com.para;

import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
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
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class TimeSlotController extends Controller {
  private int timeSlotId;
  private int movieId;
  private final HashSet<String> set = new HashSet<>();
  private final HashSet<String> bookedSet = new HashSet<>();
  private Timeline timer;
  private final char[] availableRows = { 'A', 'B', 'C', 'D', 'E' };

  @FXML
  private GridPane seatsContainer;

  @FXML
  private Button bookButton;

  @Override
  public void setParentId(int id) {
    this.movieId = id;
  }

  @Override
  public void setId(int id) {
    this.timeSlotId = id;
    onTimeSlotIdSet();
  }

  @FXML
  public void initialize() {
    if (timer != null && timer.getStatus() == Animation.Status.RUNNING) {
      timer.stop();
    }

    timer = new Timeline(new KeyFrame(Duration.millis(1000), e -> {
      for (char rowChar : availableRows) {
        for (int i = 1; i <= 10; i++) {
          String id = "" + rowChar + i;
          if (!bookedSet.contains(id) && DatabaseConnection.isBooked(this.timeSlotId, id)) {
            if (set.contains(id)) {
              set.remove(id);
              if (set.isEmpty()) {
                bookButton.setDisable(true);
              }
              try {
                FXMLLoader fxmlLoader = App.loadFXML("message");
                Parent loader = fxmlLoader.load();
                MessageController controller = fxmlLoader.getController();
                controller.setMessage("seat " + id + " booked");

                Stage newStage = new Stage();
                newStage.setTitle("seat " + id + " booked");

                Scene scene = new Scene(loader);
                newStage.setScene(scene);

                newStage.show();
              } catch (IOException e1) {
                Thread.currentThread().interrupt();
              }
            }
            CheckBox seat = (CheckBox) seatsContainer.lookup("#" + id);
            seat.setDisable(true);
            seat.setSelected(true);
          }
        }
      }
    }));
    timer.setCycleCount(Animation.INDEFINITE);
    timer.play();
  }

  public void onCloseRequest(WindowEvent event) {
    if (timer != null) {
      timer.stop();
    }
  }

  private void onTimeSlotIdSet() {
    for (int j = 0; j < availableRows.length; j++) {
      char rowChar = availableRows[j];
      for (int i = 1; i <= 10; i++) {
        int colIndex = i - 1;
        CheckBox seat = new CheckBox();
        String id = "" + rowChar + i;
        seat.setId(id);

        if (DatabaseConnection.isBooked(this.timeSlotId, id)) {
          bookedSet.add(id);
          seat.setDisable(true);
          seat.setSelected(true);
        }

        seat.setOnAction(this::toggleChooseSeat);
        seatsContainer.add(seat, colIndex > 4 ? colIndex + 15 : colIndex, j);
      }
    }
    bookButton.setDisable(true);
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

    @SuppressWarnings("unchecked")
    final HashSet<String> finalSet = (HashSet<String>) set.clone();
    timeSlot.bookSeat(controller, finalSet);

    bookButton.setDisable(true);
    set.clear();

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
