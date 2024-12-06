package com.para;

import java.io.IOException;
import java.util.HashSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class SnacksController extends Controller {
  int timeSlotId;
  int movieId;
  HashSet<String> set;

  @FXML
  private Label juiceCounter;

  @FXML
  private Label popCornCounter;

  @FXML
  private Button juiceCounterDecrease;

  @FXML
  private Button popCornCounterDecrease;

  @FXML
  private Button juiceCounterIncrease;

  @FXML
  private Button popCornCounterIncrease;

  @Override
  public void setId(int id) {
    this.timeSlotId = id;
  }

  @Override
  public void setParentId(int id) {
    this.movieId = id;
  }

  public void setSeats(HashSet<String> set) {
    this.set = set;
  }

  @FXML
  public void initialize() {
    SnacksStore snacksStore = App.getSnacksStore();
    if (!snacksStore.isAvailablePopcornStore()) {
      popCornCounter.setText("X");
      popCornCounterDecrease.setDisable(true);
      popCornCounterIncrease.setDisable(true);
    }
    if (!snacksStore.isAvailableJuiceStore()) {
      juiceCounter.setText("X");
      juiceCounterDecrease.setDisable(true);
      juiceCounterIncrease.setDisable(true);
    }
  }

  @FXML
  void onDecreaseJuice(ActionEvent event) {
    juiceCounter.setText((Integer.parseInt(juiceCounter.getText()) - 1) + "");
  }

  @FXML
  void onDecreasePopcorn(ActionEvent event) {
    popCornCounter.setText((Integer.parseInt(popCornCounter.getText()) - 1) + "");
  }

  @FXML
  void onIncreaseJuice(ActionEvent event) {
    juiceCounter.setText((Integer.parseInt(juiceCounter.getText()) + 1) + "");
  }

  @FXML
  void onIncreasePopcorn(ActionEvent event) {
    popCornCounter.setText((Integer.parseInt(popCornCounter.getText()) + 1) + "");
  }

  @FXML
  void goToInvoice(ActionEvent event) throws IOException {
    FXMLLoader fxmlLoader = App.loadFXML("message");
    Parent loader = fxmlLoader.load();
    MessageController controller = fxmlLoader.getController();
    Scene scene = (Scene) juiceCounter.getScene();

    controller.setId(timeSlotId);
    controller.setParentId(movieId);
    controller.setSeats(set);
    controller.setMessage("Booking Snacks in Progress");
    scene.setRoot(loader);
    int popcornCount = popCornCounter.getText().compareTo("X") == 0 ? 0 : Integer.parseInt(popCornCounter.getText());
    int juiceCount = juiceCounter.getText().compareTo("X") == 0 ? 0 : Integer.parseInt(juiceCounter.getText());
    App.getSnacksStore().bookSnacks(controller, popcornCount, juiceCount);
  }

}
