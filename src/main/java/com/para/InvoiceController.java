package com.para;

import java.util.HashSet;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class InvoiceController extends Controller {

  @FXML
  private Label juice;

  @FXML
  private Label movie;

  @FXML
  private Label popcorn;

  @FXML
  private Label seats;

  @FXML
  private Label time;

  public void setInvoice(int movieId, int timeSlotId, HashSet<String> seats, int popcornCount, int juiceCount) {
    this.juice.setText("" + juiceCount);
    this.popcorn.setText("" + popcornCount);
    Movie movieObj = App.getMovieMap().get(movieId);
    this.movie.setText(movieObj.toString());
    TimeSlot timeSlot = movieObj.getTimeSlotsMap().get(timeSlotId);
    this.time.setText(timeSlot.toString());
    String seatsString = seats.toString().replace('[', ' ').replace(']', ' ').replace(',', ' ');
    this.seats.setText(seatsString);
  }
}
