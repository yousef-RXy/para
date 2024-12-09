package com.para;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

import javafx.application.Platform;
import javafx.stage.Stage;

public class Test {
  static Semaphore mutxSemaphore = new Semaphore(1);

  public void bookSeat(Message controller, HashSet<String> set, Stage stage) {
    new Thread(() -> {
      try {
        Platform.runLater(() -> controller.setMessage("Processing seats: " + set.toString()));
        mutxSemaphore.acquire();

        Thread.sleep(5000);

        Platform.runLater(() -> controller.setMessage("Seats booked: " + set.toString()));
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      } finally {
        mutxSemaphore.release();
      }
    }).start();
  }

}
