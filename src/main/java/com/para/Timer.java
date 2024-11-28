package com.para;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.util.Duration;

public class Timer {
  private long startTime;
  private Timeline timer;

  @FXML
  private Label currentTime;

  @FXML
  private Label totalTime;

  @FXML
  void onStartClicked(ActionEvent event) {
    startTime = System.currentTimeMillis();

    if (timer != null && timer.getStatus() == Animation.Status.RUNNING) {
      timer.stop();
    }

    timer = new Timeline(new KeyFrame(Duration.millis(100), e -> {
      double elapsedTime = (System.currentTimeMillis() - startTime) / 1000.0;
      currentTime.setText(elapsedTime + " s");
    }));
    timer.setCycleCount(Animation.INDEFINITE);
    timer.play();
  }

  @FXML
  void onEndClicked(ActionEvent event) {
    double endTime = (System.currentTimeMillis() - startTime) / 1000.0;
    if (timer != null) {
      timer.stop();
    }
    currentTime.setText(endTime + " s");
    totalTime.setText(endTime + " s");
  }

}
