package org.damageprofiler.gui.dialogues;

import javafx.scene.control.ColorPicker;
import javafx.scene.paint.Color;

public class ColorPickerPane {
  private final ColorPicker colorPicker;

  public ColorPickerPane(Color color) {
    colorPicker = new ColorPicker();
    colorPicker.setValue(color);
  }

  public ColorPicker getPicker() {
    return colorPicker;
  }
}
