package ui;

import java.util.Scanner;

public class InputReader {
  private final Scanner scanner = new Scanner(System.in);

  public int readInt(String prompt, int min, int max) {
    while (true) {
      System.out.print(prompt);
      String line = scanner.nextLine().trim();

      int value;
      try {
        value = Integer.parseInt(line);
      } catch (NumberFormatException e) {
        System.out.println("Please enter a number, not letters.");
        continue;
      }

      if (value >= min && value <= max) {
        return value;
      }
      System.out.println("Please enter a number from " + min + " to " + max + ".");
    }
  }

  public String readText(String prompt) {
    System.out.print(prompt);
    return scanner.nextLine().trim();
  }

  public boolean readYesNo(String prompt) {
    while (true) {
      System.out.print(prompt);
      String answer = scanner.nextLine().trim().toLowerCase();
      if (answer.equals("y")) {
        return true;
      }
      if (answer.equals("n")) {
        return false;
      }
      System.out.println("Please answer y or n.");
    }
  }
}