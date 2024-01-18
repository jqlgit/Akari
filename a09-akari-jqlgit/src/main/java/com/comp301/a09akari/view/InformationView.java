package com.comp301.a09akari.view;

import com.comp301.a09akari.controller.ControllerImpl;
import com.comp301.a09akari.model.Puzzle;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

public class InformationView implements FXComponent {
  private final ControllerImpl controller;

  public InformationView(ControllerImpl controller) {
    this.controller = controller;
  }

  @Override
  public Parent render() {
    HBox layout = new HBox();
    layout.setAlignment(Pos.TOP_CENTER);
    Text puzzleText = new Text("Puzzle: ");
    puzzleText.setFont(Font.font("Futura", FontWeight.BOLD, FontPosture.REGULAR, 20));
    layout.getChildren().add(puzzleText);
    puzzleText.setFill(Color.GOLD);
    int index = controller.getIndex();
    String indexS = Integer.toString(index + 1);
    Text indexText = new Text(indexS);
    indexText.setFont(Font.font("Futura", FontWeight.BOLD, FontPosture.REGULAR, 20));
    layout.getChildren().add(indexText);
    indexText.setFill(Color.GOLD);

    Text ofText = new Text(" of ");
    ofText.setFont(Font.font("Futura", FontWeight.BOLD, FontPosture.REGULAR, 20));
    layout.getChildren().add(ofText);
    ofText.setFill(Color.GOLD);

    int numPuzzles = controller.getNumberOfPuzzles();
    String numPuzzlesS = Integer.toString(numPuzzles);
    Text totalNum = new Text(numPuzzlesS);
    totalNum.setFont(Font.font("Futura", FontWeight.BOLD, FontPosture.REGULAR, 20));
    layout.getChildren().add(totalNum);
    totalNum.setFill(Color.GOLD);
    layout.setPadding(new Insets(10, 10, 10, 10));

    return layout;
  }
}
