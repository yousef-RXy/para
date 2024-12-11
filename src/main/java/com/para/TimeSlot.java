package com.para;

import java.util.HashSet;
import java.util.concurrent.Semaphore;

import javafx.application.Platform;

public class TimeSlot {
  private int id;
  private String timeSlot;
  private Semaphore mutexSemaphore = new Semaphore(1);

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
        Platform.runLater(() -> controller.setMessage("Processing seats: " +
            seatsSet.toString()));
        mutexSemaphore.acquire();
        for (String seatNum : seatsSet) {
          if (DatabaseConnection.isBooked(this.id, seatNum)) {
            Thread.sleep(5000);
            mutexSemaphore.release();
            Platform.runLater(() -> controller.setMessage("Seat Already booked: " +
                seatNum));
            return;
          }
        }
        for (String seatNum : seatsSet) {
          DatabaseConnection.BookSeat(this.id, seatNum);
        }
        Thread.sleep(5000);

        controller.showSnacks(seatsSet);
      } catch (InterruptedException e) {
        Thread.currentThread().interrupt();
      } finally {
        mutexSemaphore.release();
      }
    }).start();

    // Platform.runLater(() -> controller.setMessage("Processing seats: " +
    // seatsSet.toString()));
    // try {
    // Thread.sleep(5000);
    // } catch (InterruptedException e) {
    // Thread.currentThread().interrupt();
    // }

    // for (String seatNum : seatsSet) {
    // if (DatabaseConnection.isBooked(this.id, seatNum)) {
    // Platform.runLater(() -> controller.setMessage("Seat Already booked: " +
    // seatNum));
    // return;
    // }
    // }
    // for (String seatNum : seatsSet) {
    // DatabaseConnection.BookSeat(this.id, seatNum);
    // }

    // Platform.runLater(() -> controller.setMessage("Seats booked: " +
    // seatsSet.toString()));
  }

  @Override
  public String toString() {
    return this.timeSlot;
  }

}
