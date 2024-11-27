package com.para;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MoviesController {

  @FXML
  private Button Movie1;

  @FXML
  void onMovieClicked(ActionEvent event) throws IOException {
    Stage newStage = new Stage();
    Scene scene = new Scene(App.loadFXML("movie"));
    newStage.setScene(scene);
    newStage.initModality(Modality.WINDOW_MODAL);
    newStage.show();
  }
}
