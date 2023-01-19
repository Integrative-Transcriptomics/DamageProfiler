package org.damageprofiler.controller;

import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.scene.control.ProgressBar;

public class ProgressBarController {

  private ProgressBar progressBar;

  public void create() {
    progressBar = new ProgressBar(0);
    progressBar.setDisable(true);
    progressBar.setPadding(new Insets(5, 5, 5, 5));
  }

  public void activate(final Task task) {
    progressBar.progressProperty().unbind();
    progressBar.setProgress(0);
    progressBar.progressProperty().bind(task.progressProperty());
  }

  public void stop() {
    progressBar.progressProperty().unbind();
    progressBar.setProgress(0);
    progressBar.setDisable(true);
  }

  public ProgressBar getProgressBar() {
    return progressBar;
  }
}
