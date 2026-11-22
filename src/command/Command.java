package command;

import decorator.IncompatibleCustomizationException;

public interface Command {
  void execute() throws IncompatibleCustomizationException;

  void undo();
}