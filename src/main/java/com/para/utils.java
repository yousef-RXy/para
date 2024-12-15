package com.para;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class utils {
  public static Button addButtonToVBox(String text, int id) {
    Button button = new Button(text);
    button.setPrefWidth(200);
    button.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));
    button.setId("" + id);

    return button;
  }

  public static Controller onButtonToDisplayClicked(Stage newStage, String fxml, ActionEvent event) throws IOException {
    if (App.getThreadingMode().equals(ModeEnum.PARALLEL)) {
      FXMLLoader fxmlLoader = App.loadFXML(fxml);
      Parent loader = fxmlLoader.load();

      Controller controller = fxmlLoader.getController();
      Button button = (Button) (event.getSource());
      int id = Integer.parseInt(button.getId());
      controller.setId(id);

      newStage.setTitle(fxml + " " + id);
      Scene scene = new Scene(loader);
      newStage.setScene(scene);
      newStage.initModality(Modality.WINDOW_MODAL);
      newStage.show();

      return controller;
    } else {
      FXMLLoader fxmlLoader = App.setRoot(fxml);
      Controller controller = fxmlLoader.getController();
      Button button = (Button) (event.getSource());
      int id = Integer.parseInt(button.getId());
      controller.setId(id);

      return controller;
    }
  }

  public static Controller returnParent(String fxml, int id, int parentId) throws IOException {
    FXMLLoader fxmlLoader = App.setRoot(fxml);
    Controller controller = fxmlLoader.getController();
    controller.setId(id);
    controller.setParentId(parentId);

    return controller;
  }
}
