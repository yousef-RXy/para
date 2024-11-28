package com.para;

import java.util.HashSet;

public class Movie {
  int id;
  String name;
  HashSet<String> set;

  public Movie(int id, String name) {
    this.id = id;
    this.name = name;
    this.set = DatabaseConnection.FetchTimeSlots(id);
  }

  @Override
  public String toString() {
    return this.name;
  }

  public int getId() {
    return id;
  }

  HashSet<String> getTimeslots() {
    return set;
  }

}
