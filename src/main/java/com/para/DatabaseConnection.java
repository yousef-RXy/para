package com.para;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;

public class DatabaseConnection {
  private static final String URL = "jdbc:mysql://localhost:3306/paraller";
  private static final String USER = "admin";
  private static final String PASSWORD = "0000";
  private static Connection connection = null;

  public static void connect() {
    try {
      connection = DriverManager.getConnection(URL, USER, PASSWORD);
      System.out.println("Connected to the database!");
    } catch (SQLException e) {
      System.out.println("Failed to connect: " + e.getMessage());
    }
  }

  private static ResultSet executeQuery(String query) throws SQLException {
    PreparedStatement stmt = connection.prepareStatement(query);
    return stmt.executeQuery();
  }

  public static HashMap<Integer, Movie> FetchMovies() {
    String sql = "SELECT * FROM movies";
    HashMap<Integer, Movie> map = new HashMap<>();

    try {
      ResultSet result = executeQuery(sql);
      while (result.next()) {
        int id = result.getInt("id");
        String name = result.getString("name");
        map.put(id, new Movie(id, name));
      }
    } catch (SQLException e) {
      Thread.interrupted();
    }
    return map;
  }

  public static HashMap<Integer, TimeSlot> FetchTimeSlots(int movieId) {
    String sql = "SELECT * FROM time_slots where movie_id=" + movieId;
    HashMap<Integer, TimeSlot> map = new HashMap<>();

    try {
      ResultSet result = executeQuery(sql);
      while (result.next()) {
        int id = result.getInt("id");
        String timeSlot = result.getString("time_slot");
        map.put(id, new TimeSlot(id, timeSlot));
      }
    } catch (SQLException e) {
      Thread.interrupted();
    }
    return map;
  }

  public static boolean isTimeSlotsAvailable(int timeSlotId, int max) {
    String sql = "SELECT COUNT(*) AS bookedSeatsCounter FROM seats WHERE is_booked=1 AND time_slot_id=" + timeSlotId;
    boolean map = false;

    try {
      ResultSet result = executeQuery(sql);
      while (result.next()) {
        int counter = result.getInt("bookedSeatsCounter");
        if (counter < max) {
          map = true;
        }
      }
    } catch (SQLException e) {
      Thread.interrupted();
    }
    return map;
  }

  public static boolean isBooked(int timeSlotId, String seatNum) {
    String sql = "SELECT * FROM `seats` WHERE time_slot_id=" + timeSlotId + " AND seat_num='" + seatNum + "'";
    try {
      ResultSet result = executeQuery(sql);
      if (result.next())
        return result.getBoolean("is_booked");
    } catch (SQLException e) {
      Thread.interrupted();
    }
    return false;
  }

  public static boolean BookSeat(int timeSlotId, String seatNum) {
    String sql = "INSERT INTO `seats`(`time_slot_id`, `seat_num`, `is_booked`) VALUES (?, ?, 1)";
    try {
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setInt(1, timeSlotId);
      stmt.setString(2, seatNum);

      int rowsAffected = stmt.executeUpdate();

      return rowsAffected > 0;
    } catch (SQLException e) {
      Thread.interrupted();
      return false;
    }
  }

  public static int availableSnacks(String type) {
    String sql = "SELECT count FROM snacks WHERE type = '" + type + "'";
    int count = -1;
    try {
      ResultSet result = executeQuery(sql);
      if (result.next())
        count = result.getInt("count");
    } catch (SQLException e) {
      Thread.interrupted();
    }
    return count;
  }

  public static void increaseSnacks(String type, int value) {
    String sql = "UPDATE snacks SET count = ? + (SELECT count FROM snacks WHERE type=?) WHERE type=?";
    try {
      PreparedStatement stmt = connection.prepareStatement(sql);
      stmt.setInt(1, value);
      stmt.setString(2, type);
      stmt.setString(3, type);
      stmt.executeUpdate();
    } catch (SQLException e) {
      Thread.interrupted();
    }
  }
}
