package command;

import decorator.IncompatibleCustomizationException;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.Objects;

public class CommandHistory {
  private final Deque<Command> history = new ArrayDeque<>();

  public void run(Command command) throws IncompatibleCustomizationException {
    Objects.requireNonNull(command, "command must not be null");
    command.execute();
    history.push(command);
  }

  public boolean undoLast() {
    if (history.isEmpty()) {
      return false;
    }
    history.pop().undo();
    return true;
  }

  public void clear() {
    history.clear();
  }
}