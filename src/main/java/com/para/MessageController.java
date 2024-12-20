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

public class MessageController extends Controller {
  int timeSlotId;
  int movieId;
  int popcornCount;
  int juiceCount;
  HashSet<String> set;

  @FXML
  private Label message;

  @FXML
  private Button continueButton;

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
  void onBackButtonClicked(ActionEvent event) throws IOException {
    System.out.println("timeSlots" + " " + this.timeSlotId + " " + this.movieId);
    utils.returnParent("timeSlots", this.timeSlotId, this.movieId);
  }

  void setMessage(String messageText) {
    message.setText(messageText);
  }

  public void showSnacks(HashSet<String> set) {
    try {
      FXMLLoader fxmlLoader = App.loadFXML("snacks");
      Parent loader = fxmlLoader.load();
      SnacksController controller = fxmlLoader.getController();
      Scene scene = (Scene) message.getScene();

      controller.setId(timeSlotId);
      controller.setParentId(movieId);
      controller.setSeats(set);
      scene.setRoot(loader);
    } catch (IOException e) {
      Thread.currentThread().interrupt();
    }
  }

  public void failedbookSnacks(String msg, int popcornCount, int juiceCount) {
    continueButton.setVisible(true);
    continueButton.setDisable(false);
    message.setText(msg);
    this.juiceCount = juiceCount;
    this.popcornCount = popcornCount;
  }

  @FXML
  void continueToInvoice() throws IOException {
    this.showInvoice(this.popcornCount, this.juiceCount);
  }

  public void showInvoice(int popcornCount, int juiceCount) {
    try {
      FXMLLoader fxmlLoader = App.loadFXML("invoice");
      Parent loader = fxmlLoader.load();
      InvoiceController controller = fxmlLoader.getController();
      Scene scene = (Scene) message.getScene();
      if (scene != null) {
        controller.setInvoice(movieId, timeSlotId, set, popcornCount, juiceCount);
        scene.setRoot(loader);
      } else {
        System.err.println("Scene not found for message controller!");
      }
    } catch (IOException e) {
      Thread.currentThread().interrupt();
    }
  }
}
