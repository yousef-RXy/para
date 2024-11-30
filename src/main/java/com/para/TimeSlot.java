package com.para;

import java.util.HashSet;

public class TimeSlot {
  int id;
  String timeSlot;
  HashSet<String> set;

  public TimeSlot(int id, String timeSlot) {
    this.id = id;
    this.timeSlot = timeSlot;
  }

  @Override
  public String toString() {
    return this.timeSlot;
  }

  public int getId() {
    return id;
  }
}
