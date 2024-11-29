package com.para;

import javafx.scene.control.Button;

public class utils {
  public static Button addToVBox(String text, int id) {
    Button button = new Button(text);
    button.setPrefWidth(200);
    button.setPadding(new javafx.geometry.Insets(10, 10, 10, 10));
    button.setId("" + id);

    return button;
  }
}
