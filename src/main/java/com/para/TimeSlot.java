package com.para;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

import javafx.application.Platform;

public class TimeSlot {
  int id;
  String timeSlot;
  Semaphore mutexSemaphore = new Semaphore(1);

  public TimeSlot(int id, String timeSlot) {
    this.id = id;
    this.timeSlot = timeSlot;
  }

  public int getId() {
    return id;
  }

  void bookSeat(MessageController controller, HashSet<String> seatsSet) {
    new Thread(() -> {
      try {
        Platform.runLater(() -> controller.setMessage("Processing seats: " + seatsSet.toString()));
        mutexSemaphore.acquire();

        Thread.sleep(5000);

        Platform.runLater(() -> controller.setMessage("Seats booked: " + seatsSet.toString()));
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      } finally {
        mutexSemaphore.release();
      }
    }).start();
  }

  @Override
  public String toString() {
    return this.timeSlot;
  }

}
