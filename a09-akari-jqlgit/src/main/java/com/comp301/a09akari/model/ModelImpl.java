package com.comp301.a09akari.model;

import java.util.ArrayList;
import java.util.List;

public class ModelImpl implements Model {
  private final PuzzleLibrary library;
  private final List<ModelObserver> modelObservers;
  private boolean[][] lamps;
  private int curr;
  private Puzzle puzzle;

  public ModelImpl(PuzzleLibrary library) {
    if (library == null) {
      throw new IllegalArgumentException();
    }
    this.library = library;
    this.curr = 0;
    this.puzzle = library.getPuzzle(0);
    this.lamps = new boolean[puzzle.getHeight()][puzzle.getWidth()];
    this.modelObservers = new ArrayList<>();
    this.resetPuzzle();
  }

  @Override
  public void addLamp(int r, int c) {
    if (r > getActivePuzzle().getHeight() - 1
        || c > getActivePuzzle().getWidth() - 1
        || c < 0
        || r < 0) {
      throw new IndexOutOfBoundsException();
    }
    if (getActivePuzzle().getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException();
    }
    lamps[r][c] = true;
    notify(this);
  }

  @Override
  public void removeLamp(int r, int c) {
    if (r > getActivePuzzle().getHeight() - 1
        || c > getActivePuzzle().getWidth() - 1
        || c < 0
        || r < 0) {
      throw new IndexOutOfBoundsException();
    }
    if (getActivePuzzle().getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException();
    }
    lamps[r][c] = false;
    notify(this);
  }

  @Override
  public boolean isLit(int r, int c) {
    if (c < 0 || r < 0 || r > puzzle.getHeight() - 1 || c > puzzle.getWidth() - 1) {
      throw new IndexOutOfBoundsException();
    }
    if (puzzle.getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException();
    }
    for (int y = c; y < puzzle.getWidth(); y++) {
      if (lamps[r][y]) {
        return true;
      } else if (puzzle.getCellType(r, y) != CellType.CORRIDOR) {
        break;
      }
    }
    for (int y = c; y >= 0; y--) {
      if (lamps[r][y]) {
        return true;
      } else if (puzzle.getCellType(r, y) != CellType.CORRIDOR) {
        break;
      }
    }
    for (int x = r; x < puzzle.getHeight(); x++) {
      if (lamps[x][c]) {
        return true;
      } else if (puzzle.getCellType(x, c) != CellType.CORRIDOR) {
        break;
      }
    }
    for (int x = r; x >= 0; x--) {
      if (lamps[x][c]) {
        return true;
      } else if (puzzle.getCellType(x, c) != CellType.CORRIDOR) {
        break;
      }
    }

    return false;
  }

  @Override
  public boolean isLamp(int r, int c) {
    if (r > getActivePuzzle().getHeight() - 1
        || c > getActivePuzzle().getWidth() - 1
        || c < 0
        || r < 0) {
      throw new IndexOutOfBoundsException();
    }
    if (getActivePuzzle().getCellType(r, c) != CellType.CORRIDOR) {
      throw new IllegalArgumentException();
    }
    return lamps[r][c];
  }

  @Override
  public boolean isLampIllegal(int r, int c) {
    if (r > getActivePuzzle().getHeight() - 1
        || c > getActivePuzzle().getWidth() - 1
        || r < 0
        || c < 0) {
      throw new IndexOutOfBoundsException();
    }
    if (!(lamps[r][c])) {
      throw new IllegalArgumentException();
    }
    for (int i = r - 1; i >= 0; i--) {
      if (lamps[i][c]) {
        return true;
      } else if (getActivePuzzle().getCellType(i, c) != CellType.CORRIDOR) {
        break;
      }
    }

    for (int i = r + 1; i < getActivePuzzle().getHeight(); i++) {
      if (lamps[i][c]) {
        return true;
      } else if (getActivePuzzle().getCellType(i, c) != CellType.CORRIDOR) {
        break;
      }
    }

    for (int i = c + 1; i < getActivePuzzle().getWidth(); i++) {
      if (lamps[r][i]) {
        return true;
      } else if (getActivePuzzle().getCellType(r, i) != CellType.CORRIDOR) {
        break;
      }
    }

    for (int i = c - 1; i >= 0; i--) {
      if (lamps[r][i]) {
        return true;
      } else if (getActivePuzzle().getCellType(r, i) != CellType.CORRIDOR) {
        break;
      }
    }
    return false;
  }

  @Override
  public Puzzle getActivePuzzle() {
    return library.getPuzzle(curr);
  }

  @Override
  public int getActivePuzzleIndex() {
    return this.curr;
  }

  @Override
  public void setActivePuzzleIndex(int index) {
    if (index < 0 || index >= getPuzzleLibrarySize()) {
      throw new IndexOutOfBoundsException();
    }
    this.curr = index;
    this.puzzle = library.getPuzzle(index);
    resetPuzzle();
  }

  @Override
  public int getPuzzleLibrarySize() {
    return library.size();
  }

  @Override
  public void resetPuzzle() {
    lamps = new boolean[getActivePuzzle().getHeight()][getActivePuzzle().getWidth()];

    notify(this);
  }

  @Override
  public boolean isSolved() {
    for (int i = 0; i < puzzle.getHeight(); i++) {
      for (int j = 0; j < puzzle.getWidth(); j++) {
        if (puzzle.getCellType(i, j) == CellType.CLUE) {
          if (!isClueSatisfied(i, j)) {
            return false;
          }
        }
        if (puzzle.getCellType(i, j) == CellType.CORRIDOR) {
          if (!isLit(i, j)) {
            return false;
          }
          if (isLamp(i, j) && isLampIllegal(i, j)) {
            return false;
          }
        }
      }
    }
    return true;
  }

  @Override
  public boolean isClueSatisfied(int r, int c) {
    if (r > getActivePuzzle().getHeight() - 1
        || c > getActivePuzzle().getWidth() - 1
        || r < 0
        || c < 0) {
      throw new IndexOutOfBoundsException();
    }
    if (getActivePuzzle().getCellType(r, c) != CellType.CLUE) {
      throw new IllegalArgumentException();
    }
    int numClues = puzzle.getClue(r, c);
    int adjacentLamps = 0;

    if (r > 0 && lamps[r - 1][c]) {
      adjacentLamps++;
    }
    if (r < puzzle.getHeight() - 1 && lamps[r + 1][c]) {
      adjacentLamps++;
    }
    if (c > 0 && lamps[r][c - 1]) {
      adjacentLamps++;
    }
    if (c < puzzle.getWidth() - 1 && lamps[r][c + 1]) {
      adjacentLamps++;
    }
    return (adjacentLamps == numClues);
  }

  @Override
  public void addObserver(ModelObserver observer) {
    modelObservers.add(observer);
  }

  @Override
  public void removeObserver(ModelObserver observer) {
    modelObservers.remove(observer);
  }

  private void notify(ModelImpl model) {
    for (ModelObserver o : modelObservers) {
      o.update(model);
    }
  }
}
