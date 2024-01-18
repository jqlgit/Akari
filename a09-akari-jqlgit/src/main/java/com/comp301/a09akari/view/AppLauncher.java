package com.comp301.a09akari.view;

import com.comp301.a09akari.SamplePuzzles;
import com.comp301.a09akari.controller.ControllerImpl;
import com.comp301.a09akari.model.*;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class AppLauncher extends Application {
  @Override
  public void start(Stage stage) {
    PuzzleLibrary library = new PuzzleLibraryImpl();
    Puzzle puzzle01 = new PuzzleImpl(SamplePuzzles.PUZZLE_01);
    Puzzle puzzle02 = new PuzzleImpl(SamplePuzzles.PUZZLE_02);
    Puzzle puzzle03 = new PuzzleImpl(SamplePuzzles.PUZZLE_03);
    Puzzle puzzle04 = new PuzzleImpl(SamplePuzzles.PUZZLE_04);
    library.addPuzzle(puzzle01);
    library.addPuzzle(puzzle02);
    library.addPuzzle(puzzle03);
    library.addPuzzle(puzzle04);

    Model model = new ModelImpl(library);

    ControllerImpl controller = new ControllerImpl(model);

    View view = new View(controller);

    Scene scene = new Scene(view.render(), 700, 700);
    stage.setScene(scene);
    scene.getStylesheets().add("main.css");

    model.addObserver(
        (Model m) -> {
          scene.setRoot(view.render());
          stage.sizeToScene();
        });

    stage.setTitle("Akari");
    stage.show();
  }
}
