package com.para;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class MessageController {

  @FXML
  private Label message;

  void setMessage(String messageText) {
    message.setText(messageText);
  }
}
