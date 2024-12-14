package com.para;

import java.util.HashMap;

public class Movie {
  private final int id;
  private final String name;
  private final HashMap<Integer, TimeSlot> timeSlotsMap;

  public Movie(int id, String name) {
    this.id = id;
    this.name = name;
    this.timeSlotsMap = DatabaseConnection.FetchTimeSlots(id);
  }

  public int getId() {
    return id;
  }

  public HashMap<Integer, TimeSlot> getTimeSlotsMap() {
    return timeSlotsMap;
  }

  @Override
  public String toString() {
    return this.name;
  }

}
