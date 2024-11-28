package com.para;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MessageController extends Controller {
  int timeSlotId;
  int movieId;

  @FXML
  private Label message;

  @Override
  public void setId(int id) {
    this.timeSlotId = id;
  }

  @Override
  public void setParentId(int id) {
    this.movieId = id;
  }

  @FXML
  void onBackButtonClicked(ActionEvent event) throws IOException {
    utils.returnParent("timeSlots", this.timeSlotId, this.movieId);
  }

  void setMessage(String messageText) {
    message.setText(messageText);
  }
}
