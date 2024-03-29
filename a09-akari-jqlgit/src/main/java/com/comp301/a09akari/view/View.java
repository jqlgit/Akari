package com.comp301.a09akari.view;

import com.comp301.a09akari.controller.ControllerImpl;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

public class View implements FXComponent {
  private final ControllerImpl controller;

  public View(ControllerImpl controller) {
    this.controller = controller;
  }

  @Override
  public Parent render() {
    VBox layout = new VBox();
    TitleView titleView = new TitleView(controller);
    ControlView controlsView = new ControlView(controller);
    MessageView solvedView = new MessageView(controller);
    PuzzleView puzzleView = new PuzzleView(controller);
    InformationView indexView = new InformationView(controller);
    layout.getChildren().add(titleView.render());
    layout.getChildren().add(indexView.render());
    layout.getChildren().add(controlsView.render());
    layout.getChildren().add(solvedView.render());
    layout.getChildren().add(puzzleView.render());

    return layout;
  }
}
