package com.para;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;

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

  public static HashSet<Movie> FetchMovies() {
    String sql = "SELECT * FROM movies";
    HashSet<Movie> set = new HashSet<>();
    try {
      ResultSet result = executeQuery(sql);
      while (result.next()) {
        int id = result.getInt("id");
        String name = result.getString("name");
        set.add(new Movie(id, name));
      }
    } catch (SQLException e) {
      Thread.interrupted();
    }
    return set;
  }

  public static HashSet<TimeSlot> FetchTimeSlots(int movieId) {
    String sql = "SELECT * FROM time_slots where movie_id=" + movieId;
    HashSet<TimeSlot> set = new HashSet<>();
    try {
      ResultSet result = executeQuery(sql);
      while (result.next()) {
        int id = result.getInt("id");
        String timeSlot = result.getString("time_slot");
        set.add(new TimeSlot(id, timeSlot));
      }
    } catch (SQLException e) {
      Thread.interrupted();
    }
    return set;
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
}
