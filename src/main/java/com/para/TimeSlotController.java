package com.para;

import java.io.IOException;
import java.util.HashSet;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class TimeSlotController extends Controller {
  int timeSlotId;
  HashSet<String> set = new HashSet<>();
  Test test = new Test();

  @Override
  public void setId(int id) {
    System.out.println("set on TimeSlotController " + id);
    this.timeSlotId = id;
  }

  @FXML
  void toggleChooseSeat(ActionEvent event) {
    CheckBox checkBox = (CheckBox) (event.getSource());
    String id = checkBox.getId();
    if (this.set.contains(id))
      this.set.remove(id);
    else
      this.set.add(id);
  }

  @FXML
  void onBookClicked(ActionEvent event) throws IOException {
    FXMLLoader fxmlLoader = App.loadFXML("message");
    Parent loader = fxmlLoader.load();
    MessageController controller = fxmlLoader.getController();

    Stage newStage = new Stage();
    newStage.setTitle("Booking in Progress");

    Scene scene = new Scene(loader);
    newStage.setScene(scene);

    newStage.initModality(Modality.WINDOW_MODAL);
    Stage parentStage = (Stage) ((Button) event.getSource()).getScene().getWindow();
    newStage.initOwner(parentStage);

    test.bookSeat(controller, set, newStage);
    newStage.showAndWait();
  }

}
